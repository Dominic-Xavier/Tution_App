package com.student;

import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.widget.AppCompatButton;
import androidx.fragment.app.Fragment;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.tutionapp.R;

import org.jetbrains.annotations.NotNull;


public class StudentList1 extends Fragment {


    private AppCompatButton taskAssignedButton;

    public StudentList1() {
        // Required empty public constructor
    }

    @Override
    public void onViewCreated(@NonNull @NotNull View view, @Nullable @org.jetbrains.annotations.Nullable Bundle savedInstanceState) {
        taskAssignedButton = getView().findViewById(R.id.ViewTask);
        taskAssignedButton.setOnClickListener((v) -> {

        });
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {

        return inflater.inflate(R.layout.fragment_student_list1, container, false);
    }
}