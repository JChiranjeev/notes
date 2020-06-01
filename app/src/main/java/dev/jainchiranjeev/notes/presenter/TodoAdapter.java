package dev.jainchiranjeev.notes.presenter;

import android.content.Context;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.KeyEvent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.appcompat.widget.AppCompatCheckBox;
import androidx.appcompat.widget.AppCompatEditText;
import androidx.recyclerview.widget.RecyclerView;

import java.util.ArrayList;
import java.util.List;

import dev.jainchiranjeev.notes.R;
import dev.jainchiranjeev.notes.models.TodoModel;
import dev.jainchiranjeev.notes.services.TodoListener;

public class TodoAdapter extends RecyclerView.Adapter<TodoAdapter.ViewHolder> {

    private List<TodoModel> todoList;
    private TodoModel todoItem;
    private Context context;
    private RecyclerView recyclerView;
    TodoListener todoListener;

    class ViewHolder extends RecyclerView.ViewHolder {
        AppCompatCheckBox cbTodo;
        AppCompatEditText etTodoItem;

        public ViewHolder(@NonNull View itemView) {
            super(itemView);
            cbTodo = itemView.findViewById(R.id.cb_todo);
            etTodoItem = itemView.findViewById(R.id.et_todo_item);

            etTodoItem.setOnKeyListener(new View.OnKeyListener() {
                @Override
                public boolean onKey(View view, int i, KeyEvent keyEvent) {
                    if (keyEvent.getAction() == KeyEvent.ACTION_DOWN && i == KeyEvent.KEYCODE_ENTER) {
                        if (etTodoItem.getText() != null && !etTodoItem.getText().toString().isEmpty()) {
                            addToList(getAdapterPosition(), new TodoModel("", false));
                        }
                        return true;
                    }
                    return false;
                }
            });
            etTodoItem.addTextChangedListener(new TextWatcher() {
                @Override
                public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2) {

                }

                @Override
                public void onTextChanged(CharSequence charSequence, int i, int i1, int i2) {
                    todoList.get(getAdapterPosition()).setTodo(etTodoItem.getText().toString());
                }

                @Override
                public void afterTextChanged(Editable editable) {

                }
            });
        }
    }

    public TodoAdapter(List<TodoModel> todoList, Context context, RecyclerView recyclerView, TodoListener todoListener) {
        this.todoList = new ArrayList<>(todoList);
        this.context = context;
        this.recyclerView = recyclerView;
        this.todoListener = todoListener;
    }

    private void addToList(int position, TodoModel todo) {
        this.todoList.add(position + 1, todo);
        this.todoListener.todoListener(todoList.size() > 0);
        notifyItemInserted(position + 1);
    }

    public List<TodoModel> getTodoList() {
        return this.todoList;
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
        holder.etTodoItem.setText(todoItem.getTodo());
//        if (position == todoList.size()) {
//            holder.etTodoItem.setFocusableInTouchMode(true);
//            recyclerView.getLayoutManager().scrollToPosition(position);
//            holder.etTodoItem.post(new Runnable() {
//                @Override
//                public void run() {
//                    holder.etTodoItem.requestFocus();
//                }
//            });
//        }
    }

    @Override
    public int getItemCount() {
        return todoList.size();
    }
}
