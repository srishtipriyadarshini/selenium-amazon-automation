package com.amazon.automation.listeners;

import com.amazon.automation.utils.ExtentManager;
import com.amazon.automation.utils.ReportDirectoryManager;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.testng.ISuite;
import org.testng.ISuiteListener;


public class FrameworkListener implements ISuiteListener {

    private static final Logger LOG = LogManager.getLogger(FrameworkListener.class);

    @Override
    public void onStart(ISuite suite) {
        LOG.info("##### Suite starting: {} #####", suite.getName());
        ReportDirectoryManager.prepare();
    
        ExtentManager.getInstance();
    }

    @Override
    public void onFinish(ISuite suite) {
        LOG.info("##### Suite finishing: {} #####", suite.getName());
        ExtentManager.flush();
    }
}
