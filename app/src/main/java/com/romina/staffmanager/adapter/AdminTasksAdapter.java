package com.romina.staffmanager.adapter;

import android.content.Context;
import android.content.Intent;
import android.graphics.Color;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.romina.staffmanager.R;
import com.romina.staffmanager.activity.TaskAssignActivity;
import com.romina.staffmanager.activity.VideoActivity;
import com.romina.staffmanager.retrofit.Task;

import java.util.List;

/**
 * Created by Abhilash on 22-07-2017
 */

public class AdminTasksAdapter extends RecyclerView.Adapter<AdminTasksAdapter.MyViewHolder> {
    private List<Task> tasks;
    public static final String VIDEO_URL="video_url";
    public static final String CREATED_ON="created_on";
    private Context context;

    public class MyViewHolder extends RecyclerView.ViewHolder{
        public TextView txtTaskName;
        public TextView txtTaskDuration;
        public TextView txtTaskAssignedTo;
        public TextView txtTaskStatus;
        public TextView txtTaskVideo;
        public TextView txtTimestamp;
        public TextView txtAssignedTime;

        public MyViewHolder(View view){
            super(view);
            txtTaskName=(TextView)view.findViewById(R.id.txtTaskName);
            txtTaskDuration=(TextView)view.findViewById(R.id.txtTaskDuration);
            txtTaskAssignedTo=(TextView)view.findViewById(R.id.txtTaskAssignedTo);
            txtTaskStatus=(TextView)view.findViewById(R.id.txtTaskStatus);
            txtTaskVideo=(TextView)view.findViewById(R.id.txtTaskVideo);
            txtTimestamp=(TextView)view.findViewById(R.id.txtTimestamp);
            txtAssignedTime=(TextView)view.findViewById(R.id.txtAssignedTime);
        }
    }

    public AdminTasksAdapter(List<Task> tasks){
        this.tasks=tasks;
    }

    @Override
    public MyViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View itemView= LayoutInflater.from(parent.getContext()).inflate(R.layout.admin_task_row_view,parent,false);
        context=parent.getContext();
        return new MyViewHolder(itemView);
    }

    @Override
    public void onBindViewHolder(MyViewHolder holder, int position) {
        final Task task=tasks.get(position);
        holder.txtTaskName.setText(task.getTask_name());
        holder.txtTaskDuration.setText(task.getTask_duration()+" Mins");
        holder.txtTimestamp.setText(task.getCreated_on());
        holder.txtTaskAssignedTo.setText(task.getAssigned_to());
        holder.txtTaskStatus.setText(task.getTask_status());
        holder.txtAssignedTime.setText(task.getAssigned_time());

        if(task.getTask_status().equalsIgnoreCase("Not Completed")){
            holder.txtTaskStatus.setTextColor(Color.RED);
        }else if(task.getTask_status().equalsIgnoreCase("In Progress")){
            holder.txtTaskStatus.setTextColor(Color.CYAN);
        }else if(task.getTask_status().equalsIgnoreCase("Completed")){
            holder.txtTaskStatus.setTextColor(Color.GREEN);
        }

        if(task.getAssigned_to().equalsIgnoreCase("Click To Assign")){
            holder.txtTaskAssignedTo.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    Intent intent=new Intent(context, TaskAssignActivity.class);
                    intent.putExtra(CREATED_ON,task.getCreated_on());
                    context.startActivity(intent);
                }
            });
        }else{
            holder.txtTaskAssignedTo.setTextColor(Color.BLACK);
        }

        holder.txtTaskVideo.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent=new Intent(context, VideoActivity.class);
                intent.putExtra(VIDEO_URL,task.getVideo_url());
                context.startActivity(intent);
            }
        });
    }

    @Override
    public int getItemCount() {
        return tasks.size();
    }
}
