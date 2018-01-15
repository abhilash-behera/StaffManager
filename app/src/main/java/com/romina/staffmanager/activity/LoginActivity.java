package com.romina.staffmanager.activity;

import android.app.ProgressDialog;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.support.design.widget.TextInputLayout;
import android.support.v7.app.AppCompatActivity;
import android.text.method.PasswordTransformationMethod;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.CompoundButton;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.Toast;

import com.romina.staffmanager.R;
import com.romina.staffmanager.Utils;
import com.romina.staffmanager.retrofit.LoginRequest;
import com.romina.staffmanager.retrofit.LoginResponse;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

import static com.romina.staffmanager.Utils.tag;

public class LoginActivity extends AppCompatActivity {

    private EditText txtUsername;
    private EditText txtPassword;
    private TextInputLayout inputUsername;
    private TextInputLayout inputPassword;
    private CheckBox chkPassword;
    private Button btnLogin;
    private LinearLayout forgotPasswordLayout;
    private ProgressDialog progressDialog;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);

        initializeViews();
    }

    private void initializeViews() {
        txtUsername=(EditText)findViewById(R.id.txtUsername);
        txtPassword=(EditText)findViewById(R.id.txtPassword);
        inputUsername=(TextInputLayout)findViewById(R.id.inputUsername);
        inputPassword=(TextInputLayout)findViewById(R.id.inputPassword);
        chkPassword=(CheckBox)findViewById(R.id.chkPassword);
        btnLogin=(Button)findViewById(R.id.btnLogin);
        forgotPasswordLayout=(LinearLayout)findViewById(R.id.forgotPasswordLayout);

        chkPassword.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                if(isChecked){
                    txtPassword.setTransformationMethod(null);
                    txtPassword.setSelection(txtPassword.length());
                }
                else{
                    txtPassword.setTransformationMethod(new PasswordTransformationMethod());
                    txtPassword.setSelection(txtPassword.length());
                }
            }
        });

        btnLogin.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(txtUsername.getText().toString().isEmpty()){
                    inputUsername.setError("Username Required!");
                    txtUsername.requestFocus();
                }else if(txtPassword.getText().toString().isEmpty()){
                    inputPassword.setError("Password Required!");
                    txtPassword.requestFocus();
                }else{
                    login(txtUsername.getText().toString(),txtPassword.getText().toString());
                }
            }
        });
    }

    public void login(String username,String password){
        progressDialog=new ProgressDialog(LoginActivity.this);
        progressDialog.setCancelable(false);
        progressDialog.setMessage("Authenticating...");
        progressDialog.show();
        Call<LoginResponse> call=Utils.getRetrofitService().login(new LoginRequest(username, password));
        call.enqueue(new Callback<LoginResponse>() {
            @Override
            public void onResponse(Call<LoginResponse> call, final Response<LoginResponse> response) {
                runOnUiThread(new Runnable() {
                    @Override
                    public void run() {
                        progressDialog.dismiss();
                        Log.d(tag,"body : "+response.body());
                        if(response.code()==200){
                            SharedPreferences sharedPreferences=getSharedPreferences(Utils.SHARED_PREFERENCE,
                                    MODE_PRIVATE);
                            sharedPreferences.edit()
                                    .putString(Utils.FIRST_NAME,response.body().data.first_name)
                                    .putString(Utils.LAST_NAME,response.body().data.last_name)
                                    .putString(Utils.USERNAME,response.body().data.username)
                                    .putString(Utils.USER_TYPE,response.body().data.type)
                                    .putString(Utils.POSITION,response.body().data.position)
                                    .apply();
                            if(response.body().data.type.equalsIgnoreCase("admin")){
                                Intent intent=new Intent(LoginActivity.this,AdminHomeActivity.class);
                                startActivity(intent);
                                finish();
                            }else if(response.body().data.type.equalsIgnoreCase("type")){
                                Intent intent=new Intent(LoginActivity.this,StaffHomeActivity.class);
                                startActivity(intent);
                                finish();
                            }
                        }else if(response.code()==209){
                            Toast.makeText(LoginActivity.this,
                                    "Could not log in at this moment. Please try again later.",
                                    Toast.LENGTH_LONG).show();
                        }else if(response.code()==309){
                            Toast.makeText(LoginActivity.this, "Invalid username or password.",
                                    Toast.LENGTH_SHORT).show();
                        }
                    }
                });
            }

            @Override
            public void onFailure(Call<LoginResponse> call, Throwable t) {
                runOnUiThread(new Runnable() {
                    @Override
                    public void run() {
                        progressDialog.dismiss();
                        Toast.makeText(LoginActivity.this, "Server did not respond. Please try again.", Toast.LENGTH_LONG).show();
                    }
                });
            }
        });
    }
}
