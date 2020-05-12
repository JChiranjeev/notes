package dev.jainchiranjeev.notes.fragments;

import android.content.Context;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.lifecycle.ViewModelProviders;

import com.bumptech.glide.Glide;

import java.util.Calendar;

import dev.jainchiranjeev.notes.R;
import dev.jainchiranjeev.notes.databinding.FragmentNoteEditorBinding;
import dev.jainchiranjeev.notes.models.NoteModel;
import dev.jainchiranjeev.notes.viewmodels.NotesViewModel;

public class FragmentNoteEditor extends Fragment implements View.OnClickListener {

    View view;
    FragmentNoteEditorBinding binding;
    FragmentManager manager;
    Context context;
    NotesViewModel notesViewModel;
    Boolean isNewNote;
    Bundle bundle;
    NoteModel note = null;
    int noteId = -1;
    Boolean isContentAvailable = false;

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        binding = FragmentNoteEditorBinding.inflate(getLayoutInflater());
        view = binding.getRoot();
        context = getContext();
        manager = getFragmentManager();

//        Set Icons
        Glide.with(view).load(R.drawable.ic_done).fitCenter().into(binding.fabSaveNote);

        bundle = this.getArguments();
        if(bundle != null) {
            noteId = bundle.getInt("NoteID",-1);
            isNewNote = bundle.getBoolean("IsNewNote");
            loadNote(noteId);
        } else {
            manager.popBackStack();
        }

        hideOrDisplayActions(isNewNote, isContentAvailable);

        binding.etNoteContent.requestFocus();

        binding.etNoteContent.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2) {

            }

            @Override
            public void onTextChanged(CharSequence charSequence, int i, int i1, int i2) {
                if(charSequence.length() > 0) {
                    isContentAvailable = true;
                } else {
                    isContentAvailable = false;
                }
                hideOrDisplayActions(isNewNote, isContentAvailable);
            }

            @Override
            public void afterTextChanged(Editable editable) {

            }
        });

        binding.fabSaveNote.setOnClickListener(this);
        binding.ibDeleteButton.setOnClickListener(this);
        binding.ibArchiveButton.setOnClickListener(this);

        return view;
    }

    private void hideOrDisplayActions(Boolean isNewNote, Boolean isContentAvailable) {
        if(!isNewNote && isContentAvailable) {
            binding.ibDeleteButton.setVisibility(View.VISIBLE);
            binding.ibArchiveButton.setVisibility(View.VISIBLE);
            Glide.with(view).load(R.drawable.ic_delete).fitCenter().into(binding.ibDeleteButton);
            Glide.with(view).load(R.drawable.ic_archive).fitCenter().into(binding.ibArchiveButton);
        } else if (isNewNote && isContentAvailable) {
            binding.ibDeleteButton.setVisibility(View.VISIBLE);
            binding.ibArchiveButton.setVisibility(View.VISIBLE);
            Glide.with(view).load(R.drawable.ic_delete).fitCenter().into(binding.ibDeleteButton);
            Glide.with(view).load(R.drawable.ic_archive).fitCenter().into(binding.ibArchiveButton);
        } else {
            binding.ibDeleteButton.setVisibility(View.GONE);
            binding.ibArchiveButton.setVisibility(View.GONE);
        }
    }

    private void loadNote(int noteId) {
        if (noteId < 0 && isNewNote) {
            isContentAvailable = false;
            note = new NoteModel();
        } else {
            isContentAvailable = true;
            notesViewModel = ViewModelProviders.of(this).get(NotesViewModel.class);
            notesViewModel.getNoteById(context, noteId).observe(this, data -> {
                note = data;
                binding.etNoteTitle.setText(data.getNoteTitle());
                binding.etNoteContent.setText(data.getNoteContent());
            });
        }
    }

    @Override
    public void onStop() {
        super.onStop();
    }

    @Override
    public void onClick(View view) {
        switch(view.getId()) {
            case R.id.fab_save_note:
                if(isNewNote) {
                    if(binding.etNoteTitle.getText() == null || binding.etNoteTitle.getText().toString().isEmpty()) {
                        String[] array = binding.etNoteContent.getText().toString().split(" ");
                        note.setNoteTitle(array[0]);
                    } else {
                        note.setNoteTitle(binding.etNoteTitle.getText().toString());
                    }
                    note.setNoteContent(binding.etNoteContent.getText().toString());
                    note.setColor(null);
                    note.setCreationDate(Calendar.getInstance().getTimeInMillis());
                    note.setModificationDate(Calendar.getInstance().getTimeInMillis());
                    note.setPasswordProtected(false);
                    note.setArchived(false);
                    if(!note.getNoteContent().equals(null) && note.getNoteContent().length() > 0) {
                        notesViewModel = ViewModelProviders.of(this).get(NotesViewModel.class);
                        notesViewModel.addNewNote(context, note).observe(this, data -> {
                            Log.i("Note Added", data.toString());
                            manager.popBackStack();
                        });
                    } else  {
                        manager.popBackStack();
                    }
                    notesViewModel = null;
                    break;
                } else {
                    note.setNoteTitle(binding.etNoteTitle.getText().toString());
                    note.setNoteContent(binding.etNoteContent.getText().toString());
                    note.setColor(null);
                    note.setModificationDate(Calendar.getInstance().getTimeInMillis());
                    note.setPasswordProtected(false);
                    note.setArchived(false);
                    notesViewModel = ViewModelProviders.of(this).get(NotesViewModel.class);
                    notesViewModel.updateNote(context, note).observe(this, data -> {
                        Log.i("Note Updated",data.toString());
                        manager.popBackStack();
                    });
                    notesViewModel = null;
                    break;
                }
            case R.id.ib_delete_button:
                if(!isNewNote) {
                    notesViewModel = ViewModelProviders.of(this).get(NotesViewModel.class);
                    notesViewModel.deleteNote(context, note).observe(this, data -> {
                        Log.i("Note Deleted",data.toString());
                        manager.popBackStack();
                    });
                } else {
                    manager.popBackStack();
                }
                notesViewModel = null;
                break;
            case R.id.ib_archive_button:
                if(!isNewNote) {
                    note.setNoteTitle(binding.etNoteTitle.getText().toString());
                    note.setNoteContent(binding.etNoteContent.getText().toString());
                    note.setColor(null);
                    note.setModificationDate(Calendar.getInstance().getTimeInMillis());
                    note.setPasswordProtected(false);
                    note.setArchived(true);
                    notesViewModel = ViewModelProviders.of(this).get(NotesViewModel.class);
                    notesViewModel.updateNote(context, note).observe(this, data -> {
                        Log.i("Note Updated",data.toString());
                        manager.popBackStack();
                    });
                    notesViewModel = null;
                } else {
                    note.setNoteTitle(binding.etNoteTitle.getText().toString());
                    note.setNoteContent(binding.etNoteContent.getText().toString());
                    note.setColor(null);
                    note.setModificationDate(Calendar.getInstance().getTimeInMillis());
                    note.setPasswordProtected(false);
                    note.setArchived(true);
                    notesViewModel = ViewModelProviders.of(this).get(NotesViewModel.class);
                    notesViewModel.addNewNote(context, note).observe(this, data -> {
                        Log.i("Note Updated",data.toString());
                        manager.popBackStack();
                    });
                    notesViewModel = null;
                }
                break;
        }
    }
}
