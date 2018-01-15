package com.romina.staffmanager.adapter;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.romina.staffmanager.R;
import com.romina.staffmanager.activity.TaskAssignActivity;
import com.romina.staffmanager.retrofit.Staff;

import java.util.List;

/**
 * Created by Abhilash on 01-08-2017
 */

public class StaffTaskAssignAdapter extends RecyclerView.Adapter<StaffTaskAssignAdapter.MyViewHolder>{
    private Context context;
    private List<Staff> staffs;

    public static class MyViewHolder extends RecyclerView.ViewHolder{
        public TextView txtStaffName;
        public TextView txtStaffPosition;
        public TextView txtPendingTasks;
        public LinearLayout linearLayout;

        public MyViewHolder(View view){
            super(view);
            txtStaffName=(TextView)view.findViewById(R.id.txtStaffName);
            txtStaffPosition=(TextView)view.findViewById(R.id.txtStaffPosition);
            txtPendingTasks=(TextView)view.findViewById(R.id.txtPendingTasks);
            linearLayout=(LinearLayout)view.findViewById(R.id.linearLayout);
        }
    }

    public StaffTaskAssignAdapter(List<Staff> staffs){
        this.staffs=staffs;
    }

    @Override
    public MyViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        this.context=parent.getContext();
        View itemView= LayoutInflater.from(parent.getContext()).inflate(R.layout.staff_task_assign_row_view,parent,false);

        return new MyViewHolder(itemView);
    }

    @Override
    public void onBindViewHolder(MyViewHolder holder, int position) {
        final Staff staff=staffs.get(position);
        holder.txtStaffName.setText(staff.getFirst_name()+" "+staff.getLast_name());
        holder.txtStaffPosition.setText(staff.getPosition());
        holder.txtPendingTasks.setText(""+staff.getPending_tasks());
        holder.linearLayout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                ((TaskAssignActivity)context).setUsername(staff.getUsername());
                ((TaskAssignActivity)context).moveToNextScreen();
            }
        });
    }

    @Override
    public int getItemCount() {
        return staffs.size();
    }
}
