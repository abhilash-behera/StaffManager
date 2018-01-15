package com.romina.staffmanager.adapter;

import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.romina.staffmanager.R;
import com.romina.staffmanager.retrofit.Staff;

import java.util.List;

/**
 * Created by Abhilash on 13-07-2017
 */

public class StaffsAdapter extends RecyclerView.Adapter<StaffsAdapter.MyViewHolder>{
    private List<Staff> staffList;

    public class MyViewHolder extends RecyclerView.ViewHolder{
        public TextView txtStaffName;
        public TextView txtStaffPosition;

        public MyViewHolder(View view){
            super(view);
            txtStaffName =(TextView)view.findViewById(R.id.txtStaffName);
            txtStaffPosition=(TextView)view.findViewById(R.id.txtStaffPosition);
        }
    }

    public StaffsAdapter(List<Staff> staffList){
        this.staffList=staffList;
    }

    @Override
    public MyViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View itemView= LayoutInflater.from(parent.getContext()).inflate(R.layout.staff_row_view,parent,false);

        return new MyViewHolder(itemView);
    }

    @Override
    public void onBindViewHolder(MyViewHolder holder, int position) {
        Staff staff=staffList.get(position);
        holder.txtStaffName.setText(staff.getFirst_name()+" "+staff.getLast_name());
        holder.txtStaffPosition.setText(staff.getPosition());
    }

    @Override
    public int getItemCount() {
        return staffList.size();
    }
}
