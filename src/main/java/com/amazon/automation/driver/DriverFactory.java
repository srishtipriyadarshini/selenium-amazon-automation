package com.amazon.automation.driver;

import com.amazon.automation.config.ConfigManager;
import com.amazon.automation.constants.FrameworkConstants;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.openqa.selenium.PageLoadStrategy;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.chrome.ChromeDriver;
import org.openqa.selenium.chrome.ChromeOptions;
import org.openqa.selenium.edge.EdgeDriver;
import org.openqa.selenium.edge.EdgeOptions;
import org.openqa.selenium.firefox.FirefoxDriver;
import org.openqa.selenium.firefox.FirefoxOptions;
import org.openqa.selenium.safari.SafariDriver;
import org.openqa.selenium.safari.SafariOptions;

import java.time.Duration;


public final class DriverFactory {

    private static final Logger LOG = LogManager.getLogger(DriverFactory.class);

    private DriverFactory() {
        // Static factory
    }

    public static WebDriver createDriver(BrowserType browser) {
        boolean headless = ConfigManager.getBoolean(FrameworkConstants.KEY_HEADLESS, false);
        int pageLoadSec = ConfigManager.getInt(
                FrameworkConstants.KEY_PAGE_LOAD_TIMEOUT,
                FrameworkConstants.DEFAULT_PAGE_LOAD_TIMEOUT_SECONDS);
        int implicitSec = ConfigManager.getInt(
                FrameworkConstants.KEY_IMPLICIT_WAIT,
                FrameworkConstants.DEFAULT_IMPLICIT_WAIT_SECONDS);

        LOG.info("Creating {} driver (headless={}, pageLoad={}s, implicit={}s)",
                browser, headless, pageLoadSec, implicitSec);

        WebDriver driver;
        switch (browser) {
            case CHROME:
                driver = new ChromeDriver(chromeOptions(headless));
                break;
            case FIREFOX:
                driver = new FirefoxDriver(firefoxOptions(headless));
                break;
            case EDGE:
                driver = new EdgeDriver(edgeOptions(headless));
                break;
            case SAFARI:
              
                driver = new SafariDriver(safariOptions());
                break;
            default:
                throw new IllegalStateException("Unhandled browser: " + browser);
        }

        driver.manage().window().maximize();
        driver.manage().timeouts().pageLoadTimeout(Duration.ofSeconds(pageLoadSec));
        driver.manage().timeouts().implicitlyWait(Duration.ofSeconds(implicitSec));

        return driver;
    }

    private static ChromeOptions chromeOptions(boolean headless) {
        ChromeOptions options = new ChromeOptions();

        
        options.setPageLoadStrategy(PageLoadStrategy.NORMAL);

        
        options.addArguments("--disable-notifications");
        options.addArguments("--disable-popup-blocking");
        options.addArguments("--disable-infobars");
        options.addArguments("--remote-allow-origins=*");
        options.setExperimentalOption("excludeSwitches", new String[]{"enable-automation"});

        if (headless) {
            
            options.addArguments("--headless=new");
            options.addArguments("--window-size=1920,1080");
            options.addArguments("--disable-gpu");
        }
        return options;
    }

    private static FirefoxOptions firefoxOptions(boolean headless) {
        FirefoxOptions options = new FirefoxOptions();
        options.setPageLoadStrategy(PageLoadStrategy.NORMAL);
        if (headless) {
            options.addArguments("-headless");
        }
        return options;
    }

    private static EdgeOptions edgeOptions(boolean headless) {
        EdgeOptions options = new EdgeOptions();
        options.setPageLoadStrategy(PageLoadStrategy.NORMAL);
        options.addArguments("--disable-notifications");
        if (headless) {
            options.addArguments("--headless=new");
            options.addArguments("--window-size=1920,1080");
        }
        return options;
    }

    private static SafariOptions safariOptions() {
        return new SafariOptions();
    }
}
