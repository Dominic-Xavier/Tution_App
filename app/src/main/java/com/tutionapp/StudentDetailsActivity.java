package com.tutionapp;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;
import android.widget.TextView;

import com.common.AlertOrToastMsg;
import com.dataHelper.CatcheData;
import com.dataHelper.Node;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import org.jetbrains.annotations.NotNull;

public class StudentDetailsActivity extends AppCompatActivity {

    static TextView studentName, studentID, studentPHNO, studentClg_Scl, studentclass_degree, parentName, parentPhoneNumber;

    private AlertOrToastMsg alertOrToastMsg = new AlertOrToastMsg(this);

    private static DatabaseReference student_reference;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.student_details);

        student_reference = FirebaseDatabase.getInstance("https://tutor-project-1cc32-default-rtdb.firebaseio.com/").getReference()
                .child(Node.Institutes.toString()).child(CatcheData.getData("Ins_id", getApplicationContext())).child(Node.Student.toString());

        String studentIDs = getIntent().getStringExtra("studentID");

        studentName = findViewById(R.id.disp_student_name);
        studentID = findViewById(R.id.disp_student_id);
        studentPHNO = findViewById(R.id.disp_phone_number);
        studentClg_Scl = findViewById(R.id.disp_scl_clg_Name);
        studentclass_degree = findViewById(R.id.disp_class_degree);
        parentName = findViewById(R.id.disp_parent_Name);
        parentPhoneNumber = findViewById(R.id.disp_parent_Number);

        System.out.println("Student ID is:"+studentIDs);
        displayStuDetails(studentIDs);
    }

    private void displayStuDetails(String stu_id){
        student_reference.child(stu_id).addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull @NotNull DataSnapshot dataSnapshot) {

                String stu_id = dataSnapshot.child("Student_ID").getValue(String.class);
                String ClassName = dataSnapshot.child("ClassName").getValue(String.class);
                String PhoneNumber = dataSnapshot.child("PhoneNumber").getValue(String.class);
                String SchoolName = dataSnapshot.child("SchoolName").getValue(String.class);
                String StudentName = dataSnapshot.child("StudentName").getValue(String.class);

                studentName.setText(StudentName);
                studentID.setText(stu_id);
                studentPHNO.setText(PhoneNumber);
                studentClg_Scl.setText(SchoolName);
                studentclass_degree.setText(ClassName);
                parentName.setText("");
                parentPhoneNumber.setText("");
            }

            @Override
            public void onCancelled(@NonNull @NotNull DatabaseError databaseError) {
                alertOrToastMsg.showAlert("Error", databaseError.toString());
            }
        });
    }

    @Override
    public void onBackPressed() {
        super.onBackPressed();
        finish();
    }
}