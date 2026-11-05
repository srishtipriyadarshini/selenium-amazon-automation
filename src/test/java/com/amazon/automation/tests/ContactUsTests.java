package com.amazon.automation.tests;

import com.amazon.automation.driver.DriverManager;
import com.amazon.automation.pages.ContactUsPage;
import com.amazon.automation.pages.HomePage;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WindowType;
import org.testng.Assert;
import org.testng.annotations.Test;

import java.util.Set;

public class ContactUsTests extends BaseTest {

    @Test(groups = {"regression", "help"},
            description = "Customer service / help page loads")
    public void contactUs_helpPageLoads() {
        ContactUsPage help = new ContactUsPage().openHelpHome();
        Assert.assertTrue(help.isLoaded(), "Help page should load");
        Assert.assertTrue(
                help.pageContainsText("help") || help.pageContainsText("customer"),
                "Help page should reference 'help' or 'customer' somewhere");
    }

    @Test(groups = {"regression", "help", "selenium4"},
            description = "Selenium 4 newWindow: open help in new tab while home stays in original")
    public void contactUs_newTab_keepsBothPagesAccessible() {
        HomePage home = openHomePage();
        WebDriver driver = DriverManager.getDriver();
        String homeHandle = driver.getWindowHandle();

        // Selenium 4: open a new tab AND switch to it in one call.
        driver.switchTo().newWindow(WindowType.TAB);
        driver.get("https://www.amazon.in/gp/help/customer/display.html");

        Set<String> handles = driver.getWindowHandles();
        Assert.assertEquals(handles.size(), 2,
                "Two windows/tabs should now be open");

        // Switch back to original tab and verify home page is still there.
        driver.switchTo().window(homeHandle);
        Assert.assertTrue(home.isLoaded(),
                "Original home tab should still be functional after new-tab open");
    }
}
