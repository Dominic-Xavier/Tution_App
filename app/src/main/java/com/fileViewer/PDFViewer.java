package com.fileViewer;

import androidx.appcompat.app.AppCompatActivity;

import android.net.Uri;
import android.os.AsyncTask;
import android.os.Bundle;
import android.view.View;
import android.widget.ProgressBar;

import com.common.AlertOrToastMsg;
import com.github.barteksc.pdfviewer.PDFView;
import com.github.barteksc.pdfviewer.listener.OnErrorListener;
import com.github.barteksc.pdfviewer.listener.OnLoadCompleteListener;
import com.tutionapp.R;

import java.io.BufferedInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.net.HttpURLConnection;
import java.net.URL;

import javax.net.ssl.HttpsURLConnection;

public class PDFViewer extends AppCompatActivity {

    PDFView pdfView;
    ProgressBar progressBar;
    AlertOrToastMsg alertOrToastMsg = new AlertOrToastMsg(this);

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_pdfviewer);

        pdfView = findViewById(R.id.pdfViewer);

        String pdfURL = getIntent().getStringExtra("pdf");

        new RetrivePDFfromUrl().execute(pdfURL);


        /*try {
            InputStream inputStream = null;
            URL url = new URL(pdfURL);
            HttpURLConnection httpURLConnection = (HttpURLConnection) url.openConnection();
            if (httpURLConnection.getResponseCode() == 200){
                inputStream = new BufferedInputStream(httpURLConnection.getInputStream());
                pdfView.fromStream(inputStream).load();
            }
        } catch (IOException e) {
            e.printStackTrace();
        }*/

        //pdfView.fromUri(Uri.parse(pdf)).load();
    }

    @Override
    public void onBackPressed() {
        finish();
    }

    class RetrivePDFfromUrl extends AsyncTask<String, Void, InputStream> {

        @Override
        protected InputStream doInBackground(String... strings) {
            InputStream inputStream = null;
            try {
                URL url = new URL(strings[0]);
                // below is the step where we are
                // creating our connection.
                HttpURLConnection urlConnection = (HttpsURLConnection) url.openConnection();
                if (urlConnection.getResponseCode() == 200) {
                    // response is success.
                    // we are getting input stream from url
                    // and storing it in our variable.
                    inputStream = new BufferedInputStream(urlConnection.getInputStream());
                }

            } catch (IOException e) {
                // this is the method
                // to handle errors.
                e.printStackTrace();
                return null;
            }
            return inputStream;
        }

        @Override
        protected void onPostExecute(InputStream inputStream) {
            pdfView.fromStream(inputStream).onLoad(new OnLoadCompleteListener() {
                @Override
                public void loadComplete(int nbPages) {
                    progressBar = findViewById(R.id.loadPDF);
                    progressBar.setVisibility(View.GONE);
                }
            }).onError(new OnErrorListener() {
                @Override
                public void onError(Throwable t) {
                    alertOrToastMsg.showAlert("Error", t.getMessage());
                }
            }).load();
        }
    }
}