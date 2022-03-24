package com.tutionapp;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.RecyclerView;

import android.content.Intent;
import android.os.Bundle;

import com.recyclerViewAdapters.RecyclerViewAdapter;
import com.dataHelper.CatcheData;
import com.student.StudentFeesDetails;

import java.util.List;
import java.util.Map;

public class FeesDetails extends AppCompatActivity {

    private RecyclerView recyclerView;
    private List<String> Stu_id, stu_names;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.student_fee_details);

    }

    @Override
    public void onBackPressed() {
        finish();
    }
}