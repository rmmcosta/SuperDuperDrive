package com.rmmcosta.superduperdrive;

import com.google.common.io.Files;
import com.rmmcosta.superduperdrive.model.File;
import com.rmmcosta.superduperdrive.model.Note;
import com.rmmcosta.superduperdrive.service.FileService;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

import java.io.*;
import java.sql.SQLException;

import static org.junit.jupiter.api.Assertions.*;

@SpringBootTest
public class FileServiceTests {
    @Autowired
    private FileService fileService;

    @Test
    public void fileLifeCycle() {
        File file = new File();
        file.setFileName("theFile");
        java.io.File binaryFile = new java.io.File("src/test/resources/dummy.png");
        String authUsername = "user1";
        int initCount = fileService.getFileNames(authUsername).size();
        int fileId = 0;
        try {
            InputStream inputStream = new BufferedInputStream(new FileInputStream(binaryFile));
            byte[] buffer = new byte[(int) binaryFile.length()];
            inputStream.read(buffer);
            file.setFileBinary(buffer);
            fileId = fileService.insertFile(file, authUsername);
        } catch (IOException e) {
            fail();
        }
        assertEquals(initCount + 1, fileService.getFileNames(authUsername).size());
        ByteArrayOutputStream out = new ByteArrayOutputStream();
        java.io.File returnedFile = new java.io.File(System.getProperty("java.io.tmpdir"),"dummy.tmp");
        try {
            out.write(fileService.getFileBinary(fileId, authUsername));
            FileOutputStream fileOutputStream = new FileOutputStream(returnedFile);
            out.writeTo(fileOutputStream);
            assertTrue(Files.equal(binaryFile, returnedFile));
        } catch (IOException | SQLException e) {
            fail(e.getMessage());
        }
        File newSameFileName = new File();
        newSameFileName.setFileName("theFile");
        assertThrowsExactly(RuntimeException.class, () -> {
            fileService.insertFile(newSameFileName, authUsername);
        }, "File already exists with that name!");
        assertTrue(fileService.deleteFile(fileId, authUsername));
        assertEquals(initCount, fileService.getFileNames(authUsername).size());
    }
}
