package com.amazon.automation.pages;

import com.amazon.automation.config.ConfigManager;
import com.amazon.automation.constants.FrameworkConstants;
import com.amazon.automation.utils.WaitUtils;
import org.openqa.selenium.By;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.support.FindBy;
import org.openqa.selenium.support.locators.RelativeLocator;


public class HomePage extends BasePage {

    @FindBy(id = "twotabsearchtextbox")
    private WebElement searchBox;

    @FindBy(id = "nav-search-submit-button")
    private WebElement searchButton;

    @FindBy(id = "nav-link-accountList")
    private WebElement accountAndListsLink;

    @FindBy(id = "nav-cart")
    private WebElement cartLink;

    @FindBy(id = "nav-logo-sprites")
    private WebElement logo;

    private final By searchDropdownBy = By.id("searchDropdownBox");

    public HomePage open() {
        String url = ConfigManager.get(FrameworkConstants.KEY_TEST_SITE_URL, "https://www.amazon.in/");
        driver.get(url);
        WaitUtils.waitForVisible(searchBox);
        LOG.info("Home page loaded: {}", driver.getCurrentUrl());
        return this;
    }

    public boolean isLoaded() {
        return WaitUtils.isPresent(By.id("twotabsearchtextbox"), 10)
                && driver.getTitle() != null
                && driver.getTitle().toLowerCase().contains("amazon");
    }

    public SearchResultsPage searchFor(String query) {
        type(searchBox, query);
        click(searchButton);
        return new SearchResultsPage();
    }

  
    public WebElement getCategoryDropdownViaRelativeLocator() {
        return driver.findElement(
                RelativeLocator.with(By.tagName("select"))
                        .toLeftOf(By.id("twotabsearchtextbox")));
    }

    public LoginPage goToLogin() {
    
        click(accountAndListsLink);
        return new LoginPage();
    }

    public CartPage goToCart() {
        click(cartLink);
        return new CartPage();
    }

    public String getLogoTooltip() {
        return logo.getAttribute("alt");
    }
}
