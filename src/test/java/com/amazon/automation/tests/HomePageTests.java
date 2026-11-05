package com.amazon.automation.tests;

import com.amazon.automation.listeners.RetryAnalyzer;
import com.amazon.automation.pages.HomePage;
import org.testng.Assert;
import org.testng.annotations.Test;

public class HomePageTests extends BaseTest {

    @Test(groups = {"smoke", "home"},
            description = "Home page loads with title containing 'Amazon'",
            retryAnalyzer = RetryAnalyzer.class)
    public void homePage_loadsSuccessfully() {
        HomePage home = openHomePage();
        Assert.assertTrue(home.isLoaded(), "Home page should load with search bar visible");
        Assert.assertTrue(
                home.getTitle().toLowerCase().contains("amazon"),
                "Page title should mention Amazon. Actual: " + home.getTitle());
    }

    @Test(groups = {"regression", "home", "selenium4"},
            description = "Relative locator finds search-category dropdown to-left-of search box")
    public void homePage_relativeLocatorFindsCategoryDropdown() {
        HomePage home = openHomePage();
        // The category select sits visually to the left of the search box.
        // We locate it via Selenium 4's RelativeLocator rather than its ID.
        Assert.assertNotNull(
                home.getCategoryDropdownViaRelativeLocator(),
                "Category dropdown should be found via relative locator");
    }
}
