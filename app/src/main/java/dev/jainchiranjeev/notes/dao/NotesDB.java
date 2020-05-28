package dev.jainchiranjeev.notes.dao;

import android.content.Context;

import androidx.annotation.NonNull;
import androidx.room.Database;
import androidx.room.DatabaseConfiguration;
import androidx.room.InvalidationTracker;
import androidx.room.Room;
import androidx.room.RoomDatabase;
import androidx.room.migration.Migration;
import androidx.sqlite.db.SupportSQLiteDatabase;
import androidx.sqlite.db.SupportSQLiteOpenHelper;

import dev.jainchiranjeev.notes.models.NoteModel;

@Database(entities = NoteModel.class, exportSchema = false, version = 2)
public abstract class NotesDB extends RoomDatabase {

    private static final String DB_NAME = "db_notes";
    private static NotesDB instance;

    static final Migration MIGRATION_1_2 = new Migration(1,2) {
        @Override
        public void migrate(@NonNull SupportSQLiteDatabase database) {
            database.execSQL("ALTER TABLE table_notes ADD COLUMN todoList TEXT");
            database.execSQL("ALTER TABLE table_notes ADD COLUMN isTodoList INTEGER");
            database.execSQL("ALTER TABLE table_notes ADD COLUMN reminderDateTime INTEGER");
        }
    };

    public static synchronized NotesDB getInstance(Context context) {
        if (instance == null) {
            instance = Room.databaseBuilder(context.getApplicationContext(), NotesDB.class, DB_NAME)
                    .addMigrations(MIGRATION_1_2)
                    .build();
        }
        return instance;
    }

    public abstract NotesDAO notesDAO();

}
