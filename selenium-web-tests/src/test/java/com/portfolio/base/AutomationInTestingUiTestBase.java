package com.portfolio.base;

import com.portfolio.pages.BookingPage;
import com.portfolio.pages.ContactPage;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.chrome.ChromeDriver;
import org.openqa.selenium.chrome.ChromeOptions;

import java.time.Duration;

public abstract class AutomationInTestingUiTestBase {
    protected static final String BASE_URL = System.getenv().getOrDefault(
            "AUTOMATION_IN_TESTING_BASE_URL",
            "https://automationintesting.online"
    );

    private static final boolean HEADLESS = Boolean.parseBoolean(
            System.getenv().getOrDefault("SELENIUM_HEADLESS", "true")
    );

    protected WebDriver driver;
    protected BookingPage bookingPage;
    protected ContactPage contactPage;

    @BeforeEach
    void setUp() {
        ChromeOptions options = new ChromeOptions();
        if (HEADLESS) {
            options.addArguments("--headless=new");
        }
        options.addArguments("--window-size=1440,1200");
        options.addArguments("--no-sandbox");
        options.addArguments("--disable-dev-shm-usage");
        options.addArguments("--disable-gpu");

        driver = new ChromeDriver(options);
        driver.manage().timeouts().implicitlyWait(Duration.ZERO);
        driver.manage().window().setSize(new org.openqa.selenium.Dimension(1440, 1200));

        bookingPage = new BookingPage(driver, BASE_URL);
        contactPage = new ContactPage(driver, BASE_URL);
    }

    @AfterEach
    void tearDown() {
        if (driver != null) {
            driver.quit();
        }
    }
}
