package com.amazon.automation.utils;

import com.amazon.automation.config.ConfigManager;
import com.amazon.automation.constants.FrameworkConstants;
import com.aventstack.extentreports.ExtentReports;
import com.aventstack.extentreports.ExtentTest;
import com.aventstack.extentreports.reporter.ExtentSparkReporter;
import com.aventstack.extentreports.reporter.configuration.Theme;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import java.io.File;


public final class ExtentManager {

    private static final Logger LOG = LogManager.getLogger(ExtentManager.class);
    private static volatile ExtentReports extent;
    private static final ThreadLocal<ExtentTest> CURRENT_TEST = new ThreadLocal<>();
    private static final Object LOCK = new Object();

    private ExtentManager() {
    }

    public static ExtentReports getInstance() {
        if (extent == null) {
            synchronized (LOCK) {
                if (extent == null) {
                    extent = build();
                }
            }
        }
        return extent;
    }

    private static ExtentReports build() {
        String reportPath = ReportDirectoryManager.getRunDir()
                + File.separator + FrameworkConstants.EXTENT_REPORT_NAME;

        ExtentSparkReporter spark = new ExtentSparkReporter(reportPath);
        spark.config().setTheme(Theme.STANDARD);
        spark.config().setDocumentTitle(FrameworkConstants.REPORT_TITLE);
        spark.config().setReportName(FrameworkConstants.REPORT_NAME);
        spark.config().setTimeStampFormat("yyyy-MM-dd HH:mm:ss");

        ExtentReports er = new ExtentReports();
        er.attachReporter(spark);

        // System info - shows up in the report header. Useful when triaging
        // "why did this pass on my machine but fail in CI" questions.
        er.setSystemInfo("OS", System.getProperty("os.name"));
        er.setSystemInfo("Java Version", System.getProperty("java.version"));
        er.setSystemInfo("User", System.getProperty("user.name"));
        er.setSystemInfo("Browser", ConfigManager.get(FrameworkConstants.KEY_BROWSER, "unknown"));
        er.setSystemInfo("Environment", ConfigManager.get("env", "qa"));
        er.setSystemInfo("Site URL", ConfigManager.get(FrameworkConstants.KEY_TEST_SITE_URL, "unknown"));

        LOG.info("Extent report will be written to: {}", reportPath);
        return er;
    }

    public static void setCurrentTest(ExtentTest test) {
        CURRENT_TEST.set(test);
    }

    public static ExtentTest getCurrentTest() {
        return CURRENT_TEST.get();
    }

    public static void removeCurrentTest() {
        CURRENT_TEST.remove();
    }

    
    public static void flush() {
        if (extent != null) {
            extent.flush();
            LOG.info("Extent report flushed");
        }
    }
}
