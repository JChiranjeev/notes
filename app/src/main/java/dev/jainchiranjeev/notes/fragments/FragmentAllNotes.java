package dev.jainchiranjeev.notes.fragments;

import android.content.Context;
import android.content.DialogInterface;
import android.os.Bundle;
import android.transition.TransitionInflater;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.view.animation.DecelerateInterpolator;
import android.widget.PopupMenu;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.core.content.ContextCompat;
import androidx.fragment.app.DialogFragment;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentTransaction;
import androidx.lifecycle.ViewModelProviders;
import androidx.recyclerview.widget.StaggeredGridLayoutManager;

import com.afollestad.materialdialogs.DialogAction;
import com.afollestad.materialdialogs.MaterialDialog;
import com.bumptech.glide.Glide;
import com.google.android.material.dialog.MaterialAlertDialogBuilder;
import com.google.android.material.snackbar.Snackbar;

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

        new MaterialShowcaseView.Builder(getActivity())
                .setTarget(binding.ibOptions)
                .setDismissText("Got It!")
                .setTitleText("Archives")
                .setContentText("Archives have moved here for easy access.")
                .setDelay(500)
                .singleUse("ArchivesIntro")
                .show();

        notesViewModel = ViewModelProviders.of(this).get(NotesViewModel.class);
        loadNotes(context, notesViewModel);

        binding.fabNewNote.setOnClickListener(this);
        binding.fabArchiveNotes.setOnClickListener(this);
        binding.fabDeleteNotes.setOnClickListener(this);
        binding.tvNotesToolbar.setOnLongClickListener(this);
        binding.ibOptions.setOnClickListener(this);

        notesViewModel = null;
        return view;
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
        notesAdapter = new NotesAdapter(context, notesList, isArchivesView, this);
        notesList = notesAdapter.notesList;
        if (notesList == null || notesList.size() < 1) {
            binding.svNotes.setVisibility(View.GONE);
            binding.clNoNotesFound.setVisibility(View.VISIBLE);
            Glide.with(view).load(R.drawable.ic_empty).fitCenter().into(binding.ivNoNotesFound);
        } else {
            binding.svNotes.setVisibility(View.VISIBLE);
            binding.clNoNotesFound.setVisibility(View.GONE);
            binding.rvNotes.setAdapter(notesAdapter);
            binding.rvNotes.setLayoutManager(new StaggeredGridLayoutManager(2, StaggeredGridLayoutManager.VERTICAL));
        }
    }

    @Override
    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.fab_new_note:
                binding.fabNewNote.setTransitionName("transition_note_content-1");
                FragmentNoteEditor noteEditor = new FragmentNoteEditor();
                manager = getFragmentManager();
                transaction = manager.beginTransaction();

                noteEditor.setSharedElementEnterTransition(TransitionInflater.from(context).inflateTransition(R.transition.transition_basic));
                noteEditor.setSharedElementReturnTransition(TransitionInflater.from(context).inflateTransition(R.transition.transition_basic));
                setEnterTransition(null);
                setExitTransition(null);

                transaction.addSharedElement(binding.fabNewNote, "transition_note_content-1");
                bundle = new Bundle();
                bundle.putBoolean("IsNewNote", true);
                noteEditor.setArguments(bundle);
                transaction.replace(R.id.crfl_main_activity, noteEditor);
                transaction.addToBackStack(null);
                transaction.commit();
                break;
            case R.id.fab_delete_notes:
                if (notesAdapter != null) {
                    new MaterialDialog.Builder(context)
                            .title("Delete?")
                            .content("Are you sure to delete selected notes?")
                            .positiveText("Delete Notes")
                            .positiveColor(ContextCompat.getColor(context, R.color.md_red_500))
                            .onPositive(new MaterialDialog.SingleButtonCallback() {
                                @Override
                                public void onClick(@NonNull MaterialDialog dialog, @NonNull DialogAction which) {
                                    List<NoteModel> selectedNotes = notesAdapter.getSelected();
                                    notesViewModel = ViewModelProviders.of(getActivity()).get(NotesViewModel.class);
                                    notesViewModel.deleteMultipleNotes(context, selectedNotes).observe(getActivity(), data -> {
                                        Snackbar snackbar = Snackbar.make(binding.clAllNotesSnackbar, "Notes deleted", Snackbar.LENGTH_SHORT);
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
            case R.id.fab_archive_notes:
                if (notesAdapter != null) {
                    List<NoteModel> selectedNotes = notesAdapter.getSelected();
                    notesViewModel = ViewModelProviders.of(this).get(NotesViewModel.class);
                    notesViewModel.archiveMultipleNotes(context, selectedNotes, !isArchivesView).observe(this, data -> {
                        Snackbar snackbar = Snackbar.make(binding.clAllNotesSnackbar, "Selected Notes Archived", Snackbar.LENGTH_SHORT);
                        snackbar.setTextColor(ContextCompat.getColor(context, R.color.colorPrimary));
                        snackbar.setAction("View", new View.OnClickListener() {
                            @Override
                            public void onClick(View view) {
                                manager = getFragmentManager();
                                FragmentArchivedNotes fragmentArchivedNotes = new FragmentArchivedNotes();
                                transaction = manager.beginTransaction();
                                transaction.replace(R.id.crfl_main_activity, fragmentArchivedNotes);
                                transaction.addToBackStack(null);
                                transaction.commit();
                            }
                        });
                        snackbar.setActionTextColor(ContextCompat.getColor(context, R.color.colorPrimary));
                        snackbar.getView().setBackgroundColor(ContextCompat.getColor(context, R.color.contrastPrimary));
                        snackbar.show();
                        loadNotes(context, notesViewModel);
                    });
                }
                break;
            case R.id.ib_options:
                PopupMenu menu = new PopupMenu(context, binding.ibOptions);
                menu.getMenuInflater().inflate(R.menu.menu_fragment_all_notes, menu.getMenu());
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
            case R.id.menu_archive_button:
                manager = getFragmentManager();
                FragmentArchivedNotes fragmentArchivedNotes = new FragmentArchivedNotes();
                transaction = manager.beginTransaction();
                transaction.replace(R.id.crfl_main_activity, fragmentArchivedNotes);
                transaction.addToBackStack(null);
                transaction.commit();
                break;
            case R.id.menu_about_button:
                manager = getFragmentManager();
                FragmentAbout fragmentAbout = new FragmentAbout();
                transaction = manager.beginTransaction();
                transaction.replace(R.id.crfl_main_activity, fragmentAbout);
                transaction.addToBackStack(null);
                transaction.commit();
                break;
        }
    }

    @Override
    public void onSelectionEnabled(Boolean selectionEnabled) {
        if (selectionEnabled) {
            binding.llAllNotesActions.setVisibility(View.VISIBLE);
            binding.fabNewNote.setVisibility(View.GONE);
            Glide.with(view).load(R.drawable.ic_delete).fitCenter().into(binding.fabDeleteNotes);
            Glide.with(view).load(R.drawable.ic_archive).fitCenter().into(binding.fabArchiveNotes);
        } else {
            binding.llAllNotesActions.setVisibility(View.GONE);
            binding.fabNewNote.setVisibility(View.VISIBLE);
        }
    }

    @Override
    public boolean onLongClick(View view) {
        manager = getFragmentManager();
        FragmentArchivedNotes fragmentArchivedNotes = new FragmentArchivedNotes();
        transaction = manager.beginTransaction();
        transaction.replace(R.id.crfl_main_activity, fragmentArchivedNotes);
        transaction.addToBackStack(null);
        transaction.commit();
        return true;
    }
}
