package dev.jainchiranjeev.notes.viewmodels;

import android.app.Application;
import android.content.Context;
import android.os.AsyncTask;

import androidx.annotation.NonNull;
import androidx.lifecycle.AndroidViewModel;
import androidx.lifecycle.LiveData;

import java.util.ArrayList;
import java.util.List;

import dev.jainchiranjeev.notes.dao.NotesDB;
import dev.jainchiranjeev.notes.models.NoteModel;

public class NotesViewModel extends AndroidViewModel {
    AllNotesLiveData allNotesLiveData;
    AddNoteLiveData addNoteLiveData;
    NoteByIdLiveData noteByIdLiveData;
    UpdateNoteLiveData updateNoteLiveData;
    DeleteNoteLiveData deleteNoteLiveData;
    DeleteMultipleNotesLiveData deleteMultipleNotesLiveData;
    ArchiveMultipleNotesLiveData archiveMultipleNotesLiveData;
    public NotesViewModel(@NonNull Application application) {
        super(application);
    }

    public LiveData<List<NoteModel>> getNotesList(Context context) {
        allNotesLiveData = new AllNotesLiveData(context);
        return allNotesLiveData;
    }

    public LiveData<Boolean> addNewNote(Context context, NoteModel note) {
        addNoteLiveData = new AddNoteLiveData(context, note);
        return addNoteLiveData;
    }

    public LiveData<NoteModel> getNoteById(Context context, int noteId) {
        noteByIdLiveData = new NoteByIdLiveData(context, noteId);
        return noteByIdLiveData;
    }

    public LiveData<Boolean> updateNote(Context context, NoteModel note) {
        updateNoteLiveData = new UpdateNoteLiveData(context, note);
        return updateNoteLiveData;
    }

    public LiveData<Boolean> deleteNote(Context context, NoteModel note) {
        deleteNoteLiveData = new DeleteNoteLiveData(context,note);
        return deleteNoteLiveData;
    }

    public LiveData<Boolean> deleteMultipleNotes(Context context, List<NoteModel> notesToDelete) {
        deleteMultipleNotesLiveData = new DeleteMultipleNotesLiveData(context, notesToDelete);
        return deleteMultipleNotesLiveData;
    }

    public LiveData<Boolean> archiveMultipleNotes(Context context, List<NoteModel> notesToArchive) {
        archiveMultipleNotesLiveData = new ArchiveMultipleNotesLiveData(context, notesToArchive);
        return archiveMultipleNotesLiveData;
    }
}

class AllNotesLiveData extends LiveData<List<NoteModel>> {
    private final Context context;

    AllNotesLiveData(Context context) {
        this.context = context;
        getAllNotes();
    }

    private void getAllNotes() {
        new AsyncTask<Void, Void, List<NoteModel>>() {
            @Override
            protected List<NoteModel> doInBackground(Void... voids) {
                NotesDB notesDB = NotesDB.getInstance(context);
                return notesDB.notesDAO().getAllNotes();
            }

            @Override
            protected void onPostExecute(List<NoteModel> noteModels) {
                super.onPostExecute(noteModels);
                setValue(noteModels);
            }
        }.execute();
    }
}

class AddNoteLiveData extends LiveData<Boolean> {
    private final Context context;

    AddNoteLiveData(Context context, NoteModel note) {
        this.context = context;
        addNote(note);
    }

    private void addNote(NoteModel note) {
        new AsyncTask<NoteModel, Void, Boolean>() {
            @Override
            protected Boolean doInBackground(NoteModel... noteModels) {
                NotesDB notesDB = NotesDB.getInstance(context);
                notesDB.notesDAO().addNote(noteModels[0]);
                return true;
            }

            @Override
            protected void onPostExecute(Boolean aBoolean) {
                super.onPostExecute(aBoolean);
                setValue(aBoolean);
            }
        }.execute(note);
    }
}

class NoteByIdLiveData extends LiveData<NoteModel> {
    private final Context context;

    NoteByIdLiveData(Context context, int noteId) {
        this.context = context;
        getNoteById(noteId);
    }

