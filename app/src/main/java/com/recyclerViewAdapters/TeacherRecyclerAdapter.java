package com.recyclerViewAdapters;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.CheckBox;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.tutionapp.R;

import java.util.List;

public class TeacherRecyclerAdapter extends RecyclerView.Adapter<TeacherRecyclerAdapter.Viewholder> {

    private List<String> date, titles, subject;
    private OnTask onTask;

    public TeacherRecyclerAdapter(List<String> date, List<String> titles, List<String> subject,OnTask onTask){
        this.date = date;
        this.titles = titles;
        this.subject = subject;
        this.onTask = onTask;
    }

    @NonNull
    @Override
    public Viewholder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.teacher_task_design,parent,false);
        TeacherRecyclerAdapter.Viewholder viewholders = new TeacherRecyclerAdapter.Viewholder(view, onTask);
        return viewholders;
    }

    @Override
    public void onBindViewHolder(@NonNull Viewholder holder, int position) {
        holder.teacher_date.setText(date.get(position));
        holder.teacher_title.setText(titles.get(position));
        holder.teacher_subject.setText(subject.get(position));
    }

    @Override
    public int getItemCount() {
        return titles.size();
    }

    class Viewholder extends RecyclerView.ViewHolder{

        TextView teacher_date, teacher_title, teacher_subject;
        CheckBox checkBox;

        public Viewholder(@NonNull View itemView, OnTask onTask) {
            super(itemView);
            checkBox = itemView.findViewById(R.id.teacher_check);
            teacher_date = itemView.findViewById(R.id.teacher_date);
            teacher_title = itemView.findViewById(R.id.teacher_title);
            teacher_subject = itemView.findViewById(R.id.teacher_subject);
        }
    }

    @FunctionalInterface
    public interface OnTask{
        void onClick(int position);
    }
}
