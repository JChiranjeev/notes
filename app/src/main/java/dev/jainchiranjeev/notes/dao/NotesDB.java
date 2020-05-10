package dev.jainchiranjeev.notes.dao;

import android.content.Context;

import androidx.annotation.NonNull;
import androidx.room.Database;
import androidx.room.DatabaseConfiguration;
import androidx.room.InvalidationTracker;
import androidx.room.Room;
import androidx.room.RoomDatabase;
import androidx.sqlite.db.SupportSQLiteOpenHelper;

import dev.jainchiranjeev.notes.models.NoteModel;

@Database(entities = NoteModel.class, exportSchema = true, version = 1)
public abstract class NotesDB extends RoomDatabase {

    private static final String DB_NAME = "db_notes";
    private static NotesDB instance;

    public static synchronized NotesDB getInstance(Context context) {
        if (instance == null) {
            instance = Room.databaseBuilder(context.getApplicationContext(), NotesDB.class, DB_NAME)
                    .fallbackToDestructiveMigration()
                    .build();
        }
        return instance;
    }

    public abstract NotesDAO notesDAO();

}
