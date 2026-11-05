package com.amazon.automation.pages;

import com.amazon.automation.utils.WaitUtils;
import org.openqa.selenium.By;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.support.FindBy;

import java.util.UUID;


public class CreateAccountPage extends BasePage {

    @FindBy(id = "ap_customer_name")
    private WebElement nameInput;

    @FindBy(id = "ap_email")
    private WebElement emailInput;

    @FindBy(id = "ap_password")
    private WebElement passwordInput;

    @FindBy(id = "ap_password_check")
    private WebElement passwordCheckInput;

    @FindBy(id = "continue")
    private WebElement createAccountButton;

    private final By validationErrorBy = By.cssSelector("div.a-alert-content, div.a-box-inner div.a-alert-content");

    public CreateAccountPage open() {
     
        new HomePage().open().goToLogin();
        By createAccountLink = By.cssSelector(
                "#createAccountSubmit, a[href*='/ap/register']");
        if (WaitUtils.isPresent(createAccountLink, 4)) {
            driver.findElement(createAccountLink).click();
            if (WaitUtils.isPresent(By.id("ap_customer_name"), 8)) {
                return this;
            }
        }

    
        By emailFieldBy = By.cssSelector(
                "#ap_email, #ap_email_login, input[name='email'][type='text'], input[name='email']");
        By continueBtnBy = By.cssSelector(
                "input#continue, #continue, input[type='submit'][aria-labelledby='continue-announce']");
        if (WaitUtils.isPresent(emailFieldBy, 8)) {
            String throwaway = "qa.user." + UUID.randomUUID().toString().substring(0, 8)
                    + "@example.com";
            WebElement field = driver.findElement(emailFieldBy);
            field.clear();
            field.sendKeys(throwaway);
            if (WaitUtils.isPresent(continueBtnBy, 4)) {
                driver.findElement(continueBtnBy).click();
            }
        }

        
        By proceedBy = By.xpath(
                "//*[self::a or self::button or self::input or self::span]"
                        + "[contains(normalize-space(.), 'Proceed to create')]"
                        + " | //input[@type='submit' and contains(@aria-labelledby, 'create')]");
        if (WaitUtils.isPresent(proceedBy, 6)) {
            driver.findElement(proceedBy).click();
        }
        return this;
    }

    public boolean isLoaded() {
        return WaitUtils.isPresent(By.id("ap_customer_name"), 12);
    }

    public CreateAccountPage fillForm(String name, String email, String password, String confirm) {
        if (name != null) type(nameInput, name);
        if (email != null) type(emailInput, email);
        if (password != null) type(passwordInput, password);
        if (confirm != null) type(passwordCheckInput, confirm);
        return this;
    }

    public CreateAccountPage submit() {
        click(createAccountButton);
        return this;
    }

    public boolean hasValidationError() {
        return WaitUtils.isPresent(validationErrorBy, 6);
    }

    public String getValidationError() {
        if (hasValidationError()) {
            return driver.findElement(validationErrorBy).getText().trim();
        }
        return "";
    }
}
