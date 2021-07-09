package com.owner;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.tutionapp.R;

import org.jetbrains.annotations.NotNull;

import java.util.ArrayList;
import java.util.List;
import java.util.Set;

import androidx.annotation.NonNull;
import androidx.appcompat.widget.LinearLayoutCompat;
import androidx.recyclerview.widget.RecyclerView;

public class RecyclerViewAdapter extends RecyclerView.Adapter<RecyclerViewAdapter.Viewholders> {

    private Context context;
    private List<String> students, studentsPhoneNumbers;

    private OnStudentListner onStudentListner;

    public RecyclerViewAdapter(Context context, List<String> students, List<String> studentsPhoneNumbers, OnStudentListner onStudentListner){
        this.context = context;
        this.students = students;
        this.studentsPhoneNumbers = studentsPhoneNumbers;
        this.onStudentListner = onStudentListner;
    }

    @NonNull
    @NotNull
    @Override
    public Viewholders onCreateViewHolder(@NonNull @NotNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.recycler_view_design,parent,false);
        Viewholders viewholders = new Viewholders(view, onStudentListner);
        return viewholders;
    }

    @Override
    public void onBindViewHolder(@NonNull @NotNull RecyclerViewAdapter.Viewholders holder, int position) {
        holder.studentName.setText(students.get(position));
        holder.phoneNumber.setText(studentsPhoneNumbers.get(position));
    }

    @Override
    public int getItemCount() {
        return students.size();
    }

    class Viewholders extends RecyclerView.ViewHolder{

        TextView studentName, phoneNumber;
        OnStudentListner onStudentListner;
        LinearLayoutCompat linearLayoutCompat;

        public Viewholders(@NonNull @NotNull View itemView, OnStudentListner onStudentListner) {
            super(itemView);
            studentName = itemView.findViewById(R.id.student_names_list);
            phoneNumber = itemView.findViewById(R.id.phoneNumber);
            linearLayoutCompat = itemView.findViewById(R.id.allStudentLinearLayout);
            this.onStudentListner = onStudentListner;
            linearLayoutCompat.setOnClickListener((v) -> {
                onStudentListner.onClick(getAdapterPosition());
            });
        }
    }

    @FunctionalInterface
    interface OnStudentListner{
        void onClick(int position);
    }
}
