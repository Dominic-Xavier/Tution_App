package com.fileViewer;

import androidx.appcompat.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.view.WindowManager;
import android.widget.ProgressBar;

import com.common.AlertOrToastMsg;
import com.ortiz.touchview.TouchImageView;
import com.squareup.picasso.Callback;
import com.squareup.picasso.Picasso;
import com.tutionapp.R;

public class ImageViewer extends AppCompatActivity {

    TouchImageView imageView;
    ProgressBar progressBar;
    AlertOrToastMsg alertOrToastMsg = new AlertOrToastMsg(this);

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_image_viewer);

        getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN, WindowManager.LayoutParams.FLAG_FULLSCREEN);

        imageView = findViewById(R.id.imageViewer);
        progressBar = findViewById(R.id.loadingImage);

        String imageUri = getIntent().getStringExtra("image");

        Picasso.get().load(imageUri).into(imageView, new Callback() {
            @Override
            public void onSuccess() {
                progressBar.setVisibility(View.GONE);
            }

            @Override
            public void onError(Exception e) {
                alertOrToastMsg.showAlert("Error", e.getMessage());
            }
        });
    }

    @Override
    public void onBackPressed() {
        super.onBackPressed();
        finish();
    }
}