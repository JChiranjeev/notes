package dev.jainchiranjeev.notes.presenter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.appcompat.widget.AppCompatEditText;
import androidx.appcompat.widget.AppCompatImageButton;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;

import java.util.List;

import dev.jainchiranjeev.notes.R;
import dev.jainchiranjeev.notes.models.ToDoModel;

public class ToDoAdapter extends RecyclerView.Adapter<ToDoAdapter.ViewHolder> {

    private List<ToDoModel> todoList;
    private ToDoModel todoItem;
    private Context context;

    class ViewHolder extends RecyclerView.ViewHolder {
        AppCompatImageButton ibCheckbox;
        AppCompatEditText etTodoItem;
        public ViewHolder(@NonNull View itemView) {
            super(itemView);
            ibCheckbox = itemView.findViewById(R.id.ib_checkbox);
            etTodoItem = itemView.findViewById(R.id.et_todo_item);
        }
    }

    public ToDoAdapter(List<ToDoModel> todoList, Context context) {
        this.todoList = todoList;
        this.context = context;
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        Context context = parent.getContext();
        LayoutInflater inflater = LayoutInflater.from(context);
        View todoItem = inflater.inflate(R.layout.rv_item_todo, parent, false);

        ViewHolder viewHolder = new ViewHolder(todoItem);
        return viewHolder;
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int position) {
        todoItem = todoList.get(position);
        if(todoItem.isDone()) {
            Glide.with(holder.itemView).load(R.drawable.ic_check_box_checked).into(holder.ibCheckbox);
        } else {
            Glide.with(holder.itemView).load(R.drawable.ic_check_box_unchecked).into(holder.ibCheckbox);
        }
        holder.etTodoItem.setText(todoItem.getTodo());
    }

    @Override
    public int getItemCount() {
        return todoList.size();
    }
}
