package com.romina.staffmanager.fragment;


import android.app.ProgressDialog;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v7.widget.DefaultItemAnimator;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;

import com.romina.staffmanager.R;
import com.romina.staffmanager.Utils;
import com.romina.staffmanager.adapter.StaffTaskAssignAdapter;
import com.romina.staffmanager.adapter.StaffsAdapter;
import com.romina.staffmanager.retrofit.Staff;
import com.romina.staffmanager.retrofit.StaffListResponse;

import java.util.List;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

/**
 * A simple {@link Fragment} subclass.
 */
public class TaskAssignFirstStepFragment extends Fragment {
    private View view;
    private RecyclerView recyclerView;
    private List<Staff> staffs;


    public TaskAssignFirstStepFragment() {
        // Required empty public constructor
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        view= inflater.inflate(R.layout.fragment_task_assign_first_step, container, false);
        initializeViews();
        return view;
    }

    private void initializeViews() {
        recyclerView=(RecyclerView)view.findViewById(R.id.recyclerView);
        recyclerView.setLayoutManager(new LinearLayoutManager(getActivity()));
        recyclerView.setItemAnimator(new DefaultItemAnimator());
        if(staffs==null){
            getStaffList();
        }else{
            prepareData();
        }

    }

    private void prepareData() {
        recyclerView.setAdapter(new StaffTaskAssignAdapter(staffs));
    }

    private void getStaffList(){
        final ProgressDialog progressDialog=new ProgressDialog(getActivity());
        progressDialog.setTitle("Getting Staff List");
        progressDialog.setMessage("please wait...");
        progressDialog.setCancelable(false);
        progressDialog.show();

        Utils.getRetrofitService().getStaffList().enqueue(new Callback<StaffListResponse>() {
            @Override
            public void onResponse(Call<StaffListResponse> call, final Response<StaffListResponse> response) {
                getActivity().runOnUiThread(new Runnable() {
                    @Override
                    public void run() {
                        progressDialog.dismiss();
                        staffs=response.body().getData();
                        prepareData();
                    }
                });
            }

            @Override
            public void onFailure(Call<StaffListResponse> call, Throwable t) {
                getActivity().runOnUiThread(new Runnable() {
                    @Override
                    public void run() {
                        progressDialog.dismiss();
                        Toast.makeText(getActivity(), "Something went wrong. Please try again.", Toast.LENGTH_SHORT).show();
                    }
                });
            }
        });
    }

}
