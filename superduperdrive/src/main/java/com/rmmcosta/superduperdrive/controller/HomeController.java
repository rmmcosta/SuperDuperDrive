package com.rmmcosta.superduperdrive.controller;

import com.rmmcosta.superduperdrive.model.*;
import com.rmmcosta.superduperdrive.service.CredentialService;
import com.rmmcosta.superduperdrive.service.FileService;
import com.rmmcosta.superduperdrive.service.NoteService;
import org.modelmapper.ModelMapper;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.util.FileCopyUtils;
import org.springframework.util.StringUtils;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.multipart.MultipartFile;

import javax.annotation.PostConstruct;
import javax.servlet.http.HttpServletResponse;
import java.io.ByteArrayInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.net.URLConnection;
import java.sql.SQLException;
import java.util.List;
import java.util.Objects;

@Controller
public class HomeController {

    private Logger logger = LoggerFactory.getLogger(HomeController.class);
    private static final String EXTERNAL_FILE_PATH = "C:/Users/Public/Downloads/";

    @Autowired
    private FileService fileService;
    @Autowired
    private NoteService noteService;
    @Autowired
    private CredentialService credentialService;

    private ModelMapper modelMapper;

    @PostConstruct
    public void initializeModelMapper() {
        modelMapper = new ModelMapper();
    }

    @GetMapping("/home")
    public String getHomePage(Model model) {
        model.addAttribute("fileList", fileService.getFileNames());
        model.addAttribute("noteList", noteService.getNotes());
        model.addAttribute("credentialList", credentialService.getCredentials());
        model.addAttribute("tabActive", "Files");
        return "home";
    }

    //https://attacomsian.com/blog/spring-boot-thymeleaf-file-upload
    @PostMapping("/uploadFile")
    public String newFile(@RequestParam("file-new") MultipartFile multipartFile, Model model) {
        File file = new File();
        // normalize the file path
        file.setFileName(StringUtils.cleanPath(Objects.requireNonNull(multipartFile.getOriginalFilename())));

        try {
            file.setFileBinary(multipartFile.getInputStream().readAllBytes());
            fileService.insertFile(file);
            model.addAttribute("fileList", fileService.getFileNames());
        } catch (IOException e) {
            logger.error(e.getMessage());
        }
        model.addAttribute("tabActive", "Files");
        model.addAttribute("credentialList", credentialService.getCredentials());
        model.addAttribute("noteList", noteService.getNotes());
        model.addAttribute("fileList", fileService.getFileNames());
        return "home";
    }

    @PostMapping("/newNote")
    public String newNote(@ModelAttribute NoteForm noteForm, Model model) {
        if (noteForm.getNoteId() != null) {
            System.out.println("update note: " + noteForm);
            noteService.updateNote(modelMapper.map(noteForm, Note.class));
        } else {
            System.out.println("create note: " + noteForm);
            noteService.insertNote(modelMapper.map(noteForm, Note.class));
        }
        model.addAttribute("noteForm", new NoteForm());
        model.addAttribute("tabActive", "Notes");
        model.addAttribute("credentialList", credentialService.getCredentials());
        model.addAttribute("noteList", noteService.getNotes());
        model.addAttribute("fileList", fileService.getFileNames());
        return "home";
    }

    @PostMapping("/newCredential")
    public String newCredential(@ModelAttribute CredentialForm credentialForm, Model model) {
        if (credentialForm.getCredentialId() != null) {
            System.out.println("edit Credential: " + credentialForm);
            credentialService.updateCredential(modelMapper.map(credentialForm, Credential.class));
        } else {
            System.out.println("new Credential: " + credentialForm);
            credentialService.insertCredential(modelMapper.map(credentialForm, Credential.class));
        }
        model.addAttribute("credentialForm", new CredentialForm());
        model.addAttribute("tabActive", "Credentials");
        model.addAttribute("credentialList", credentialService.getCredentials());
        model.addAttribute("noteList", noteService.getNotes());
        model.addAttribute("fileList", fileService.getFileNames());
        return "home";
    }

    @PostMapping("/editCredential")
    public String editCredential(@RequestParam("credential-list-edit-id") String credentialId, Model model) {
        System.out.println("edit Credential: " + credentialId);
        CredentialForm credentialForm = new CredentialForm();
        Credential credential = credentialService.getCredentialById(Integer.valueOf(credentialId));
        modelMapper.map(credential, credentialForm);
        model.addAttribute("credentialForm", credentialForm);
        model.addAttribute("tabActive", "Credentials");
        model.addAttribute("credentialList", credentialService.getCredentials());
        model.addAttribute("noteList", noteService.getNotes());
        model.addAttribute("fileList", fileService.getFileNames());
        return "home";
    }

