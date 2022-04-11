package com.recyclerViewAdapters;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.CheckBox;
import android.widget.CompoundButton;
import android.widget.LinearLayout;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.student.StudentTask;
import com.tuann.floatingactionbuttonexpandable.FloatingActionButtonExpandable;
import com.tutionapp.R;
import com.tutionapp.Teacher_task;

import java.util.ArrayList;
import java.util.List;

public class TeacherRecyclerAdapter extends RecyclerView.Adapter<TeacherRecyclerAdapter.Viewholder> {

    private List<String> date, titles, subject, taskIDs;
    private OnTask onTask;
    public static boolean isChecked = false;
    private FloatingActionButtonExpandable actionButton;
    private FloatingActionButton floatingActionButton;
    private List<String> checkData = new ArrayList<>();
    private TopicListener topicListener;
    private static boolean isCheckBoxChecked = false;

    public TeacherRecyclerAdapter(List<String> date, List<String> titles, List<String> subject, List<String> taskIDs,
                                  OnTask onTask, TopicListener topicListener){
        this.date = date;
        this.titles = titles;
        this.subject = subject;
        this.onTask = onTask;
        this.taskIDs = taskIDs;
        this.topicListener = topicListener;
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
        final CheckBox checkBox = holder.checkBox;
        checkBox.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton compoundButton, boolean b) {
                isCheckBoxChecked = b;
                checkBox.setChecked(isCheckBoxChecked);
            }
        });
        checkBox.setOnCheckedChangeListener((CompoundButton compoundButton, boolean isChecked) -> {
            actionButton = StudentTask.getActionButton();
            floatingActionButton = Teacher_task.getActionButton();
            if(actionButton==null && floatingActionButton==null)
                return;
            if(isChecked){
                //checkBox.setChecked(true);
                checkData.add(taskIDs.get(position));
            }
            else{
                //checkBox.setChecked(false);
                checkData.remove(taskIDs.get(position));
            }
            if(actionButton!=null){
                if(checkData.size()!=0){
                    actionButton.setVisibility(View.VISIBLE);
                    actionButton.setClickable(true);
                } else {
                    actionButton.setVisibility(View.INVISIBLE);
                    actionButton.setClickable(false);
                }
            }
            topicListener.selectTopic(checkData);
        });
    }

    @Override
    public int getItemCount() {
        return titles.size();
    }

    class Viewholder extends RecyclerView.ViewHolder{

        TextView teacher_date, teacher_title, teacher_subject;
        CheckBox checkBox;
        LinearLayout linearLayout;

        public Viewholder(@NonNull View itemView, OnTask onTask) {
            super(itemView);
            checkBox = itemView.findViewById(R.id.teacher_check);
            teacher_date = itemView.findViewById(R.id.teacher_date);
            teacher_title = itemView.findViewById(R.id.teacher_title);
            teacher_subject = itemView.findViewById(R.id.teacher_subject);
            linearLayout = itemView.findViewById(R.id.teacher_layout);
            linearLayout.setOnClickListener((v) -> {
                onTask.onClick(getAdapterPosition());
            });
        }
    }

    @FunctionalInterface
    public interface OnTask{
        void onClick(int position);
    }
}
