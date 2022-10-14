package com.rmmcosta.superduperdrive;

import io.github.bonigarcia.wdm.WebDriverManager;
import org.junit.jupiter.api.AfterAll;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.chrome.ChromeDriver;
import org.openqa.selenium.chrome.ChromeOptions;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.web.server.LocalServerPort;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotEquals;

@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
public class SignupAndLoginLogoutAutomatedTests {
    @LocalServerPort
    private Integer port;

    private static final String DOMAIN = "http://localhost:";

    private static WebDriver driver;
    private LoginPage loginPage;
    private SignupPage signupPage;
    private HomePage homePage;

    private static final String USERNAME = "rmmcosta_test";
    private static final String PASSWORD = "12345";
    private static final String F_NAME = "Ricardo";
    private static final String L_NAME = "Costa";

    @BeforeAll
    public static void initializeWebDriver() {
        WebDriverManager.chromedriver().setup();
        ChromeOptions chromeOptions = new ChromeOptions();
        chromeOptions.setHeadless(true);
        driver = new ChromeDriver(chromeOptions);
    }

    @AfterAll
    public static void finishWebDriver() {
        driver.quit();
    }

    @BeforeEach
    public void initializePageObjects() {
        System.out.println("port: " + port);
        driver.get(DOMAIN + port + "/home");
        loginPage = new LoginPage(driver);
        signupPage = new SignupPage(driver);
        homePage = new HomePage(driver);
    }

    @Test
    public void signupAndSuccessfulLoginAndSuccessfulLogout() {
        //signup
        loginPage.go2Signup();
        signupPage.doSignup(USERNAME, PASSWORD, F_NAME, L_NAME);
        //login
        driver.get(DOMAIN + port + "/login");
        loginPage.doLogin(USERNAME, PASSWORD);
        assertEquals(DOMAIN+port+"/home", driver.getCurrentUrl());
        homePage.doLogout();
        driver.get(DOMAIN + port + "/home");
        assertNotEquals(DOMAIN + port + "/home", driver.getCurrentUrl());
    }
}
