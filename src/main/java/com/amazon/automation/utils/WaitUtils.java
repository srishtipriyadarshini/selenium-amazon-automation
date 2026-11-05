package com.amazon.automation.utils;

import com.amazon.automation.config.ConfigManager;
import com.amazon.automation.constants.FrameworkConstants;
import com.amazon.automation.driver.DriverManager;
import org.openqa.selenium.By;
import org.openqa.selenium.NoSuchElementException;
import org.openqa.selenium.StaleElementReferenceException;
import org.openqa.selenium.TimeoutException;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.support.ui.ExpectedConditions;
import org.openqa.selenium.support.ui.FluentWait;
import org.openqa.selenium.support.ui.WebDriverWait;

import java.time.Duration;
import java.util.function.Function;


public final class WaitUtils {

    private WaitUtils() {
    }

    private static int explicitTimeout() {
        return ConfigManager.getInt(
                FrameworkConstants.KEY_EXPLICIT_WAIT,
                FrameworkConstants.DEFAULT_EXPLICIT_WAIT_SECONDS);
    }

    private static WebDriverWait newWait(int seconds) {
        return new WebDriverWait(DriverManager.getDriver(), Duration.ofSeconds(seconds));
    }

    public static WebElement waitForVisible(By locator) {
        return newWait(explicitTimeout()).until(ExpectedConditions.visibilityOfElementLocated(locator));
    }

    public static WebElement waitForVisible(By locator, int timeoutSeconds) {
        return newWait(timeoutSeconds).until(ExpectedConditions.visibilityOfElementLocated(locator));
    }

    public static WebElement waitForVisible(WebElement element) {
        return newWait(explicitTimeout()).until(ExpectedConditions.visibilityOf(element));
    }

    public static WebElement waitForClickable(By locator) {
        return newWait(explicitTimeout()).until(ExpectedConditions.elementToBeClickable(locator));
    }

    public static WebElement waitForClickable(WebElement element) {
        return newWait(explicitTimeout()).until(ExpectedConditions.elementToBeClickable(element));
    }

    public static boolean waitForInvisible(By locator) {
        return newWait(explicitTimeout()).until(ExpectedConditions.invisibilityOfElementLocated(locator));
    }

    public static boolean waitForUrlContains(String fragment) {
        return newWait(explicitTimeout()).until(ExpectedConditions.urlContains(fragment));
    }

    public static boolean waitForTitleContains(String fragment) {
        return newWait(explicitTimeout()).until(ExpectedConditions.titleContains(fragment));
    }

   
    public static boolean isPresent(By locator, int timeoutSeconds) {
        try {
            newWait(timeoutSeconds).until(ExpectedConditions.presenceOfElementLocated(locator));
            return true;
        } catch (TimeoutException e) {
            return false;
        }
    }

    
    public static <T> T fluentWait(Function<WebDriver, T> condition, int timeoutSeconds) {
        FluentWait<WebDriver> wait = new FluentWait<>(DriverManager.getDriver())
                .withTimeout(Duration.ofSeconds(timeoutSeconds))
                .pollingEvery(Duration.ofMillis(FrameworkConstants.DEFAULT_POLLING_INTERVAL_MILLIS))
                .ignoring(NoSuchElementException.class)
                .ignoring(StaleElementReferenceException.class);
        return wait.until(condition);
    }
}
