package com.fileViewer;

import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.AppCompatImageView;

import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.net.Uri;
import android.os.Bundle;
import android.view.View;
import android.widget.ProgressBar;

import com.common.AlertOrToastMsg;
import com.ortiz.touchview.TouchImageView;
import com.squareup.picasso.Callback;
import com.squareup.picasso.Picasso;
import com.tutionapp.R;

import java.io.IOException;
import java.net.MalformedURLException;
import java.net.URL;

public class ImageViewer extends AppCompatActivity {

    TouchImageView imageView;
    ProgressBar progressBar;
    AlertOrToastMsg alertOrToastMsg = new AlertOrToastMsg(this);

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_image_viewer);

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