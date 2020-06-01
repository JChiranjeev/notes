package dev.jainchiranjeev.notes.models;

public class TodoModel {
    private String todo;
    private boolean isDone = false;

    public TodoModel() {
    }

    public TodoModel(String todo, boolean isDone) {
        this.todo = todo;
        this.isDone = isDone;
    }

    public String getTodo() {
        return todo;
    }

    public void setTodo(String todo) {
        this.todo = todo;
    }

    public boolean isDone() {
        return isDone;
    }

    public void setDone(boolean done) {
        isDone = done;
    }
}
