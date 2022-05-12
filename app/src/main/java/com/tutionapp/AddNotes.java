package com.tutionapp;

import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.AppCompatImageButton;
import androidx.recyclerview.widget.RecyclerView;

import android.graphics.Color;
import android.os.Bundle;
import android.os.Handler;
import android.os.Looper;
import android.view.View;
import android.widget.FrameLayout;
import android.widget.ProgressBar;

import com.common.AlertOrToastMsg;
import com.dataHelper.CatcheData;
import com.dataHelper.NotesCRUDOperations;
import com.recyclerViewAdapters.RecyclerViewAdapter;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class AddNotes extends AppCompatActivity implements RecyclerViewAdapter.OnStudentListner, NotesDetails.GetNotesData{

    AppCompatImageButton imageButton;
    static RecyclerView recyclerView;
    AlertOrToastMsg alertOrToastMsg;
    FrameLayout frameLayout;
    boolean doubleBackToExitPressedOnce = false, ifPresent;
    Handler handler;
    NotesCRUDOperations notesCRUDOperations;
    static ProgressBar progressBar;
    static Map<String, Object> map = new HashMap<>();

    public static ProgressBar getProgressBar() {
        return progressBar;
    }

    public static RecyclerView getRecyclerView() {
        return recyclerView;
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_add_notes);

        imageButton = findViewById(R.id.newNotes);
        recyclerView = findViewById(R.id.notesRecyclerView);
        alertOrToastMsg = new AlertOrToastMsg(this);
        frameLayout = findViewById(R.id.fragment_container);
        progressBar = findViewById(R.id.notesProgress);

        notesCRUDOperations = new NotesCRUDOperations(AddNotes.this, CatcheData.getData("Ins_id", AddNotes.this), progressBar);

        handler = new Handler();
        handler.postAtFrontOfQueue(() -> {
            progressBar.setVisibility(View.VISIBLE);
            notesCRUDOperations.getNotes(recyclerView);
        });

        imageButton.setOnClickListener((v) -> {
            frameLayout.setVisibility(View.VISIBLE);
            recyclerView.setVisibility(View.GONE);
            getSupportFragmentManager().beginTransaction().add(R.id.fragment_container, new NotesDetails()).commit();
            imageButton.setClickable(false);
            imageButton.setEnabled(false);
            ifPresent = true;
        });
    }

    @Override
    public void onBackPressed() {
        if(ifPresent){
            frameLayout.setVisibility(View.GONE);
            recyclerView.setVisibility(View.VISIBLE);
            imageButton.setClickable(true);
            imageButton.setEnabled(true);
            getSupportFragmentManager().beginTransaction().remove(getSupportFragmentManager().findFragmentById(R.id.fragment_container)).commit();
            ifPresent = false;
        }
        if (doubleBackToExitPressedOnce) {
            super.onBackPressed();
            return;
        }
        alertOrToastMsg.ToastMsg("Press again to exit...!");

        this.doubleBackToExitPressedOnce = true;

        new Handler(Looper.getMainLooper()).postDelayed(() -> {
                doubleBackToExitPressedOnce=false;
        }, 2000);
    }

    @Override
    public void onClick(int position) {

    }

    @Override
    public void getNotesData(String title, String notes) {
        getSupportFragmentManager().beginTransaction().remove(getSupportFragmentManager().findFragmentById(R.id.fragment_container)).commit();
        frameLayout.setVisibility(View.GONE);
        recyclerView.setVisibility(View.GONE);
        progressBar.setVisibility(View.VISIBLE);
        imageButton.setClickable(true);
        imageButton.setEnabled(true);
        //AllStudentsList.recyclerView(AddNotes.this, title, notes, AddNotes.this::onClick, recyclerView);
        alertOrToastMsg.ToastMsg("Description "+notes);
        map.put("Description", notes);
        map.put("Title", title);
        notesCRUDOperations.addNotes(map, recyclerView);
        ifPresent = false;
    }
}