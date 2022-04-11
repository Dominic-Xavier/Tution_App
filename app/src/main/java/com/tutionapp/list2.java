package com.tutionapp;

import android.content.Intent;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.widget.AppCompatButton;
import androidx.fragment.app.Fragment;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import org.jetbrains.annotations.NotNull;

/**
 * A simple {@link Fragment} subclass.
 * Use the {@link list2#} factory method to
 * create an instance of this fragment.
 */

public class list2 extends Fragment {

    private AppCompatButton assignTask, downloads;
    Intent intent;

    public list2() {
        // Required empty public constructor
    }

    @Override
    public void onViewCreated(@NonNull @NotNull View view, @Nullable @org.jetbrains.annotations.Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        assignTask = getView().findViewById(R.id.assignTask);
        downloads = getView().findViewById(R.id.downloads);

        assignTask.setOnClickListener((v)-> {
            intent = new Intent(getActivity(), AllStudentsList.class);
            intent.putExtra("keyword", "assignTask");
            startActivity(intent);
        });

        downloads.setOnClickListener((v) -> {
            intent = new Intent(getActivity(), StudyMaterials.class);
            startActivity(intent);
        });
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.teacher_list2, container, false);
    }
}