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

    public List<Note> getNotes(String ownerUsername) {
        if (!noteMapper.getNotes(ownerUsername).isEmpty())
            System.out.println("1st note id = " + noteMapper.getNotes(ownerUsername).get(0).getNoteId());
        return noteMapper.getNotes(ownerUsername);
    }

    public Note getNoteById(Integer noteId, String ownerUsername) {
        return noteMapper.getNoteById(noteId, ownerUsername);
    }

    public int insertNote(Note note, String ownerUsername) {
        checkExistingNote(note, ownerUsername);
        return noteMapper.insertNote(note, ownerUsername);
    }

    public boolean deleteNote(Integer credentialId, String ownerUsername) {
        return noteMapper.deleteNote(credentialId, ownerUsername);
    }

    public boolean updateNote(Note note, String ownerUsername) {
        checkExistingNote(note, ownerUsername);
        return noteMapper.updateNote(note, ownerUsername);
    }

    private void checkExistingNote(Note note, String ownerUsername) {
        Note existingNote = noteMapper.getNoteByTitle(note.getTitle(), ownerUsername);
        if (existingNote != null && (note.getNoteId() == null || existingNote.getNoteId() != note.getNoteId())) {
            throw new RuntimeException("Note already exists with that title!");
        }
    }
}
