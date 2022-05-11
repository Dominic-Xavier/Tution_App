package com.recyclerViewAdapters;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.tutionapp.R;

import org.jetbrains.annotations.NotNull;

import java.util.List;

import androidx.annotation.NonNull;
import androidx.appcompat.widget.LinearLayoutCompat;
import androidx.recyclerview.widget.RecyclerView;

public class RecyclerViewAdapter extends RecyclerView.Adapter<RecyclerViewAdapter.Viewholder> {

    Context context;
    private List<String> students, studentsPhoneNumbers;

    private OnStudentListner onStudentListner;

    public RecyclerViewAdapter(Context context, List<String> students, List<String> studentsPhoneNumbers, OnStudentListner onStudentListner){
        this.context = context;
        this.students = students;
        this.studentsPhoneNumbers = studentsPhoneNumbers;
        this.onStudentListner = onStudentListner;
    }

    //public abstract void onBindViewHolders(Viewholders holder, int position);
    //public abstract RecyclerView.ViewHolder setViewHolder(ViewGroup parent);

    @NonNull
    @NotNull
    @Override
    public Viewholder onCreateViewHolder(@NonNull @NotNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.recycler_view_design,parent,false);
        Viewholder viewholders = new Viewholder(view, onStudentListner);
        return viewholders;
    }

    @Override
    public void onBindViewHolder(@NonNull @NotNull RecyclerViewAdapter.Viewholder holder, int position) {
        String student = students.get(position);
        holder.studentName.setText(student);
        holder.phoneNumber.setText(studentsPhoneNumbers.get(position));
    }

    @Override
    public int getItemCount() {
        return students.size();
    }

    public static class Viewholder extends RecyclerView.ViewHolder{

        TextView studentName, phoneNumber;
        OnStudentListner onStudentListner;
        LinearLayoutCompat linearLayoutCompat;

        public Viewholder(@NonNull @NotNull View itemView, OnStudentListner onStudentListner) {
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
    public interface OnStudentListner {
        void onClick(int position);
    }
}
