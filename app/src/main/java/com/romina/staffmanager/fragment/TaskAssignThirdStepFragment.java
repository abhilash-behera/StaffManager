package com.romina.staffmanager.fragment;


import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TextView;

import com.romina.staffmanager.R;
import com.romina.staffmanager.Utils;
import com.romina.staffmanager.activity.TaskAssignActivity;
import com.romina.staffmanager.retrofit.AssignTaskRequest;


public class TaskAssignThirdStepFragment extends Fragment {
    private View view;
    public static TextView txtName;
    public static TextView txtTimestamp;
    private Button btnChange;
    private Button btnProceed;

    public TaskAssignThirdStepFragment() {
        // Required empty public constructor
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        view= inflater.inflate(R.layout.fragment_task_assign_third_step, container, false);
        initializeViews();
        return view;
    }

    private void initializeViews() {
        txtName=(TextView)view.findViewById(R.id.txtName);
        txtTimestamp=(TextView)view.findViewById(R.id.txtTimestamp);
        btnChange=(Button)view.findViewById(R.id.btnChange);
        btnProceed=(Button)view.findViewById(R.id.btnProceed);

        btnChange.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                ((TaskAssignActivity)getActivity()).goToBegening();
            }
        });

        btnProceed.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                ((TaskAssignActivity)getActivity()).assignTask();
            }
        });

    }
}
