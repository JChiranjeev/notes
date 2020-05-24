package dev.jainchiranjeev.notes.fragments;

import android.content.Context;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.PopupMenu;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.core.content.ContextCompat;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentTransaction;
import androidx.lifecycle.ViewModelProviders;
import androidx.recyclerview.widget.StaggeredGridLayoutManager;

import com.afollestad.materialdialogs.DialogAction;
import com.afollestad.materialdialogs.MaterialDialog;
import com.bumptech.glide.Glide;
import com.google.android.material.snackbar.Snackbar;

import java.util.List;

import dev.jainchiranjeev.notes.R;
import dev.jainchiranjeev.notes.databinding.FragmentArchivedNotesBinding;
import dev.jainchiranjeev.notes.models.NoteModel;
import dev.jainchiranjeev.notes.presenter.NotesAdapter;
import dev.jainchiranjeev.notes.services.SelectionEnabledListener;
import dev.jainchiranjeev.notes.viewmodels.NotesViewModel;

public class FragmentArchivedNotes extends Fragment implements SelectionEnabledListener, View.OnClickListener {

    View view;
    FragmentArchivedNotesBinding binding;
    Context context;
    List<NoteModel> notesList;
    NotesViewModel notesViewModel;
    FragmentManager manager;
    FragmentTransaction transaction;
    NotesAdapter adapter;
    Boolean isArchives = true;

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        binding = FragmentArchivedNotesBinding.inflate(getLayoutInflater(), container, false);
        view = binding.getRoot();
        context = getContext();

        notesViewModel = ViewModelProviders.of(this).get(NotesViewModel.class);
        loadNotes(context, notesViewModel);

        binding.fabUnarchiveNotes.setOnClickListener(this);
        binding.fabDeleteNotes.setOnClickListener(this);
        binding.ibOptions.setOnClickListener(this);

        notesViewModel = null;
        return view;
    }

    private void loadNotes(Context context, NotesViewModel notesViewModel) {
        notesViewModel.getNotesList(context).observe(this, data -> {
            notesList = data;
            handleNotes(notesList);
        });
    }

    private void handleNotes(List<NoteModel> notesList) {
        adapter = new NotesAdapter(context, notesList, isArchives, this);
        notesList = adapter.notesList;
        if (notesList == null || notesList.size() < 1) {
            binding.svNotes.setVisibility(View.GONE);
            binding.clNoNotesFound.setVisibility(View.VISIBLE);
            binding.tvNoNotesFound.setText("Err. No archived notes found.");
            Glide.with(view).load(R.drawable.ic_empty).fitCenter().into(binding.ivNoNotesFound);
        } else {
            binding.svNotes.setVisibility(View.VISIBLE);
            binding.clNoNotesFound.setVisibility(View.GONE);
            binding.rvNotes.setAdapter(adapter);
            binding.rvNotes.setLayoutManager(new StaggeredGridLayoutManager(2, StaggeredGridLayoutManager.VERTICAL));
        }
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        binding = null;
    }

    @Override
    public void onSelectionEnabled(Boolean selectionEnabled) {
        if (selectionEnabled) {
            binding.llAllNotesActions.setVisibility(View.VISIBLE);
            Glide.with(view).load(R.drawable.ic_delete).fitCenter().into(binding.fabDeleteNotes);
            Glide.with(view).load(R.drawable.ic_unarchive).fitCenter().into(binding.fabUnarchiveNotes);
        } else {
            binding.llAllNotesActions.setVisibility(View.GONE);
        }
    }

    @Override
    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.fab_delete_notes:
                if (adapter != null) {
                    new MaterialDialog.Builder(context)
                            .title("Delete?")
                            .content("Are you sure to delete selected notes?")
                            .positiveText("Delete Notes")
                            .positiveColor(ContextCompat.getColor(context, R.color.md_red_500))
                            .onPositive(new MaterialDialog.SingleButtonCallback() {
                                @Override
                                public void onClick(@NonNull MaterialDialog dialog, @NonNull DialogAction which) {
                                    List<NoteModel> selectedNotes = adapter.getSelected();
                                    notesViewModel = ViewModelProviders.of(getActivity()).get(NotesViewModel.class);
                                    notesViewModel.deleteMultipleNotes(context, selectedNotes).observe(getActivity(), data -> {
                                        Snackbar snackbar = Snackbar.make(binding.clArchivedNotesSnackbar, "Notes deleted", Snackbar.LENGTH_SHORT);
                                        snackbar.setTextColor(ContextCompat.getColor(context, R.color.colorPrimary));
                                        snackbar.getView().setBackgroundColor(ContextCompat.getColor(context, R.color.contrastPrimary));
                                        snackbar.show();
                                        loadNotes(context, notesViewModel);
                                    });
                                }
                            })
                            .negativeText("Cancel Delete")
                            .negativeColor(ContextCompat.getColor(context, R.color.contrastPrimary))
                            .onNegative(new MaterialDialog.SingleButtonCallback() {
                                @Override
                                public void onClick(@NonNull MaterialDialog dialog, @NonNull DialogAction which) {
//                                    Snackbar snackbar = Snackbar.make(binding.clAllNotesSnackbar, "Notes not deleted", Snackbar.LENGTH_SHORT);
//                                    snackbar.setTextColor(ContextCompat.getColor(context, R.color.colorPrimary));
//                                    snackbar.getView().setBackgroundColor(ContextCompat.getColor(context, R.color.contrastPrimary));
//                                    snackbar.show();
                                }
                            })
                            .show();
                }
                break;
            case R.id.fab_unarchive_notes:
                List<NoteModel> selectedNotes = adapter.getSelected();
                notesViewModel = ViewModelProviders.of(this).get(NotesViewModel.class);
                notesViewModel.archiveMultipleNotes(context, selectedNotes, !isArchives).observe(this, data -> {
                    Snackbar snackbar = Snackbar.make(binding.clArchivedNotesSnackbar, "Selected Notes Unarchived", Snackbar.LENGTH_SHORT);
                    snackbar.setTextColor(ContextCompat.getColor(context, R.color.colorPrimary));
                    snackbar.setAction("View", new View.OnClickListener() {
                        @Override
                        public void onClick(View view) {
                            manager = getFragmentManager();
                            manager.popBackStack();
                        }
                    });
                    snackbar.setActionTextColor(ContextCompat.getColor(context, R.color.colorPrimary));
                    snackbar.getView().setBackgroundColor(ContextCompat.getColor(context, R.color.contrastPrimary));
                    snackbar.show();
                    loadNotes(context, notesViewModel);
                });
                break;
            case R.id.ib_options:
                PopupMenu menu = new PopupMenu(context, binding.ibOptions);
                menu.getMenuInflater().inflate(R.menu.menu_fragment_archived_notes, menu.getMenu());
                menu.setOnMenuItemClickListener(new PopupMenu.OnMenuItemClickListener() {
                    @Override
                    public boolean onMenuItemClick(MenuItem menuItem) {
                        menuItemClick(menuItem);
                        return true;
                    }
                });
                menu.show();
                break;
        }
    }

    private void menuItemClick(MenuItem menuItem) {
        switch (menuItem.getItemId()) {
            case R.id.menu_unarchive_button:
                manager = getFragmentManager();
                manager.popBackStack();
                break;
        }
    }
}
