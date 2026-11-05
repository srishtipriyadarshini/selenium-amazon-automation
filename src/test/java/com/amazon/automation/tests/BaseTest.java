package com.amazon.automation.tests;

import com.amazon.automation.config.ConfigManager;
import com.amazon.automation.constants.FrameworkConstants;
import com.amazon.automation.driver.BrowserType;
import com.amazon.automation.driver.DriverFactory;
import com.amazon.automation.driver.DriverManager;
import com.amazon.automation.pages.HomePage;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.openqa.selenium.WebDriver;
import org.testng.annotations.AfterMethod;
import org.testng.annotations.BeforeMethod;
import org.testng.annotations.Optional;
import org.testng.annotations.Parameters;

import java.lang.reflect.Method;


public abstract class BaseTest {

    protected static final Logger LOG = LogManager.getLogger(BaseTest.class);

    @BeforeMethod(alwaysRun = true)
    @Parameters({"browser"})
    public void setUp(@Optional("") String suiteBrowser, Method method) {
        String browserName = resolveBrowserName(suiteBrowser);
        LOG.info(">>> Setting up driver for test '{}' with browser '{}'",
                method.getName(), browserName);

        WebDriver driver = DriverFactory.createDriver(BrowserType.fromString(browserName));
        DriverManager.setDriver(driver);
    }

    @AfterMethod(alwaysRun = true)
    public void tearDown(Method method) {
        LOG.info("<<< Tearing down driver for test '{}'", method.getName());
        DriverManager.quitDriver();
    }

    /**
     * Helper for tests that always need to start at the home page.
     * Returns the page object so tests can chain.
     */
    protected HomePage openHomePage() {
        return new HomePage().open();
    }

    private String resolveBrowserName(String suiteBrowser) {
        // Suite-XML param wins if non-empty - allows per-suite browser pinning.
        if (suiteBrowser != null && !suiteBrowser.isEmpty()) {
            return suiteBrowser;
        }
        // Otherwise fall back to config (which already handles -D overrides).
        return ConfigManager.get(FrameworkConstants.KEY_BROWSER, "chrome");
    }
}
