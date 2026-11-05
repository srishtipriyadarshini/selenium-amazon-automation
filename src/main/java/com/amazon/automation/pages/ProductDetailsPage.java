package com.amazon.automation.pages;

import com.amazon.automation.utils.WaitUtils;
import org.openqa.selenium.By;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.support.FindBy;


public class ProductDetailsPage extends BasePage {

    @FindBy(id = "productTitle")
    private WebElement productTitle;

    // Price - several selectors because the layout varies. We check both.
    private final By priceBy = By.cssSelector("span.a-price-whole, #priceblock_ourprice, #priceblock_dealprice");

    @FindBy(id = "add-to-cart-button")
    private WebElement addToCartButton;

    private final By interstitialDeclineBy = By.cssSelector(
            "input[name='siNoCoverage'], #attachSiNoCoverage-announce, input#attachSiNoCoverage");

    private final By cartConfirmationBy = By.cssSelector("#attachDisplayAddBaseAlert, #huc-v2-order-row-confirm-text, #sw-atc-details-single-container");

    public boolean isLoaded() {
        return WaitUtils.isPresent(By.id("productTitle"), 15);
    }

    public String getProductTitle() {
        return getText(productTitle);
    }

    public String getPrice() {
        if (WaitUtils.isPresent(priceBy, 5)) {
            return driver.findElement(priceBy).getText().trim();
        }
        return ""; // Some products show "Currently unavailable" - no price.
    }

    public boolean isAddToCartAvailable() {
        return WaitUtils.isPresent(By.id("add-to-cart-button"), 8);
    }

    public CartPage addToCart() {
        scrollIntoView(addToCartButton);
        click(addToCartButton);

       
        if (WaitUtils.isPresent(interstitialDeclineBy, 4)) {
            try {
                driver.findElement(interstitialDeclineBy).click();
                LOG.info("Dismissed add-to-cart interstitial");
            } catch (Exception e) {
                LOG.debug("Interstitial present but click failed: {}", e.getMessage());
            }
        }

        // Wait for cart confirmation OR redirect to cart page.
        WaitUtils.isPresent(cartConfirmationBy, 10);
        return new CartPage();
    }
}
