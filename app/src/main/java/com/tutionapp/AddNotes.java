package com.tutionapp;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.AppCompatImageButton;
import androidx.appcompat.widget.Toolbar;
import androidx.recyclerview.widget.RecyclerView;

import android.graphics.Color;
import android.os.Bundle;
import android.os.Handler;
import android.os.Looper;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
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

public class AddNotes extends AppCompatActivity implements NotesDetails.GetNotesData{

    AppCompatImageButton imageButton;
    static RecyclerView recyclerView;
    AlertOrToastMsg alertOrToastMsg;
    FrameLayout frameLayout;
    boolean doubleBackToExitPressedOnce = false, ifPresent;
    Handler handler;
    NotesCRUDOperations notesCRUDOperations;
    static ProgressBar progressBar;
    static Map<String, Object> map = new HashMap<>();
    Toolbar toolbar;

    public static MenuItem getDelete() {
        return delete;
    }

    public static MenuItem getSelectAll() {
        return selectAll;
    }

    static MenuItem delete, selectAll;

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

        int pos = RecyclerView.NO_POSITION;

        imageButton = findViewById(R.id.newNotes);
        recyclerView = findViewById(R.id.notesRecyclerView);
        alertOrToastMsg = new AlertOrToastMsg(this);
        frameLayout = findViewById(R.id.fragment_container);
        progressBar = findViewById(R.id.notesProgress);
        toolbar = findViewById(R.id.notesTaskBar);

        setSupportActionBar(toolbar);

        notesCRUDOperations = new NotesCRUDOperations(AddNotes.this, CatcheData.getData("Ins_id", AddNotes.this), recyclerView, progressBar);

        handler = new Handler();
        handler.postAtFrontOfQueue(() -> {
            progressBar.setVisibility(View.VISIBLE);
            notesCRUDOperations.getNotes();
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
    public boolean onCreateOptionsMenu(Menu menu) {
        MenuInflater inflater = getMenuInflater();
        inflater.inflate(R.menu.remove_notes, menu);
        delete = menu.findItem(R.id.deleteNotes);
        selectAll = menu.findItem(R.id.selectAllNotes);
        return super.onCreateOptionsMenu(menu);
    }

    @Override
    public boolean onOptionsItemSelected(@NonNull MenuItem item) {
        switch (item.getItemId()){
            case R.id.deleteNotes:
                notesCRUDOperations.deleteNotes(NotesCRUDOperations.selectedItems);
                alertOrToastMsg.ToastMsg("Delete is Clicked");
            return true;

            case R.id.selectAllNotes:
                alertOrToastMsg.ToastMsg("Select all is Clicked");
            return true;
        }
        return super.onOptionsItemSelected(item);
    }

    @Override
    public boolean onPrepareOptionsMenu(Menu menu) {
        delete.setVisible(false);
        selectAll.setVisible(false);
        return super.onPrepareOptionsMenu(menu);
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
            finish();
            return;
        }
        alertOrToastMsg.ToastMsg("Press again to exit...!");

        this.doubleBackToExitPressedOnce = true;

        new Handler(Looper.getMainLooper()).postDelayed(() -> {
                doubleBackToExitPressedOnce=false;
        }, 2000);
    }

    @Override
    public void getNotesData(String title, String notes) {
        getSupportFragmentManager().beginTransaction().remove(getSupportFragmentManager().findFragmentById(R.id.fragment_container)).commit();
        frameLayout.setVisibility(View.GONE);
        recyclerView.setVisibility(View.GONE);
        progressBar.setVisibility(View.VISIBLE);
        imageButton.setClickable(true);
        imageButton.setEnabled(true);
        map.put("Description", notes);
        map.put("Title", title);
        notesCRUDOperations.addNotes(map);
        ifPresent = false;
    }
}