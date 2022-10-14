package com.rmmcosta.superduperdrive;

import com.rmmcosta.superduperdrive.model.Credential;
import com.rmmcosta.superduperdrive.model.File;
import com.rmmcosta.superduperdrive.model.Note;
import org.openqa.selenium.By;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.support.FindBy;
import org.openqa.selenium.support.PageFactory;
import org.openqa.selenium.support.ui.ExpectedConditions;
import org.openqa.selenium.support.ui.WebDriverWait;

import java.time.Duration;
import java.util.ArrayList;
import java.util.List;

public class HomePage {
    @FindBy(id = "logout")
    private WebElement logout;

    @FindBy(id = "nav-files-tab")
    private WebElement navFilesTab;
    @FindBy(id = "nav-notes-tab")
    private WebElement navNotesTab;
    @FindBy(id = "nav-credentials-tab")
    private WebElement navCredentialsTab;

    @FindBy(id = "nav-files")
    private WebElement navFiles;
    @FindBy(id = "nav-notes")
    private WebElement navNotes;
    @FindBy(id = "nav-credentials")
    private WebElement navCredentials;

    @FindBy(id = "file-new")
    private WebElement fileNew;
    @FindBy(id = "file-upload")
    private WebElement fileUpload;
    @FindBy(id = "file-list-view")
    private List<WebElement> fileListView;
    @FindBy(id = "file-list-delete")
    private List<WebElement> fileListDelete;
    @FindBy(id = "file-list-view-id")
    private List<WebElement> fileListViewId;
    @FindBy(id = "file-list-name")
    private List<WebElement> fileListName;

    @FindBy(id = "note-new")
    private WebElement noteNew;
    @FindBy(id = "note-list-edit")
    private WebElement noteListEdit;
    @FindBy(id = "note-list-delete")
    private WebElement noteListDelete;
    @FindBy(id = "note-list-id")
    private List<WebElement> noteListId;
    @FindBy(id = "note-list-title")
    private List<WebElement> noteListTitle;
    @FindBy(id = "note-list-description")
    private List<WebElement> noteListDescription;

    @FindBy(id = "credential-new")
    private WebElement credentialNew;
    @FindBy(id = "credential-list-edit")
    private List<WebElement> credentialListEdit;
    @FindBy(id = "credential-list-delete")
    private List<WebElement> credentialListDelete;
    @FindBy(id = "credential-list-id")
    private List<WebElement> credentialListId;
    @FindBy(id = "credential-list-url")
    private List<WebElement> credentialListUrl;
    @FindBy(id = "credential-list-username")
    private List<WebElement> credentialListUsername;
    @FindBy(id = "credential-list-password")
    private List<WebElement> credentialListPassword;

    @FindBy(id = "note-title")
    private WebElement noteTitle;
    @FindBy(id = "note-description")
    private WebElement noteDescription;
    @FindBy(id = "note-save")
    private WebElement noteSave;

    @FindBy(id = "credential-url")
    private WebElement credentialURL;
    @FindBy(id = "credential-username")
    private WebElement credentialUsername;
    @FindBy(id = "credential-password")
    private WebElement credentialPassword;
    @FindBy(id = "credential-save")
    private WebElement credentialSave;

    private WebDriver driver;

    public HomePage(WebDriver driver) {
        PageFactory.initElements(driver, this);
        this.driver = driver;
    }

    public void createFile(String name, Byte[] binary) {

    }

    public int getFilesCount() {
        return fileListName.size();
    }

    public List<File> getFiles() {
        List<File> files = new ArrayList<>();
        File tempFile = new File();
        for (int i = 0; i < fileListName.size(); i++) {
            tempFile.setFileId(Integer.valueOf(fileListViewId.get(i).getText()));
            tempFile.setFileName(fileListName.get(i).getText());
            files.add(tempFile);
        }
        return files;
    }

    public void createNote(String title, String description) {
        navNotesTab.click();
        noteNew.click();
        noteTitle.clear();
        noteTitle.sendKeys(title);
        noteDescription.clear();
        noteDescription.sendKeys(description);
        noteSave.click();
    }

    public int getNotesCount() {
        return noteListTitle.size();
    }

    public List<Note> getNotes() {
        List<Note> notes = new ArrayList<>();
        Note tempNote = new Note();
        for (int i = 0; i < noteListTitle.size(); i++) {
            tempNote.setNoteId(Integer.valueOf(noteListId.get(i).getText()));
            tempNote.setTitle(noteListTitle.get(i).getText());
            tempNote.setDescription(noteListDescription.get(i).getText());
            notes.add(tempNote);
        }
        return notes;
    }

    public void createCredential(String url, String username, String password) {
        navCredentialsTab.click();
        WebDriverWait wait = new WebDriverWait(driver, Duration.ofSeconds(1));
        wait.until(webDriver -> webDriver.findElement(By.id("credential-new")));
        credentialNew.click();
        wait.until(ExpectedConditions.visibilityOfElementLocated(By.id("credential-url")));
        credentialURL.clear();
        credentialURL.sendKeys(url);
        credentialUsername.clear();
        credentialUsername.sendKeys(username);
        credentialPassword.clear();
        credentialPassword.sendKeys(password);
        credentialSave.click();
    }

    public int getCredentialsCount() {
        return credentialListUsername.size();
    }

    public List<Credential> getCredentials() {
        List<Credential> credentials = new ArrayList<>();
        Credential tempCredential = new Credential();
        for (int i = 0; i < credentialListUrl.size(); i++) {
            //tempCredential.setCredentialId(Integer.valueOf(credentialListId.get(i).getText()));
            tempCredential.setUrl(credentialListUrl.get(i).getText());
            tempCredential.setUsername(credentialListUsername.get(i).getText());
            tempCredential.setPassword(credentialListPassword.get(i).getText());
            credentials.add(tempCredential);
        }
        return credentials;
    }

    public boolean deleteCredential() {
        if(!credentialListDelete.isEmpty()) {
            credentialListDelete.get(0).click();
            return true;
        }
        else
            return false;
    }

    public boolean isNoteFormEmpty() {
        return noteTitle.getText().isEmpty() && noteDescription.getText().isEmpty();
    }

    public boolean isCredentialFormEmpty() {
        return credentialURL.getText().isEmpty() && credentialPassword.getText().isEmpty() && credentialUsername.getText().isEmpty();
    }

    public void doLogout() {
        logout.click();
    }
}
