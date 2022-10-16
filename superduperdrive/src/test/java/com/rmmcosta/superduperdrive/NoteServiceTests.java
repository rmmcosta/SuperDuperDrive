package com.rmmcosta.superduperdrive;

import com.rmmcosta.superduperdrive.model.Note;
import com.rmmcosta.superduperdrive.service.NoteService;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

import static org.junit.jupiter.api.Assertions.*;

@SpringBootTest
public class NoteServiceTests {
    @Autowired
    private NoteService noteService;

    @Test
    public void noteLifeCycle() {
        Note note = new Note();
        note.setTitle("xpto");
        note.setDescription("any description will due");
        String authUsername = "user1";
        int initCount = noteService.getNotes(authUsername).size();
        int insertedNoteId = noteService.insertNote(note, authUsername);
        assertEquals(initCount + 1, noteService.getNotes(authUsername).size());
        assertNotNull(noteService.getNoteById(insertedNoteId, authUsername));
        Note newSameTitleNote = new Note();
        newSameTitleNote.setTitle("xpto");
        assertThrowsExactly(RuntimeException.class, () -> {
            noteService.insertNote(newSameTitleNote, authUsername);
        }, "Note already exists with that title!");
        Note insertedNoteFromList = noteService.getNotes(authUsername).stream().filter(n -> n.getNoteId() == insertedNoteId).toList().get(0);
        assertEquals(insertedNoteId, insertedNoteFromList.getNoteId());
        assertTrue(noteService.deleteNote(insertedNoteId, authUsername));
        assertEquals(initCount, noteService.getNotes(authUsername).size());
        assertNull(noteService.getNoteById(insertedNoteId, authUsername));
    }
}
