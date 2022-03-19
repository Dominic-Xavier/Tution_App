package com.student;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.RecyclerView;
import androidx.swiperefreshlayout.widget.SwipeRefreshLayout;

import android.os.Bundle;
import android.os.Handler;
import android.view.View;
import android.widget.ProgressBar;

import com.common.AlertOrToastMsg;
import com.dataHelper.CatcheData;
import com.dataHelper.TaskDetails;
import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.recyclerViewAdapters.TeacherRecyclerAdapter;
import com.tuann.floatingactionbuttonexpandable.FloatingActionButtonExpandable;
import com.tutionapp.R;

import java.util.List;
import java.util.Map;

public class StudentTask extends AppCompatActivity {

    private Handler handler;
    private TaskDetails taskDetails;
    private RecyclerView recyclerView;
    private SwipeRefreshLayout swipeRefreshLayout;
    private static String Stu_ID;
    private ProgressBar progressBar;

    public static FloatingActionButtonExpandable getActionButton() {
        return actionButton;
    }

    public void setActionButton(FloatingActionButtonExpandable actionButton) {
        this.actionButton = actionButton;
    }

    private static FloatingActionButtonExpandable actionButton;
    private AlertOrToastMsg alertOrToastMsg = new AlertOrToastMsg(this);

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_student_task);

        recyclerView = findViewById(R.id.student_recycler);
        swipeRefreshLayout = findViewById(R.id.student_refresh);
        actionButton = findViewById(R.id.completed_Task);
        progressBar = findViewById(R.id.stu_progressBar);
        setActionButton(actionButton);

        handler = new Handler();
        handler.postAtFrontOfQueue(() -> {
            Stu_ID = CatcheData.getData("Stu_id",this);
            taskDetails = new TaskDetails(StudentTask.this, progressBar);
            progressBar.setVisibility(View.VISIBLE);
            taskDetails.getTaskDetails(Stu_ID, recyclerView);
        });

        actionButton.setOnClickListener((v) -> {
            List<String> ids = TaskDetails.getListOfSelectedIds();
            alertOrToastMsg.showAlert("List Of Ids", ids.toString());
            taskDetails = new TaskDetails(this);
            taskDetails.updateFinishedTask(Stu_ID, ids);
        });

        recyclerView.addOnScrollListener(new RecyclerView.OnScrollListener() {
            @Override
            public void onScrolled(@NonNull RecyclerView recyclerView, int dx, int dy) {
            super.onScrolled(recyclerView, dx, dy);
            if(dy>0)
                actionButton.collapse(true);
            else
                actionButton.expand(true);
            }
        });

        swipeRefreshLayout.setOnRefreshListener(() -> {
            progressBar.setVisibility(View.VISIBLE);
            taskDetails.getTaskDetails(Stu_ID, recyclerView);
            swipeRefreshLayout.setRefreshing(false);
        });
    }

    @Override
    public void onBackPressed() {
        super.onBackPressed();
        finish();
    }
}