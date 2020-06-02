package dev.jainchiranjeev.notes.presenter;

import android.content.Context;
import android.os.Bundle;
import android.transition.TransitionInflater;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.core.content.ContextCompat;
import androidx.fragment.app.FragmentActivity;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentTransaction;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.google.android.material.textview.MaterialTextView;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import dev.jainchiranjeev.notes.R;
import dev.jainchiranjeev.notes.fragments.FragmentNoteEditor;
import dev.jainchiranjeev.notes.models.NoteModel;
import dev.jainchiranjeev.notes.services.SelectionEnabledListener;

public class NotesAdapter extends RecyclerView.Adapter<NotesAdapter.ViewHolder> {

    public List<NoteModel> notesList = null;
    FragmentManager manager;
    FragmentTransaction transaction;
    Context context;
    NoteModel selectedNote = null;
    Boolean isSelectionEnabled = false;
    int selectionCount = 0;
    Boolean archives = false;
    SelectionEnabledListener selectionEnabledListener;

    class ViewHolder extends RecyclerView.ViewHolder {

        private MaterialTextView tvNoteTitle, tvNoteContent, tvModifiedDate;
        private RecyclerView rvTodoPreview;
        private View viewRvCover;

        public ViewHolder(@NonNull View itemView) {
            super(itemView);
            tvNoteTitle = itemView.findViewById(R.id.tv_note_preview_title);
            tvNoteContent = itemView.findViewById(R.id.tv_note_preview_content);
            tvModifiedDate = itemView.findViewById(R.id.tv_note_preview_modified);
            rvTodoPreview = itemView.findViewById(R.id.rv_todo_preview_content);
            viewRvCover = itemView.findViewById(R.id.view_rv_cover);
            manager = ((FragmentActivity) context).getSupportFragmentManager();
        }

        void bind(final NoteModel note) {
            tvNoteTitle.setTransitionName("transition_note_title"+note.getNoteId());
            tvNoteTitle.setText(note.getNoteTitle());
            if(note.isTodoList()) {
                rvTodoPreview.setVisibility(View.VISIBLE);
                tvNoteContent.setVisibility(View.GONE);
                TodoPreviewAdapter adapter = new TodoPreviewAdapter(note.getTodoList(), context);
                rvTodoPreview.setAdapter(adapter);
                rvTodoPreview.setLayoutManager(new LinearLayoutManager(context));
                viewRvCover.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {
                        itemView.performClick();
                    }
                });
            } else {
                rvTodoPreview.setVisibility(View.GONE);
                tvNoteContent.setVisibility(View.VISIBLE);
                tvNoteContent.setTransitionName("transition_note_content"+note.getNoteId());
                tvNoteContent.setText(note.getNoteContent());
            }

            Date date = new Date(note.getModificationDate());
            SimpleDateFormat formatter = new SimpleDateFormat("dd MMM yyyy HH:mm");
            tvModifiedDate.setText(formatter.format(date));
            itemView.setBackground(note.isChecked() ?
                    ContextCompat.getDrawable(context, R.drawable.bg_note_preview_selected) :
                    ContextCompat.getDrawable(context, R.drawable.bg_note_preview));

            itemView.setOnLongClickListener(new View.OnLongClickListener() {
                @Override
                public boolean onLongClick(View view) {
                    isSelectionEnabled = !isSelectionEnabled;
                    selectionCount++;
                    note.setChecked(!note.isChecked());
                    itemView.setBackground(note.isChecked() ?
                            ContextCompat.getDrawable(context, R.drawable.bg_note_preview_selected) :
                            ContextCompat.getDrawable(context, R.drawable.bg_note_preview));
                    selectionEnabledListener.onSelectionEnabled(isSelectionEnabled);
                    return isSelectionEnabled;
                }
            });

            itemView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    if (isSelectionEnabled) {
                        note.setChecked(!note.isChecked());
                        itemView.setBackground(note.isChecked() ?
                                ContextCompat.getDrawable(context, R.drawable.bg_note_preview_selected) :
                                ContextCompat.getDrawable(context, R.drawable.bg_note_preview));
                        selectionCount = note.isChecked() ? selectionCount+1 : selectionCount-1;
                        if(selectionCount == 0) {
                            isSelectionEnabled = !isSelectionEnabled;
                            selectionEnabledListener.onSelectionEnabled(isSelectionEnabled);
                        }
                    } else {
                        transaction = manager.beginTransaction();
                        FragmentNoteEditor noteEditor = new FragmentNoteEditor();
                        Bundle bundle = new Bundle();
                        bundle.putInt("NoteID",note.getNoteId());
                        bundle.putBoolean("IsNewNote",false);
                        noteEditor.setArguments(bundle);
                        noteEditor.setSharedElementEnterTransition(TransitionInflater.from(context).inflateTransition(R.transition.transition_basic));
                        noteEditor.setSharedElementReturnTransition(TransitionInflater.from(context).inflateTransition(R.transition.transition_basic));
                        transaction.addSharedElement(tvNoteTitle, "transition_note_title"+note.getNoteId());
                        if (!note.isTodoList()) {
                            transaction.addSharedElement(tvNoteContent, "transition_note_content" + note.getNoteId());
                        }
                        transaction.replace(R.id.crfl_main_activity, noteEditor);
                        transaction.addToBackStack(null);
                        transaction.commit();
                    }
                }
            });
        }
    }

    public List<NoteModel> getAllNotes() {
        return notesList;
    }

    public List<NoteModel> getSelected() {
        List<NoteModel> selectedNotes = new ArrayList<>();
        for (NoteModel note : notesList) {
            if (note.isChecked()) {
                selectedNotes.add(note);
            }
        }
        return selectedNotes;
    }

    public NotesAdapter(Context context, List<NoteModel> notesList, Boolean archives, SelectionEnabledListener listener) {
        this.selectionEnabledListener = listener;
        listener.onSelectionEnabled(false);
        this.context = context;
        this.notesList = new ArrayList<>();
        this.archives = archives;
        for (NoteModel note : notesList) {
            if(note.isArchived() == archives) {
                this.notesList.add(note);
            }
        }
    }

    public void setNotesList(List<NoteModel> notesList) {
        this.notesList = new ArrayList<>();
        for (NoteModel note : notesList) {
            if(note.isArchived() == archives) {
                this.notesList.add(note);
            }
        }
        notifyDataSetChanged();
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        Context context = parent.getContext();
        LayoutInflater inflater = LayoutInflater.from(context);
        View notePreview = inflater.inflate(R.layout.rv_item_note_preview, parent, false);

        ViewHolder viewHolder = new ViewHolder(notePreview);
        return viewHolder;
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int position) {
        NoteModel note = notesList.get(position);
        holder.bind(note);
    }

    @Override
    public int getItemCount() {
        return notesList.size();
    }

}
