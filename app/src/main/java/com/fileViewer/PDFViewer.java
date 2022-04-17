package com.fileViewer;

import androidx.appcompat.app.AppCompatActivity;

import android.net.Uri;
import android.os.Bundle;

import com.github.barteksc.pdfviewer.PDFView;
import com.tutionapp.R;

public class PDFViewer extends AppCompatActivity {

    PDFView pdfView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_pdfviewer);

        pdfView = findViewById(R.id.pdfViewer);

        String pdf = getIntent().getStringExtra("pdf");

        pdfView.fromUri(Uri.parse(pdf)).load();
    }

    @Override
    public void onBackPressed() {
        finish();
    }
}