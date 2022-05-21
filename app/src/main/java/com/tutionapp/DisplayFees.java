package com.tutionapp;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.widget.TableLayout;

public class DisplayFees extends AppCompatActivity {

    TableLayout tableLayout;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_display_fees);

        Intent intent = getIntent();
        intent.getSerializableExtra("");

        tableLayout = findViewById(R.id.displayFeesDetailsLayout);
        tableLayout.setColumnStretchable(0,true);
        tableLayout.setColumnStretchable(1,true);
        tableLayout.setColumnStretchable(2,true);

        TableLayout.LayoutParams Params = new TableLayout.LayoutParams(TableLayout.LayoutParams.WRAP_CONTENT, TableLayout.LayoutParams.WRAP_CONTENT);
        Params.setMargins(5, 5, 5, 5);


    }
}