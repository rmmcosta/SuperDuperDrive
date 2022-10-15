package com.rmmcosta.superduperdrive.service;

import com.rmmcosta.superduperdrive.mapper.FileMapper;
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

    public List<FileName> getFileNames() {
        return fileMapper.getFileNames();
    }

    public byte[] getFileBinary(Integer fileId) throws SQLException {
        return fileMapper.getFileBinary(fileId).getFileBinary();
    }

    public String getFileName(Integer fileId) {
        return fileMapper.getFileName(fileId);
    }

    public int insertFile(File file) {
        return fileMapper.insertFile(file);
    }

    public boolean deleteFile(Integer fileId) {
        return fileMapper.deleteFile(fileId);
    }
}
