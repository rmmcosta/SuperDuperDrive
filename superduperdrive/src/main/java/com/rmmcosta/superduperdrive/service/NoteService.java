package com.rmmcosta.superduperdrive.service;

import com.rmmcosta.superduperdrive.mapper.NoteMapper;
import com.rmmcosta.superduperdrive.model.Note;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class NoteService {
    @Autowired
    private NoteMapper noteMapper;

    public List<Note> getNotes() {
        if (!noteMapper.getNotes().isEmpty())
            System.out.println("1st note id = " + noteMapper.getNotes().get(0).getNoteId());
        return noteMapper.getNotes();
    }

    public Note getNoteById(Integer noteId) {
        return noteMapper.getNoteById(noteId);
    }

    public int insertNote(Note note) {
        return noteMapper.insertNote(note);
    }

    public boolean deleteNote(Integer credentialId) {
        return noteMapper.deleteNote(credentialId);
    }

    public boolean updateNote(Note note) {
        return noteMapper.updateNote(note);
    }
}
