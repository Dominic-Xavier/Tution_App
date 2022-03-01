package com.tutionapp;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.RecyclerView;

import android.content.Intent;
import android.os.Bundle;

import com.recyclerViewAdapters.RecyclerViewAdapter;
import com.dataHelper.CatcheData;
import com.student.AllStudentsList;
import com.student.StudentFeesDetails;

import java.util.List;
import java.util.Map;

public class FeesDetails extends AppCompatActivity implements RecyclerViewAdapter.OnStudentListner {

    private RecyclerView recyclerView;
    private List<String> Stu_id, stu_names;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.student_fee_details);
        recyclerView = findViewById(R.id.student_details);

        Map<String, List<String>> map = new AllStudentsList().getNamesAndPhoneNumbers(CatcheData.getData("Ins_id", this));
        System.out.println("Value of map is:"+map);
        stu_names = map.get("StudentNames");
        Stu_id = map.get("Student_IDs");

        if(stu_names!=null && Stu_id!=null)
            AllStudentsList.recyclerView(this, stu_names, Stu_id, this::onClick, recyclerView);
    }

    @Override
    public void onClick(int position) {
        String studentId = Stu_id.get(position);
        Intent intent = new Intent(this, StudentFeesDetails.class);
        intent.putExtra("Stu_id", studentId);
        startActivity(intent);
        finish();
    }

    @Override
    public void onBackPressed() {
        finish();
    }
}