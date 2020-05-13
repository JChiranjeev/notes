package dev.jainchiranjeev.notes.fragments;

import android.content.Context;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.animation.DecelerateInterpolator;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentTransaction;
import androidx.lifecycle.ViewModelProviders;
import androidx.recyclerview.widget.StaggeredGridLayoutManager;

import com.bumptech.glide.Glide;

import java.util.List;

import dev.jainchiranjeev.notes.R;
import dev.jainchiranjeev.notes.databinding.FragmentAllNotesBinding;
import dev.jainchiranjeev.notes.models.NoteModel;
import dev.jainchiranjeev.notes.presenter.NotesAdapter;
import dev.jainchiranjeev.notes.services.SelectionEnabledListener;
import dev.jainchiranjeev.notes.utils.Constants;
import dev.jainchiranjeev.notes.utils.PersistentDataStorage;
import dev.jainchiranjeev.notes.viewmodels.NotesViewModel;
import uk.co.deanwild.materialshowcaseview.MaterialShowcaseView;

public class FragmentAllNotes extends Fragment implements View.OnClickListener, SelectionEnabledListener, View.OnLongClickListener {

    View view;
    FragmentAllNotesBinding binding;
    Context context;
    List<NoteModel> notesList;
    NotesViewModel notesViewModel;
    FragmentManager manager;
    FragmentTransaction transaction;
    Bundle bundle;
    NotesAdapter notesAdapter;
    Boolean isArchivesView = false;

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        binding = FragmentAllNotesBinding.inflate(getLayoutInflater(), container, false);
        view = binding.getRoot();
        context = getContext();

        Bundle bundle = getArguments();
        switchNotesAndArchives(bundle);

        new MaterialShowcaseView.Builder(getActivity())
                .setTarget(binding.tvNotesToolbar)
                .setDismissText("Got It!")
                .setContentText("Long Press to switch between Notes/Archives")
                .setDelay(1000)
                .singleUse("NotesArchivesIntro")
                .show();

        notesViewModel = ViewModelProviders.of(this).get(NotesViewModel.class);
        loadNotes(context, notesViewModel);

        binding.fabNewNote.setOnClickListener(this);
        binding.ibArchiveButton.setOnClickListener(this);
        binding.ibDeleteButton.setOnClickListener(this);
        binding.tvNotesToolbar.setOnLongClickListener(this);

        notesViewModel = null;
        return view;
    }

    private void switchNotesAndArchives(Bundle bundle) {
        if (bundle == null) {
            isArchivesView = false;
        } else {
            isArchivesView = bundle.getBoolean("IsArchivesView", false);
        }

        if (isArchivesView) {
            binding.tvNotesToolbar.setText("Archives");
        } else {
            binding.tvNotesToolbar.setText("Notes");
        }
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        binding = null;
    }

    private void loadNotes(Context context, NotesViewModel notesViewModel) {
        notesViewModel.getNotesList(context).observe(this, data -> {
            notesList = data;
            handleNotes(notesList);
        });
    }

    private void handleNotes(List<NoteModel> notesList) {
        if (notesList == null || notesList.size() < 1) {
            binding.svNotes.setVisibility(View.GONE);
            binding.clNoNotesFound.setVisibility(View.VISIBLE);
            Glide.with(view).load(R.drawable.ic_empty).fitCenter().into(binding.ivNoNotesFound);
        } else {
            binding.svNotes.setVisibility(View.VISIBLE);
            binding.clNoNotesFound.setVisibility(View.GONE);
            notesAdapter = new NotesAdapter(context, notesList, isArchivesView, this);
            binding.rvNotes.setAdapter(notesAdapter);
            binding.rvNotes.setLayoutManager(new StaggeredGridLayoutManager(2, StaggeredGridLayoutManager.VERTICAL));
        }
    }

    @Override
    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.fab_new_note:
                FragmentNoteEditor noteEditor = new FragmentNoteEditor();
                manager = getFragmentManager();
                transaction = manager.beginTransaction();
                bundle = new Bundle();
                bundle.putBoolean("IsNewNote", true);
                noteEditor.setArguments(bundle);
                transaction.setTransition(FragmentTransaction.TRANSIT_FRAGMENT_OPEN);
                transaction.replace(R.id.crfl_main_activity, noteEditor);
                transaction.addToBackStack(null);
                transaction.commit();
                break;
            case R.id.ib_delete_button:
                if (notesAdapter != null) {
                    List<NoteModel> selectedNotes = notesAdapter.getSelected();
                    notesViewModel = ViewModelProviders.of(this).get(NotesViewModel.class);
                    notesViewModel.deleteMultipleNotes(context, selectedNotes).observe(this, data -> {
                        Log.i("Multiple Notes Deleted", data.toString());
                        loadNotes(context, notesViewModel);
                    });
                }
                break;
            case R.id.ib_archive_button:
                if (notesAdapter != null) {
                    List<NoteModel> selectedNotes = notesAdapter.getSelected();
                    notesViewModel = ViewModelProviders.of(this).get(NotesViewModel.class);
                    notesViewModel.archiveMultipleNotes(context, selectedNotes).observe(this, data -> {
                        Log.i("Multiple Notes Archived", data.toString());
                        loadNotes(context, notesViewModel);
                    });
                }
                break;
        }
    }

    @Override
    public void onSelectionEnabled(Boolean selectionEnabled) {
        if (selectionEnabled) {
            binding.llAllNotesActions.setVisibility(View.VISIBLE);
            Glide.with(view).load(R.drawable.ic_delete).fitCenter().into(binding.ibDeleteButton);
            Glide.with(view).load(R.drawable.ic_archive).fitCenter().into(binding.ibArchiveButton);
        } else {
            binding.llAllNotesActions.setVisibility(View.GONE);
        }
    }

    @Override
    public boolean onLongClick(View view) {
        if (isArchivesView) {
            manager = getFragmentManager();
            manager.popBackStack();
        } else {
            manager = getFragmentManager();
            FragmentAllNotes fragmentAllNotes = new FragmentAllNotes();
            transaction = manager.beginTransaction();
            Bundle bundle = new Bundle();
            bundle.putBoolean("IsArchivesView", true);
            fragmentAllNotes.setArguments(bundle);
            manager = getFragmentManager();
            transaction = manager.beginTransaction();
            transaction.replace(R.id.crfl_main_fragment, fragmentAllNotes);
            transaction.addToBackStack(null);
            transaction.commit();
        }
        return true;
    }
}
