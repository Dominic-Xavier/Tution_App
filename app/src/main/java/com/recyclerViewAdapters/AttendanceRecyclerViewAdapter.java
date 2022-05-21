package com.recyclerViewAdapters;

import android.content.Context;
import android.os.Build;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.RequiresApi;
import androidx.appcompat.widget.AppCompatRadioButton;
import androidx.appcompat.widget.AppCompatTextView;
import androidx.recyclerview.widget.RecyclerView;

import com.common.AlertOrToastMsg;
import com.common.DateFunctions;
import com.tutionapp.R;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;

public class AttendanceRecyclerViewAdapter extends RecyclerView.Adapter<AttendanceRecyclerViewAdapter.Viewholder> {

    private List<String> studentName, student_ID;
    private Attendance attendanceListener;
    private List<String> stuName = new ArrayList<>();
    private List<String> stuID = new ArrayList<>();
    private Map<String, Object> present = new HashMap<>();
    private Map<String, Object> absent = new HashMap<>();
    private Map<String, Object> late = new HashMap<>();

    public AttendanceRecyclerViewAdapter(List<String> studentName, List<String> student_ID, Attendance attendanceListener){
        this.studentName = studentName;
        this.student_ID = student_ID;
        this.attendanceListener = attendanceListener;
    }

    @NonNull
    @Override
    public Viewholder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.attendence_design, parent, false);
        AttendanceRecyclerViewAdapter.Viewholder viewholders = new AttendanceRecyclerViewAdapter.Viewholder(view);
        return viewholders;
    }

    @Override
    public int getItemCount() {
        return studentName.size();
    }

    @RequiresApi(api = Build.VERSION_CODES.O)
    @Override
    public void onBindViewHolder(@NonNull Viewholder holder, int position) {
        holder.date.setText(DateFunctions.getCurrentDate());
        String StudentName = studentName.get(position);
        String StudentID = student_ID.get(position);
        holder.studentName.setText(StudentName);
        holder.studentID.setText(StudentID);
        holder.radioGroup.setOnCheckedChangeListener(new RadioGroup.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(RadioGroup radioGroup, int i) {
                int id = radioGroup.getCheckedRadioButtonId();
                AppCompatRadioButton radioButton = holder.itemView.findViewById(id);
                String radiobuttonText = radioButton.getText().toString();
                switch (radiobuttonText.toLowerCase()){
                    case "present":
                        //Code for present
                        present.put(StudentID, StudentName);
                        absent.remove(StudentID);
                        late.remove(StudentID);
                        attendanceListener.getPresentStudents(present);
                    break;
                    case "absent":
                        //Code for absent
                        absent.put(StudentID, StudentName);
                        present.remove(StudentID);
                        late.remove(StudentID);
                        attendanceListener.getAbsentStudents(absent);
                    break;
                    case "late":
                        //Code for Late
                        late.put(StudentID, StudentName);
                        present.remove(StudentID);
                        absent.remove(StudentID);
                        attendanceListener.getLateStudents(late);
                    break;
                }
            }
        });
    }

    class Viewholder extends RecyclerView.ViewHolder {

        AppCompatTextView studentName, studentID, date;
        RadioGroup radioGroup;
        AppCompatRadioButton present, absent, late;

        public Viewholder(@NonNull View itemView) {
            super(itemView);

            studentName = itemView.findViewById(R.id.att_stu_name);
            studentID = itemView.findViewById(R.id.att_stu_ID);
            date = itemView.findViewById(R.id.att_stu_date);
            radioGroup = itemView.findViewById(R.id.att_stu_radioGroup);
            present = itemView.findViewById(R.id.att_stu_present);
            absent = itemView.findViewById(R.id.att_stu_absent);
            late = itemView.findViewById(R.id.att_stu_late);
        }
    }
}
