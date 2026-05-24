package com.portfolio.pages;

import org.openqa.selenium.By;
import org.openqa.selenium.WebDriver;

public class ContactPage extends BasePage {
    private static final By HEADING = By.xpath(
            "//*[self::h1 or self::h2 or self::h3][normalize-space()='Send Us a Message']"
    );
    private static final By NAME_INPUT = By.cssSelector("#name");
    private static final By EMAIL_INPUT = By.cssSelector("#email");
    private static final By PHONE_INPUT = By.cssSelector("#phone");
    private static final By SUBJECT_INPUT = By.cssSelector("#subject");
    private static final By MESSAGE_INPUT = By.cssSelector("#description");
    private static final By SUBMIT_BUTTON = By.xpath("//button[normalize-space()='Submit']");
    private static final By SUCCESS_MESSAGE = By.xpath(
            "//*[contains(translate(normalize-space(.), 'ABCDEFGHIJKLMNOPQRSTUVWXYZ', 'abcdefghijklmnopqrstuvwxyz'), 'thank you') or " +
                    "contains(translate(normalize-space(.), 'ABCDEFGHIJKLMNOPQRSTUVWXYZ', 'abcdefghijklmnopqrstuvwxyz'), 'thanks') or " +
                    "contains(translate(normalize-space(.), 'ABCDEFGHIJKLMNOPQRSTUVWXYZ', 'abcdefghijklmnopqrstuvwxyz'), 'message sent')]"
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

    public ContactPage(WebDriver driver, String baseUrl) {
        super(driver, baseUrl);
    }

    public void gotoPage() {
        open("/#contact");
        visible(HEADING);
    }

    public void fillMessage(String name, String email, String phone, String subject, String message) {
        type(NAME_INPUT, name);
        type(EMAIL_INPUT, email);
        type(PHONE_INPUT, phone);
        type(SUBJECT_INPUT, subject);
        type(MESSAGE_INPUT, message);
    }

    public void submit() {
        click(SUBMIT_BUTTON);
    }

    public void expectSuccessMessage() {
        visible(SUCCESS_MESSAGE);
    }
}
