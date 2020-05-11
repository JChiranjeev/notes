package dev.jainchiranjeev.notes.models;

import androidx.room.Entity;
import androidx.room.PrimaryKey;

import java.time.LocalDateTime;

@Entity(tableName = "table_notes")
public class NoteModel {
    @PrimaryKey(autoGenerate = true)
    public int noteId;
    public String noteTitle,noteContent;
    public long creationDate,modificationDate;
    public String color;
    public boolean isPasswordProtected;

    public NoteModel(String noteTitle, String noteContent, long creationDate, long modificationDate, String color, boolean isPasswordProtected) {
        this.noteTitle = noteTitle;
        this.noteContent = noteContent;
        this.creationDate = creationDate;
        this.modificationDate = modificationDate;
        this.color = color;
        this.isPasswordProtected = isPasswordProtected;
    }
}
