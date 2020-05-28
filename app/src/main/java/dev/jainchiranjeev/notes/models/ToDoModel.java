package dev.jainchiranjeev.notes.models;

public class ToDoModel {
    private String toDO;
    private boolean isDone = false;

    public ToDoModel(String toDO, boolean isDone) {
        this.toDO = toDO;
        this.isDone = isDone;
    }

    public String getToDO() {
        return toDO;
    }

    public void setToDO(String toDO) {
        this.toDO = toDO;
    }

    public boolean isDone() {
        return isDone;
    }

    public void setDone(boolean done) {
        isDone = done;
    }
}
