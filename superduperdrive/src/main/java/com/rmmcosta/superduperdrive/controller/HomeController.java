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
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import javax.annotation.PostConstruct;
import javax.servlet.http.HttpServletResponse;
import java.io.ByteArrayInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.net.URLConnection;
import java.security.Principal;
import java.sql.SQLException;
import java.util.ArrayList;
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
    public String getHomePage(Model model, Principal principal) {
        model.addAttribute("loggedUsername", principal.getName());
        model.addAttribute("fileList", fileService.getFileNames(principal.getName()));
        model.addAttribute("noteList", noteService.getNotes(principal.getName()));
        model.addAttribute("credentialList", credentialService.getCredentials(principal.getName()));
        model.addAttribute("tabActive", "Files");
        return "home";
    }

    //https://attacomsian.com/blog/spring-boot-thymeleaf-file-upload
    @PostMapping("/uploadFile")
    public String newFile(@RequestParam("file-new") MultipartFile multipartFile, Model model, Principal principal) {
        File file = new File();
        // normalize the file path
        file.setFileName(StringUtils.cleanPath(Objects.requireNonNull(multipartFile.getOriginalFilename())));

        try {
            file.setFileBinary(multipartFile.getInputStream().readAllBytes());

            try {
                fileService.insertFile(file, principal.getName());
                model.addAttribute("successMessage", "File uploaded with success!");
            } catch (Exception e) {
                model.addAttribute("errorMessage", e.getMessage());
            }
            model.addAttribute("fileList", fileService.getFileNames(principal.getName()));
        } catch (IOException e) {
            logger.error(e.getMessage());
        }
        model.addAttribute("tabActive", "Files");
        model.addAttribute("credentialList", credentialService.getCredentials(principal.getName()));
        model.addAttribute("noteList", noteService.getNotes(principal.getName()));
        model.addAttribute("fileList", fileService.getFileNames(principal.getName()));
        model.addAttribute("loggedUsername", principal.getName());
        return "home";
    }

    @PostMapping("/newNote")
    public String newNote(@ModelAttribute NoteForm noteForm, Model model, Principal principal) {
        boolean error = false;
        String successOperation = noteForm.getNoteId() != null ? "updated" : "created";
        if (noteForm.getNoteId() != null) {
            System.out.println("update note: " + noteForm);
            try {
                noteService.updateNote(modelMapper.map(noteForm, Note.class), principal.getName());
            } catch (Exception e) {
                model.addAttribute("noteErrorMessage", e.getMessage());
                error = true;
            }
        } else {
            System.out.println("create note: " + noteForm);
            try {
                noteService.insertNote(modelMapper.map(noteForm, Note.class), principal.getName());
            } catch (Exception e) {
                model.addAttribute("noteErrorMessage", e.getMessage());
                error = true;
            }
        }
        if (error) {
            model.addAttribute("noteForm", noteForm);
        } else {
            model.addAttribute("noteForm", new NoteForm());
            model.addAttribute("successMessage", "Note " + successOperation + " with success!");
        }
        model.addAttribute("tabActive", "Notes");
        model.addAttribute("credentialList", credentialService.getCredentials(principal.getName()));
        model.addAttribute("noteList", noteService.getNotes(principal.getName()));
        model.addAttribute("fileList", fileService.getFileNames(principal.getName()));
        model.addAttribute("loggedUsername", principal.getName());
        return "home";
    }

    @PostMapping("/newCredential")
    public String newCredential(@ModelAttribute CredentialForm credentialForm, Model model, Principal principal) {
        boolean error = false;
        String successOperation = credentialForm.getCredentialId() != null ? "updated" : "created";
        if (credentialForm.getCredentialId() != null) {
            System.out.println("edit Credential: " + credentialForm);
            try {
                credentialService.updateCredential(modelMapper.map(credentialForm, Credential.class), principal.getName());
            } catch (Exception e) {
                model.addAttribute("credentialErrorMessage", e.getMessage());
                error = true;
            }
        } else {
            System.out.println("new Credential: " + credentialForm);
            try {
                credentialService.insertCredential(modelMapper.map(credentialForm, Credential.class), principal.getName());
            } catch (Exception e) {
                model.addAttribute("credentialErrorMessage", e.getMessage());
                error = true;
            }
        }
        if (error) {
            model.addAttribute("credentialForm", credentialForm);
        } else {
            model.addAttribute("credentialForm", new CredentialForm());
            model.addAttribute("successMessage", "Credential " + successOperation + " with success!");
        }
        model.addAttribute("tabActive", "Credentials");
        model.addAttribute("credentialList", credentialService.getCredentials(principal.getName()));
        model.addAttribute("noteList", noteService.getNotes(principal.getName()));
        model.addAttribute("fileList", fileService.getFileNames(principal.getName()));
        model.addAttribute("loggedUsername", principal.getName());
        return "home";
    }

    @PostMapping("/editCredential")
    public String editCredential(@RequestParam("credential-list-edit-id") String credentialId, Model model, Principal principal) {
        System.out.println("edit Credential: " + credentialId);
        CredentialForm credentialForm = new CredentialForm();
        Credential credential = credentialService.getCredentialById(Integer.valueOf(credentialId), principal.getName());
        modelMapper.map(credential, credentialForm);
        model.addAttribute("credentialForm", credentialForm);
        model.addAttribute("tabActive", "Credentials");
        model.addAttribute("credentialList", credentialService.getCredentials(principal.getName()));
        model.addAttribute("noteList", noteService.getNotes(principal.getName()));
        model.addAttribute("fileList", fileService.getFileNames(principal.getName()));
        model.addAttribute("loggedUsername", principal.getName());
        return "home";
    }

    @PostMapping("/editNote")
    public String editNote(@RequestParam("note-list-edit-id") String noteId, Model model, Principal principal) {
        System.out.println("edit Note: " + noteId);
        NoteForm noteForm = new NoteForm();
        Note note = noteService.getNoteById(Integer.valueOf(noteId), principal.getName());
        modelMapper.map(note, noteForm);
        model.addAttribute("noteForm", noteForm);
        model.addAttribute("tabActive", "Notes");
        model.addAttribute("credentialList", credentialService.getCredentials(principal.getName()));
        model.addAttribute("noteList", noteService.getNotes(principal.getName()));
        model.addAttribute("fileList", fileService.getFileNames(principal.getName()));
        model.addAttribute("loggedUsername", principal.getName());
        return "home";
    }

    @PostMapping("/deleteCredential")
    public String deleteCredential(@RequestParam("credential-list-delete-id") String credentialId, Model model, Principal principal) {
        System.out.println("delete Credential: " + credentialId);
        if (!credentialId.isEmpty()) {
            boolean deleteResult = credentialService.deleteCredential(Integer.valueOf(credentialId), principal.getName());
            if (deleteResult) {
                model.addAttribute("successMessage", "Credential deleted with success!");
            }
        }
        model.addAttribute("credentialForm", new CredentialForm());
        model.addAttribute("tabActive", "Credentials");
        model.addAttribute("credentialList", credentialService.getCredentials(principal.getName()));
        model.addAttribute("noteList", noteService.getNotes(principal.getName()));
        model.addAttribute("fileList", fileService.getFileNames(principal.getName()));
        model.addAttribute("loggedUsername", principal.getName());
        return "home";
    }

    @PostMapping("/deleteNote")
    public String deleteNote(@RequestParam("note-list-delete-id") String noteId, Model model, Principal principal) {
        System.out.println("delete Note: " + noteId);
        if (!noteId.isEmpty()) {
            boolean deleteResult = noteService.deleteNote(Integer.valueOf(noteId), principal.getName());
            if (deleteResult) {
                model.addAttribute("successMessage", "Note deleted with success!");
            }
        }
        model.addAttribute("noteForm", new NoteForm());
        model.addAttribute("tabActive", "Notes");
        model.addAttribute("credentialList", credentialService.getCredentials(principal.getName()));
        model.addAttribute("noteList", noteService.getNotes(principal.getName()));
        model.addAttribute("fileList", fileService.getFileNames(principal.getName()));
        model.addAttribute("loggedUsername", principal.getName());
        return "home";
    }

    @PostMapping("/deleteFile")
    public String deleteFile(@RequestParam("file-list-delete-id") String fileId, Model model, Principal principal) {
        System.out.println("delete File: " + fileId);
        if (!fileId.isEmpty()) {
            boolean deleteResult = fileService.deleteFile(Integer.valueOf(fileId), principal.getName());
            if (deleteResult) {
                model.addAttribute("successMessage", "File deleted with success!");
            }
        }
        model.addAttribute("tabActive", "Files");
        model.addAttribute("credentialList", credentialService.getCredentials(principal.getName()));
        model.addAttribute("noteList", noteService.getNotes(principal.getName()));
        model.addAttribute("fileList", fileService.getFileNames(principal.getName()));
        model.addAttribute("loggedUsername", principal.getName());
        return "home";
    }

    @PostMapping("/downloadFile")
    public void downloadFile(@RequestParam("file-list-view-id") String fileId, HttpServletResponse response, Principal principal) {
        System.out.println("download File: " + fileId);
        if (!fileId.isEmpty()) {
            try {
                //get the mimetype
                String fileName = fileService.getFileName(Integer.valueOf(fileId), principal.getName());
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

                byte[] fileBinary = fileService.getFileBinary(Integer.valueOf(fileId), principal.getName());
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
        return new ArrayList<>();
    }

    @ModelAttribute("noteList")
    public List<Note> getNoteList() {
        return new ArrayList<>();
    }

    @ModelAttribute("credentialList")
    public List<Credential> getCredentialList() {
        return new ArrayList<>();
    }

    @ModelAttribute("successMessage")
    public String getSuccessMessage() {
        return "-1";
    }
}
