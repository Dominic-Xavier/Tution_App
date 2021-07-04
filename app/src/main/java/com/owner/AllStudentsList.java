package com.owner;

import android.content.Intent;
import android.os.Bundle;

import com.alertOrToast.AlertOrToastMsg;
import com.tutionapp.R;
import com.tutionapp.TutionActivity;

import java.util.ArrayList;
import java.util.List;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

public class AllStudentsList extends AppCompatActivity implements RecyclerViewAdapter.OnStudentListner {

    RecyclerView recyclerView;
    RecyclerViewAdapter recyclerViewAdapter;
    List<String> studentNames = new ArrayList<>();
    List<String> studentPhoneNumbers = new ArrayList<>();
    AlertOrToastMsg alertOrToastMsg = new AlertOrToastMsg(this);

    @Override
    protected void onCreate(@Nullable @org.jetbrains.annotations.Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.all_students_list);

        recyclerView = findViewById(R.id.recyclerView);

        studentNames.add("Suresh");
        studentPhoneNumbers.add("9600066074");

        studentNames.add("Dominic");
        studentPhoneNumbers.add("9600066075");

        studentNames.add("Xavier");
        studentPhoneNumbers.add("9600066076");

        recyclerViewAdapter = new RecyclerViewAdapter(this, studentNames, studentPhoneNumbers, this::onClick);
        recyclerView.setAdapter(recyclerViewAdapter);
        recyclerView.setLayoutManager(new LinearLayoutManager(this));

    }

    @Override
    public void onClick(int position) {
        String Name = studentNames.get(position);
        alertOrToastMsg.ToastMsg("Clicked "+Name);
    }

    @Override
    public void onBackPressed() {
        finish();
    }
}
