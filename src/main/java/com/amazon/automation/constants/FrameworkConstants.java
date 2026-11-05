package com.amazon.automation.constants;


public final class FrameworkConstants {

    private FrameworkConstants() {
        // Prevent instantiation - constants only
    }

    // ===== Path constants =====
    // We use System.getProperty("user.dir") so paths are portable across machines.
    private static final String USER_DIR = System.getProperty("user.dir");

    public static final String CONFIG_FILE_PATH =
            USER_DIR + "/src/test/resources/config/config.properties";

    public static final String TESTDATA_JSON_PATH =
            USER_DIR + "/src/test/resources/testdata/testdata.json";

    public static final String TESTDATA_EXCEL_PATH =
            USER_DIR + "/src/test/resources/testdata/testdata.xlsx";

    public static final String CURRENT_RESULTS_DIR =
            USER_DIR + "/Current_Test_Results";

    public static final String ARCHIVED_RESULTS_DIR =
            USER_DIR + "/Archived_Test_Results";

    public static final String LOGS_DIR =
            USER_DIR + "/logs";

    public static final String SCREENSHOTS_SUBDIR = "screenshots";

    public static final String EXTENT_REPORT_NAME = "ExtentReport.html";

    // ===== Default timeouts (overridden by config.properties) =====
    public static final int DEFAULT_IMPLICIT_WAIT_SECONDS = 0;     
    public static final int DEFAULT_EXPLICIT_WAIT_SECONDS = 20;
    public static final int DEFAULT_PAGE_LOAD_TIMEOUT_SECONDS = 60;
    public static final int DEFAULT_POLLING_INTERVAL_MILLIS = 500;

    // ===== Config property keys (avoids typos like "broswerName" lurking) =====
    public static final String KEY_BROWSER = "browserName";
    public static final String KEY_TEST_SITE_URL = "testSiteURL";
    public static final String KEY_IMPLICIT_WAIT = "implicitWait";
    public static final String KEY_EXPLICIT_WAIT = "explicitWait";
    public static final String KEY_PAGE_LOAD_TIMEOUT = "pageLoadTimeout";
    public static final String KEY_HEADLESS = "headless";
    public static final String KEY_SCREENSHOT_ON_FAILURE = "screenshotOnFailure";

    // ===== Report metadata =====
    public static final String REPORT_TITLE = "Amazon.in Automation Report";
    public static final String REPORT_NAME = "Regression Suite";

    // ===== Timestamp format for the rotating results folder =====
    public static final String TIMESTAMP_PATTERN = "yyyy-MM-dd_HH-mm-ss";
}
