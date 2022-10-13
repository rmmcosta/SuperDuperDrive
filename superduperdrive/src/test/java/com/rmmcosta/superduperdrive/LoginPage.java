package com.rmmcosta.superduperdrive;

import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.support.FindBy;
import org.openqa.selenium.support.PageFactory;

public class LoginPage {
    @FindBy(id = "floatingInput")
    private WebElement inputUsername;
    @FindBy(id = "floatingPassword")
    private WebElement inputPassword;
    @FindBy(id = "login")
    private WebElement btnLogin;
    @FindBy(id = "signup")
    private WebElement btnSignup;

    public LoginPage(WebDriver driver) {
        PageFactory.initElements(driver, this);
    }

    public void doLogin(String username, String password) {
        inputUsername.clear();
        inputUsername.sendKeys(username);
        inputPassword.clear();
        inputPassword.sendKeys(password);
        btnLogin.click();
    }

    public void go2Signup() {
        btnSignup.click();
    }
}
