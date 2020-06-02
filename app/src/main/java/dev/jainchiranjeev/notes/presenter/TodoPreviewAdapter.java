package dev.jainchiranjeev.notes.presenter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.widget.AppCompatImageView;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;
import com.google.android.material.textview.MaterialTextView;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.List;

import dev.jainchiranjeev.notes.R;
import dev.jainchiranjeev.notes.models.TodoModel;

public class TodoPreviewAdapter extends RecyclerView.Adapter<TodoPreviewAdapter.ViewHolder> {

    List<TodoModel> todoList;
    Context context;
    TodoModel todo;

    static class ViewHolder extends RecyclerView.ViewHolder {

        private AppCompatImageView ivTodoPreview;
        private MaterialTextView tvTodoPreview;
        private Context context;

        public ViewHolder(@NonNull View itemView, Context context) {
            super(itemView);
            this.context = context;
            ivTodoPreview = itemView.findViewById(R.id.iv_todo_preview);
            tvTodoPreview = itemView.findViewById(R.id.tv_todo_preview);

            itemView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    Toast.makeText(context, "Clicked", Toast.LENGTH_SHORT).show();
                }
            });
        }
    }

    public TodoPreviewAdapter(List<TodoModel> todoList, Context context) {
        this.todoList = new ArrayList<>(todoList);
        Collections.sort(this.todoList, new Comparator<TodoModel>() {
            @Override
            public int compare(TodoModel t0, TodoModel t1) {
                return t0.isDone().compareTo(t1.isDone());
            }
        });
        this.context = context;
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        Context context = parent.getContext();
        LayoutInflater inflater = LayoutInflater.from(context);
        View view = inflater.inflate(R.layout.rv_item_todo_preview, parent, false);

        ViewHolder viewHolder = new ViewHolder(view, context);
        return viewHolder;
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int position) {
        todo = todoList.get(position);
        if(todo.isDone()) {
            Glide.with(holder.itemView).load(R.drawable.ic_check_box_checked).fitCenter().into(holder.ivTodoPreview);
        } else {
            Glide.with(holder.itemView).load(R.drawable.ic_check_box_unchecked).fitCenter().into(holder.ivTodoPreview);
        }
        holder.tvTodoPreview.setText(todo.getTodo());
    }

    @Override
    public int getItemCount() {
        return (todoList.size() > 5) ? 5 : todoList.size();
    }
}
