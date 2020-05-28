package dev.jainchiranjeev.notes.models;

import android.text.SpannableString;

import androidx.room.Entity;
import androidx.room.Ignore;
import androidx.room.PrimaryKey;
import androidx.room.TypeConverters;

import java.io.Serializable;
import java.time.LocalDateTime;
import java.util.List;

import dev.jainchiranjeev.notes.utils.TodoConverters;

@Entity(tableName = "table_notes")
public class NoteModel implements Serializable {
    @PrimaryKey(autoGenerate = true)
    private int noteId;
    private String noteTitle,noteContent;
    private long creationDate,modificationDate;
    private String color;
    private boolean isPasswordProtected;
    private boolean isChecked = false;
    private boolean isArchived = false;
    private boolean isTodoList,isReminder;
    private long reminderDateTime;
    @TypeConverters(TodoConverters.class)
    private List<ToDoModel> todoList;

    @Ignore
    public NoteModel() {
    }

    public NoteModel(String noteTitle, String noteContent, long creationDate, long modificationDate, String color, boolean isPasswordProtected) {
        this.noteTitle = noteTitle;
        this.noteContent = noteContent;
        this.creationDate = creationDate;
        this.modificationDate = modificationDate;
        this.color = color;
        this.isPasswordProtected = isPasswordProtected;
        this.isChecked = false;
    }

    public boolean isChecked() {
        return isChecked;
    }

    public void setChecked(boolean checked) {
        isChecked = checked;
    }

    public boolean isArchived() {
        return isArchived;
    }

    public void setArchived(boolean archived) {
        isArchived = archived;
    }

    public int getNoteId() {
        return noteId;
    }

    public void setNoteId(int noteId) {
        this.noteId = noteId;
    }

    public String getNoteTitle() {
        return noteTitle;
    }

    public void setNoteTitle(String noteTitle) {
        this.noteTitle = noteTitle;
    }

    public String getNoteContent() {
        return noteContent;
    }

    public void setNoteContent(String noteContent) {
        this.noteContent = noteContent;
    }

    public long getCreationDate() {
        return creationDate;
    }

    public void setCreationDate(long creationDate) {
        this.creationDate = creationDate;
    }

    public long getModificationDate() {
        return modificationDate;
    }

    public void setModificationDate(long modificationDate) {
        this.modificationDate = modificationDate;
    }

    public String getColor() {
        return color;
    }

    public void setColor(String color) {
        this.color = color;
    }

    public boolean isPasswordProtected() {
        return isPasswordProtected;
    }

    public void setPasswordProtected(boolean passwordProtected) {
        isPasswordProtected = passwordProtected;
    }

    public List<ToDoModel> getTodoList() {
        return todoList;
    }

    public void setTodoList(List<ToDoModel> todoList) {
        this.todoList = todoList;
    }

    public boolean isTodoList() {
        return isTodoList;
    }

    public void setTodoList(boolean todoList) {
        isTodoList = todoList;
    }

    public long getReminderDateTime() {
        return reminderDateTime;
    }

    public void setReminderDateTime(long reminderDateTime) {
        this.reminderDateTime = reminderDateTime;
    }

    public boolean isReminder() {
        return isReminder;
    }

    public void setReminder(boolean reminder) {
        isReminder = reminder;
    }
}
