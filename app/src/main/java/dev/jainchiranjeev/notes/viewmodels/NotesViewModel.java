package dev.jainchiranjeev.notes.viewmodels;

import android.app.Application;
import android.content.Context;
import android.os.AsyncTask;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.lifecycle.AndroidViewModel;
import androidx.lifecycle.LiveData;

import java.util.List;

import dev.jainchiranjeev.notes.dao.NotesDB;
import dev.jainchiranjeev.notes.models.NoteModel;

public class NotesViewModel extends AndroidViewModel {
    NotesLiveData notesLiveData;
    public NotesViewModel(@NonNull Application application) {
        super(application);
    }

    public LiveData<List<NoteModel>> getNotesList(Context context) {
        notesLiveData = new NotesLiveData(context);
        return notesLiveData;
    }

}

class NotesLiveData extends LiveData<List<NoteModel>> {
    private final Context context;

    NotesLiveData(Context context) {
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