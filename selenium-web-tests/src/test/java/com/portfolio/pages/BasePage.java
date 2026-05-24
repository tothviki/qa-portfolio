package com.portfolio.pages;

import lombok.extern.slf4j.Slf4j;
import org.openqa.selenium.By;
import org.openqa.selenium.ElementNotInteractableException;
import org.openqa.selenium.TimeoutException;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.JavascriptExecutor;
import org.openqa.selenium.StaleElementReferenceException;
import org.openqa.selenium.support.ui.ExpectedConditions;
import org.openqa.selenium.support.ui.WebDriverWait;

import java.time.Duration;
import java.util.ArrayList;
import java.util.List;

@Slf4j
abstract class BasePage {
    protected final WebDriver driver;
    protected final WebDriverWait wait;
    private final String baseUrl;

    BasePage(WebDriver driver, String baseUrl) {
        this.driver = driver;
        this.baseUrl = baseUrl;
        this.wait = new WebDriverWait(driver, Duration.ofSeconds(15));
    }

    protected void open(String path) {
        driver.get(resolveUrl(path));
    }

    protected WebElement visible(By locator) {
        try {
            return wait.until(ExpectedConditions.visibilityOfElementLocated(locator));
        } catch (TimeoutException exception) {
            dumpPageState("Timeout waiting for visibility of " + locator);
            throw exception;
        }
    }

    protected WebElement visible(By locator, int index) {
        List<WebElement> elements = visibleElements(locator);
        if (elements.size() <= index) {
            throw new TimeoutException("Expected at least " + (index + 1) + " visible elements for " + locator);
        }
        return elements.get(index);
    }

    protected List<WebElement> visibleElements(By locator) {
        return wait.until(unused -> {
            List<WebElement> elements = new ArrayList<>();
            for (WebElement element : driver.findElements(locator)) {
                try {
                    if (element.isDisplayed()) {
                        elements.add(element);
                    }
                } catch (StaleElementReferenceException ignored) {
                    return null;
                }
            }
            return elements.isEmpty() ? null : elements;
        });
    }

    protected void type(By locator, String value) {
        WebElement element = visible(locator);
        element.clear();
        element.sendKeys(value);
    }

    protected void type(WebElement element, String value) {
        element.clear();
        element.sendKeys(value);
    }

    protected void click(By locator) {
        StaleElementReferenceException lastStale = null;
        for (int attempt = 0; attempt < 3; attempt++) {
            try {
                click(wait.until(ExpectedConditions.elementToBeClickable(locator)));
                return;
            } catch (StaleElementReferenceException exception) {
                lastStale = exception;
            }
        }

        throw lastStale;
    }

    protected void click(WebElement element) {
        scrollIntoView(element);
        try {
            element.click();
        } catch (ElementNotInteractableException exception) {
            ((JavascriptExecutor) driver).executeScript("arguments[0].click();", element);
        }
    }

    protected WebElement controlByLabel(By scope, String labelText) {
        By locator = By.xpath(
                ".//label[normalize-space()=" + xpathLiteral(labelText) + "]/following::input[1]"
        );
        return wait.until(unused -> {
            WebElement section = visible(scope);
            try {
                WebElement control = section.findElement(locator);
                return control.isDisplayed() ? control : null;
            } catch (StaleElementReferenceException ignored) {
                return null;
            }
        });
    }

    protected String resolveUrl(String path) {
        return baseUrl.replaceAll("/+$", "") + path;
    }

    protected String baseUrl() {
        return baseUrl;
    }

    protected void dumpPageState(String message) {
        log.error(message);
        log.error("Current URL: {}", driver.getCurrentUrl());
        log.error("Page title: {}", driver.getTitle());

        log.error("Headings:");
        for (WebElement heading : driver.findElements(By.xpath("//*[self::h1 or self::h2 or self::h3 or self::h4 or self::h5 or self::h6]"))) {
            if (heading.isDisplayed()) {
                log.error(" - [{}] {}", heading.getTagName(), heading.getText());
            }
        }

        log.error("Form controls:");
        for (WebElement control : driver.findElements(By.cssSelector("input, textarea, button, a"))) {
            if (control.isDisplayed()) {
                log.error(
                        " - [{}] text='{}' id='{}' name='{}' placeholder='{}' aria-label='{}' href='{}'",
                        control.getTagName(),
                        control.getText(),
                        safeAttr(control, "id"),
                        safeAttr(control, "name"),
                        safeAttr(control, "placeholder"),
                        safeAttr(control, "aria-label"),
                        safeAttr(control, "href")
                );
            }
        }

        dumpSectionText("rooms");
        dumpSectionText("booking");
        dumpSectionText("contact");
    }

    private String safeAttr(WebElement element, String attribute) {
        String value = element.getAttribute(attribute);
        return value == null ? "" : value;
    }

    private void scrollIntoView(WebElement element) {
        ((JavascriptExecutor) driver).executeScript(
                "arguments[0].scrollIntoView({block: 'center', inline: 'nearest'});",
                element
        );
    }

    protected String xpathLiteral(String value) {
        if (!value.contains("'")) {
            return "'" + value + "'";
        }

        String[] parts = value.split("'");
        StringBuilder builder = new StringBuilder("concat(");
        for (int i = 0; i < parts.length; i++) {
            if (i > 0) {
                builder.append(", \"'\", ");
            }
            builder.append("'").append(parts[i]).append("'");
        }
        builder.append(")");
        return builder.toString();
    }

    private void dumpSectionText(String id) {
        List<WebElement> sections = driver.findElements(By.id(id));
        if (sections.isEmpty()) {
            return;
        }

        WebElement section = sections.get(0);
        if (!section.isDisplayed()) {
            return;
        }

        log.error("Section #{}:", id);
        log.error("{}", section.getText());
    }
}
