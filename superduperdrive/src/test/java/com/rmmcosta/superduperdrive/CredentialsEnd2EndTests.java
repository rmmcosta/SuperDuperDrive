package com.rmmcosta.superduperdrive;

import io.github.bonigarcia.wdm.WebDriverManager;
import org.junit.jupiter.api.*;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.chrome.ChromeDriver;
import org.openqa.selenium.chrome.ChromeOptions;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.web.server.LocalServerPort;

import static org.junit.jupiter.api.Assertions.*;

@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
public class CredentialsEnd2EndTests {
    @LocalServerPort
    private Integer port;

    private static final String DOMAIN = "http://localhost:";

    private static WebDriver driver;
    private LoginPage loginPage;
    private SignupPage signupPage;
    private HomePage homePage;

    private static final String USERNAME = "rmmcosta1";
    private static final String PASSWORD = "12345";
    private static final String F_NAME = "Ricardo";
    private static final String L_NAME = "Costa";
    private static final String URL = "http://localhost:8080/chat";

    private static final String NEW_USERNAME = "rmmcosta11";
    private static final String NEW_PASSWORD = "123451";
    private static final String NEW_URL = "http://localhost:8080/chat1";

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
        if (!driver.getCurrentUrl().equals(DOMAIN + port + "/home")) {
            loginPage = new LoginPage(driver);
            signupPage = new SignupPage(driver);
            //signup
            driver.get(DOMAIN + port + "/signup");
            signupPage.doSignup(USERNAME, PASSWORD, F_NAME, L_NAME);
            //login
            driver.get(DOMAIN + port + "/login");
            loginPage.doLogin(USERNAME, PASSWORD);
        }
        homePage = new HomePage(driver);
    }

    @AfterEach
    public void terminateSession() {
        driver.get(DOMAIN + port + "/home");
        if (driver.getCurrentUrl().equals(DOMAIN + port + "/home"))
            homePage.doLogout();
    }

    @Test
    public void credentialCreateEditDelete() {
        assertEquals(DOMAIN + port + "/home", driver.getCurrentUrl());
        int initialCredentialsCount = homePage.getCredentialsCount();
        homePage.createCredential(URL, USERNAME, PASSWORD);
        assertEquals(initialCredentialsCount + 1, homePage.getCredentialsCount());
        boolean foundMatchingCredential;
        foundMatchingCredential = homePage.getCredentials().stream().filter(credential -> credential.getUrl().equals(URL) && credential.getUsername().equals(USERNAME)).count() == 1;
        assertTrue(foundMatchingCredential);
        //let's try to create a credential with the same url and username and assert if an error message is displayed in the screen
        assertEquals(homePage.createSameCredential(URL, USERNAME, ""), "Credential already exists with that Url and Username!");
        homePage.closeCredentialModal();
        homePage.updateCredential(URL, NEW_URL, NEW_USERNAME, NEW_PASSWORD);
        foundMatchingCredential = homePage.getCredentials().stream().filter(credential -> credential.getUrl().equals(NEW_URL) && credential.getUsername().equals(NEW_USERNAME)).count() == 1;
        assertTrue(foundMatchingCredential);
        //now let's see if the old credential was gone
        foundMatchingCredential = homePage.getCredentials().stream().filter(credential -> credential.getUrl().equals(URL) && credential.getUsername().equals(USERNAME)).count() == 0;
        assertTrue(foundMatchingCredential);//doesn't matter which credential we are deleting. We just want to make sure that the delete works.
        homePage.deleteCredential(NEW_URL, NEW_USERNAME);
        assertEquals(initialCredentialsCount, homePage.getCredentialsCount());
    }
}
