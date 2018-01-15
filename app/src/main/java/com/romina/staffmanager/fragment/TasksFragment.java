package com.romina.staffmanager.fragment;

import android.annotation.SuppressLint;
import android.app.Dialog;
import android.app.ProgressDialog;
import android.content.ContentUris;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.database.Cursor;
import android.net.Uri;
import android.os.AsyncTask;
import android.os.Build;
import android.os.Bundle;
import android.os.Environment;
import android.provider.DocumentsContract;
import android.provider.MediaStore;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.TextInputLayout;
import android.support.v4.app.Fragment;
import android.support.v7.app.AlertDialog;
import android.support.v7.widget.DefaultItemAnimator;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import com.cloudinary.utils.ObjectUtils;
import com.romina.staffmanager.R;
import com.romina.staffmanager.Utils;
import com.romina.staffmanager.adapter.AdminTasksAdapter;
import com.romina.staffmanager.adapter.NotificationDurationAdapter;
import com.romina.staffmanager.retrofit.AdminTasksListResponse;
import com.romina.staffmanager.retrofit.CreateTaskResponse;
import com.romina.staffmanager.retrofit.Task;

import java.util.List;
import java.util.Map;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

import static android.app.Activity.RESULT_OK;
import static com.romina.staffmanager.Utils.tag;


public class TasksFragment extends Fragment{
    private View view;
    private FloatingActionButton fab;
    private static final int SELECT_VIDEO = 1;
    private String selectedVideoPath="";
    private TextView txtVideoUpload;
    private String selectedVideoUrl="";
    private RecyclerView recyclerView;
    private List<Task> tasks;

    public TasksFragment() {
        // Required empty public constructor
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        view= inflater.inflate(R.layout.fragment_tasks, container, false);
        initializeViews();
        return view;
    }

