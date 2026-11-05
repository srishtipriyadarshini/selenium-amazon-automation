package com.amazon.automation.utils;

import com.amazon.automation.driver.DriverManager;
import org.apache.commons.io.FileUtils;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.openqa.selenium.OutputType;
import org.openqa.selenium.TakesScreenshot;
import org.openqa.selenium.WebDriver;

import java.io.File;
import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.Date;


public final class ScreenshotUtils {

    private static final Logger LOG = LogManager.getLogger(ScreenshotUtils.class);

    private ScreenshotUtils() {
    }

   
    public static String capture(String screenshotDir, String testName) {
        WebDriver driver;
        try {
            driver = DriverManager.getDriver();
        } catch (IllegalStateException e) {
            LOG.warn("Cannot capture screenshot - driver not initialised");
            return null;
        }

        try {
            File source = ((TakesScreenshot) driver).getScreenshotAs(OutputType.FILE);
            String timestamp = new SimpleDateFormat("yyyyMMdd_HHmmss_SSS").format(new Date());
            String safeName = testName.replaceAll("[^a-zA-Z0-9._-]", "_");
            String filename = safeName + "_" + timestamp + ".png";
            File dest = new File(screenshotDir, filename);
            FileUtils.copyFile(source, dest);
            LOG.info("Screenshot saved: {}", dest.getAbsolutePath());
            return dest.getAbsolutePath();
        } catch (IOException e) {
            LOG.error("Failed to save screenshot", e);
            return null;
        } catch (Exception e) {
            // Could be SessionNotCreatedException if browser already closed.
            LOG.error("Failed to capture screenshot: {}", e.getMessage());
            return null;
        }
    }
}
