package com.rmmcosta.superduperdrive;

import com.rmmcosta.superduperdrive.model.User;
import com.rmmcosta.superduperdrive.service.UserService;
import io.github.bonigarcia.wdm.WebDriverManager;
import org.junit.jupiter.api.*;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.chrome.ChromeDriver;
import org.openqa.selenium.chrome.ChromeOptions;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.web.server.LocalServerPort;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotEquals;

@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
public class SignupAndLoginLogoutAutomatedTests {
    @LocalServerPort
    private Integer port;

    private static final String DOMAIN = "http://localhost:";

    @Autowired
    private UserService userService;

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

    @AfterEach
    public void deleteUser() {
        userService.deleteUser(USERNAME);
    }

    @Test
    public void signupAndSuccessfulLoginAndSuccessfulLogout() {
        //signup
        loginPage.go2Signup();
        signupPage.doSignup(USERNAME, PASSWORD, F_NAME, L_NAME);
        //login
        driver.get(DOMAIN + port + "/login");
        loginPage.doLogin(USERNAME, PASSWORD);
        assertEquals(DOMAIN + port + "/home", driver.getCurrentUrl());
        homePage.doLogout();
        driver.get(DOMAIN + port + "/home");
        assertNotEquals(DOMAIN + port + "/home", driver.getCurrentUrl());
    }

    @Test
    public void signupSameUsernameGivesError() {
        driver.get(DOMAIN + port + "/signup");
        signupPage.doSignup(USERNAME, PASSWORD, F_NAME, L_NAME);
        driver.get(DOMAIN + port + "/signup");
        signupPage.doSignup(USERNAME, PASSWORD, F_NAME, L_NAME);
        assertEquals("User already exists with that username!", signupPage.getErrorMessage());
    }
}
