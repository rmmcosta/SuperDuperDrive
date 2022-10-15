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
        int initCount = noteService.getNotes().size();
        int insertedNoteId = noteService.insertNote(note);
        assertEquals(initCount + 1, noteService.getNotes().size());
        assertNotNull(noteService.getNoteById(insertedNoteId));
        Note newSameTitleNote = new Note();
        newSameTitleNote.setTitle("xpto");
        assertThrows(RuntimeException.class, () -> {
            noteService.insertNote(newSameTitleNote);
        }, "Note already exists with that title!");
        Note insertedNoteFromList = noteService.getNotes().stream().filter(n -> n.getNoteId() == insertedNoteId).toList().get(0);
        assertEquals(insertedNoteId, insertedNoteFromList.getNoteId());
        assertTrue(noteService.deleteNote(insertedNoteId));
        assertEquals(initCount, noteService.getNotes().size());
        assertNull(noteService.getNoteById(insertedNoteId));
    }
}
