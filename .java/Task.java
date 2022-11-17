package com.example.dotomorrow;

import android.opengl.Visibility;

import java.util.Comparator;

public class Task {

    private int id;
    private String text;
    private boolean isdone;

    public Task(int id, String text, boolean isdone) {
        this.id = id;
        this.text = text;
        this.isdone = isdone;
    }

    //TODO Another attribute for Task for DEADLINE (including a null deadline)
    //TODO Another attribute for Task for PRIORITY (default on null(normal))

    @Override
    public String toString() {
        return "Task{" +
                "id=" + id +
                ", text='" + text + '\'' +
                ", isdone=" + isdone +
                '}';
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getText() {
        return text;
    }

    public void setText(String text) {
        this.text = text;
    }

    public boolean isIsdone() {
        return isdone;
    }

    public void setIsdone(boolean isdone) {
        this.isdone = isdone;
    }


    public static Comparator<Task> byName = new Comparator<Task>() {
        @Override
        public int compare(Task t1, Task t2) {
            return t1.getText().compareToIgnoreCase(t2.getText());
        }
    };

    public static Comparator<Task> byName_Reverse = new Comparator<Task>() {
        @Override
        public int compare(Task t1, Task t2) {
            return t2.getText().compareToIgnoreCase(t1.getText());
        }
    };








    

//    ================================================
    public static Comparator<Task> byId = new Comparator<Task>() {
        @Override
        public int compare(Task t1, Task t2) {
            return t1.getId() - t2.getId();
        }
    };

    public static Comparator<Task> byId_Reverse = new Comparator<Task>() {
        @Override
        public int compare(Task t1, Task t2) {
            return t2.getId() - t1.getId();
        }
    };
    
}
