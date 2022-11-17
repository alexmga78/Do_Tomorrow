package com.example.dotomorrow;

import static com.example.dotomorrow.R.layout.activity_task_add_pop_window;

import android.annotation.SuppressLint;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.res.Configuration;
import android.graphics.Color;
import android.graphics.Rect;
import android.graphics.drawable.ColorDrawable;
import android.os.Build;
import android.os.Bundle;
import android.util.DisplayMetrics;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewTreeObserver;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.EditText;
import android.widget.PopupWindow;
import android.widget.Spinner;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.widget.NestedScrollView;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;

import java.lang.reflect.Type;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Timer;
import java.util.TimerTask;

public class MainActivity extends AppCompatActivity {
    private static final String TAG = "MainActivity";

    /*
    * START of Keys:
     * */

    /*
    * END of Keys.
    * */

    /*
    * START of SharedPreferences
    * */
    private SharedPreferences sharedpreferences, sharedpreferences_TasksDone, sharedpreferences_id_number;
    /*
    * END of SharedPreferences
    * */

    /*
    * START of ScrollViews
    * */
    private NestedScrollView scrollview_Task;
    /*
    * END of ScrollViews
    * */

    /*
    * START of RecyclerViews + RecyclerViewwAdapters
    * */
    private RecyclerView recyclerview_Task_Done;
    private RecyclerViewTaskAdapter adapter;
    private RecyclerViewTaskDoneAdapter adapter_Task_Done;
    /*
    * END of RecyclerViews + RecyclerViewwAdapters
    * */

    /*
    * START of Arrays
    * */
    private static ArrayList<Task> Tasks = null, Tasks_Done = null;
    /*
    * END of Arrays
    * */

    private FloatingActionButton floatingbutton_TaskAdd;
    private int id_number;





    /*
    * START of PopupWindow items
    * */
    View mPopView;
    PopupWindow popupwindow_TaskAdd;
    /*
    * END of PopupWindow items
    * */

    /*
    * START of Menu items
    * */
    MenuItem threedotsTasksDone_Show;
    MenuItem threedotsTasksDone_Hide;
    MenuItem threedots_Sort_byName;
    MenuItem threedots_Sort_byName_Reverse;


    MenuItem lastItem;
    MenuItem lastItem_Reverse;
    MenuItem threedots_Sort_byId;
    MenuItem threedots_Sort_byId_Reverse;
    /*
    * End of Menu items
    * */

    private String Sorting_Constant;





    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        Initialing();
        Refresh_Timer();

        //TODO Categories when swiping
        //TODO Editable Titles (Action Bar)




        /*
        * Click listener for the floating add button
        * */
        floatingbutton_TaskAdd.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                PopWindow_Initialing();
                Button TaskAdd_button_TaskAdd = mPopView.findViewById(R.id.button_TaskAdd);
                EditText TaskAdd_edittext_Task = mPopView.findViewById(R.id.edittext_Task);
                Spinner TaskAdd_spinner_TaskWhenToDo = mPopView.findViewById(R.id.spinner_TaskWhenToDo);

