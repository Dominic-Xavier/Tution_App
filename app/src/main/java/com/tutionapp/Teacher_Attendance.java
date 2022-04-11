package com.tutionapp;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.os.Bundle;
import android.os.Handler;
import android.view.View;
import android.widget.Button;
import android.widget.ProgressBar;

import com.common.AlertOrToastMsg;
import com.dataHelper.AttendanceDetails;
import com.dataHelper.CatcheData;
import com.dataHelper.Node;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.recyclerViewAdapters.Attendance;
import com.recyclerViewAdapters.AttendanceRecyclerViewAdapter;

import org.jetbrains.annotations.NotNull;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class Teacher_Attendance extends AppCompatActivity implements Attendance {

    private RecyclerView displayAttendance;
    private Button viewAttendance, Submit;
    private AttendanceRecyclerViewAdapter attendanceAdapter;
    private Handler handler;
    private static final DatabaseReference reference = FirebaseDatabase.getInstance("https://tutor-project-1cc32-default-rtdb.firebaseio.com/")
            .getReference().child(Node.Institutes.toString());
    private static Map<String, List<String>> map = new HashMap<>();
    private List<String> studentIDs;
    private AlertOrToastMsg alertOrToastMsg;
    private ProgressBar progressBar;

    public Map<String, Object> getPresent() {
        return present;
    }

    public void setPresent(Map<String, Object> present) {
        this.present = present;
    }

    public Map<String, Object> getAbsent() {
        return absent;
    }

    public void setAbsent(Map<String, Object> absent) {
        this.absent = absent;
    }

    public Map<String, Object> getLate() {
        return late;
    }

    public void setLate(Map<String, Object> late) {
        this.late = late;
    }

    private Map<String, Object> present, absent, late;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_teacher_attendance);

        alertOrToastMsg = new AlertOrToastMsg(this);
        progressBar = findViewById(R.id.att_progress);

        displayAttendance = findViewById(R.id.attendance_recycler);
        viewAttendance = findViewById(R.id.att_view_attendance);
        Submit = findViewById(R.id.att_submit);

        handler = new Handler();
        handler.postAtFrontOfQueue(() -> {
            progressBar.setVisibility(View.VISIBLE);
            String insID = CatcheData.getData("Ins_id", this);
            getNamesAndIDs(insID);
        });

        viewAttendance.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                //Code for viewing attendance
            }
        });

        Submit.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                /*if(getPresent()!=null)
                    alertOrToastMsg.showAlert("Present", getPresent().toString());
                if(getAbsent()!=null)
                    alertOrToastMsg.showAlert("Absent", getAbsent().toString());
                if(getLate()!=null)
                    alertOrToastMsg.showAlert("Late", getLate().toString());*/
                if(getPresent()!=null && getAbsent()!=null && getLate()!=null){
                    AttendanceDetails attendanceDetails = new AttendanceDetails(Teacher_Attendance.this);
                    attendanceDetails.addAttendance(getPresent(), getAbsent(), getLate());
                }
                else
                    alertOrToastMsg.showAlert("Attendance", "Kindly take attendance for all students");
            }
        });
    }

    public Map<String, List<String>> getNamesAndIDs(String ins_id){

        reference.child(ins_id).child(Node.Student.toString()).child("")
                .addListenerForSingleValueEvent(new ValueEventListener() {

                    @Override
                    public void onDataChange(@NonNull @NotNull DataSnapshot dataSnapshot) {
                        List<String> studentNames = new ArrayList<>();
                        studentIDs = new ArrayList<>();
                        System.out.println("Student Lists:-"+dataSnapshot.getValue());
                        for(DataSnapshot snapshot : dataSnapshot.getChildren()){
                            String Names = snapshot.child("StudentName").getValue(String.class);
                            String student_ID = snapshot.child("Student_ID").getValue(String.class);
                            studentNames.add(Names);
                            studentIDs.add(student_ID);
                        }
                        attendanceAdapter = new AttendanceRecyclerViewAdapter(studentNames, studentIDs, Teacher_Attendance.this);

                        displayAttendance.setAdapter(attendanceAdapter);
                        displayAttendance.setLayoutManager(new LinearLayoutManager(Teacher_Attendance.this));

                        progressBar.setVisibility(View.GONE);
                    }

                    @Override
                    public void onCancelled(@NonNull @NotNull DatabaseError databaseError) {
                        alertOrToastMsg.ToastMsg(databaseError.toString());
                    }
                });
        System.out.println("Map are:- "+map);
        return map;
    }

    @Override
    public void onBackPressed() {
        super.onBackPressed();
        finish();
    }

    @Override
    public void getPresentStudents(Map<String, Object> present) {
        setPresent(present);
    }

    @Override
    public void getAbsentStudents(Map<String, Object> absent) {
        setAbsent(absent);
    }

    @Override
    public void getLateStudents(Map<String, Object> late) {
        setLate(late);
    }
}