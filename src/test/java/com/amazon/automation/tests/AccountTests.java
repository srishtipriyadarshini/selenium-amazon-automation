package com.amazon.automation.tests;

import com.amazon.automation.pages.CreateAccountPage;
import com.amazon.automation.utils.JsonDataReader;
import org.testng.Assert;
import org.testng.annotations.Test;

public class AccountTests extends BaseTest {

    @Test(groups = {"regression", "account", "negative"},
            description = "Sign-up with mismatched passwords shows validation error")
    public void signup_mismatchedPasswords_showsError() {
        String name = JsonDataReader.getString("signup.name");
        String email = JsonDataReader.getString("signup.email");
        String password = JsonDataReader.getString("signup.password");
        String wrongConfirm = JsonDataReader.getString("signup.wrongConfirm");

        CreateAccountPage page = new CreateAccountPage().open();
        Assert.assertTrue(page.isLoaded(), "Create-account page should load");

        page.fillForm(name, email, password, wrongConfirm).submit();

        Assert.assertTrue(page.hasValidationError(),
                "Mismatched passwords should produce a validation error");
    }
}
