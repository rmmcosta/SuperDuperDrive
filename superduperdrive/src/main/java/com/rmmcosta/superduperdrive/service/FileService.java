package com.rmmcosta.superduperdrive.service;

import com.rmmcosta.superduperdrive.mapper.FileMapper;
import com.rmmcosta.superduperdrive.model.Credential;
import com.rmmcosta.superduperdrive.model.File;
import com.rmmcosta.superduperdrive.model.FileName;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.sql.SQLException;
import java.util.List;

@Service
public class FileService {
    @Autowired
    private FileMapper fileMapper;

    public List<FileName> getFileNames(String ownerUsername) {
        return fileMapper.getFileNames(ownerUsername);
    }

    public byte[] getFileBinary(Integer fileId, String ownerUsername) throws SQLException {
        return fileMapper.getFileBinary(fileId, ownerUsername).getFileBinary();
    }

    public String getFileName(Integer fileId, String ownerUsername) {
        return fileMapper.getFileName(fileId, ownerUsername);
    }

    public int insertFile(File file, String ownerUsername) {
        checkExistingFile(file, ownerUsername);
        return fileMapper.insertFile(file, ownerUsername);
    }

    public boolean deleteFile(Integer fileId, String ownerUsername) {
        return fileMapper.deleteFile(fileId, ownerUsername);
    }
    private void checkExistingFile(File file, String ownerUsername) {
        FileName existingFile = fileMapper.getFileByName(file.getFileName(), ownerUsername);
        if (existingFile != null && (file.getFileId() == null || existingFile.getFileId() != file.getFileId())) {
            throw new RuntimeException("File already exists with that name!");
        }
    }
}
