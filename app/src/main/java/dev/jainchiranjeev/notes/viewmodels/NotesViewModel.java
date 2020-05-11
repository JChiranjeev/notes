package dev.jainchiranjeev.notes.viewmodels;

import android.app.Application;
import android.content.Context;
import android.os.AsyncTask;

import androidx.annotation.NonNull;
import androidx.lifecycle.AndroidViewModel;
import androidx.lifecycle.LiveData;

import java.util.List;

import dev.jainchiranjeev.notes.dao.NotesDB;
import dev.jainchiranjeev.notes.models.NoteModel;

public class NotesViewModel extends AndroidViewModel {
    AllNotesLiveData allNotesLiveData;
    AddNoteLiveData addNoteLiveData;
    NoteByIdLiveData noteByIdLiveData;
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
}

class AllNotesLiveData extends LiveData<List<NoteModel>> {
    private final Context context;

    AllNotesLiveData(Context context) {
        this.context = context;
        loadData();
    }

    @Override
    protected void postValue(List<NoteModel> value) {
        super.postValue(value);
    }

    @Override
    protected void setValue(List<NoteModel> value) {
        super.setValue(value);
    }

    private void loadData() {
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
        loadData(note);
    }

    @Override
    protected void postValue(Boolean value) {
        super.postValue(value);
    }

    @Override
    protected void setValue(Boolean value) {
        super.setValue(value);
    }

    private void loadData(NoteModel note) {
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
        loadData(noteId);
    }

    @Override
    protected void postValue(NoteModel value) {
        super.postValue(value);
    }

    @Override
    protected void setValue(NoteModel value) {
        super.setValue(value);
    }

    private void loadData(int noteId) {
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