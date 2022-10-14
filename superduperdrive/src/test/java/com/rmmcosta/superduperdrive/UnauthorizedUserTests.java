package com.rmmcosta.superduperdrive;

import io.github.bonigarcia.wdm.WebDriverManager;
import org.junit.jupiter.api.AfterAll;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Test;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.chrome.ChromeDriver;
import org.openqa.selenium.chrome.ChromeOptions;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.web.server.LocalServerPort;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotEquals;

@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
public class UnauthorizedUserTests {
    @LocalServerPort
    private Integer port;
    private static final String DOMAIN = "http://localhost:";
    private static WebDriver driver;

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

    @Test
    public void noAccess() {
        String pageUrl = DOMAIN + port + "/login";
        driver.get(pageUrl);
        assertEquals(pageUrl, driver.getCurrentUrl());
        pageUrl = DOMAIN + port + "/signup";
        driver.get(pageUrl);
        assertEquals(pageUrl, driver.getCurrentUrl());
        pageUrl = DOMAIN + port + "/home";
        driver.get(pageUrl);
        assertNotEquals(pageUrl, driver.getCurrentUrl());
    }

}
