package com.portfolio.performance.core;

import java.io.File;
import java.net.URI;
import java.net.URISyntaxException;
import java.net.URL;

import com.portfolio.performance.scenario.ScenarioDefinition;
import org.apache.jmeter.assertions.ResponseAssertion;
import org.apache.jmeter.control.LoopController;
import org.apache.jmeter.engine.StandardJMeterEngine;
import org.apache.jmeter.protocol.http.control.Header;
import org.apache.jmeter.protocol.http.control.HeaderManager;
import org.apache.jmeter.protocol.http.sampler.HTTPSamplerProxy;
import org.apache.jmeter.testelement.TestPlan;
import org.apache.jmeter.threads.ThreadGroup;
import org.apache.jmeter.util.JMeterUtils;
import org.apache.jorphan.collections.ListedHashTree;

public final class JMeterScenarioRunner {
    private static final String DEFAULT_BASE_URL = "https://automationintesting.online";

    private final URI baseUri;

    private JMeterScenarioRunner(URI baseUri) {
        this.baseUri = baseUri;
    }

    public static JMeterScenarioRunner createDefault() {
        return new JMeterScenarioRunner(resolveBaseUri());
    }

    public void configure() {
        JMeterUtils.setJMeterHome(System.getProperty("user.dir"));

        URL propertiesResource = JMeterScenarioRunner.class.getResource("/jmeter.properties");
        if (propertiesResource == null) {
            throw new IllegalStateException("Missing test resource: jmeter.properties");
        }

        try {
            JMeterUtils.loadJMeterProperties(new File(propertiesResource.toURI()).getAbsolutePath());
        } catch (URISyntaxException exception) {
            throw new IllegalStateException("Could not load jmeter.properties", exception);
        }

        JMeterUtils.initLocale();
    }

    public ScenarioResult run(ScenarioDefinition definition) {
        StandardJMeterEngine engine = new StandardJMeterEngine();
        ListedHashTree testPlanTree = new ListedHashTree();

        TestPlan testPlan = new TestPlan(definition.name());
        testPlan.setFunctionalMode(false);
        testPlan.setSerialized(true);

        ThreadGroup threadGroup = new ThreadGroup();
        threadGroup.setName(definition.name() + "-threads");
        threadGroup.setNumThreads(definition.users());
        threadGroup.setRampUp(definition.rampUpSeconds());
        threadGroup.setScheduler(false);

        LoopController loopController = new LoopController();
        loopController.setLoops(definition.loops());
        loopController.setFirst(true);
        loopController.initialize();
        threadGroup.setSamplerController(loopController);

        ListedHashTree testPlanSubTree = testPlanTree.add(testPlan);
        ListedHashTree threadGroupSubTree = testPlanSubTree.add(threadGroup);

        HTTPSamplerProxy sampler = httpSampler(definition);
        ResponseTracker tracker = new ResponseTracker();
        ListedHashTree samplerSubTree = threadGroupSubTree.add(sampler);
        samplerSubTree.add(headerManager(definition.body() != null));
        samplerSubTree.add(responseCodeAssertion(definition.expectedStatus()));
        samplerSubTree.add(tracker);

        engine.configure(testPlanTree);
        engine.run();

        return tracker.snapshot();
    }

    private HTTPSamplerProxy httpSampler(ScenarioDefinition definition) {
        HTTPSamplerProxy sampler = new HTTPSamplerProxy();
        sampler.setName(definition.name());
        sampler.setProtocol(baseUri.getScheme());
        sampler.setDomain(baseUri.getHost());

        if (baseUri.getPort() != -1) {
            sampler.setPort(baseUri.getPort());
        }

        sampler.setPath(definition.path());
        sampler.setMethod(definition.method());
        sampler.setFollowRedirects(true);
        sampler.setUseKeepAlive(true);

        if (definition.body() != null) {
            sampler.setPostBodyRaw(true);
            sampler.addNonEncodedArgument("", definition.body(), "");
        }

        return sampler;
    }

    private HeaderManager headerManager(boolean jsonBody) {
        HeaderManager headerManager = new HeaderManager();
        headerManager.add(new Header("Accept", "application/json"));

        if (jsonBody) {
            headerManager.add(new Header("Content-Type", "application/json"));
        }

        return headerManager;
    }

    private ResponseAssertion responseCodeAssertion(int expectedStatus) {
        ResponseAssertion assertion = new ResponseAssertion();
        assertion.setName("Expect HTTP " + expectedStatus);
        assertion.setTestFieldResponseCode();
        assertion.setToEqualsType();
        assertion.addTestString(Integer.toString(expectedStatus));
        assertion.setAssumeSuccess(false);
        assertion.setCustomFailureMessage("Unexpected HTTP status code");
        return assertion;
    }

    private static URI resolveBaseUri() {
        String configuredBaseUrl = System.getenv().getOrDefault("AUTOMATION_IN_TESTING_BASE_URL", DEFAULT_BASE_URL);

        try {
            return new URI(configuredBaseUrl);
        } catch (URISyntaxException exception) {
            throw new IllegalStateException("Invalid AUTOMATION_IN_TESTING_BASE_URL value: " + configuredBaseUrl, exception);
        }
    }
}
