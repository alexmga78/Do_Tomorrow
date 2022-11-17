package com.example.dotomorrow;

import android.content.Context;
import android.content.SharedPreferences;
import android.util.Log;
import android.view.ContextMenu;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.CompoundButton;
import android.widget.QuickContactBadge;
import android.widget.RadioButton;
import android.widget.ScrollView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.constraintlayout.widget.ConstraintLayout;
import androidx.recyclerview.widget.RecyclerView;

import com.google.gson.Gson;

import org.w3c.dom.Text;

import java.util.ArrayList;

public class RecyclerViewTaskAdapter extends RecyclerView.Adapter<RecyclerViewTaskAdapter.ViewHolder> {
    private static final String TAG = "RecyclerViewTaskAdapter";

    /*
    * START of SharedPreferences
    * */
    private SharedPreferences sharedpreferences,sharedPreferences_TasksDone;
    /*
    * END of SharedPreferences
    * */

    /*
    * START of RecyclerViewAdapters
    * */
    private RecyclerViewTaskDoneAdapter adapter_Task_Done;
    /*
    * END of RecyclerViewAdapters
    * */

    /*
    * START of Arrays
    * */
    private ArrayList<Task> tasks = new ArrayList<>();
    private ArrayList<Task> tasks_done = new ArrayList<>();
    /*
    * END of Arrays
    * */

    private int id_number_TasksDone = 0;







    private Context mContext;
    public RecyclerViewTaskAdapter(Context mContext) {
        this.mContext = mContext;
    }

    @Override
    public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()) . inflate(R.layout.list_item_task, parent, false);
        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(ViewHolder holder, int position) {
        Log.d(TAG, "onBindViewHolder: Called");
//        holder.parent.setText(tasks.get(position).getText());
        holder.parent_TextView.setText(tasks.get(position).getText());

        Animation anim = AnimationUtils.loadAnimation(mContext, R.anim.fade_out);
        int finalPosition = position;
        anim.setAnimationListener(new Animation.AnimationListener() {
            @Override
            public void onAnimationStart(Animation animation) {

            }
            @Override
            public void onAnimationEnd(Animation animation) {
                if (holder.parent_RadioButton.isChecked()){
                    if (removeTask(tasks.get(finalPosition))) {

                        Toast.makeText(mContext, "Done", Toast.LENGTH_SHORT).show();
                        holder.parent_RadioButton.setChecked(false);
                        notifyDataSetChanged();
                        UpdateDataBase();
                    }else{
                        Toast.makeText(mContext, "Something went wrong", Toast.LENGTH_SHORT).show();
                    }
                }

            }
            @Override
            public void onAnimationRepeat(Animation animation) {

            }
        });

        holder.parent_RadioButton.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                holder.parent.startAnimation(anim);
            }
        });

        System.out.println("Items " + getItemCount() + " position " + position + " " + tasks.get(position));













    }

    @Override
    public int getItemCount() {
        return tasks.size();
    }

    public void setTasks(ArrayList<Task> tasks) {
        this.tasks = tasks;
        notifyDataSetChanged();
        System.out.println("I am at the setTasks");
    }

    public class ViewHolder extends RecyclerView.ViewHolder{

        public RadioButton parent_RadioButton;
        public TextView parent_TextView;
        public ConstraintLayout parent;
        public ViewHolder(View itemView) {
            super(itemView);
            adapter_Task_Done = new RecyclerViewTaskDoneAdapter(mContext);
            parent = itemView.findViewById(R.id.parent);
            parent_RadioButton = itemView.findViewById(R.id.parent_RadioButton);
            parent_TextView = itemView.findViewById(R.id.parent_TextView);
            System.out.println("I am at the ViewHolder (TaskAdapter)");

        }

        //TODO When touching RadioButton's CIRCLE to make normal action, but when touching RadioButton's TEXT to open Editing PopupWindow
        //TODO When holding on RadioButton's TEXT to be able to move the Task/Task_Done
    }

    private boolean removeTask(Task task) {
        id_number_TasksDone++;
        ((MainActivity)mContext).TaskDoneAdd(task.getId(), task.getText());

        System.out.println("--------------------------------------------------------------------------------------");
        System.out.println(tasks_done.size());
        System.out.println(tasks_done);
        System.out.println(adapter_Task_Done);
        return tasks.remove(task);
    }

    private void UpdateDataBase() {
        sharedpreferences = mContext.getSharedPreferences("alternate_db", Context.MODE_PRIVATE);
        SharedPreferences.Editor editor = sharedpreferences.edit();
        Gson gson = new Gson();
        editor.remove(TaskAddPopWindow.TASKS_KEY);
        editor.putString(TaskAddPopWindow.TASKS_KEY, gson.toJson(tasks));
        editor.apply();
    }

}