    private void getNoteById(int noteId) {
        new AsyncTask<Integer, Void, NoteModel>() {

            @Override
            protected NoteModel doInBackground(Integer... integers) {
                NotesDB notesDB = NotesDB.getInstance(context);
                NoteModel note = notesDB.notesDAO().getNoteById(noteId);
                return note;
            }

            @Override
            protected void onPostExecute(NoteModel noteModel) {
                super.onPostExecute(noteModel);
                setValue(noteModel);
            }
        }.execute(noteId);
    }
}

class UpdateNoteLiveData extends LiveData<Boolean> {
    private final Context context;

    UpdateNoteLiveData(Context context, NoteModel note) {
        this.context = context;
        deleteNote(note);
    }

    private void deleteNote(NoteModel note) {
        new AsyncTask<NoteModel, Void, Boolean>() {
            @Override
            protected Boolean doInBackground(NoteModel... noteModels) {
                NotesDB notesDB = NotesDB.getInstance(context);
                notesDB.notesDAO().updateNote(noteModels[0]);
                return true;
            }

            @Override
            protected void onPostExecute(Boolean aBoolean) {
                super.onPostExecute(aBoolean);
                setValue(aBoolean);
            }
        }.execute(note);
    }
}

class DeleteNoteLiveData extends LiveData<Boolean> {
    private final Context context;

    DeleteNoteLiveData(Context context, NoteModel note) {
        this.context = context;
        deleteNote(note);
    }

    private void deleteNote(NoteModel note) {
        new AsyncTask<NoteModel, Void, Boolean>() {
            @Override
            protected Boolean doInBackground(NoteModel... noteModels) {
                NotesDB notesDB = NotesDB.getInstance(context);
                notesDB.notesDAO().deleteNote(note);
                return true;
            }

            @Override
            protected void onPostExecute(Boolean aBoolean) {
                super.onPostExecute(aBoolean);
                setValue(aBoolean);
            }
        }.execute(note);
    }
}

class DeleteMultipleNotesLiveData extends LiveData<Boolean> {
    private final Context context;

    DeleteMultipleNotesLiveData(Context context, List<NoteModel> notesList) {
        this.context = context;
        deleteMultipleNotes(notesList);
    }

    private void deleteMultipleNotes(List<NoteModel> notesList) {
        new AsyncTask<List<NoteModel>, Void, Boolean>() {

            @Override
            protected Boolean doInBackground(List<NoteModel>... lists) {
                List<Integer> noteIdsToDelete = new ArrayList<>();
                for (NoteModel note : lists[0]) {
                    noteIdsToDelete.add(note.getNoteId());
                }
                NotesDB notesDB = NotesDB.getInstance(context);
                notesDB.notesDAO().deleteMultipleNotes(noteIdsToDelete);
                return true;
            }

            @Override
            protected void onPostExecute(Boolean aBoolean) {
                super.onPostExecute(aBoolean);
                setValue(aBoolean);
            }
        }.execute(notesList);
    }
}

class ArchiveMultipleNotesLiveData extends LiveData<Boolean> {
    private final Context context;

    ArchiveMultipleNotesLiveData(Context context, List<NoteModel> notesList) {
        this.context = context;
        archiveMultiplenotes(notesList);
    }

    private void archiveMultiplenotes(List<NoteModel> notesList) {
        new AsyncTask<List<NoteModel>, Void, Boolean>() {
            @Override
            protected Boolean doInBackground(List<NoteModel>... lists) {
                List<Integer> noteIdsToArchive = new ArrayList<>();
                for(NoteModel note : notesList) {
                    noteIdsToArchive.add(note.getNoteId());
                }
                NotesDB notesDB = NotesDB.getInstance(context);
                notesDB.notesDAO().archiveMultipleNotes(true, noteIdsToArchive);
                return true;
            }

            @Override
            protected void onPostExecute(Boolean aBoolean) {
                super.onPostExecute(aBoolean);
                setValue(aBoolean);
            }
        }.execute(notesList);
    }
}