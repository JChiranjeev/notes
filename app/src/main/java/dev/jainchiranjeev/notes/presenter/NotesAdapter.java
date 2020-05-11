package dev.jainchiranjeev.notes.presenter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentTransaction;
import androidx.recyclerview.widget.RecyclerView;

import com.google.android.material.textview.MaterialTextView;

import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;

import dev.jainchiranjeev.notes.R;
import dev.jainchiranjeev.notes.databinding.RvItemNotePreviewBinding;
import dev.jainchiranjeev.notes.models.NoteModel;

public class NotesAdapter extends RecyclerView.Adapter<NotesAdapter.ViewHolder> {

    public List<NoteModel> notesList = null;
    FragmentManager manager;
    FragmentTransaction transaction;
    Context context;
    NoteModel selectedNote = null;

    class ViewHolder extends RecyclerView.ViewHolder {

        MaterialTextView tvNoteTitle, tvNoteContent, tvModifiedDate;

        public ViewHolder(@NonNull View itemView) {
            super(itemView);
            tvNoteTitle = (MaterialTextView) itemView.findViewById(R.id.tv_note_preview_title);
            tvNoteContent = (MaterialTextView) itemView.findViewById(R.id.tv_note_preview_content);
            tvModifiedDate = (MaterialTextView) itemView.findViewById(R.id.tv_note_preview_modified);
        }
    }

    public NotesAdapter(Context context, List<NoteModel> notesList) {
        this.context = context;
        this.notesList = notesList;
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
        MaterialTextView tvNoteTitle, tvNoteContent, tvModifiedDate;
        tvNoteTitle = holder.tvNoteTitle;
        tvNoteContent = holder.tvNoteContent;
        tvModifiedDate = holder.tvModifiedDate;
        tvNoteTitle.setText(note.noteTitle);
        tvNoteContent.setText(note.noteContent);
        Date date = new Date(note.modificationDate);
        SimpleDateFormat formatter = new SimpleDateFormat("dd MMM yyyy HH:mm");
        tvModifiedDate.setText(formatter.format(date));
    }

    @Override
    public int getItemCount() {
        return notesList.size();
    }

}
