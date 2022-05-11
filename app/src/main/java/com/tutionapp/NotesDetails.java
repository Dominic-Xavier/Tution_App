package com.tutionapp;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;

import androidx.activity.result.ActivityResult;
import androidx.activity.result.ActivityResultCallback;
import androidx.activity.result.ActivityResultLauncher;
import androidx.activity.result.contract.ActivityResultContracts;
import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.fragment.app.Fragment;

import android.speech.RecognizerIntent;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;

import com.common.AlertOrToastMsg;
import com.google.android.material.textfield.TextInputEditText;
import com.recyclerViewAdapters.RecyclerViewAdapter;

import java.util.ArrayList;
import java.util.List;


public class NotesDetails extends Fragment  {

    Toolbar toolbar;
    AlertOrToastMsg alertOrToastMsg;
    TextInputEditText title, notes;
    private static final List<String> titleList = new ArrayList<>();
    private static final List<String> notesList = new ArrayList<>();
    GetNotesData getData;
    Intent intent;

    public NotesDetails() {
        // Required empty public constructor
    }

    ActivityResultLauncher<Intent> launcher = registerForActivityResult(
        new ActivityResultContracts.StartActivityForResult(),
        result -> {
            Intent intent = result.getData();
            String text = intent.getStringArrayListExtra(RecognizerIntent.EXTRA_RESULTS).get(0);
            String resultText = notes.getText().toString()+" "+text;
            notes.setText(resultText);
        });

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setHasOptionsMenu(true);
    }

    @Override
    public void onCreateOptionsMenu(@NonNull Menu menu, @NonNull MenuInflater inflater) {
        super.onCreateOptionsMenu(menu, inflater);
        inflater.inflate(R.menu.notes, menu);
    }

    @Override
    public boolean onOptionsItemSelected(@NonNull MenuItem item) {
        switch (item.getItemId()){
            case R.id.done:
                String getTitle = title.getText().toString();
                String getNotes = notes.getText().toString();
                titleList.add(getTitle);
                notesList.add(getNotes);
                getData.getNotesData(titleList, notesList);
            break;

            case R.id.mic:
                intent = new Intent(RecognizerIntent.ACTION_RECOGNIZE_SPEECH);
                intent.putExtra(RecognizerIntent.EXTRA_LANGUAGE_MODEL, RecognizerIntent.LANGUAGE_MODEL_FREE_FORM);
                intent.putExtra(RecognizerIntent.EXTRA_PROMPT, "Start Speaking");
                launcher.launch(intent);
            break;
        }
        return super.onOptionsItemSelected(item);
    }

    @Override
    public void onAttach(@NonNull Context context) {
        super.onAttach(context);
        Activity activity = (Activity) context;
        getData =(GetNotesData) activity;
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.fragment_notes_details, container, false);
        alertOrToastMsg = new AlertOrToastMsg(view.getContext());
        AppCompatActivity compatActivity = (AppCompatActivity) getActivity();
        toolbar = view.findViewById(R.id.toolBar);
        toolbar.setTitle("Notes");
        compatActivity.setSupportActionBar(toolbar);
        title = view.findViewById(R.id.notesTitle);
        notes = view.findViewById(R.id.notesDescription);
        return view;
    }

    @FunctionalInterface
    interface GetNotesData{
        void getNotesData(List<String> title, List<String> notes);
    }
}