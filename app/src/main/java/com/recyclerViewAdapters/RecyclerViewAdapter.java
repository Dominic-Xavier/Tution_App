package com.recyclerViewAdapters;

import android.content.Context;
import android.graphics.Color;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.tutionapp.R;

import org.jetbrains.annotations.NotNull;

import java.util.ArrayList;
import java.util.List;

import androidx.annotation.NonNull;
import androidx.appcompat.widget.LinearLayoutCompat;
import androidx.recyclerview.widget.RecyclerView;

public class RecyclerViewAdapter extends RecyclerView.Adapter<RecyclerViewAdapter.Viewholder> {

    Context context;
    private List<String> studentIDs, students, studentsPhoneNumbers;
    private static final List<String> selectedItems = new ArrayList<>();
    static boolean isSelected = false;

    private OnStudentListener onStudentListener;

    public RecyclerViewAdapter(Context context,List<String>studentIDs, List<String> students, List<String> studentsPhoneNumbers, OnStudentListener onStudentListener){
        this.context = context;
        this.studentIDs = studentIDs;
        this.students = students;
        this.studentsPhoneNumbers = studentsPhoneNumbers;
        this.onStudentListener = onStudentListener;
    }

    //public abstract void onBindViewHolders(Viewholders holder, int position);
    //public abstract RecyclerView.ViewHolder setViewHolder(ViewGroup parent);

    @NonNull
    @NotNull
    @Override
    public Viewholder onCreateViewHolder(@NonNull @NotNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.recycler_view_design,parent,false);
        Viewholder viewholders = new Viewholder(view, onStudentListener);
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

    public class Viewholder extends RecyclerView.ViewHolder{

        TextView studentName, phoneNumber;
        OnStudentListener onStudentListner;
        LinearLayoutCompat linearLayoutCompat;

        public Viewholder(@NonNull @NotNull View itemView, OnStudentListener onStudentListener) {
            super(itemView);

            studentName = itemView.findViewById(R.id.student_names_list);
            phoneNumber = itemView.findViewById(R.id.phoneNumber);
            linearLayoutCompat = itemView.findViewById(R.id.allStudentLinearLayout);
            this.onStudentListner = onStudentListener;

            selectedItems.removeAll(selectedItems);

            linearLayoutCompat.setOnLongClickListener(v -> {
                isSelected = true;
                if(selectedItems.contains(studentIDs.get(getAdapterPosition()))){
                    itemView.setBackgroundColor(Color.WHITE);
                    selectedItems.remove(studentIDs.get(getAdapterPosition()));
                }
                else{
                    itemView.setBackgroundColor(Color.GRAY);
                    selectedItems.add(studentIDs.get(getAdapterPosition()));
                }
                if(selectedItems.size()==0)
                    isSelected = false;
                onStudentListener.selectedData(selectedItems);
                return true;
            });

            linearLayoutCompat.setOnClickListener(v -> {
                if (getAdapterPosition() == RecyclerView.NO_POSITION)
                    return;
                if(isSelected){
                    if(selectedItems.contains(studentIDs.get(getAdapterPosition()))){
                        itemView.setBackgroundColor(Color.WHITE);
                        selectedItems.remove(studentIDs.get(getAdapterPosition()));
                    }
                    else{
                        itemView.setBackgroundColor(Color.GRAY);
                        selectedItems.add(studentIDs.get(getAdapterPosition()));
                    }
                }
                else
                    onStudentListner.onClick(getAdapterPosition());
                if(selectedItems.size()==0)
                    isSelected = false;
                onStudentListener.selectedData(selectedItems);
            });
        }
    }

    public interface OnStudentListener {
        void onClick(int position);
        void selectedData(List<String> selectedData);
    }
}
