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

import com.owner.AllStudentsList;

import org.jetbrains.annotations.NotNull;

/**
 * A simple {@link Fragment} subclass.
 * Use the {@link list1#newInstance} factory method to
 * create an instance of this fragment.
 */
public class list1 extends Fragment {

    private AppCompatButton allStudents, addSubject, feeDetails, addTeacher, studentPregress, attendence;

    // TODO: Rename parameter arguments, choose names that match
    // the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
    private static final String ARG_PARAM1 = "param1";
    private static final String ARG_PARAM2 = "param2";

    // TODO: Rename and change types of parameters
    private String mParam1;
    private String mParam2;

    public list1() {
        // Required empty public constructor
    }

    /**
     * Use this factory method to create a new instance of
     * this fragment using the provided parameters.
     *
     * @param param1 Parameter 1.
     * @param param2 Parameter 2.
     * @return A new instance of fragment list1.
     */
    // TODO: Rename and change types and number of parameters
    public static list1 newInstance(String param1, String param2) {
        list1 fragment = new list1();
        Bundle args = new Bundle();
        args.putString(ARG_PARAM1, param1);
        args.putString(ARG_PARAM2, param2);
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public void onViewCreated(@NonNull @NotNull View view, @Nullable @org.jetbrains.annotations.Nullable Bundle savedInstanceState) {
        allStudents = getView().findViewById(R.id.all_students);
        allStudents.setOnClickListener((v) -> {
            startActivity(new Intent(getActivity(), AllStudentsList.class));
        });

        addSubject = getView().findViewById(R.id.add_sub);
        addSubject.setOnClickListener((v)-> {
            startActivity(new Intent(getActivity(), AddSubject.class));
        });

        feeDetails = getView().findViewById(R.id.fee_details);
        feeDetails.setOnClickListener((v) -> {
            startActivity(new Intent(getActivity(), FeesDetails.class));
        });
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment

        return inflater.inflate(R.layout.teacher_list1, container, false);
    }
}