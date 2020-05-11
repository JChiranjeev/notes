package dev.jainchiranjeev.notes.fragments;

import android.content.Context;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.lifecycle.ViewModelProviders;

import java.time.LocalDateTime;
import java.util.Calendar;
import java.util.Date;

import dev.jainchiranjeev.notes.R;
import dev.jainchiranjeev.notes.dao.NotesDB;
import dev.jainchiranjeev.notes.databinding.FragmentNewNoteBinding;
import dev.jainchiranjeev.notes.models.NoteModel;
import dev.jainchiranjeev.notes.viewmodels.NotesViewModel;

public class FragmentNewNote extends Fragment implements View.OnClickListener {

    View view;
    FragmentNewNoteBinding binding;
    FragmentManager manager;
    Context context;
    NotesViewModel notesViewModel;

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        binding = FragmentNewNoteBinding.inflate(getLayoutInflater());
        view = binding.getRoot();
        context = getContext();
        manager = getFragmentManager();

        binding.etNoteNoteContent.requestFocus();

        binding.fabSaveNote.setOnClickListener(this);

        return view;
    }

    @Override
    public void onStop() {
        super.onStop();

    }

    @Override
    public void onClick(View view) {
        switch(view.getId()) {
            case R.id.fab_save_note:
                String noteTitle,noteContent,color;
                long creationDate, modificationDate;
                noteTitle = binding.etNoteTitle.getText().toString();
                noteContent = binding.etNoteNoteContent.getText().toString();
                color = null;
                creationDate = Calendar.getInstance().getTimeInMillis();
                modificationDate = Calendar.getInstance().getTimeInMillis();
                if(!noteContent.equals(null) && noteContent.length() > 0) {
                    notesViewModel = ViewModelProviders.of(this).get(NotesViewModel.class);
                    notesViewModel.addNewNote(context, new NoteModel(noteTitle, noteContent, creationDate, modificationDate, color, false)).observe(this, data -> {
                        Log.i("Note Added", data.toString());
                    });
                }
                manager.popBackStack();
                break;
        }
    }
}
