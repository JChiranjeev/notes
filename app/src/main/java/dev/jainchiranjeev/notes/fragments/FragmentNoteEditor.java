package dev.jainchiranjeev.notes.fragments;

import android.content.Context;
import android.content.Intent;
import android.content.res.ColorStateList;
import android.graphics.Color;
import android.os.Bundle;
import android.text.Editable;
import android.text.Html;
import android.text.TextUtils;
import android.text.TextWatcher;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.inputmethod.InputMethodManager;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.core.content.ContextCompat;
import androidx.core.text.HtmlCompat;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.lifecycle.ViewModelProviders;
import androidx.loader.app.LoaderManager;

import com.bumptech.glide.Glide;

import org.wordpress.aztec.Aztec;
import org.wordpress.aztec.ITextFormat;
import org.wordpress.aztec.toolbar.IAztecToolbarClickListener;

import java.util.Calendar;

import dev.jainchiranjeev.notes.R;
import dev.jainchiranjeev.notes.databinding.FragmentNoteEditorBinding;
import dev.jainchiranjeev.notes.models.NoteModel;
import dev.jainchiranjeev.notes.viewmodels.NotesViewModel;
import uk.co.deanwild.materialshowcaseview.IShowcaseListener;
import uk.co.deanwild.materialshowcaseview.MaterialShowcaseView;

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
    Boolean isSharedNote  = false;
    String sharedContent = "";
    Boolean isArchived = false;

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        binding = FragmentNoteEditorBinding.inflate(getLayoutInflater());
        view = binding.getRoot();
        context = getContext();
        manager = getFragmentManager();

