package com.amazon.automation.listeners;

import com.amazon.automation.utils.ExtentManager;
import com.amazon.automation.utils.ReportDirectoryManager;
import com.amazon.automation.utils.ScreenshotUtils;
import com.aventstack.extentreports.ExtentTest;
import com.aventstack.extentreports.Status;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.ThreadContext;
import org.apache.logging.log4j.Logger;
import org.testng.ITestContext;
import org.testng.ITestListener;
import org.testng.ITestResult;


public class TestListener implements ITestListener {

    private static final Logger LOG = LogManager.getLogger(TestListener.class);

    @Override
    public void onStart(ITestContext context) {
        LOG.info("===== Starting test suite/context: {} =====", context.getName());
    }

    @Override
    public void onTestStart(ITestResult result) {
        String testName = result.getMethod().getMethodName();

       
        ThreadContext.put("testName", testName);

        LOG.info("---- Test started: {} ----", testName);

        ExtentTest test = ExtentManager.getInstance().createTest(
                testName,
                result.getMethod().getDescription() != null
                        ? result.getMethod().getDescription()
                        : "No description");

        // Add groups as ExtentReports categories so the report filter works.
        for (String group : result.getMethod().getGroups()) {
            test.assignCategory(group);
        }

        ExtentManager.setCurrentTest(test);
    }

    @Override
    public void onTestSuccess(ITestResult result) {
        ExtentTest test = ExtentManager.getCurrentTest();
        if (test != null) {
            test.log(Status.PASS, "Test passed");
        }
        LOG.info("PASS: {}", result.getMethod().getMethodName());
        cleanup();
    }

    @Override
    public void onTestFailure(ITestResult result) {
        String testName = result.getMethod().getMethodName();
        Throwable error = result.getThrowable();

        LOG.error("FAIL: {} - {}", testName,
                error != null ? error.getMessage() : "unknown error");

        ExtentTest test = ExtentManager.getCurrentTest();
        if (test != null) {
            test.fail(error != null ? error : new Throwable("Test failed - no exception captured"));

            String shotPath = ScreenshotUtils.capture(
                    ReportDirectoryManager.getScreenshotDir(), testName);
            if (shotPath != null) {
                try {
                    
                    test.addScreenCaptureFromPath(shotPath);
                } catch (Exception e) {
                    LOG.warn("Could not attach screenshot to report: {}", e.getMessage());
                }
            }
        }
        cleanup();
    }

    @Override
    public void onTestSkipped(ITestResult result) {
        ExtentTest test = ExtentManager.getCurrentTest();
        if (test != null) {
            Throwable cause = result.getThrowable();
            test.skip(cause != null ? cause.getMessage() : "Test skipped");
        }
        LOG.warn("SKIP: {}", result.getMethod().getMethodName());
        cleanup();
    }

    private void cleanup() {
        
        ExtentManager.removeCurrentTest();
        ThreadContext.remove("testName");
    }
}
