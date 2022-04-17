package com.fileViewer;

import androidx.appcompat.app.AppCompatActivity;

import android.net.Uri;
import android.os.Bundle;
import android.widget.MediaController;
import android.widget.VideoView;

import com.tutionapp.R;

public class VideoViewer extends AppCompatActivity {

    VideoView videoViewer;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_video_viewer);

        videoViewer = findViewById(R.id.videoViewer);

        String video = getIntent().getStringExtra("video");

        videoViewer.setVideoURI(Uri.parse(video));
        MediaController mediaController = new MediaController(this);
        mediaController.setAnchorView(videoViewer);
        videoViewer.setMediaController(mediaController);
        videoViewer.start();
    }

    @Override
    public void onBackPressed() {
        super.onBackPressed();
        finish();
    }
}