package com.tutionapp;

import androidx.annotation.RequiresApi;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.content.DialogInterface;
import android.os.Build;
import android.os.Bundle;
import android.os.Handler;
import android.text.InputType;
import android.view.View;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.Toast;

import com.common.AlertOrToastMsg;
import com.dataHelper.TaskDetails;
import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.recyclerViewAdapters.TeacherRecyclerAdapter;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class Teacher_task extends AppCompatActivity implements TeacherRecyclerAdapter.OnTask{

    private FloatingActionButton actionButton, add,delete;
    private AlertDialog.Builder builder;
    private AlertDialog dialog;
    private LinearLayout linearLayout;
    private String txtdates, txttitle, txtDescription, stu_ID, txtSubject;
    private List<String> allDates, allTitles, allDescriptions, allSubjects;
    private TeacherRecyclerAdapter recyclerAdapter;
    private RecyclerView recyclerView;
    private AlertOrToastMsg alertOrToastMsg;
    private Animation fabOpen, fabClose, rotateFordward, rotateBackword;
    private boolean isOpen = false;
    private static final Map<String, Object> task = new HashMap<>();
    private TaskDetails taskDetails;
    private Handler handler;

    @RequiresApi(api = Build.VERSION_CODES.O)
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_teacher_task);

        handler = new Handler();
        handler.postAtFrontOfQueue(() -> {
            stu_ID = getIntent().getStringExtra("studentID");

        });

        actionButton = findViewById(R.id.floating_button);
        add = findViewById(R.id.addTask);
        delete = findViewById(R.id.deleteTask);
        recyclerView = findViewById(R.id.list_of_student_task);
        allDates = new ArrayList<>();
        allTitles = new ArrayList<>();
        allDescriptions = new ArrayList<>();
        allSubjects = new ArrayList<>();

        fabOpen = AnimationUtils.loadAnimation(this, R.anim.fab_open);
        fabClose = AnimationUtils.loadAnimation(this, R.anim.fab_close);

        rotateFordward = AnimationUtils.loadAnimation(this, R.anim.rotate_fordward);
        rotateBackword = AnimationUtils.loadAnimation(this, R.anim.rotate_backward);

        stu_ID = getIntent().getStringExtra("studentID");
        alertOrToastMsg = new AlertOrToastMsg(this);
        alertOrToastMsg.ToastMsg("Student ID is: "+stu_ID);

        actionButton.setOnClickListener((v) -> {
            animateFab();
        });

        add.setOnClickListener((v) -> {
            animateFab();
            linearLayout = new LinearLayout(Teacher_task.this);
                EditText txtDate = date();
                EditText txtTitle = title();
                EditText txtTextArea = textarea();
                EditText subject = subject();
                linearLayout.setOrientation(LinearLayout.VERTICAL);
                linearLayout.addView(txtDate);
                linearLayout.addView(txtTitle);
                linearLayout.addView(subject);
                linearLayout.addView(txtTextArea);

                builder = new AlertDialog.Builder(Teacher_task.this)
                        .setView(linearLayout)
                        .setTitle("Add Task")
                        .setPositiveButton("Ok", (DialogInterface dialogInterface, int i) -> {

                            txtdates = txtDate.getText().toString();
                            txttitle = txtTitle.getText().toString();
                            txtDescription = txtTextArea.getText().toString();
                            txtSubject = subject.getText().toString();

                            task.put("Date", txtdates);
                            task.put("Title", txttitle);
                            task.put("Description", txtDescription);
                            task.put("Subject", txtSubject);
                            taskDetails = new TaskDetails(Teacher_task.this, task);

                            if(!taskDetails.validformat(txtdates)){
                                Toast.makeText(this, "Invalid Date format", Toast.LENGTH_SHORT).show();
                                return;
                            }

                            if(txttitle.isEmpty() || txtDescription.isEmpty() || txtSubject.isEmpty()){
                                Toast.makeText(this, "Empty fields not allowed", Toast.LENGTH_SHORT).show();
                                return;
                            }

                            taskDetails.addTask(stu_ID);

                            allDates.add(txtdates);
                            allTitles.add(txttitle);
                            allDescriptions.add(txtDescription);
                            allSubjects.add(txtSubject);

                            recyclerAdapter = new TeacherRecyclerAdapter(allDates, allTitles, allSubjects, Teacher_task.this::onClick);
                            recyclerView.setAdapter(recyclerAdapter);
                            recyclerView.setLayoutManager(new LinearLayoutManager(Teacher_task.this));

                            linearLayout.removeAllViews();
                            linearLayout = null;
                        })
                        .setNegativeButton("Cancel", (DialogInterface dialogInterface, int i) -> {
                            linearLayout.removeAllViews();
                            linearLayout = null;
                        });
                dialog = builder.create();
                dialog.setCanceledOnTouchOutside(false);
                dialog.show();
        });

        delete.setOnClickListener((v) -> {
            animateFab();
            Toast.makeText(this, "Delete Task Clicked", Toast.LENGTH_SHORT).show();
        });
    }

    private EditText date(){
        EditText date = new EditText(this);
        date.setHint("DD-MM-YYYY");
        date.setTextSize(20);
        date.setInputType(InputType.TYPE_CLASS_DATETIME);
        return date;
    }

    private EditText title(){
        EditText title = new EditText(this);
        title.setHint("Title");
        title.setTextSize(20);
        return title;
    }

    private EditText textarea(){
        EditText textArea = new EditText(this);
        textArea.setHint("Description");
        textArea.setTextSize(20);
        return textArea;
    }

    private EditText subject(){
        EditText textArea = new EditText(this);
        textArea.setHint("Subject");
        textArea.setTextSize(20);
        return textArea;
    }

    @Override
    public void onBackPressed() {
        super.onBackPressed();
        finish();
    }

    @Override
    public void onClick(int position) {

    }

    private void animateFab(){
        if(isOpen){
            actionButton.startAnimation(rotateBackword);
            add.startAnimation(fabClose);
            delete.startAnimation(fabClose);
            add.setClickable(false);
            delete.setClickable(false);
            isOpen = false;
        }
        else{
            actionButton.startAnimation(rotateFordward);
            add.startAnimation(fabOpen);
            delete.startAnimation(fabOpen);
            add.setClickable(true);
            delete.setClickable(true);
            add.setVisibility(View.VISIBLE);
            delete.setVisibility(View.VISIBLE);
            isOpen = true;
        }
    }
}