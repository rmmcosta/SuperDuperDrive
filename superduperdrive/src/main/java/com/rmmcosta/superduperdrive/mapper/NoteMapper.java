package com.rmmcosta.superduperdrive.mapper;

import com.rmmcosta.superduperdrive.model.Note;
import org.apache.ibatis.annotations.*;

import java.util.List;

@Mapper
public interface NoteMapper {
    @Select("SELECT * FROM notes WHERE owner_username = #{ownerUsername}")
    List<Note> getNotes(String ownerUsername);

    @Select("SELECT * FROM notes WHERE note_id=#{noteId} and owner_username = #{ownerUsername}")
    Note getNoteById(Integer noteId, String ownerUsername);

    @Select("SELECT * FROM notes WHERE title=#{title}")
    Note getNoteByTitle(String title, String ownerUsername);

    @Insert("INSERT INTO notes(title, description, owner_username) VALUES(#{note.title}, #{note.description}, #{ownerUsername})")
    @Options(useGeneratedKeys = true, keyProperty = "note.noteId")
    int insertNote(Note note, String ownerUsername);

    @Delete("DELETE FROM notes WHERE note_id = #{noteId} and owner_username = #{ownerUsername}")
    boolean deleteNote(Integer noteId, String ownerUsername);

    @Update("UPDATE notes SET title = #{note.title}, description = #{note.description} WHERE note_id = #{note.noteId} and owner_username = #{ownerUsername}")
    boolean updateNote(Note note, String ownerUsername);
}
