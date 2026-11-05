package com.amazon.automation.listeners;

import com.amazon.automation.config.ConfigManager;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.testng.IRetryAnalyzer;
import org.testng.ITestResult;


public class RetryAnalyzer implements IRetryAnalyzer {

    private static final Logger LOG = LogManager.getLogger(RetryAnalyzer.class);
    private int attempt = 0;

    @Override
    public boolean retry(ITestResult result) {
        int maxRetries = ConfigManager.getInt("retryCount", 0);
        if (attempt < maxRetries) {
            attempt++;
            LOG.warn("Retrying {} (attempt {}/{})",
                    result.getMethod().getMethodName(), attempt, maxRetries);
            return true;
        }
        return false;
    }
}
