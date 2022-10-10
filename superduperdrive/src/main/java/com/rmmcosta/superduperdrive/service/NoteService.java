package com.rmmcosta.superduperdrive.service;

import com.rmmcosta.superduperdrive.mapper.NoteMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class NoteService {
    @Autowired
    private NoteMapper noteMapper;
}
