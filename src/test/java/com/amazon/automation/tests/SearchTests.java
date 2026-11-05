package com.amazon.automation.tests;

import com.amazon.automation.constants.FrameworkConstants;
import com.amazon.automation.pages.HomePage;
import com.amazon.automation.pages.SearchResultsPage;
import com.amazon.automation.utils.ExcelReader;
import org.testng.Assert;
import org.testng.annotations.DataProvider;
import org.testng.annotations.Test;

import java.util.List;
import java.util.Map;

public class SearchTests extends BaseTest {

    @DataProvider(name = "searchData")
    public Object[][] searchData() {
        List<Map<String, String>> rows = ExcelReader.readSheet(
                FrameworkConstants.TESTDATA_EXCEL_PATH, "search");

        Object[][] data = new Object[rows.size()][2];
        for (int i = 0; i < rows.size(); i++) {
            Map<String, String> row = rows.get(i);
            data[i][0] = row.get("query");
            data[i][1] = Boolean.parseBoolean(row.get("expectResults"));
        }
        return data;
    }

    @Test(groups = {"smoke", "search"},
            description = "Search for a valid product returns results",
            dataProvider = "searchData")
    public void search_returnsAppropriateResults(String query, boolean expectResults) {
        HomePage home = openHomePage();
        SearchResultsPage results = home.searchFor(query);

        if (expectResults) {
            Assert.assertTrue(results.hasResults(),
                    "Expected results for query: " + query);
            Assert.assertTrue(results.getResultCount() > 0,
                    "Result count should be > 0 for query: " + query);
        } else {
            // Negative path: gibberish query should yield zero results OR a
            // "no results" banner. Either is acceptable.
            boolean noResults = results.isNoResultsShown() || results.getResultCount() == 0;
            Assert.assertTrue(noResults,
                    "Expected no results for query: " + query);
        }
    }
}
