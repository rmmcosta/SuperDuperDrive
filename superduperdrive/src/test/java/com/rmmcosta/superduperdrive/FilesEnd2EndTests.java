package com.rmmcosta.superduperdrive;

import io.github.bonigarcia.wdm.WebDriverManager;
import org.junit.jupiter.api.*;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.chrome.ChromeDriver;
import org.openqa.selenium.chrome.ChromeOptions;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.web.server.LocalServerPort;

import java.io.*;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertTrue;

@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
public class FilesEnd2EndTests {
    @LocalServerPort
    private Integer port;

    private static final String DOMAIN = "http://localhost:";

    private static WebDriver driver;
    private LoginPage loginPage;
    private SignupPage signupPage;
    private HomePage homePage;

    private static final String USERNAME = "rmmcosta_files";
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
    public void fileUploadDownloadDelete() {
        assertEquals(DOMAIN + port + "/home", driver.getCurrentUrl());
        int initialFilesCount = homePage.getFilesCount();
        homePage.uploadFile("src/test/resources/dummy.png");
        assertEquals(initialFilesCount + 1, homePage.getFilesCount());
        boolean foundMatchingFile;
        foundMatchingFile = homePage.getFiles().stream().filter(file -> file.getFileName().equals("dummy.png")).count() == 1;
        assertTrue(foundMatchingFile);
        //let's try to create a file with the same name and assert if an error message is displayed in the screen
        assertEquals(homePage.uploadSameFile("src/test/resources/dummy.png"), "File already exists with that name!");
        assertTrue(homePage.deleteFile("dummy.png"));
        assertEquals(initialFilesCount, homePage.getFilesCount());
        foundMatchingFile = homePage.getFiles().stream().filter(file -> file.getFileName().equals("dummy.png")).count() == 0;
        assertTrue(foundMatchingFile);
    }
}
