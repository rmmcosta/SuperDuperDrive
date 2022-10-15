package com.rmmcosta.superduperdrive.mapper;

import com.rmmcosta.superduperdrive.model.Note;
import org.apache.ibatis.annotations.*;

import java.util.List;

@Mapper
public interface NoteMapper {
    @Select("SELECT * FROM notes")
    List<Note> getNotes();

    @Select("SELECT * FROM notes WHERE note_id=#{noteId}")
    Note getNoteById(Integer noteId);

    @Select("SELECT * FROM notes WHERE title=#{title}")
    Note getNoteByTitle(String title);

    @Insert("INSERT INTO notes(title, description) VALUES(#{title}, #{description})")
    @Options(useGeneratedKeys = true, keyProperty = "noteId")
    int insertNote(Note note);

    @Delete("DELETE FROM notes WHERE note_id = #{noteId}")
    boolean deleteNote(Integer credentialId);

    @Update("UPDATE notes SET title = #{title}, description = #{description} WHERE note_id = #{noteId}")
    boolean updateNote(Note note);
}
