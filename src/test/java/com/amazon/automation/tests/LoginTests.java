package com.amazon.automation.tests;

import com.amazon.automation.pages.HomePage;
import com.amazon.automation.pages.LoginPage;
import com.amazon.automation.utils.JsonDataReader;
import org.testng.Assert;
import org.testng.annotations.Test;


public class LoginTests extends BaseTest {

    @Test(groups = {"regression", "login", "negative"},
            description = "Submitting login form without email shows validation error")
    public void login_emptyEmail_showsError() {
        HomePage home = openHomePage();
        LoginPage login = home.goToLogin();

        Assert.assertTrue(login.isLoaded(), "Login page should be loaded");

        login.submitWithoutEmail();
        Assert.assertTrue(login.isEmailErrorShown(),
                "Validation error should appear for empty email submission");
    }

    @Test(groups = {"regression", "login", "negative"},
            description = "Invalid email format triggers Amazon's email-format error")
    public void login_invalidEmailFormat_showsError() {
        String invalidEmail = JsonDataReader.getString("login.invalidEmail");

        HomePage home = openHomePage();
        LoginPage login = home.goToLogin();
        login.enterEmail(invalidEmail).clickContinue();

        Assert.assertTrue(login.isEmailErrorShown() || !login.isPasswordPageShown(),
                "Either an error should show or password step should not load");
    }
}
