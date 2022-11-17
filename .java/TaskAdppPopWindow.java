package com.example.dotomorrow;

import android.content.SharedPreferences;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Spinner;
import android.widget.Toast;

import com.google.gson.Gson;

import java.util.ArrayList;

public class TaskAddPopWindow {
    private static final String TAG = "TaskAddPopWindow";

    /*
     * START of Keys
     * */
    public static final String TASKS_KEY = "tasks_key";
    public static final String KEY_ID_NUMBER = "ID_NUMBER";
    /*
     * END of Keys
     * */

    /*
     * START of SharedPreferences
     * */
    private SharedPreferences sharedpreferences, sharedpreferences_id_number;
    /*
     * END of SharedPreferences
     * */

    /*
     * START of RecyclerViewAdapters
     * */
    private RecyclerViewTaskAdapter adapter;
    /*
     * END of RecyclerViewAdapters
     * */

    /*
     * START of Arrays
     * */
    private ArrayList<Task> Tasks;
    /*
     * END of Arrays
     * */

    /*
     * START of EditTexts
     * */
    private EditText edittext_Task;
    /*
     * END of EditTexts
     * */

    /*
     * START of Buttons
     * */
    private Button button_TaskAdd;
    /*
     * END of buttons
     * */

    private int id_number;
    private Spinner spinner_TaskWhenToDo;


    /*
    * Getting all the objects / adapters / databases / variables
    * from MainActivity
    * */
    public TaskAddPopWindow(SharedPreferences sharedpreferences, SharedPreferences sharedpreferences_id_number, RecyclerViewTaskAdapter adapter, ArrayList<Task> tasks, EditText edittext_Task, Button button_TaskAdd, int id_number, Spinner spinner_TaskWhenToDo) {
        this.sharedpreferences = sharedpreferences;
        this.sharedpreferences_id_number = sharedpreferences_id_number;
        this.adapter = adapter;
        Tasks = tasks;
        this.edittext_Task = edittext_Task;
        this.button_TaskAdd = button_TaskAdd;
        this.id_number = id_number;
        this.spinner_TaskWhenToDo = spinner_TaskWhenToDo;
    }


    /*
    * Adding a Task to the RecyclerView
    * */
    public void TaskAdd (int id_number, String Text, boolean isDone) {
        Tasks.add(new Task(id_number, Text, isDone));
        adapter.setTasks(Tasks);
        adapter.notifyDataSetChanged();
        UpdateDataBase();
        Log.d(TAG, "TaskAdd: Task added succsesfully!");
    }

    /*
    * Updating database with the new task and id_number
    * */
    private void UpdateDataBase() {
        SharedPreferences.Editor editor = sharedpreferences.edit();
        Gson gson = new Gson();
        editor.remove(TASKS_KEY);
        editor.putString(TASKS_KEY, gson.toJson(Tasks));
        editor.apply();

        SharedPreferences.Editor editor_id_number = sharedpreferences_id_number.edit();
        editor_id_number.putInt(KEY_ID_NUMBER, id_number);
        editor_id_number.apply();
    }
}