    private void initializeViews() {
        fab=(FloatingActionButton)view.findViewById(R.id.fab);
        recyclerView=(RecyclerView)view.findViewById(R.id.recyclerView);
        recyclerView.setLayoutManager(new LinearLayoutManager(getActivity()));
        recyclerView.setItemAnimator(new DefaultItemAnimator());
        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                AlertDialog.Builder builder=new AlertDialog.Builder(getActivity());

                View dialogView=((LayoutInflater)getActivity().getSystemService(Context.LAYOUT_INFLATER_SERVICE))
                        .inflate(R.layout.new_task_dialog_layout,null);

                final EditText txtTaskName=(EditText)dialogView.findViewById(R.id.txtTaskName);
                final EditText txtTaskDuration=(EditText)dialogView.findViewById(R.id.txtTaskDuration);
                final TextInputLayout inputTaskName=(TextInputLayout) dialogView.findViewById(R.id.inputTaskName);
                final TextInputLayout inputTaskDuration=(TextInputLayout)dialogView.findViewById(R.id.inputTaskDuration);
                final Spinner spinner=(Spinner)dialogView.findViewById(R.id.spinner);
                String[] durations={"5 min","10 min","15 min"};
                spinner.setAdapter(new NotificationDurationAdapter(getActivity(),durations));

                txtVideoUpload=(TextView)dialogView.findViewById(R.id.txtVideoUpload);
                txtVideoUpload.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        showFileChooser();
                    }
                });

                builder.setView(dialogView)
                        .setTitle("New Task")
                        .setPositiveButton("Create Task",null)
                        .setNegativeButton("Cancel",null);
                final Dialog dialog=builder.create();
                dialog.setOnShowListener(new DialogInterface.OnShowListener() {
                    @Override
                    public void onShow(final DialogInterface d) {
                        Button positiveButton=((AlertDialog)d).getButton(DialogInterface.BUTTON_POSITIVE);
                        Button negativeButton=((AlertDialog)d).getButton(DialogInterface.BUTTON_NEGATIVE);

                        positiveButton.setOnClickListener(new View.OnClickListener() {
                            @Override
                            public void onClick(View v) {
                                if(txtTaskName.getText().toString().isEmpty()){
                                    inputTaskName.setError("Task Name Required");
                                }else if(selectedVideoPath.isEmpty()){
                                    Toast.makeText(getActivity(), "Please select a video first!", Toast.LENGTH_SHORT).show();
                                }else if(txtTaskDuration.getText().toString().isEmpty()){
                                    inputTaskDuration.setError("Task Duration Required");
                                }else{
                                    createTask(txtTaskName.getText().toString(),selectedVideoPath,txtTaskDuration.getText().toString(),String.valueOf(spinner.getSelectedItem()),dialog);
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

        if(tasks==null){
            getAdminTasksList();
        }else{
            prepareData();
        }

    }

    private void getAdminTasksList() {
        final ProgressDialog progressDialog=new ProgressDialog(getActivity());
        progressDialog.setTitle("Loading Tasks");
        progressDialog.setMessage("please wait...");
        progressDialog.setCancelable(false);
        progressDialog.show();
        Utils.getRetrofitService().getAdminTasksList().enqueue(new Callback<AdminTasksListResponse>() {
            @Override
            public void onResponse(Call<AdminTasksListResponse> call, final Response<AdminTasksListResponse> response) {
                getActivity().runOnUiThread(new Runnable() {
                    @Override
                    public void run() {
                        if(response.body().isSuccess()){
                            progressDialog.dismiss();
                            tasks=response.body().getData();
                            prepareData();
                        }else{
                            progressDialog.dismiss();
                            Toast.makeText(getActivity(), "Something went wrong. Please try again.", Toast.LENGTH_LONG).show();
                        }
                    }
                });
            }

            @Override
            public void onFailure(Call<AdminTasksListResponse> call, Throwable t) {
                getActivity().runOnUiThread(new Runnable() {
                    @Override
                    public void run() {
                        progressDialog.dismiss();
                        Toast.makeText(getActivity(), "Server did not respond. Please try again.", Toast.LENGTH_LONG).show();
                    }
                });
            }
        });
    }

    private void prepareData(){
        recyclerView.setAdapter(new AdminTasksAdapter(tasks));
    }

    private void showFileChooser() {
        Intent intent = new Intent(Intent.ACTION_GET_CONTENT);
        intent.setType("video/mp4");
        intent.addCategory(Intent.CATEGORY_OPENABLE);


        try {
            startActivityForResult(
                    Intent.createChooser(intent, "Select a Video to Upload"),
                    SELECT_VIDEO);
        } catch (android.content.ActivityNotFoundException ex) {
            // Potentially direct the user to the Market with a Dialog
            Toast.makeText(getActivity(), "Please install a File Manager.",
                    Toast.LENGTH_SHORT).show();
        }
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if(resultCode==RESULT_OK){
            if(requestCode==SELECT_VIDEO){
                selectedVideoPath=getAbsolutePath(getActivity(),data.getData());
                txtVideoUpload.setText(selectedVideoPath);
                Log.d(tag,"Path: "+selectedVideoPath);
            }
        }
    }

    @SuppressLint("NewApi")
    public static String getAbsolutePath(final Context context, final Uri uri) {

        final boolean isKitKat = Build.VERSION.SDK_INT >= Build.VERSION_CODES.KITKAT;

// DocumentProvider
        if (isKitKat && DocumentsContract.isDocumentUri(context, uri)) {
            // ExternalStorageProvider
            if (isExternalStorageDocument(uri)) {
                final String docId = DocumentsContract.getDocumentId(uri);
                final String[] split = docId.split(":");
                final String type = split[0];

                if ("primary".equalsIgnoreCase(type)) {
                    return Environment.getExternalStorageDirectory() + "/" + split[1];
                }
            }
            // DownloadsProvider
            else if (isDownloadsDocument(uri)) {

                final String id = DocumentsContract.getDocumentId(uri);
                final Uri contentUri = ContentUris.withAppendedId(
                        Uri.parse("content://downloads/public_downloads"), Long.valueOf(id));

                return getDataColumn(context, contentUri, null, null);
            }
            // MediaProvider
            else if (isMediaDocument(uri)) {
                final String docId = DocumentsContract.getDocumentId(uri);
                final String[] split = docId.split(":");
                final String type = split[0];

                Uri contentUri = null;
                if ("image".equals(type)) {
                    contentUri = MediaStore.Images.Media.EXTERNAL_CONTENT_URI;
                } else if ("video".equals(type)) {
                    contentUri = MediaStore.Video.Media.EXTERNAL_CONTENT_URI;
                } else if ("audio".equals(type)) {
                    contentUri = MediaStore.Audio.Media.EXTERNAL_CONTENT_URI;
                }

                final String selection = "_id=?";
                final String[] selectionArgs = new String[] {
                        split[1]
                };

                return getDataColumn(context, contentUri, selection, selectionArgs);
            }
        }
// MediaStore (and general)
        else if ("content".equalsIgnoreCase(uri.getScheme())) {

            // Return the remote address
            if (isGooglePhotosUri(uri))
                return uri.getLastPathSegment();

            return getDataColumn(context, uri, null, null);
        }
// File
        else if ("file".equalsIgnoreCase(uri.getScheme())) {
            return uri.getPath();
        }

        return null;
    }


    public static String getDataColumn(Context context, Uri uri, String selection,
                                       String[] selectionArgs) {

        Cursor cursor = null;
        final String column = "_data";
        final String[] projection = {
                column
        };

        try {
            cursor = context.getContentResolver().query(uri, projection, selection, selectionArgs,
                    null);
            if (cursor != null && cursor.moveToFirst()) {
                final int index = cursor.getColumnIndexOrThrow(column);
                return cursor.getString(index);
            }
        } finally {
            if (cursor != null)
                cursor.close();
        }
        return null;
    }

    public static boolean isExternalStorageDocument(Uri uri) {
        return "com.android.externalstorage.documents".equals(uri.getAuthority());
    }

    public static boolean isDownloadsDocument(Uri uri) {
        return "com.android.providers.downloads.documents".equals(uri.getAuthority());
    }


    public static boolean isMediaDocument(Uri uri) {
        return "com.android.providers.media.documents".equals(uri.getAuthority());
    }

    public static boolean isGooglePhotosUri(Uri uri) {
        return "com.google.android.apps.photos.content".equals(uri.getAuthority());
    }

    private void createTask(final String taskName,final String videoPath,final String taskDuration,final String notificationDuration,final Dialog dialog) {
        final ProgressDialog progressDialog=new ProgressDialog(getActivity());
        progressDialog.setTitle("Uploading Video");
        progressDialog.setMessage("Please Wait...");
        progressDialog.setCancelable(false);
        progressDialog.show();

        new AsyncTask<Void,Void,Void>(){
            private Map result;
            @Override
            protected void onPreExecute() {
                super.onPreExecute();
                Log.d(tag,"Video Upload Started");
                Toast.makeText(getActivity(), "Video Upload Started.", Toast.LENGTH_SHORT).show();
            }

            @Override
            protected Void doInBackground(Void... params) {
                try {
                    result=Utils.getCloudinary().uploader().upload(selectedVideoPath, ObjectUtils.asMap("resource_type", "video","public_id",taskName));
                    Log.d(tag,"Result: "+result);

                    selectedVideoUrl=(String)result.get("secure_url");

                }catch (Exception e){
                    Log.d(tag,"Exception in uploading file: "+e.toString());

                }
                return null;
            }

            @Override
            protected void onPostExecute(Void aVoid) {
                super.onPostExecute(aVoid);
                Toast.makeText(getActivity(), "Finished Video Upload", Toast.LENGTH_SHORT).show();
                getActivity().runOnUiThread(new Runnable() {
                    @Override
                    public void run() {
                        progressDialog.dismiss();
                        progressDialog.setTitle("Creating Task");
                        progressDialog.setMessage("Please Wait...");
                        progressDialog.show();
                        String duration=notificationDuration.replace(" min","");
                        Utils.getRetrofitService().createTask(new Task(taskName,selectedVideoUrl,taskDuration,"Not Completed","Click To Assign",notificationDuration,Utils.getTimeStamp())).enqueue(new Callback<CreateTaskResponse>() {
                            @Override
                            public void onResponse(Call<CreateTaskResponse> call,final Response<CreateTaskResponse> response) {
                                getActivity().runOnUiThread(new Runnable() {
                                    @Override
                                    public void run() {
                                        Log.d(tag,"Got response success:"+response.body().isSuccess());
                                        if(response.body().isSuccess()){
                                            Toast.makeText(getActivity(), "Task Created Successfully.", Toast.LENGTH_SHORT).show();
                                            progressDialog.dismiss();
                                            dialog.dismiss();
                                            getAdminTasksList();
                                        }else{
                                            progressDialog.dismiss();
                                            Toast.makeText(getActivity(), response.body().getData(), Toast.LENGTH_LONG).show();
                                        }
                                    }
                                });
                            }

                            @Override
                            public void onFailure(Call<CreateTaskResponse> call, Throwable t) {
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
                });
            }
        }.execute();
    }
}
