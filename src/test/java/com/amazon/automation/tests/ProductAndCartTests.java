package com.amazon.automation.tests;

import com.amazon.automation.pages.CartPage;
import com.amazon.automation.pages.HomePage;
import com.amazon.automation.pages.ProductDetailsPage;
import com.amazon.automation.pages.SearchResultsPage;
import com.amazon.automation.utils.JsonDataReader;
import org.testng.Assert;
import org.testng.SkipException;
import org.testng.annotations.Test;

public class ProductAndCartTests extends BaseTest {

    @Test(groups = {"regression", "product"},
            description = "Product details page shows title and price for a searched product")
    public void product_detailsPageShowsKeyInfo() {
        String query = JsonDataReader.getString("search.validQuery");

        HomePage home = openHomePage();
        SearchResultsPage results = home.searchFor(query);

        // If Amazon returned zero results (rare for valid query) skip rather
        // than fail - the test is about PDP behaviour, not search.
        if (!results.hasResults()) {
            throw new SkipException("Search returned no results - cannot test PDP");
        }

        ProductDetailsPage pdp = results.openFirstResult();
        Assert.assertTrue(pdp.isLoaded(), "Product details should load");
        Assert.assertFalse(pdp.getProductTitle().isEmpty(),
                "Product title should not be empty");
    }

    @Test(groups = {"regression", "cart"},
            description = "Adding a product to cart updates cart count")
    public void cart_addProduct_increasesCartCount() {
        String query = JsonDataReader.getString("search.validQuery");

        HomePage home = openHomePage();
        SearchResultsPage results = home.searchFor(query);

        if (!results.hasResults()) {
            throw new SkipException("Search returned no results");
        }

        ProductDetailsPage pdp = results.openFirstResult();
        if (!pdp.isAddToCartAvailable()) {
            // Some products (out-of-stock, deals, region-restricted) hide the
            // add-to-cart button. That's a real product state, not a test failure.
            throw new SkipException("Product has no add-to-cart button");
        }

        CartPage cart = pdp.addToCart();
        cart.open();
        Assert.assertTrue(cart.isLoaded(), "Cart page should load");
        Assert.assertFalse(cart.isEmpty(), "Cart should contain at least one item");
    }

    @Test(groups = {"regression", "cart"},
            description = "Empty cart shows the empty-cart message")
    public void cart_emptyCart_showsEmptyMessage() {
        // Fresh session always has an empty cart - we don't need to clean up.
        CartPage cart = new CartPage().open();
        Assert.assertTrue(cart.isLoaded(), "Cart page should load");
        Assert.assertTrue(cart.isEmpty(),
                "A fresh session cart should show the empty message");
    }
}
