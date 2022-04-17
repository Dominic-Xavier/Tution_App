package com.fileViewer;

import androidx.appcompat.app.AppCompatActivity;

import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.net.Uri;
import android.os.Bundle;

import com.common.AlertOrToastMsg;
import com.ortiz.touchview.TouchImageView;
import com.tutionapp.R;

import java.io.IOException;
import java.net.MalformedURLException;
import java.net.URL;

public class ImageViewer extends AppCompatActivity {

    TouchImageView imageView;
    AlertOrToastMsg alertOrToastMsg = new AlertOrToastMsg(this);

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_image_viewer);

        imageView = findViewById(R.id.imageViewer);

        String imageUri = getIntent().getStringExtra("image");
        alertOrToastMsg.showAlert("URL", imageUri);

        try {
            URL url = new URL(imageUri);
            Bitmap bmp = BitmapFactory.decodeStream(url.openConnection().getInputStream());
            imageView.setImageBitmap(bmp);
        } catch (Exception e) {
            //alertOrToastMsg.showAlert("Error Occurred", e.getMessage());
            e.printStackTrace();
        }


    }

    @Override
    public void onBackPressed() {
        super.onBackPressed();
        finish();
    }
}