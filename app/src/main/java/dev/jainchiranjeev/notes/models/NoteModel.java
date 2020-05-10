package dev.jainchiranjeev.notes.models;

import androidx.room.Entity;
import androidx.room.PrimaryKey;

import java.time.LocalDateTime;

@Entity(tableName = "table_notes")
public class NoteModel {
    @PrimaryKey(autoGenerate = true)
    public int noteId;
    public String noteTitle,noteContent;
    public float creationDate,modificationDate;
    public String color;

    public NoteModel(int noteId, String noteTitle, String noteContent, float creationDate, float modificationDate, String color) {
        this.noteId = noteId;
        this.noteTitle = noteTitle;
        this.noteContent = noteContent;
        this.creationDate = creationDate;
        this.modificationDate = modificationDate;
        this.color = color;
    }
}
