package com.romina.staffmanager.activity;

import android.Manifest;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.pm.PackageManager;
import android.os.Build;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.v4.content.ContextCompat;
import android.support.v7.app.AppCompatActivity;
import android.view.animation.AlphaAnimation;
import android.view.animation.Animation;
import android.widget.RelativeLayout;
import android.widget.Toast;

import com.romina.staffmanager.R;
import com.romina.staffmanager.Utils;

public class SplashScreenActivity extends AppCompatActivity {
    private RelativeLayout relativeLayout;
    private Animation animation;
    private SharedPreferences sharedPreferences;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_splash_screen);
        sharedPreferences=getSharedPreferences(Utils.SHARED_PREFERENCE,MODE_PRIVATE);
        relativeLayout=(RelativeLayout)findViewById(R.id.relativeLayout);
        animation=new AlphaAnimation(1.0f,0.0f);
        animation.setDuration(3000);

        if (android.os.Build.VERSION.SDK_INT >= android.os.Build.VERSION_CODES.M){
            if(ContextCompat.checkSelfPermission(SplashScreenActivity.this, Manifest.permission.WRITE_EXTERNAL_STORAGE)== PackageManager.PERMISSION_GRANTED){
                continueExecution();
            }else{
                requestPermissions(new String[]{Manifest.permission.WRITE_EXTERNAL_STORAGE},1);
            }
        } else{
            continueExecution();
        }
    }

    private void continueExecution(){
        relativeLayout.startAnimation(animation);
        relativeLayout.postDelayed(new Runnable() {
            @Override
            public void run() {
                if(sharedPreferences.contains("username")){
                    if(sharedPreferences.getString("type","").equalsIgnoreCase("admin")){
                        Intent intent=new Intent(SplashScreenActivity.this,AdminHomeActivity.class);
                        startActivity(intent);
                        finish();
                    }else{
                        Intent intent=new Intent(SplashScreenActivity.this,StaffHomeActivity.class);
                        startActivity(intent);
                        finish();
                    }
                }else{
                    Intent intent=new Intent(SplashScreenActivity.this,LoginActivity.class);
                    startActivity(intent);
                    finish();
                }
            }
        },2500);
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);
        if(ContextCompat.checkSelfPermission(SplashScreenActivity.this,permissions[0])==PackageManager.PERMISSION_GRANTED){
            continueExecution();
        }else{
            Toast.makeText(this, "You need to allow this permission.", Toast.LENGTH_LONG).show();
            finish();
        }
    }
}
