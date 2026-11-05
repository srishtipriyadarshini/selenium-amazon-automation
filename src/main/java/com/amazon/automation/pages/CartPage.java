package com.amazon.automation.pages;

import com.amazon.automation.utils.WaitUtils;
import org.openqa.selenium.By;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.support.FindBy;

import java.util.List;


public class CartPage extends BasePage {

    private final By cartItemsBy = By.cssSelector("div[data-name='Active Items'] div.sc-list-item, div.sc-list-item-content");
    private final By emptyCartHeadingBy = By.xpath("//*[contains(text(),'Your Amazon Cart is empty')]");
    private final By subtotalBy = By.id("sc-subtotal-amount-activecart");
    private final By cartCountBy = By.id("nav-cart-count");

    @FindBy(name = "proceedToRetailCheckout")
    private WebElement proceedToCheckoutButton;

    public CartPage open() {
        driver.get("https://www.amazon.in/gp/cart/view.html");
        return this;
    }

    public boolean isLoaded() {
        // Either the empty heading or the items list signals we're on the cart.
        return WaitUtils.isPresent(emptyCartHeadingBy, 5)
                || WaitUtils.isPresent(cartItemsBy, 5);
    }

    public boolean isEmpty() {
        return WaitUtils.isPresent(emptyCartHeadingBy, 5);
    }

    public int getItemCount() {
        return driver.findElements(cartItemsBy).size();
    }

    public int getCartBadgeCount() {
        try {
            String count = driver.findElement(cartCountBy).getText().trim();
            return count.isEmpty() ? 0 : Integer.parseInt(count);
        } catch (Exception e) {
            return 0;
        }
    }

    public String getSubtotal() {
        if (WaitUtils.isPresent(subtotalBy, 5)) {
            return driver.findElement(subtotalBy).getText().trim();
        }
        return "";
    }

    
    public CartPage removeFirstItem() {
        List<WebElement> deleteLinks = driver.findElements(
                By.cssSelector("input[value='Delete'], span.a-declarative input[name='submit.delete']"));
        if (!deleteLinks.isEmpty()) {
            deleteLinks.get(0).click();
        }
        return this;
    }
}
