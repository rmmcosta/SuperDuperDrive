package com.rmmcosta.superduperdrive;

import io.github.bonigarcia.wdm.WebDriverManager;
import org.junit.jupiter.api.*;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.chrome.ChromeDriver;
import org.openqa.selenium.chrome.ChromeOptions;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.web.server.LocalServerPort;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertTrue;

@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
public class CredentialsEnd2EndTests {
    @LocalServerPort
    private Integer port;

    private static final String DOMAIN = "http://localhost:";

    private static WebDriver driver;
    private LoginPage loginPage;
    private SignupPage signupPage;
    private HomePage homePage;

    private static final String USERNAME = "rmmcosta";
    private static final String PASSWORD = "12345";
    private static final String F_NAME = "Ricardo";
    private static final String L_NAME = "Costa";
    private static final String URL = "http://localhost:8080/chat";

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
        if(!driver.getCurrentUrl().equals(DOMAIN + port + "/home")) {
            loginPage = new LoginPage(driver);
            signupPage = new SignupPage(driver);
            //signup
            loginPage.go2Signup();
            signupPage.doSignup(USERNAME, PASSWORD, F_NAME, L_NAME);
            //login
            loginPage.doLogin(USERNAME, PASSWORD);
        }
        homePage = new HomePage(driver);
    }

    @AfterEach
    public void terminateSession() {
        homePage.doLogout();
    }

    @Test
    public void credentialCreateDelete() {
        assertEquals(DOMAIN + port + "/home", driver.getCurrentUrl());
        int initialCredentialsCount = homePage.getCredentialsCount();
        homePage.createCredential(URL, USERNAME, PASSWORD);
        assertEquals(initialCredentialsCount + 1, homePage.getCredentialsCount());
        boolean foundMatchingCredential;
        foundMatchingCredential = homePage.getCredentials().stream().filter(credential -> credential.getUrl() == URL && credential.getUsername() == USERNAME).count() == 1;
        assertTrue(foundMatchingCredential);
        homePage.deleteCredential();
        assertEquals(initialCredentialsCount, homePage.getCredentialsCount());
    }
}
