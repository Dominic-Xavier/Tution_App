package com.tutionapp;

import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.AppCompatImageButton;
import androidx.recyclerview.widget.RecyclerView;

import android.os.Bundle;
import android.os.Handler;
import android.os.Looper;
import android.view.View;
import android.widget.FrameLayout;

import com.common.AlertOrToastMsg;
import com.recyclerViewAdapters.RecyclerViewAdapter;

import java.util.List;

public class AddNotes extends AppCompatActivity implements RecyclerViewAdapter.OnStudentListner, NotesDetails.GetNotesData{

    AppCompatImageButton imageButton;
    RecyclerView recyclerView;
    AlertOrToastMsg alertOrToastMsg;
    FrameLayout frameLayout;
    boolean doubleBackToExitPressedOnce = false, ifPresent;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_add_notes);

        imageButton = findViewById(R.id.newNotes);
        recyclerView = findViewById(R.id.notesRecyclerView);
        alertOrToastMsg = new AlertOrToastMsg(this);
        frameLayout = findViewById(R.id.fragment_container);

        imageButton.setOnClickListener((v) -> {
            frameLayout.setVisibility(View.VISIBLE);
            recyclerView.setVisibility(View.GONE);
            getSupportFragmentManager().beginTransaction().add(R.id.fragment_container, new NotesDetails()).commit();
            imageButton.setClickable(false);
            ifPresent = true;
        });
    }

    @Override
    public void onBackPressed() {
        if(ifPresent){
            frameLayout.setVisibility(View.GONE);
            recyclerView.setVisibility(View.VISIBLE);
            imageButton.setClickable(true);
            getSupportFragmentManager().beginTransaction().remove(getSupportFragmentManager().findFragmentById(R.id.fragment_container)).commit();
            ifPresent = false;
        }
        if (doubleBackToExitPressedOnce) {
            super.onBackPressed();
            return;
        }

        this.doubleBackToExitPressedOnce = true;

        new Handler(Looper.getMainLooper()).postDelayed(() -> {
                doubleBackToExitPressedOnce=false;
        }, 2000);
    }

    @Override
    public void onClick(int position) {

    }

    @Override
    public void getNotesData(List<String> title, List<String> notes) {
        getSupportFragmentManager().beginTransaction().remove(getSupportFragmentManager().findFragmentById(R.id.fragment_container)).commit();
        frameLayout.setVisibility(View.GONE);
        recyclerView.setVisibility(View.VISIBLE);
        imageButton.setClickable(true);
        AllStudentsList.recyclerView(AddNotes.this, title, notes, AddNotes.this::onClick, recyclerView);
        ifPresent = false;
    }
}