                final View root = findViewById(R.id.relativelayout_main).getRootView();
                root.getViewTreeObserver().addOnGlobalLayoutListener(new ViewTreeObserver.OnGlobalLayoutListener() {
                    @Override
                    public void onGlobalLayout() {
//                        ViewTreeObserver.OnGlobalLayoutListener g = this;

                        popupwindow_TaskAdd.update(0, getNeededHeight(), -1, -1);
                        //TODO Set an animation when the popup moves (updates location)


                        if (!popupwindow_TaskAdd.isShowing()){
                            root.getViewTreeObserver().removeOnGlobalLayoutListener(this);
                        }

                        TaskAdd_button_TaskAdd.setOnClickListener(v1 -> {
                            TaskAddPopWindow TaskAddPopWindow = new TaskAddPopWindow(sharedpreferences, sharedpreferences_id_number, adapter, Tasks, TaskAdd_edittext_Task, TaskAdd_button_TaskAdd, id_number, TaskAdd_spinner_TaskWhenToDo);
                            id_number++;
                            TaskAddPopWindow.TaskAdd(id_number, TaskAdd_edittext_Task.getText().toString(), false);
                            popupwindow_TaskAdd.dismiss();
                            UpdateLastMenuItem_Checked();       //Resets three_dot_menu Sorting options (ex: from Name, Id (Reverse) to Name, Id) and vice-versa
                        });
                    }
                });

            }
        });

        Toast.makeText(this, "At Second Point", Toast.LENGTH_SHORT).show();
        System.out.println("At Second Point");

    }

    private int getNeededHeight() {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.JELLY_BEAN_MR1){
            final View root = findViewById(R.id.relativelayout_main).getRootView();
            Rect rect = new Rect();
            root.getWindowVisibleDisplayFrame(rect);
            DisplayMetrics metrics = new DisplayMetrics();
            getWindowManager().getDefaultDisplay().getMetrics(metrics);
            int usableHeight = metrics.heightPixels;
            getWindowManager().getDefaultDisplay().getRealMetrics(metrics);
            int realHeight = metrics.heightPixels;
            if (realHeight > usableHeight){
                return (root.getHeight() - rect.height()) - ((realHeight - usableHeight) / 2);
            }else
                return  0;
        }
        return 0;
    }

    private int getNeededWidth() {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.JELLY_BEAN_MR1){
            final View root = findViewById(R.id.relativelayout_main).getRootView();
            Rect rect = new Rect();
            root.getWindowVisibleDisplayFrame(rect);
            DisplayMetrics metrics = new DisplayMetrics();
            getWindowManager().getDefaultDisplay().getMetrics(metrics);
            int usableWidth = metrics.widthPixels;
            getWindowManager().getDefaultDisplay().getRealMetrics(metrics);
            int realWidth = metrics.widthPixels;
            if (realWidth > usableWidth){
                return usableWidth;
            }else
                return 0;
        }
        return 0;
    }

    /*
    * Initialing PopupWindow and it's objects / variables
    * */
    private void PopWindow_Initialing() {
        //TODO a more stylish PopupWindow (and Priority/Deadline/other attributes available)
        DisplayMetrics displaymetrics = new DisplayMetrics();
        getWindowManager().getDefaultDisplay().getRealMetrics(displaymetrics);
        mPopView = LayoutInflater.from(MainActivity.this).inflate(activity_task_add_pop_window, null);
        popupwindow_TaskAdd = new PopupWindow(
                mPopView ,WindowManager.LayoutParams.MATCH_PARENT, WindowManager.LayoutParams.WRAP_CONTENT);
        popupwindow_TaskAdd.setFocusable(true);
        popupwindow_TaskAdd.setInputMethodMode(PopupWindow.INPUT_METHOD_NEEDED);
        popupwindow_TaskAdd.setSoftInputMode(WindowManager.LayoutParams.SOFT_INPUT_ADJUST_RESIZE);
        popupwindow_TaskAdd.setClippingEnabled(false);
        popupwindow_TaskAdd.setBackgroundDrawable(new ColorDrawable(Color.WHITE));    /*Only the corners whick remained untouched by XML files*/
        //TODO Hide bottom stroke (perimeter line)  AND (MAYBE) lateral strokes (Might be possible by increasing lateral size, but might also be unreliable)
        if (getResources().getConfiguration().orientation == Configuration.ORIENTATION_LANDSCAPE){
            popupwindow_TaskAdd.setWidth(getNeededWidth());
        }
        popupwindow_TaskAdd.setAnimationStyle(R.style.animation_PopupWindow);
        popupwindow_TaskAdd.showAtLocation(mPopView, Gravity.BOTTOM | Gravity.START, 0, getNeededHeight());      // if y == 0  the PopupWindow goes into phone buttons (but 0 should be ok, and anything over it might cause problems)
        //TODO Error:
        /*
        *           #The error happens vice-versa
        *           #The app doesn't crash, ONLY the PopupWindow
        *           E/WindowManager: android.view.WindowLeaked: Activity com.example.dotomorrow.MainActivity has leaked window android.widget.PopupWindow$PopupDecorView{9f9b626 V.E...... R....... 0,0-2701,939 aid=1073741840} that was originally added here
        *
        *           1) Open app
        *           2) Change on Landscape
        *           3) Press FloatingButton
        *           4) Change on Portrait
        * */
    }



    /*
    * Showing the 3 dots menu in top-right corner
    * */
    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        MenuInflater inflater = getMenuInflater();
        inflater.inflate(R.menu.menu_dots, menu);

        threedotsTasksDone_Show = menu.findItem(R.id.threedots_TasksDone_Show);
        threedotsTasksDone_Hide = menu.findItem(R.id.threedots_TasksDone_Hide);
        threedots_Sort_byName = menu.findItem(R.id.threedots_Sort_byName);
        threedots_Sort_byName_Reverse = menu.findItem(R.id.threedots_Sort_byName_Reverse);








        lastItem = menu.findItem(R.id.threedots_Sort_byName);
        lastItem_Reverse = menu.findItem(R.id.threedots_Sort_byName_Reverse);


        threedots_Sort_byId = menu.findItem(R.id.threedots_Sort_byId);
        threedots_Sort_byId_Reverse = menu.findItem(R.id.threedots_Sort_byId_Reverse);
        return true;
    }

    /*
    * On click listener for the 3 dots menu
    * */
    @SuppressLint("NonConstantResourceId")
    @Override
    public boolean onOptionsItemSelected(@NonNull MenuItem item) {
        switch (item.getItemId()) {
            case R.id.threedots_Settings:
                Intent intent = new Intent(MainActivity.this,Settings.class);
                startActivity(intent);
                Toast.makeText(this, "Settinngs pressed", Toast.LENGTH_SHORT).show();
                return true;
            case R.id.threedots_TasksDone_Show:
                recyclerview_Task_Done.setVisibility(View.VISIBLE);
                item.setVisible(false);
                threedotsTasksDone_Hide.setVisible(true);
                return true;
            case R.id.threedots_TasksDone_Hide:
                recyclerview_Task_Done.setVisibility(View.GONE);
                item.setVisible(false);
                threedotsTasksDone_Show.setVisible(true);
                return true;

            case R.id.threedots_Sort_byName:
                Sort_byName();
                item.setVisible(false);
                threedots_Sort_byName_Reverse.setVisible(true);
                Sorting_Constant = "Sort_byName";
                //UpdateDatabase();
                return true;
            case R.id.threedots_Sort_byName_Reverse:
                Sort_byName_Reverse();
                threedots_Sort_byName.setVisible(true);
                Sorting_Constant = "Sort_byName_Reverse";
                //UpdateDatabase();
                return true;


            case R.id.threedots_Sort_byId:
                Sort_byId();
                item.setVisible(false);
                threedots_Sort_byId_Reverse.setVisible(true);
                Sorting_Constant = "Sort_byId";
                //UpdateDatabase();
                return true;
            case R.id.threedots_Sort_byId_Reverse:
                Sort_byId_Reverse();
                threedots_Sort_byId.setVisible(true);
                Sorting_Constant = "Sort_byId_Reverse";
                //UpdateDatabase();
                return true;
        }
        //ToDo Restore done tasks
        return super.onOptionsItemSelected(item);

    }


    /*
    * Initialing all objects / adapters / loading recyclerviews
    * */
    public void Initialing() {
        adapter = new RecyclerViewTaskAdapter(this);
        adapter_Task_Done = new RecyclerViewTaskDoneAdapter(this);
        RecyclerView recyclerview_Task = findViewById(R.id.recyclerview_Task);
        recyclerview_Task_Done = findViewById(R.id.recyclerview_Task_Done);
        floatingbutton_TaskAdd = findViewById(R.id.floatingbutton_TaskAdd);
        scrollview_Task = findViewById(R.id.scrollview_Task);

        recyclerview_Task_Done.setAdapter(adapter_Task_Done);

//        CustomLinearLayoutManager test = new CustomLinearLayoutManager(this);
//        test.setScrollEnabled(false);
//        test.canScrollVertically();
//        recyclerview_Task_Done.setLayoutManager(test);


        recyclerview_Task_Done.setLayoutManager(new GridLayoutManager(this, 1));
        recyclerview_Task.setAdapter(adapter);

//        CustomLinearLayoutManager test1 = new CustomLinearLayoutManager(this);
//        test1.setScrollEnabled(false);
//        test1.canScrollVertically();
//        recyclerview_Task.setLayoutManager(test1);






        recyclerview_Task.setLayoutManager(new GridLayoutManager(this, 1));

        Tasks = new ArrayList<>();
        Tasks_Done = new ArrayList<>();



        /*
         * Setting some Tasks for the FIRST time usage of the app OR loading the existent tasks
         */
//        Log.d(TAG, "Loading Tasks...");
        sharedpreferences = this.getSharedPreferences("alternate_db", MODE_PRIVATE);
        Gson gson = new Gson();
        Type type = new TypeToken<ArrayList<Task>>() {}.getType();
        Tasks = gson.fromJson(sharedpreferences.getString(TaskAddPopWindow.TASKS_KEY, null), type);
        if (Tasks == null) {
            InitTasksArray();
        }
        adapter.setTasks(Tasks);
//        Log.d(TAG, "Finished loading Tasks!");
        
//        Log.d(TAG, "Loading Tasks_Done...");
        sharedpreferences_TasksDone = this.getSharedPreferences("alternate_db_TasksDone", MODE_PRIVATE);
        gson = new Gson();
        type = new TypeToken<ArrayList<Task>>() {}.getType();
        Tasks_Done = gson.fromJson(sharedpreferences_TasksDone.getString(RecyclerViewTaskDoneAdapter.TASKSDONE_KEY, null), type);
        /*If it's the first time starting the app, it will add the following Tasks_Done*/
        if (Tasks_Done == null) {
            Tasks_Done = new ArrayList<>();
            id_number++;
            Tasks_Done.add(new Task(id_number, "First done task's test", true));
        }
        adapter_Task_Done.setTasks_Done(Tasks_Done);
//        System.out.println("==========================================" +
//                "\n Tasks_Done" + Tasks_Done +
//                "\n =============================================");
//        Log.d(TAG, "Finished loading Tasks_Done!");


        /*
        * Loading the id_number
        * */
        sharedpreferences_id_number = this.getSharedPreferences("alternate_db_id_number", MODE_PRIVATE);
        id_number = sharedpreferences_id_number.getInt(TaskAddPopWindow.KEY_ID_NUMBER, id_number);
        id_number++;

    }

    /*
    * If it's the first time starting the app, it'll be added the following Tasks
    * */
    private void InitTasksArray() {
        Tasks = new ArrayList<>();
        id_number++;
        Tasks.add(new Task(id_number, "Do your homework", false));

        id_number++;
        Tasks.add(new Task(id_number, "Breakfast", false));

        id_number++;
        Tasks.add(new Task(id_number, "Hello", false));

        id_number++;
        Tasks.add(new Task(id_number, getString(R.string.test), false));
    }

    public void TaskDoneAdd(int id_number, String text) {
        Tasks_Done.add(new Task(id_number, text, true));
        adapter_Task_Done.setTasks_Done(Tasks_Done);

        switch (Sorting_Constant){
            case "Sort_byName":
                Sort_byName_TasksDone();
                //Toast.makeText(this, "1", Toast.LENGTH_SHORT).show();
                break;
            case "Sort_byName_Reverse":
                Sort_byName_Reverse_TasksDone();
                //Toast.makeText(this, "2", Toast.LENGTH_SHORT).show();
                break;
            case "Sort_byId":
                Sort_byId_TasksDone();
                //Toast.makeText(this, "3", Toast.LENGTH_SHORT).show();
                break;
            case "Sort_byId_Reverse":
                Sort_byId_Reverse_TasksDone();
                //Toast.makeText(this, "4", Toast.LENGTH_SHORT).show();
                break;
        }

        /*Updating the Tasks_Done database (after adding a new Tasks_Done)*/
        sharedpreferences_TasksDone = this.getSharedPreferences("alternate_db_TasksDone", MODE_PRIVATE);
        SharedPreferences.Editor editor = sharedpreferences_TasksDone.edit();
        Gson gson = new Gson();
        editor.remove(RecyclerViewTaskDoneAdapter.TASKSDONE_KEY);
        editor.putString(RecyclerViewTaskDoneAdapter.TASKSDONE_KEY, gson.toJson(Tasks_Done));
        editor.apply();
    }
    public void TaskAdd(int id_number, String text) {
        Tasks.add(new Task(id_number, text, false));
        adapter.setTasks(Tasks);

        sharedpreferences = this.getSharedPreferences("alternate_db", MODE_PRIVATE);
        SharedPreferences.Editor editor = sharedpreferences.edit();
        Gson gson = new Gson();
        editor.remove(TaskAddPopWindow.TASKS_KEY);
        editor.putString(TaskAddPopWindow.TASKS_KEY, gson.toJson(Tasks));
        editor.apply();

    }

    /*
    * Sorting the three_dot_menu
    * */
    private void Sort_byName() {
        UpdateLastMenuItem_Checked();
        Collections.sort(Tasks, Task.byName);
        Collections.sort(Tasks_Done, Task.byName);
        adapter.notifyDataSetChanged();
        adapter_Task_Done.notifyDataSetChanged();
        //TODO Bug: If you have like "Homeworks, Fishing, Shopping" and sort it "by Name" it will show at the top of the RecyclerView
        UpdateDatabase();

    }

    private void Sort_byName_Reverse() {
        UpdateLastMenuItem_Checked();
        Collections.sort(Tasks, Task.byName_Reverse);
        Collections.sort(Tasks_Done, Task.byName_Reverse);
        adapter.notifyDataSetChanged();
        adapter_Task_Done.notifyDataSetChanged();
        //TODO Bug: If you have like "Homeworks, Fishing, Shopping" and sort it "by Name" it will show at the top of the RecyclerView
        UpdateDatabase();
    }

    private void Sort_byId() {
        UpdateLastMenuItem_Checked();
        Collections.sort(Tasks, Task.byId);
        Collections.sort(Tasks_Done, Task.byId);
        adapter.notifyDataSetChanged();
        adapter_Task_Done.notifyDataSetChanged();
        UpdateDatabase();
    }

    private void Sort_byId_Reverse() {
        UpdateLastMenuItem_Checked();
        Collections.sort(Tasks, Task.byId_Reverse);
        Collections.sort(Tasks_Done, Task.byId_Reverse);
        adapter.notifyDataSetChanged();
        adapter_Task_Done.notifyDataSetChanged();
        UpdateDatabase();
    }

    private void Sort_byName_TasksDone() {
        Collections.sort(Tasks_Done, Task.byName);
        adapter_Task_Done.notifyDataSetChanged();
        UpdateDatabase();

    }

    private void Sort_byName_Reverse_TasksDone() {
        Collections.sort(Tasks_Done, Task.byName_Reverse);
        adapter_Task_Done.notifyDataSetChanged();
        UpdateDatabase();
    }

    private void Sort_byId_TasksDone() {
        Collections.sort(Tasks_Done, Task.byId);
        adapter_Task_Done.notifyDataSetChanged();
        UpdateDatabase();
    }

    private void Sort_byId_Reverse_TasksDone() {
        Collections.sort(Tasks_Done, Task.byId_Reverse);
        adapter_Task_Done.notifyDataSetChanged();
        UpdateDatabase();
    }



    private void UpdateLastMenuItem_Checked() {
        threedots_Sort_byName.setVisible(true);
        threedots_Sort_byName_Reverse.setVisible(false);


        threedots_Sort_byId.setVisible(true);
        threedots_Sort_byId_Reverse.setVisible(false);
    }

    /*
    * Sorting the three_dot_menu
    * */




    public void setTitle(String Title) {
        setTitle(Title);
    }




    /*
    * PS: Without this thread the OnBindViewHolder won't be called after adding a new Task
    *
    * Updating the RecyclerViews (Calling the OnBindHolders of the adapters)
    * */
    private void Refresh_Timer() {
        new Timer().scheduleAtFixedRate(new TimerTask() {
            @Override
            public void run() {
                MainActivity.this.runOnUiThread(() -> {
                    Refresh();
                    System.out.println("YEP");
                    System.out.println(Tasks.size());
                    System.out.println(Tasks);
                    System.out.println("------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------");
                    System.out.println(Tasks_Done.size());
                    System.out.println(Tasks_Done);

                });
            }
        },0,5000);

        /*
        * If there is no tasks id's value resets to 0
        * */
        new Timer().scheduleAtFixedRate(new TimerTask() {
            @Override
            public void run() {
                MainActivity.this.runOnUiThread(() -> {
                    System.out.println("Yaaaaasssss");
                    if (Tasks.size() == 0) {
                        id_number = 0;
                        System.out.println("id_number reseted");
                    }
                });
            }
        },15000,60000);
    }


    private void Refresh() {
        adapter.notifyDataSetChanged();
        adapter_Task_Done.notifyDataSetChanged();
    }

    private void UpdateDatabase() {
        SharedPreferences.Editor editor = sharedpreferences.edit();
        Gson gson = new Gson();
        editor.remove(TaskAddPopWindow.TASKS_KEY);
        editor.putString(TaskAddPopWindow.TASKS_KEY, gson.toJson(Tasks));
        editor.apply();


        sharedpreferences_TasksDone = this.getSharedPreferences("alternate_db_TasksDone", Context.MODE_PRIVATE);
        editor = sharedpreferences_TasksDone.edit();
        gson = new Gson();
        editor.remove(RecyclerViewTaskDoneAdapter.TASKSDONE_KEY);
        editor.putString(RecyclerViewTaskDoneAdapter.TASKSDONE_KEY, gson.toJson(Tasks_Done));
        editor.apply();
    }



}