    @PostMapping("/editNote")
    public String editNote(@RequestParam("note-list-edit-id") String noteId, Model model) {
        System.out.println("edit Note: " + noteId);
        NoteForm noteForm = new NoteForm();
        Note note = noteService.getNoteById(Integer.valueOf(noteId));
        modelMapper.map(note, noteForm);
        model.addAttribute("noteForm", noteForm);
        model.addAttribute("tabActive", "Notes");
        model.addAttribute("credentialList", credentialService.getCredentials());
        model.addAttribute("noteList", noteService.getNotes());
        model.addAttribute("fileList", fileService.getFileNames());
        return "home";
    }

    @PostMapping("/deleteCredential")
    public String deleteCredential(@RequestParam("credential-list-delete-id") String credentialId, Model model) {
        System.out.println("delete Credential: " + credentialId);
        if (!credentialId.isEmpty()) {
            credentialService.deleteCredential(Integer.valueOf(credentialId));
        }
        model.addAttribute("credentialForm", new CredentialForm());
        model.addAttribute("tabActive", "Credentials");
        model.addAttribute("credentialList", credentialService.getCredentials());
        model.addAttribute("noteList", noteService.getNotes());
        model.addAttribute("fileList", fileService.getFileNames());
        return "home";
    }

    @PostMapping("/deleteNote")
    public String deleteNote(@RequestParam("note-list-delete-id") String noteId, Model model) {
        System.out.println("delete Note: " + noteId);
        if (!noteId.isEmpty()) {
            noteService.deleteNote(Integer.valueOf(noteId));
        }
        model.addAttribute("noteForm", new NoteForm());
        model.addAttribute("tabActive", "Notes");
        model.addAttribute("credentialList", credentialService.getCredentials());
        model.addAttribute("noteList", noteService.getNotes());
        model.addAttribute("fileList", fileService.getFileNames());
        return "home";
    }

    @PostMapping("/deleteFile")
    public String deleteFile(@RequestParam("file-list-delete-id") String fileId, Model model) {
        System.out.println("delete File: " + fileId);
        if (!fileId.isEmpty()) {
            fileService.deleteFile(Integer.valueOf(fileId));
        }
        model.addAttribute("tabActive", "Files");
        model.addAttribute("credentialList", credentialService.getCredentials());
        model.addAttribute("noteList", noteService.getNotes());
        model.addAttribute("fileList", fileService.getFileNames());
        return "home";
    }

    @PostMapping("/downloadFile")
    public void downloadFile(@RequestParam("file-list-view-id") String fileId, HttpServletResponse response) {
        System.out.println("download File: " + fileId);
        if (!fileId.isEmpty()) {
            try {
                //get the mimetype
                String fileName = fileService.getFileName(Integer.valueOf(fileId));
                String mimeType = URLConnection.guessContentTypeFromName(fileName);
                if (mimeType == null) {
                    //unknown mimetype so set the mimetype to application/octet-stream
                    mimeType = "application/octet-stream";
                }

                response.setContentType(mimeType);

                //Here we have mentioned it to show inline
                //response.setHeader("Content-Disposition", String.format("inline; filename=\"" + fileName + "\""));

                //Here we have mentioned it to show as attachment
                response.setHeader("Content-Disposition", String.format("attachment; filename=\"" + fileName + "\""));

                byte[] fileBinary = fileService.getFileBinary(Integer.valueOf(fileId));
                // get file as InputStream
                InputStream inputStream = new ByteArrayInputStream(fileBinary);

                response.setContentLength(fileBinary.length);
                FileCopyUtils.copy(inputStream, response.getOutputStream());

            } catch (IOException | SQLException e) {
                logger.error(e.getMessage());
            }
        }
    }

    @ModelAttribute("fileForm")
    public FileForm initFileForm() {
        return new FileForm();
    }

    @ModelAttribute("noteForm")
    public NoteForm initNoteForm() {
        return new NoteForm();
    }

    @ModelAttribute("credentialForm")
    public CredentialForm initCredentialForm() {
        return new CredentialForm();
    }

    @ModelAttribute("fileList")
    public List<FileName> getFileList() {
        return fileService.getFileNames();
    }

    @ModelAttribute("noteList")
    public List<Note> getNoteList() {
        return noteService.getNotes();
    }

    @ModelAttribute("credentialList")
    public List<Credential> getCredentialList() {
        return credentialService.getCredentials();
    }

}
