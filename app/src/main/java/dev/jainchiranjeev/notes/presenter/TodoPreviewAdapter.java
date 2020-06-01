package dev.jainchiranjeev.notes.presenter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.CheckBox;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;
import com.google.android.material.textview.MaterialTextView;

import java.util.List;

import dev.jainchiranjeev.notes.R;
import dev.jainchiranjeev.notes.models.TodoModel;

public class TodoPreviewAdapter extends RecyclerView.Adapter<TodoPreviewAdapter.ViewHolder> {

    List<TodoModel> todoList;
    Context context;
    TodoModel todo;

    static class ViewHolder extends RecyclerView.ViewHolder {

        private CheckBox cbTodoPreview;
        private MaterialTextView tvTodoPreview;

        public ViewHolder(@NonNull View itemView) {
            super(itemView);
            cbTodoPreview = itemView.findViewById(R.id.cb_todo_preview);
            tvTodoPreview = itemView.findViewById(R.id.tv_todo_preview);
        }
    }

    public TodoPreviewAdapter(List<TodoModel> todoList, Context context) {
        this.todoList = todoList;
        this.context = context;
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        Context context = parent.getContext();
        LayoutInflater inflater = LayoutInflater.from(context);
        View view = inflater.inflate(R.layout.rv_item_todo_preview, parent, false);

        ViewHolder viewHolder = new ViewHolder(view);
        return viewHolder;
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int position) {
        todo = todoList.get(position);
        holder.cbTodoPreview.setChecked(todo.isDone());
        holder.tvTodoPreview.setText(todo.getTodo());
    }

    @Override
    public int getItemCount() {
        return (todoList.size() > 5) ? 5 : todoList.size();
    }
}
