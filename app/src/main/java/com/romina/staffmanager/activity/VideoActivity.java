package com.romina.staffmanager.activity;

import android.app.ProgressDialog;
import android.media.MediaPlayer;
import android.net.Uri;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.widget.MediaController;
import android.widget.Toast;
import android.widget.VideoView;

import com.romina.staffmanager.R;
import com.romina.staffmanager.adapter.AdminTasksAdapter;

import static com.romina.staffmanager.Utils.tag;

public class VideoActivity extends AppCompatActivity {
    private String video_url="";
    private VideoView videoView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_video);
        video_url=getIntent().getStringExtra(AdminTasksAdapter.VIDEO_URL);
        initializeViews();
    }

    private void initializeViews() {
        videoView=(VideoView)findViewById(R.id.videoView);
        final ProgressDialog progressDialog=new ProgressDialog(VideoActivity.this);
        progressDialog.setTitle("Fetching Video");
        progressDialog.setMessage("Please wait...");
        progressDialog.setCancelable(false);
        progressDialog.show();

        try{
            MediaController mediaController=new MediaController(VideoActivity.this);
            mediaController.setAnchorView(videoView);
            Uri videoUri=Uri.parse(video_url);
            videoView.setMediaController(mediaController);
            videoView.setVideoURI(videoUri);
        }catch(Exception e){
            Log.d(tag,"Exception in playing video: "+e.toString());
            Toast.makeText(this, "Something went wrong. Please try again.", Toast.LENGTH_SHORT).show();
            progressDialog.dismiss();
            finish();
        }

        videoView.requestFocus();
        videoView.setOnPreparedListener(new MediaPlayer.OnPreparedListener() {
            @Override
            public void onPrepared(MediaPlayer mp) {
                progressDialog.dismiss();
                videoView.start();
            }
        });
    }
}
