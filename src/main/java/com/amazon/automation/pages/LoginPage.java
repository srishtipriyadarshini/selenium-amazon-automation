package com.amazon.automation.pages;

import com.amazon.automation.utils.WaitUtils;
import org.openqa.selenium.By;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.support.FindBy;


public class LoginPage extends BasePage {

    @FindBy(css = "#ap_email, #ap_email_login, input[name='email'][type='text'], input[name='email']")
    private WebElement emailInput;

    @FindBy(css = "input#continue, #continue, input[type='submit'][aria-labelledby='continue-announce']")
    private WebElement continueButton;

    @FindBy(id = "ap_password")
    private WebElement passwordInput;

    @FindBy(id = "signInSubmit")
    private WebElement signInButton;

    @FindBy(id = "auth-email-missing-alert")
    private WebElement emailMissingAlert;

    private final By emailFieldBy = By.cssSelector(
            "#ap_email, #ap_email_login, input[name='email'][type='text'], input[name='email']");
    private final By emailErrorBy = By.cssSelector(
            "div#auth-error-message-box, #auth-email-missing-alert, div.a-alert-content");
    private final By invalidEmailBy = By.cssSelector(".a-alert-content");

    public boolean isLoaded() {
        return WaitUtils.isPresent(emailFieldBy, 12);
    }

    public LoginPage enterEmail(String email) {
        type(emailInput, email);
        return this;
    }

    public LoginPage clickContinue() {
        click(continueButton);
        return this;
    }

    public LoginPage submitWithoutEmail() {
        // Click continue without entering anything - triggers form validation.
        click(continueButton);
        return this;
    }

    public boolean isEmailErrorShown() {
        return WaitUtils.isPresent(emailErrorBy, 5);
    }

    public String getErrorText() {
        WebElement err = WaitUtils.waitForVisible(invalidEmailBy, 10);
        return err.getText().trim();
    }

    public boolean isPasswordPageShown() {
        // After a valid (but possibly unregistered) email, Amazon either shows
        // the password field OR an "we couldn't find an account" message.
        return WaitUtils.isPresent(By.id("ap_password"), 8);
    }
}
