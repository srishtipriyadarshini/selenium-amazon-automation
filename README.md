# Amazon.in Selenium Automation Framework

A test automation framework for `https://www.amazon.in/` built with **Selenium 4**, **TestNG**, and **Maven**.

## Overview

| Component | Choice | Why |
|---|---|---|
| Build tool | Maven | Standard, IDE-friendly, easy CI |
| Test runner | TestNG | Groups, parallel execution, data providers |
| Driver mgmt | Selenium Manager (built into Selenium 4.6+) | No WebDriverManager dependency needed |
| Design pattern | Page Object Model + PageFactory | Maintainable, test code stays declarative |
| Waits | Explicit (`WebDriverWait`) + Fluent | Implicit waits disabled to avoid mixed-wait bugs |
| Reports | ExtentReports 5 (Spark) | HTML, embeds screenshots, metadata, history |
| Logging | Log4j2 with Routing appender | Per-test log files + rolling framework log |
| Test data | `config.properties` + JSON + Excel (POI) | One mechanism per data type |
| Parallel safety | `ThreadLocal<WebDriver>` | Each test thread owns its driver |

## Project structure

```
amazon-automation/
├── pom.xml                                    Maven build, dependency versions
├── README.md                                  This file
├── src/main/java/com/amazon/automation/
│   ├── config/        ConfigManager           Read config.properties + -D overrides
│   ├── constants/     FrameworkConstants      Paths, timeouts, keys
│   ├── driver/        BrowserType, DriverFactory, DriverManager
│   ├── listeners/     TestListener, FrameworkListener, RetryAnalyzer
│   ├── pages/         BasePage + page objects
│   └── utils/         WaitUtils, ScreenshotUtils, ExtentManager,
│                      ReportDirectoryManager, ExcelReader, JsonDataReader
├── src/main/resources/
│   └── log4j2.xml                             Logger config
├── src/test/java/com/amazon/automation/tests/ Test classes
├── src/test/resources/
│   ├── config/config.properties               Runtime config (browser, URL, waits)
│   ├── testdata/testdata.json                 JSON test data
│   ├── testdata/testdata.xlsx                 Excel test data (data provider)
│   └── suites/                                TestNG XML files
├── Current_Test_Results/                      This run's reports + screenshots
├── Archived_Test_Results/                     Previous runs (auto-archived)
└── logs/                                      Framework + per-test logs
```

## Prerequisites

- **Java 11+** (`java -version`)
- **Maven 3.6+** (`mvn -v`)
- **Chrome**, **Firefox**, or **Edge** installed (Selenium Manager auto-downloads matching drivers)
- Internet connection (it's a UI test against amazon.in)

## Setup

```cmd
git clone <your-repo-url>
cd amazon-automation
mvn clean install -DskipTests
```

## Running tests

### Default regression suite

```cmd
mvn clean test
```

### Specific suite

```cmd
mvn clean test -DsuiteXmlFile=src/test/resources/suites/testng-smoke.xml
mvn clean test -DsuiteXmlFile=src/test/resources/suites/testng-parallel.xml
mvn clean test -DsuiteXmlFile=src/test/resources/suites/testng-crossbrowser.xml
```

### From an IDE

- IntelliJ / Eclipse: import as Maven project, right-click any test class → Run.
- Run a TestNG suite: right-click the XML in `src/test/resources/suites/` → Run.

## Viewing results

After each run:

- **HTML report**: `Current_Test_Results/Current_Test_Results_<timestamp>/ExtentReport.html`
- **Screenshots**: `Current_Test_Results/Current_Test_Results_<timestamp>/screenshots/`
- **Framework log**: `logs/framework.log` (rolling, all events)
- **Per-test logs**: `logs/by-test/<testName>.log`

Old `Current_Test_Results/` contents are automatically moved to `Archived_Test_Results/` before each run.

## Modifying configuration

Edit `src/test/resources/config/config.properties`:

```properties
browserName=chrome           # chrome | firefox | edge | safari
testSiteURL=https://www.amazon.in/
implicitWait=0               # Keep 0 - explicit waits only
explicitWait=20
pageLoadTimeout=60
headless=false
screenshotOnFailure=true
retryCount=0                 # 0 disables retries
env=qa
```


## Test data

- **JSON** (`testdata.json`): use for nested or grouped data accessed by dotted path: `JsonDataReader.getString("login.invalidEmail")`.
- **Excel** (`testdata.xlsx`): use as a `@DataProvider` source. First row of each sheet is the header.

## TestNG groups

| Group | Used for |
|---|---|
| `smoke` | Critical-path tests run on every commit |
| `regression` | Full suite |
| `negative` | Error-path / validation tests |
| `selenium4` | Tests exercising Selenium 4-specific APIs |
| `home`, `search`, `login`, `cart`, `account`, `help`, `product` | By page area |



