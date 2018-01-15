package com.romina.staffmanager.fragment;


import android.app.Dialog;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.graphics.Color;
import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.TextInputLayout;
import android.support.v4.app.Fragment;
import android.support.v7.app.AlertDialog;
import android.support.v7.widget.DefaultItemAnimator;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

import com.romina.staffmanager.R;
import com.romina.staffmanager.Utils;
import com.romina.staffmanager.adapter.StaffsAdapter;
import com.romina.staffmanager.retrofit.CreateStaffRequest;
import com.romina.staffmanager.retrofit.CreateStaffResponse;
import com.romina.staffmanager.retrofit.Staff;
import com.romina.staffmanager.retrofit.StaffListResponse;
import com.romina.staffmanager.retrofit.UsernameAvailabilityResponse;
import com.romina.staffmanager.retrofit.UsernameAvailablityRequest;

import java.util.List;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class StaffsFragment extends Fragment {
    private View view;
    private RecyclerView recyclerView;
    private FloatingActionButton fab;
    private boolean usernameAvailable;
    private List<Staff> staffs;


    public StaffsFragment() {
        // Required empty public constructor
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        view= inflater.inflate(R.layout.fragment_staffs, container, false);
        initializeViews();
        return view;
    }

    private void initializeViews() {
        recyclerView=(RecyclerView)view.findViewById(R.id.recyclerView);
        recyclerView.setLayoutManager(new LinearLayoutManager(getActivity()));
        recyclerView.setItemAnimator(new DefaultItemAnimator());
        fab=(FloatingActionButton)view.findViewById(R.id.fab);

        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                usernameAvailable=false;
                AlertDialog.Builder builder=new AlertDialog.Builder(getActivity());
                builder.setTitle("New Staff")
                        .setPositiveButton("Create Staff",null)
                        .setNegativeButton("Cancel",null);

                View dialogView=((LayoutInflater)getActivity().getSystemService(Context.LAYOUT_INFLATER_SERVICE)).inflate(R.layout.new_staff_dialog_layout,null);

                final TextInputLayout inputFirstName=(TextInputLayout)dialogView.findViewById(R.id.inputFirstName);
                final TextInputLayout inputLastName=(TextInputLayout)dialogView.findViewById(R.id.inputLastName);
                TextInputLayout inputUsername=(TextInputLayout)dialogView.findViewById(R.id.inputUsername);
                final TextInputLayout inputPosition=(TextInputLayout)dialogView.findViewById(R.id.inputPosition);
                final TextInputLayout inputPassword=(TextInputLayout)dialogView.findViewById(R.id.inputPassword);
                final TextInputLayout inputConfirmPassword=(TextInputLayout)dialogView.findViewById(R.id.inputConfirmPassword);

                final EditText txtFirstName=(EditText)dialogView.findViewById(R.id.txtFirstName);
                final EditText txtLastName=(EditText)dialogView.findViewById(R.id.txtLastName);
                final EditText txtUsername=(EditText)dialogView.findViewById(R.id.txtUsername);
                final TextView txtUsernameAvailability=(TextView)dialogView.findViewById(R.id.txtUsernameAvailability);
                final EditText txtPosition=(EditText)dialogView.findViewById(R.id.txtPosition);
                final EditText txtPassword=(EditText)dialogView.findViewById(R.id.txtPassword);
                final EditText txtConfirmPassword=(EditText)dialogView.findViewById(R.id.txtConfirmPassword);

                final Button btnCheckAvailability=(Button)dialogView.findViewById(R.id.btnCheckAvailability);
                final ProgressBar progressAvailability=(ProgressBar)dialogView.findViewById(R.id.progressAvailability);

                btnCheckAvailability.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        if(txtUsername.getText().toString().length()>2){
                            btnCheckAvailability.setEnabled(false);
                            progressAvailability.setVisibility(View.VISIBLE);
                            checkUsernameAvailability(txtUsername.getText().toString(),progressAvailability,btnCheckAvailability,txtUsernameAvailability);
                        }else{
                            Toast.makeText(getActivity(), "Username must be atlease 3 characters.", Toast.LENGTH_SHORT).show();
                        }

                    }
                });

                builder.setView(dialogView);

                final Dialog dialog=builder.create();

                dialog.setOnShowListener(new DialogInterface.OnShowListener() {
                    @Override
                    public void onShow(final DialogInterface d) {
                        Button positiveButton=((AlertDialog)dialog).getButton(DialogInterface.BUTTON_POSITIVE);
                        Button negativeButton=((AlertDialog)dialog).getButton(DialogInterface.BUTTON_NEGATIVE);

                        positiveButton.setOnClickListener(new View.OnClickListener() {
                            @Override
                            public void onClick(View v) {
                                if(txtFirstName.getText().toString().isEmpty()){
                                    inputFirstName.setError("First Name Required");
                                }else if(txtLastName.getText().toString().isEmpty()){
                                    inputFirstName.setError(null);
                                    inputLastName.setError("Last Name Required");
                                }else if(!usernameAvailable){
                                    inputLastName.setError(null);
                                    Toast.makeText(getActivity(), "Please check username availability first.", Toast.LENGTH_SHORT).show();
                                }else if(txtPosition.getText().toString().isEmpty()){
                                    inputPosition.setError("Position Required");
                                }else if(txtPassword.getText().toString().isEmpty()){
                                    inputPosition.setError(null);
                                    inputPassword.setError("Password Required");
                                }else if(txtConfirmPassword.getText().toString().isEmpty()){
                                    inputPassword.setError(null);
                                    inputConfirmPassword.setError("Retype Password");
                                }else if(txtPassword.getText().toString().compareTo(txtConfirmPassword.getText().toString())!=0){
                                    inputConfirmPassword.setError("Password do not match");
                                }else{
                                    inputConfirmPassword.setError(null);
                                    createStaff(txtFirstName.getText().toString(),txtLastName.getText().toString(),
                                            txtUsername.getText().toString(),txtPosition.getText().toString(),
                                            txtPassword.getText().toString(),"staff",dialog);
                                }
                            }
                        });

                        negativeButton.setOnClickListener(new View.OnClickListener() {
                            @Override
                            public void onClick(View v) {
                                d.dismiss();
                            }
                        });
                    }
                });

                dialog.show();
            }
        });

        if(staffs==null){
            getStaffList();
        }else{
            prepareData();
        }

    }

    private void prepareData() {
        recyclerView.setAdapter(new StaffsAdapter(staffs));
    }

    private void checkUsernameAvailability(String username, final ProgressBar progressAvailability, final Button btnCheckAvailability, final TextView txtUsernameAvailability) {
        Utils.getRetrofitService().checkUsernameAvailability(new UsernameAvailablityRequest(username)).enqueue(new Callback<UsernameAvailabilityResponse>() {
            @Override
            public void onResponse(Call<UsernameAvailabilityResponse> call,final  Response<UsernameAvailabilityResponse> response) {
                getActivity().runOnUiThread(new Runnable() {
                    @Override
                    public void run() {
                        progressAvailability.setVisibility(View.GONE);
                        btnCheckAvailability.setEnabled(true);
                        if(response.body().isSuccess()){
                            usernameAvailable=true;
                            txtUsernameAvailability.setTextColor(Color.GREEN);
                        }else{
                            txtUsernameAvailability.setTextColor(Color.RED);
                        }

                        txtUsernameAvailability.setText(response.body().getData());
                    }
                });

            }

            @Override
            public void onFailure(Call<UsernameAvailabilityResponse> call, Throwable t) {
                getActivity().runOnUiThread(new Runnable() {
                    @Override
                    public void run() {
                        txtUsernameAvailability.setTextColor(Color.RED);
                        txtUsernameAvailability.setText("Something went wrong. Please Try Again");
                    }
                });

            }
        });
    }

    private void createStaff(String first_name, String last_name, String username, String position, String password, String type, final Dialog dialog) {
        final ProgressDialog progressDialog=new ProgressDialog(getActivity());
        progressDialog.setTitle("Creating Staff");
        progressDialog.setMessage("please wait...");
        progressDialog.setCancelable(false);
        progressDialog.show();
        Utils.getRetrofitService().createStaff(new CreateStaffRequest(first_name,last_name,username,position,type,password)).enqueue(new Callback<CreateStaffResponse>() {
            @Override
            public void onResponse(Call<CreateStaffResponse> call,final Response<CreateStaffResponse> response) {
                getActivity().runOnUiThread(new Runnable() {
                    @Override
                    public void run() {
                        if(response.code()==200){
                            Toast.makeText(getActivity(), "Staff Created Successfully...", Toast.LENGTH_SHORT).show();
                            dialog.dismiss();
                            progressDialog.dismiss();
                            getStaffList();
                        }else{
                            progressDialog.dismiss();
                            Toast.makeText(getActivity(), response.body().getData(), Toast.LENGTH_LONG).show();
                        }
                    }
                });
            }

            @Override
            public void onFailure(Call<CreateStaffResponse> call, Throwable t) {
                getActivity().runOnUiThread(new Runnable() {
                    @Override
                    public void run() {
                        Toast.makeText(getActivity(), "Internal Server Error. Please try again.", Toast.LENGTH_SHORT).show();
                    }
                });
            }
        });
    }

    private void getStaffList(){
        final ProgressDialog progressDialog=new ProgressDialog(getActivity());
        progressDialog.setTitle("Getting Staff List");
        progressDialog.setCancelable(false);
        progressDialog.setMessage("Please wait...");
        progressDialog.show();

        Utils.getRetrofitService().getStaffList().enqueue(new Callback<StaffListResponse>() {
            @Override
            public void onResponse(Call<StaffListResponse> call, final Response<StaffListResponse> response) {
                getActivity().runOnUiThread(new Runnable() {
                    @Override
                    public void run() {
                        progressDialog.dismiss();
                        if(response.body().isSuccess()){
                            staffs=response.body().getData();
                            prepareData();
                        }else{
                            Toast.makeText(getActivity(), "Something Went Wrong. Please try again.", Toast.LENGTH_SHORT).show();
                        }
                    }
                });
            }

            @Override
            public void onFailure(Call<StaffListResponse> call, Throwable t) {
                getActivity().runOnUiThread(new Runnable() {
                    @Override
                    public void run() {
                        Toast.makeText(getActivity(), "Server did not respond. Please try again.", Toast.LENGTH_SHORT).show();
                    }
                });
            }
        });
    }

}
