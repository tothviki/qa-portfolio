package com.portfolio.pages.mobile;

import io.appium.java_client.android.AndroidDriver;
import org.openqa.selenium.By;

public final class MobileContactPage extends MobilePage {
    private static final By NAME_INPUT = By.cssSelector("#name");
    private static final By EMAIL_INPUT = By.cssSelector("#email");
    private static final By PHONE_INPUT = By.cssSelector("#phone");
    private static final By SUBJECT_INPUT = By.cssSelector("#subject");
    private static final By MESSAGE_INPUT = By.cssSelector("#description");
    private static final By SUBMIT_BUTTON = By.xpath("//button[normalize-space()='Submit']");
    private static final By CONTACT_HEADING = By.xpath(
            "//*[self::h1 or self::h2 or self::h3][normalize-space()='Send Us a Message']"
    );
    private static final By VALIDATION_FEEDBACK = By.xpath(
            "//*[contains(translate(normalize-space(.), 'ABCDEFGHIJKLMNOPQRSTUVWXYZ', 'abcdefghijklmnopqrstuvwxyz'), 'must') or " +
                    "contains(translate(normalize-space(.), 'ABCDEFGHIJKLMNOPQRSTUVWXYZ', 'abcdefghijklmnopqrstuvwxyz'), 'valid') or " +
                    "contains(translate(normalize-space(.), 'ABCDEFGHIJKLMNOPQRSTUVWXYZ', 'abcdefghijklmnopqrstuvwxyz'), 'error') or " +
                    "contains(translate(normalize-space(.), 'ABCDEFGHIJKLMNOPQRSTUVWXYZ', 'abcdefghijklmnopqrstuvwxyz'), 'required') or " +
                    "contains(translate(normalize-space(.), 'ABCDEFGHIJKLMNOPQRSTUVWXYZ', 'abcdefghijklmnopqrstuvwxyz'), 'between') or " +
                    "contains(translate(normalize-space(.), 'ABCDEFGHIJKLMNOPQRSTUVWXYZ', 'abcdefghijklmnopqrstuvwxyz'), 'phone') or " +
                    "contains(translate(normalize-space(.), 'ABCDEFGHIJKLMNOPQRSTUVWXYZ', 'abcdefghijklmnopqrstuvwxyz'), 'email')]"
    );

    public MobileContactPage(AndroidDriver driver) {
        super(driver);
    }

    public void submitInvalidMessage() {
        visible(CONTACT_HEADING);
        type(NAME_INPUT, "A");
        type(EMAIL_INPUT, "not-an-email");
        type(PHONE_INPUT, "123");
        type(SUBJECT_INPUT, "No");
        type(MESSAGE_INPUT, "Too short");
        click(SUBMIT_BUTTON);
    }

    public void expectValidationFeedback() {
        visible(VALIDATION_FEEDBACK);
    }
}
