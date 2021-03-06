package com.tutionapp;

import androidx.annotation.RequiresApi;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.content.Context;
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
import android.widget.ProgressBar;
import android.widget.Toast;

import com.common.AlertOrToastMsg;
import com.common.DateFunctions;
import com.dataHelper.TaskDetails;
import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.recyclerViewAdapters.TeacherRecyclerAdapter;
import com.recyclerViewAdapters.TopicListener;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class Teacher_task extends AppCompatActivity {

    private static FloatingActionButton actionButton, add,delete;
    private AlertDialog.Builder builder;
    private AlertDialog dialog;
    private LinearLayout linearLayout;
    private String txtdates, txttitle, txtDescription, stu_ID, txtSubject;
    private static TeacherRecyclerAdapter recyclerAdapter;
    private static RecyclerView recyclerView;
    private static AlertOrToastMsg alertOrToastMsg;
    private Animation fabOpen, fabClose, rotateFordward, rotateBackword;
    private boolean isOpen = false;
    private static final Map<String, Object> task = new HashMap<>();
    private TaskDetails taskDetails;
    private Handler handler;
    private ProgressBar progressBar;

    public static FloatingActionButton getActionButton() {
        return actionButton;
    }

    public void setActionButton(FloatingActionButton actionButton) {
        this.actionButton = actionButton;
    }

    @RequiresApi(api = Build.VERSION_CODES.O)
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_teacher_task);

        alertOrToastMsg = new AlertOrToastMsg(this);
        progressBar = findViewById(R.id.prgress_bar);
        actionButton = findViewById(R.id.floating_button);
        add = findViewById(R.id.addTask);
        delete = findViewById(R.id.deleteTask);

        recyclerView = findViewById(R.id.list_of_student_task);

        handler = new Handler();
        handler.postAtFrontOfQueue(() -> {
            stu_ID = getIntent().getStringExtra("studentID");
            taskDetails = new TaskDetails(Teacher_task.this, progressBar);
            progressBar.setVisibility(View.VISIBLE);
            taskDetails.getTaskDetails(stu_ID, recyclerView);
        });

        fabOpen = AnimationUtils.loadAnimation(this, R.anim.fab_open);
        fabClose = AnimationUtils.loadAnimation(this, R.anim.fab_close);

        rotateFordward = AnimationUtils.loadAnimation(this, R.anim.rotate_fordward);
        rotateBackword = AnimationUtils.loadAnimation(this, R.anim.rotate_backward);

        stu_ID = getIntent().getStringExtra("studentID");

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
                        task.put("Topic", txttitle);
                        task.put("Description", txtDescription);
                        task.put("Subject", txtSubject);
                        task.put("Status", "InProgress");
                        taskDetails = new TaskDetails(Teacher_task.this, task);

                        if(!DateFunctions.validateDateFormat(txtdates)){
                            Toast.makeText(this, "Invalid DateFunctions format", Toast.LENGTH_SHORT).show();
                            return;
                        }

                        if(txttitle.isEmpty() || txtDescription.isEmpty() || txtSubject.isEmpty()){
                            Toast.makeText(this, "Empty fields not allowed", Toast.LENGTH_SHORT).show();
                            return;
                        }

                        taskDetails.addTask(stu_ID, recyclerView);

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
            setActionButton(delete);
            List<String> list = TaskDetails.getListOfSelectedIds();
            if(list!=null){
                if(!list.isEmpty())
                    taskDetails.deleteTasks(list, stu_ID, recyclerView);
                else
                    alertOrToastMsg.ToastMsg("Please select a task...!");
            }
            else
                alertOrToastMsg.ToastMsg("Please select a task...!");
        });
    }

    public static void recyclerView(Context context, List<String> dates, List<String> allTitles, List<String> allSubjects, List<String> topicIDs,
                                    RecyclerView recyclerView, TeacherRecyclerAdapter.OnTask task, TopicListener topicListener){
        recyclerAdapter = new TeacherRecyclerAdapter(dates, allTitles, allSubjects,topicIDs, task, topicListener);
        recyclerView.setAdapter(recyclerAdapter);
        recyclerView.setLayoutManager(new LinearLayoutManager(context));
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