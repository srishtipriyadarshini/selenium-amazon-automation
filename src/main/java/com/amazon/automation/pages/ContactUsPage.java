package com.amazon.automation.pages;

import com.amazon.automation.utils.WaitUtils;
import org.openqa.selenium.By;


public class ContactUsPage extends BasePage {

    private final By helpTopicsBy = By.cssSelector("div.help-content, h1");
    private final By helpSearchBy = By.id("helpsearch");
    private final By helpSearchButtonBy = By.cssSelector("button[type='submit']");

    public ContactUsPage open() {
        driver.get("https://www.amazon.in/hz/contact-us/foresight/hubgateway");
        return this;
    }

    public ContactUsPage openHelpHome() {
        driver.get("https://www.amazon.in/gp/help/customer/display.html");
        return this;
    }

    public boolean isLoaded() {
        return WaitUtils.isPresent(helpTopicsBy, 12)
                || WaitUtils.isPresent(By.cssSelector("[data-feature-name='help']"), 8);
    }

    public boolean pageContainsText(String text) {
        return driver.getPageSource().toLowerCase().contains(text.toLowerCase());
    }
}
