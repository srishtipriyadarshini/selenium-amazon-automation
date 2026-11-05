package com.amazon.automation.utils;

import com.amazon.automation.constants.FrameworkConstants;
import org.apache.commons.io.FileUtils;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import java.io.File;
import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.Date;


public final class ReportDirectoryManager {

    private static final Logger LOG = LogManager.getLogger(ReportDirectoryManager.class);

    // The active results folder for THIS run. Set in prepare(), read by the
    // reporter and screenshot util.
    private static String currentRunDir;
    private static String currentScreenshotDir;

    private ReportDirectoryManager() {
    }

    
    public static synchronized void prepare() {
        if (currentRunDir != null) {
            LOG.debug("Report directory already prepared this run: {}", currentRunDir);
            return;
        }

        File current = new File(FrameworkConstants.CURRENT_RESULTS_DIR);
        File archive = new File(FrameworkConstants.ARCHIVED_RESULTS_DIR);

        // Make sure both top-level folders exist.
        try {
            FileUtils.forceMkdir(current);
            FileUtils.forceMkdir(archive);
        } catch (IOException e) {
            LOG.error("Could not create top-level report folders", e);
            throw new RuntimeException(e);
        }

        // Archive whatever is in Current_Test_Results, then clean it.
        File[] existing = current.listFiles();
        if (existing != null && existing.length > 0) {
            String timestamp = new SimpleDateFormat(FrameworkConstants.TIMESTAMP_PATTERN).format(new Date());
            File archiveTarget = new File(archive, "Current_Test_Results_" + timestamp);
            try {
                FileUtils.forceMkdir(archiveTarget);
                for (File f : existing) {
                    // moveToDirectory handles both files and directories.
                    FileUtils.moveToDirectory(f, archiveTarget, false);
                }
                LOG.info("Archived previous results to: {}", archiveTarget.getAbsolutePath());
            } catch (IOException e) {
                // Don't kill the run for this - log and continue with a clean folder.
                LOG.error("Failed to archive previous results - continuing anyway", e);
                try {
                    FileUtils.cleanDirectory(current);
                } catch (IOException ce) {
                    LOG.error("Also failed to clean Current_Test_Results", ce);
                }
            }
        }

        
        String timestamp = new SimpleDateFormat(FrameworkConstants.TIMESTAMP_PATTERN).format(new Date());
        File runDir = new File(current, "Current_Test_Results_" + timestamp);
        File screenshotDir = new File(runDir, FrameworkConstants.SCREENSHOTS_SUBDIR);
        try {
            FileUtils.forceMkdir(runDir);
            FileUtils.forceMkdir(screenshotDir);
        } catch (IOException e) {
            throw new RuntimeException("Cannot create run output directory", e);
        }

        currentRunDir = runDir.getAbsolutePath();
        currentScreenshotDir = screenshotDir.getAbsolutePath();
        LOG.info("This run will write to: {}", currentRunDir);
    }

    public static String getRunDir() {
        if (currentRunDir == null) prepare();
        return currentRunDir;
    }

    public static String getScreenshotDir() {
        if (currentScreenshotDir == null) prepare();
        return currentScreenshotDir;
    }
}
