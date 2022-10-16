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
public class NotesEnd2EndTests {
    @LocalServerPort
    private Integer port;

    private static final String DOMAIN = "http://localhost:";

    private static WebDriver driver;
    private LoginPage loginPage;
    private SignupPage signupPage;
    private HomePage homePage;

    private static final String USERNAME = "rmmcosta_notes";
    private static final String PASSWORD = "12345";
    private static final String F_NAME = "Ricardo";
    private static final String L_NAME = "Costa";

    private static final String NOTE_TITLE = "The Best Note Ever";
    private static final String NEW_NOTE_TITLE = "The Best Note Ever 2";
    private static final String NOTE_DESCRIPTION = "This a description that really gives us a look on wha this note really is.";
    private static final String NEW_NOTE_DESCRIPTION = "This a description that really gives us a look on what this note really is.";

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
    public void noteCreateEditDelete() {
        assertEquals(DOMAIN + port + "/home", driver.getCurrentUrl());
        int initialNotesCount = homePage.getNotesCount();
        homePage.createNote(NOTE_TITLE, NOTE_DESCRIPTION);
        assertEquals(initialNotesCount + 1, homePage.getNotesCount());
        boolean foundMatchingNote;
        foundMatchingNote = homePage.getNotes().stream().filter(note -> note.getTitle().equals(NOTE_TITLE) && note.getDescription().equals(NOTE_DESCRIPTION)).count() == 1;
        assertTrue(foundMatchingNote);
        //let's try to create a note with the same title and assert if an error message is displayed in the screen
        assertEquals(homePage.createSameNote(NOTE_TITLE,""), "Note already exists with that title!");
        homePage.closeNoteModal();
        homePage.updateNote(NOTE_TITLE, NEW_NOTE_TITLE, NEW_NOTE_DESCRIPTION);
        foundMatchingNote = homePage.getNotes().stream().filter(note -> note.getTitle().equals(NEW_NOTE_TITLE) && note.getDescription().equals(NEW_NOTE_DESCRIPTION)).count() == 1;
        assertTrue(foundMatchingNote);
        //now let's see if the old note was gone
        foundMatchingNote = homePage.getNotes().stream().filter(note -> note.getTitle().equals(NOTE_TITLE) && note.getDescription().equals(NOTE_DESCRIPTION)).count() == 0;
        assertTrue(foundMatchingNote);
        assertTrue(homePage.deleteNote(NEW_NOTE_TITLE));
        assertEquals(initialNotesCount, homePage.getNotesCount());
        foundMatchingNote = homePage.getNotes().stream().filter(note -> note.getTitle().equals(NEW_NOTE_TITLE) && note.getDescription().equals(NEW_NOTE_DESCRIPTION)).count() == 0;
        assertTrue(foundMatchingNote);
    }
}