//        Set Icons
        Glide.with(view).load(R.drawable.ic_done).fitCenter().into(binding.fabSaveNote);
        Glide.with(view).load(R.drawable.ic_formatting).into(binding.ibRteditor);
        Glide.with(view).load(R.drawable.ic_share).into(binding.ibShareNote);

        bundle = this.getArguments();
        if(bundle != null) {
            noteId = bundle.getInt("NoteID",-1);
            isNewNote = bundle.getBoolean("IsNewNote");
            isSharedNote = bundle.getBoolean("IsSharedNote");
            if(isSharedNote) {
                sharedContent = bundle.getString("SharedContent");
            }
            setupAztec();
            loadNote(noteId);
        } else {
            manager.popBackStack();
        }

        new MaterialShowcaseView.Builder(getActivity())
                .setTarget(binding.ibRteditor)
                .setDismissText("Got It!")
                .setTitleText("More Editing")
                .setContentText("Click this button to reveal more editing options.")
                .setDelay(500)
                .singleUse("FormattingButton")
                .show().addShowcaseListener(new IShowcaseListener() {
            @Override
            public void onShowcaseDisplayed(MaterialShowcaseView showcaseView) {

            }

            @Override
            public void onShowcaseDismissed(MaterialShowcaseView showcaseView) {
                new MaterialShowcaseView.Builder(getActivity())
                        .setTarget(binding.ibShareNote)
                        .setDismissText("Got It!")
                        .setTitleText("Share")
                        .setContentText("Share this note to other apps")
                        .setDelay(500)
                        .singleUse("SharingButton")
                        .show();
            }
        });

        binding.atNoteContent.addTextChangedListener(new TextWatcher() {
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
        binding.ibRteditor.setOnClickListener(this);
        binding.ibShareNote.setOnClickListener(this);

        return view;
    }

    private void setupAztec() {
        binding.atToolbar.setEditor(binding.atNoteContent, null);
        binding.atNoteContent.setBackground(ContextCompat.getDrawable(context, R.drawable.bg_edittext));
        binding.atToolbar.enableMediaMode(false);
    }

    private void hideOrDisplayActions(Boolean isNewNote, Boolean isContentAvailable) {
        if(!isNewNote && isContentAvailable) {
            binding.ibDeleteButton.setVisibility(View.VISIBLE);
            binding.ibArchiveButton.setVisibility(View.VISIBLE);
            Glide.with(view).load(R.drawable.ic_delete).fitCenter().into(binding.ibDeleteButton);
            if(isArchived) {
                Glide.with(view).load(R.drawable.ic_unarchive).fitCenter().into(binding.ibArchiveButton);
            } else {
                Glide.with(view).load(R.drawable.ic_archive).fitCenter().into(binding.ibArchiveButton);
            }
        } else if (isNewNote && isContentAvailable) {
            binding.ibDeleteButton.setVisibility(View.VISIBLE);
            binding.ibArchiveButton.setVisibility(View.VISIBLE);
            Glide.with(view).load(R.drawable.ic_delete).fitCenter().into(binding.ibDeleteButton);
            if(isArchived) {
                Glide.with(view).load(R.drawable.ic_unarchive).fitCenter().into(binding.ibArchiveButton);
            } else {
                Glide.with(view).load(R.drawable.ic_archive).fitCenter().into(binding.ibArchiveButton);
            }
        } else {
            binding.ibDeleteButton.setVisibility(View.GONE);
            binding.ibArchiveButton.setVisibility(View.GONE);
        }
    }

    private void loadNote(int noteId) {
        if (noteId < 0 && isNewNote) {
            binding.atNoteContent.setTransitionName("transition_note_content-1");
            isContentAvailable = false;
            note = new NoteModel();
            if(isSharedNote && !sharedContent.isEmpty() && sharedContent.length() > 0) {
                binding.atNoteContent.setText(sharedContent);
                isContentAvailable = true;
            }
        } else {
            binding.etNoteTitle.setTransitionName("transition_note_title"+noteId);
            binding.atNoteContent.setTransitionName("transition_note_content"+noteId);
            isContentAvailable = true;
            notesViewModel = ViewModelProviders.of(this).get(NotesViewModel.class);
            notesViewModel.getNoteById(context, noteId).observe(this, data -> {
                note = data;
                isArchived = note.isArchived();
                binding.etNoteTitle.setText(data.getNoteTitle());
                binding.atNoteContent.fromHtml(data.getNoteContent(), true);
                hideOrDisplayActions(isNewNote, isContentAvailable);
            });
        }
    }

    @Override
    public void onClick(View view) {
        switch(view.getId()) {
            case R.id.fab_save_note:
                if(isNewNote) {
                    if(binding.etNoteTitle.getText() == null || binding.etNoteTitle.getText().toString().isEmpty()) {
                        String[] array = binding.atNoteContent.getText().toString().split(" ");
                        note.setNoteTitle(array[0]);
                    } else {
                        note.setNoteTitle(binding.etNoteTitle.getText().toString());
                    }
                    note.setNoteContent(binding.atNoteContent.toHtml(false));
                    note.setColor(null);
                    note.setCreationDate(Calendar.getInstance().getTimeInMillis());
                    note.setModificationDate(Calendar.getInstance().getTimeInMillis());
                    note.setPasswordProtected(false);
                    note.setArchived(isArchived);
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
                    note.setNoteContent(binding.atNoteContent.toHtml(true));
                    Log.i("Content", binding.atNoteContent.toHtml(true));
                    note.setColor(null);
                    note.setModificationDate(Calendar.getInstance().getTimeInMillis());
                    note.setPasswordProtected(false);
                    note.setArchived(note.isArchived());
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
                    note.setNoteContent(binding.atNoteContent.toHtml(true));
                    note.setColor(null);
                    note.setModificationDate(Calendar.getInstance().getTimeInMillis());
                    note.setPasswordProtected(false);
                    note.setArchived(!note.isArchived());
                    notesViewModel = ViewModelProviders.of(this).get(NotesViewModel.class);
                    notesViewModel.updateNote(context, note).observe(this, data -> {
                        Log.i("Note Updated",data.toString());
                        manager.popBackStack();
                    });
                    notesViewModel = null;
                } else {
                    note.setNoteTitle(binding.etNoteTitle.getText().toString());
                    note.setNoteContent(binding.atNoteContent.toHtml(true));
                    note.setColor(null);
                    note.setModificationDate(Calendar.getInstance().getTimeInMillis());
                    note.setPasswordProtected(false);
                    note.setArchived(!isArchived);
                    notesViewModel = ViewModelProviders.of(this).get(NotesViewModel.class);
                    notesViewModel.addNewNote(context, note).observe(this, data -> {
                        Log.i("Note Updated",data.toString());
                        manager.popBackStack();
                    });
                    notesViewModel = null;
                }
                break;
            case R.id.ib_rteditor:
                if(binding.atToolbar.getVisibility() == View.GONE) {
                    binding.ibRteditor.setColorFilter(ContextCompat.getColor(context, R.color.contrastPrimary));
                    binding.atToolbar.setAlpha(0.0f);
                    binding.atToolbar.setVisibility(View.VISIBLE);
                    // Start the animation
                    binding.atToolbar.animate()
                            .setDuration(500)
                            .alpha(1.0f)
                            .setListener(null);
                } else {
                    binding.ibRteditor.setColorFilter(ContextCompat.getColor(context, R.color.contrastTertiary));
                    binding.atToolbar.setAlpha(1.0f);
                    binding.atToolbar.setVisibility(View.GONE);
                    // Start the animation
                    binding.atToolbar.animate()
                            .setDuration(500)
                            .alpha(0.0f)
                            .setListener(null);
                }
                break;
            case R.id.ib_share_note:
                String title = binding.etNoteTitle.getText().toString();
                String content = HtmlCompat.fromHtml(binding.atNoteContent.toHtml(true), HtmlCompat.FROM_HTML_MODE_COMPACT).toString();
                if(content.isEmpty() || content.trim().length() == 0) {
                    Toast.makeText(context, "Cannot share empty note", Toast.LENGTH_SHORT).show();
                } else {
                    String shareBody = title + "\n\n" + content;
                    Intent shareIntent = new Intent(Intent.ACTION_SEND);
                    Log.i("Share Body", shareBody);
                    shareIntent.setType("text/plain");
                    shareIntent.putExtra(Intent.EXTRA_SUBJECT, title);
                    shareIntent.putExtra(Intent.EXTRA_TEXT, content);
                    startActivity(Intent.createChooser(shareIntent,"Share note using..."));
                }
                break;
        }
    }

    @Override
    public void onResume() {
        super.onResume();
//        binding.atNoteContent.requestFocus();
//        InputMethodManager imm = (InputMethodManager) context.getSystemService(Context.INPUT_METHOD_SERVICE);
//        imm.toggleSoftInput(InputMethodManager.SHOW_IMPLICIT, 0);
    }
}
