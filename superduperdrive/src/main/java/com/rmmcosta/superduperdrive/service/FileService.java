package com.rmmcosta.superduperdrive.service;

import com.rmmcosta.superduperdrive.mapper.FileMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class FileService {
    @Autowired
    private FileMapper fileMapper;
}
