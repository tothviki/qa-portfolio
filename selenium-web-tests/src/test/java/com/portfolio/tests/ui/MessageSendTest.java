package com.portfolio.tests.ui;

import com.portfolio.base.AutomationInTestingUiTestBase;
import com.portfolio.data.AutomationInTestingTestData;
import org.junit.jupiter.api.Test;

public class MessageSendTest extends AutomationInTestingUiTestBase {

    @Test
    void shouldSendAContactMessage() {
        contactPage.gotoPage();
        contactPage.fillMessage(
                AutomationInTestingTestData.randomName(),
                AutomationInTestingTestData.randomEmail(),
                "07123456789",
                "Selenium portfolio message",
                "Hello from an automated portfolio test. Please ignore."
        );
        contactPage.submit();
        contactPage.expectSuccessMessage();
    }
}
