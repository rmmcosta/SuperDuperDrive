package com.rmmcosta.superduperdrive;

import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.support.FindBy;
import org.openqa.selenium.support.PageFactory;

public class SignupPage {
    @FindBy(id = "username")
    private WebElement inputUsername;
    @FindBy(id = "password")
    private WebElement inputPassword;
    @FindBy(id = "firstName")
    private WebElement inputFirstName;
    @FindBy(id = "lastName")
    private WebElement inputLastName;
    @FindBy(id = "signup")
    private WebElement btnSignup;

    public SignupPage(WebDriver driver) {
        PageFactory.initElements(driver, this);
    }

    public void doSignup(String username, String password, String fname, String lname) {
        inputUsername.clear();
        inputPassword.clear();
        inputFirstName.clear();
        inputLastName.clear();
        inputUsername.sendKeys(username);
        inputPassword.sendKeys(password);
        inputFirstName.sendKeys(fname);
        inputLastName.sendKeys(lname);
        btnSignup.click();
    }
}
