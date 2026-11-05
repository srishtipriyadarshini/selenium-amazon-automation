package com.amazon.automation.pages;

import com.amazon.automation.utils.WaitUtils;
import org.openqa.selenium.By;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.support.FindBy;
import org.openqa.selenium.support.locators.RelativeLocator;

import java.util.List;

public class SearchResultsPage extends BasePage {

    private final By resultCardsBy = By.cssSelector("div[data-component-type='s-search-result']");
    private final By resultTitlesBy = By.cssSelector("div[data-component-type='s-search-result'] h2 span");
    private final By noResultsBy = By.cssSelector(
            "div.s-no-outline, div.a-row.a-spacing-medium.a-spacing-top-large, "
                    + "div.s-suggestion-container");

    @FindBy(css = "div[data-component-type='s-search-result'] h2 a")
    private List<WebElement> resultLinks;

    public boolean hasResults() {
        try {
            WaitUtils.waitForVisible(resultCardsBy, 15);
            return true;
        } catch (Exception e) {
            return false;
        }
    }

    public boolean isNoResultsShown() {
        if (WaitUtils.isPresent(noResultsBy, 4)) {
            return true;
        }
        try {
            String body = driver.findElement(By.tagName("body")).getText().toLowerCase();
            return body.contains("no results for")
                    || body.contains("did not match any products")
                    || body.contains("we couldn't find that page");
        } catch (Exception e) {
            return false;
        }
    }

    public int getResultCount() {
        return driver.findElements(resultCardsBy).size();
    }

    public List<WebElement> getResultTitles() {
        return driver.findElements(resultTitlesBy);
    }

    public ProductDetailsPage openFirstResult() {
       
        List<WebElement> cards = driver.findElements(resultCardsBy);
        if (cards.isEmpty()) {
            throw new IllegalStateException("No search results to click on");
        }
        String href = null;
        for (WebElement card : cards) {
          
            List<WebElement> sponsoredBadge = card.findElements(
                    By.cssSelector("span.puis-sponsored-label-text, span[aria-label='View Sponsored ad']"));
            if (!sponsoredBadge.isEmpty()) {
                continue;
            }
            List<WebElement> anchors = card.findElements(
                    By.cssSelector("a.a-link-normal[href*='/dp/'], h2 a[href*='/dp/']"));
            if (!anchors.isEmpty()) {
                String candidate = anchors.get(0).getAttribute("href");
                if (candidate != null && !candidate.isEmpty()) {
                    href = candidate;
                    break;
                }
            }
        }
        if (href == null) {
            // Fallback: any /dp/ link inside any result card, sponsored or not.
            List<WebElement> anyDp = driver.findElements(
                    By.cssSelector("div[data-component-type='s-search-result'] a[href*='/dp/']"));
            if (!anyDp.isEmpty()) {
                href = anyDp.get(0).getAttribute("href");
            }
        }
        if (href == null) {
            throw new IllegalStateException("Could not resolve a product link from search results");
        }
        driver.get(href);
        return new ProductDetailsPage();
    }

   
    public WebElement findSortDropdownBelowBreadcrumb() {
        WebElement breadcrumb = driver.findElement(By.cssSelector("div.s-breadcrumb"));
        return driver.findElement(
                RelativeLocator.with(By.tagName("select")).below(breadcrumb));
    }
}
