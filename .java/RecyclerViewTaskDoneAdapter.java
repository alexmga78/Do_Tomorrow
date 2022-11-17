package com.example.dotomorrow;

import android.content.Context;
import android.content.SharedPreferences;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.CompoundButton;
import android.widget.RadioButton;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.constraintlayout.widget.ConstraintLayout;
import androidx.recyclerview.widget.RecyclerView;

import com.google.gson.Gson;

import java.util.ArrayList;

public class RecyclerViewTaskDoneAdapter extends RecyclerView.Adapter<RecyclerViewTaskDoneAdapter.ViewHolder>{
    private static final String TAG = "RecyclerViewTaskDoneAdapter";

    /*
    * START of Keys
    * */
    public static  final String TASKSDONE_KEY = "tasksdone_key";
    /*
    * END of Keys
    * */

    /*
    * START of SharedPreferences
    * */
    private SharedPreferences sharedPreferences_TasksDone;
    /*
    * END of SharedPreferences
    * */

    /*
    * START of Arrays
    * */
    private ArrayList<Task> Tasks_Done  = new ArrayList<>();
    /*
    * END of Arrays
    * */






    private Context mContext;
    public RecyclerViewTaskDoneAdapter(Context mContext) {
        this.mContext = mContext;
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.list_item_task_done, parent, false);
        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int position) {
        Log.d(TAG, "onBindViewHolderTaskDone: Called");
        holder.parent_TextView.setText(Tasks_Done.get(position).getText());


        Animation anim = AnimationUtils.loadAnimation(mContext, R.anim.fade_out);
        int finalPosition = position;
        anim.setAnimationListener(new Animation.AnimationListener() {
            @Override
            public void onAnimationStart(Animation animation) {

            }
            @Override
            public void onAnimationEnd(Animation animation) {
                if (removeTask_Done(Tasks_Done.get(finalPosition))) {
                    holder.parent_RadioButton.setChecked(false);
                    notifyDataSetChanged();
                    Toast.makeText(mContext, "Done Task selected", Toast.LENGTH_SHORT).show();

                    UpdateDataBase();
                }else{
                    Toast.makeText(mContext, "Something went wrong", Toast.LENGTH_SHORT).show();
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
    }

    @Override
    public int getItemCount() {
        return Tasks_Done.size();
    }

    public void setTasks_Done(ArrayList<Task> tasks_Done) {
        this.Tasks_Done = tasks_Done;
        notifyDataSetChanged();
        System.out.println(Tasks_Done);
        System.out.println("I am at setTasks_Done");
    }

    public class ViewHolder extends RecyclerView.ViewHolder{


        public RadioButton parent_RadioButton;
        public TextView parent_TextView;
        public ConstraintLayout parent;
        public ViewHolder(@NonNull View itemView) {
            super(itemView);
            parent = itemView.findViewById(R.id.parent1);
            parent_RadioButton = itemView.findViewById(R.id.parent1_RadioButton);
            parent_TextView = itemView.findViewById(R.id.parent1_TextView);
            System.out.println("I am at the ViewHolder (TaskDoneAdapter)");
        }

        //TODO Load only the last 7 days Tasks_Done, the other needs to be loaded by clicking a sort of Button
    }

    private boolean removeTask_Done (Task task){
        ((MainActivity)mContext).TaskAdd(task.getId(), task.getText());

        return Tasks_Done.remove(task);
    }

    private void UpdateDataBase() {
        sharedPreferences_TasksDone = mContext.getSharedPreferences("alternate_db_TasksDone", Context.MODE_PRIVATE);
        SharedPreferences.Editor editor = sharedPreferences_TasksDone.edit();
        Gson gson = new Gson();
        editor.remove(TASKSDONE_KEY);
        editor.putString(TASKSDONE_KEY, gson.toJson(Tasks_Done));
        editor.apply();
    }

}
