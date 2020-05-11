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
import androidx.fragment.app.FragmentTransaction;
import androidx.lifecycle.ViewModelProviders;

import java.util.List;

import dev.jainchiranjeev.notes.R;
import dev.jainchiranjeev.notes.dao.NotesDB;
import dev.jainchiranjeev.notes.databinding.FragmentNotesBinding;
import dev.jainchiranjeev.notes.models.NoteModel;
import dev.jainchiranjeev.notes.viewmodels.NotesViewModel;

public class FragmentNotes extends Fragment implements View.OnClickListener {

    View view;
    FragmentNotesBinding binding;
    Context context;
    List<NoteModel> notesList;
    NotesViewModel notesViewModel;
    FragmentManager manager;
    FragmentTransaction transaction;

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        binding = FragmentNotesBinding.inflate(getLayoutInflater(), container, false);
        view = binding.getRoot();
        context = getContext();

        notesViewModel = ViewModelProviders.of(this).get(NotesViewModel.class);
        notesViewModel.getNotesList(context).observe(this, data -> {
            notesList = data;
            handleNotes(notesList);
        });

        binding.fabNewNote.setOnClickListener(this);

        return view;
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        binding = null;
    }

    private void handleNotes(List<NoteModel> notesList) {
        Log.i("Notes",notesList.toString());
        Log.i("Notes",notesList.get(notesList.size() -1).noteContent);
    }

    @Override
    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.fab_new_note:
                manager = getFragmentManager();
                transaction = manager.beginTransaction();
                transaction.setTransition(FragmentTransaction.TRANSIT_FRAGMENT_OPEN);
                transaction.replace(R.id.crfl_main_activity, new FragmentNewNote());
                transaction.addToBackStack(null);
                transaction.commit();
                break;
        }
    }
}
