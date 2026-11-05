package com.amazon.automation.pages;

import com.amazon.automation.driver.DriverManager;
import com.amazon.automation.utils.WaitUtils;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.openqa.selenium.By;
import org.openqa.selenium.JavascriptExecutor;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.support.PageFactory;

import java.util.Set;


public abstract class BasePage {

    protected static final Logger LOG = LogManager.getLogger(BasePage.class);
    protected final WebDriver driver;

    protected BasePage() {
        this.driver = DriverManager.getDriver();
        PageFactory.initElements(driver, this);
    }

    

    protected void click(WebElement element) {
        WaitUtils.waitForClickable(element).click();
    }

    protected void click(By locator) {
        WaitUtils.waitForClickable(locator).click();
    }

    protected void type(WebElement element, String text) {
        WebElement el = WaitUtils.waitForVisible(element);
        el.clear();
        el.sendKeys(text);
    }

    protected void type(By locator, String text) {
        WebElement el = WaitUtils.waitForVisible(locator);
        el.clear();
        el.sendKeys(text);
    }

    protected String getText(WebElement element) {
        return WaitUtils.waitForVisible(element).getText().trim();
    }

    protected String getText(By locator) {
        return WaitUtils.waitForVisible(locator).getText().trim();
    }

    public boolean isDisplayed(By locator, int timeoutSec) {
        return WaitUtils.isPresent(locator, timeoutSec);
    }

    
    protected void scrollIntoView(WebElement element) {
        ((JavascriptExecutor) driver).executeScript(
                "arguments[0].scrollIntoView({block: 'center', behavior: 'instant'});",
                element);
    }

   
    protected void openNewTab(String url) {
        driver.switchTo().newWindow(org.openqa.selenium.WindowType.TAB);
        driver.get(url);
    }

    protected void openNewWindow(String url) {
        driver.switchTo().newWindow(org.openqa.selenium.WindowType.WINDOW);
        driver.get(url);
    }

    
    protected void closeExtraTabsAndReturnToMain() {
        String firstHandle = driver.getWindowHandles().iterator().next();
        Set<String> handles = driver.getWindowHandles();
        for (String handle : handles) {
            if (!handle.equals(firstHandle)) {
                driver.switchTo().window(handle).close();
            }
        }
        driver.switchTo().window(firstHandle);
    }

    public String getCurrentUrl() {
        return driver.getCurrentUrl();
    }

    public String getTitle() {
        return driver.getTitle();
    }

   
    protected boolean dismissContinueShoppingInterstitial() {
        By continueBtn = By.cssSelector(
                "button.a-button-text, input[type='submit'][value*='Continue'], button[type='submit']");
        try {
            for (WebElement candidate : driver.findElements(continueBtn)) {
                String text = candidate.getText();
                if (text != null && text.toLowerCase().contains("continue shopping")) {
                    candidate.click();
                    return true;
                }
            }
        } catch (Exception ignored) {
            // Best-effort dismissal - never let this break a test.
        }
        return false;
    }
}
