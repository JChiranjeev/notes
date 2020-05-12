package dev.jainchiranjeev.notes.dao;

import androidx.room.Dao;
import androidx.room.Delete;
import androidx.room.Insert;
import androidx.room.Query;
import androidx.room.Update;

import java.util.List;

import dev.jainchiranjeev.notes.models.NoteModel;

@Dao
public interface NotesDAO {
    @Query("SELECT * FROM table_notes")
    List<NoteModel> getAllNotes();
    @Insert
    void addNote(NoteModel note);
    @Update
    void updateNote(NoteModel note);
    @Delete
    void deleteNote(NoteModel note);
    @Query("SELECT * FROM table_notes WHERE noteId =:id")
    NoteModel getNoteById(int id);
    @Query("DELETE FROM table_notes WHERE noteId IN (:ids)")
    void deleteMultipleNotes(List<Integer> ids);
    @Query("UPDATE table_notes SET isArchived = :isArchive WHERE noteId IN (:ids)")
    void archiveMultipleNotes(Boolean isArchive, List<Integer> ids);
}
