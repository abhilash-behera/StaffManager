package com.romina.staffmanager.activity;

import android.app.ProgressDialog;
import android.support.design.widget.TabLayout;
import android.support.v4.view.ViewPager;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.MenuItem;
import android.widget.Toast;

import com.romina.staffmanager.R;
import com.romina.staffmanager.Utils;
import com.romina.staffmanager.adapter.AdminTasksAdapter;
import com.romina.staffmanager.adapter.TaskAssignPagerAdapter;
import com.romina.staffmanager.retrofit.AssignTaskRequest;
import com.romina.staffmanager.retrofit.AssignTaskResponse;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class TaskAssignActivity extends AppCompatActivity {
    private ViewPager viewPager;
    private TabLayout tabLayout;
    private String username;
    private String timestamp;
    private String created_on;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_task_assign);
        getSupportActionBar().setTitle("Assign Task");
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        created_on=getIntent().getStringExtra(AdminTasksAdapter.CREATED_ON);
        initializeViews();
    }

    private void initializeViews() {
        viewPager=(ViewPager)findViewById(R.id.viewPager);
        tabLayout=(TabLayout)findViewById(R.id.tabDots);
        tabLayout.setupWithViewPager(viewPager);

        TaskAssignPagerAdapter taskAssignPagerAdapter=new TaskAssignPagerAdapter(getSupportFragmentManager());
        viewPager.setAdapter(taskAssignPagerAdapter);
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()){
            case android.R.id.home:
                finish();
                break;
        }

        return true;
    }

    public void setUsername(String username){
        this.username=username;
    }

    public void setTimestamp(String timestamp){
        this.timestamp=timestamp;
    }

    public void moveToNextScreen(){
        viewPager.setCurrentItem(viewPager.getCurrentItem()+1);
    }

    public void goToBegening(){
        viewPager.setCurrentItem(0);
    }

    public String getData(String key){
        if(key.equalsIgnoreCase("username")){
            return username;
        }else if(key.equalsIgnoreCase("timestamp")){
            return timestamp;
        }else{
            return null;
        }
    }

    public void assignTask(){
        final ProgressDialog progressDialog=new ProgressDialog(TaskAssignActivity.this);
        progressDialog.setTitle("Assigning Task");
        progressDialog.setMessage("Please wait...");
        progressDialog.setCancelable(false);
        progressDialog.show();

        Utils.getRetrofitService().assignTask(new AssignTaskRequest(username,created_on,timestamp)).enqueue(new Callback<AssignTaskResponse>() {
            @Override
            public void onResponse(Call<AssignTaskResponse> call, final Response<AssignTaskResponse> response) {
                runOnUiThread(new Runnable() {
                    @Override
                    public void run() {
                        progressDialog.dismiss();
                        if(response.body().isSuccess()){
                            Toast.makeText(TaskAssignActivity.this, "Task assigned successfully", Toast.LENGTH_SHORT).show();
                            finish();
                        }else{
                            Toast.makeText(TaskAssignActivity.this, response.body().getData(), Toast.LENGTH_SHORT).show();
                        }
                    }
                });
            }

            @Override
            public void onFailure(Call<AssignTaskResponse> call, Throwable t) {
                runOnUiThread(new Runnable() {
                    @Override
                    public void run() {
                        progressDialog.dismiss();
                        Toast.makeText(TaskAssignActivity.this, "Something went wrong. Please try again.", Toast.LENGTH_SHORT).show();
                    }
                });
            }
        });
    }
}
