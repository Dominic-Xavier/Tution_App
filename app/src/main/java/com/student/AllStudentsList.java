package com.student;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.widget.Toast;

import com.common.AlertOrToastMsg;
import com.recyclerViewAdapters.RecyclerViewAdapter;
import com.dataHelper.CatcheData;
import com.dataHelper.Node;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.tutionapp.R;
import com.tutionapp.StudentDetailsActivity;
import com.tutionapp.Teacher_task;

import org.jetbrains.annotations.NotNull;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

public class AllStudentsList extends AppCompatActivity implements RecyclerViewAdapter.OnStudentListner {

    private static RecyclerView recyclerView;
    private static RecyclerViewAdapter recyclerViewAdapter;
    private static AlertOrToastMsg alertOrToastMsg;
    private static Map<String, List<String>> map = new HashMap<>();
    private static Map<String, List<String>> map1;
    private Intent intent;
    List<String> all_student_IDs;
    private static final DatabaseReference reference = FirebaseDatabase.getInstance("https://tutor-project-1cc32-default-rtdb.firebaseio.com/")
            .getReference().child(Node.Institutes.toString());

    @Override
    protected void onCreate(@Nullable @org.jetbrains.annotations.Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.all_students_list);

        String ins_id = CatcheData.getData("Ins_id", this);
        alertOrToastMsg = new AlertOrToastMsg(this);

        Map<String, List<String>> maps = getNamesAndPhoneNumbers(ins_id);
        Map<String, List<String>> maps1 = getMap();
        System.out.println("Get Map is "+maps1);
        if(maps.isEmpty())
            Toast.makeText(this,"Sorry Try Again...!",Toast.LENGTH_SHORT).show();
        List<String> names = maps.get("StudentNames");
        List<String> phoneNumbers = maps.get("StudentPhoneNumbers");
        all_student_IDs = maps.get("Student_IDs");

        recyclerView = findViewById(R.id.recyclerView);

        if(names!=null && phoneNumbers!=null){
            recyclerView(this, names, phoneNumbers, this::onClick, recyclerView);
        }
    }

    public static void recyclerView(Context context, List<String> names, List<String> phoneNumbers, RecyclerViewAdapter.OnStudentListner onStudentListner, RecyclerView recyclerView){
        recyclerViewAdapter = new RecyclerViewAdapter(context, names, phoneNumbers, onStudentListner);
        recyclerView.setAdapter(recyclerViewAdapter);
        recyclerView.setLayoutManager(new LinearLayoutManager(context));
    }

    public Map<String, List<String>> getNamesAndPhoneNumbers(String ins_id){

        reference.child(ins_id).child(Node.Student.toString()).child("")
                .addListenerForSingleValueEvent(new ValueEventListener() {
                @Override
                public void onDataChange(@NonNull @NotNull DataSnapshot dataSnapshot) {
                    List<String> studentNames = new ArrayList<>();
                    List<String> studentPhoneNumbers = new ArrayList<>();
                    List<String> studentIDs = new ArrayList<>();
                    System.out.println("Student Lists:-"+dataSnapshot.getValue());
                    for(DataSnapshot snapshot : dataSnapshot.getChildren()){
                        String Names = snapshot.child("StudentName").getValue(String.class);
                        String phone_Number = snapshot.child("PhoneNumber").getValue(String.class);
                        String student_ID = snapshot.child("Student_ID").getValue(String.class);
                        studentNames.add(Names);
                        studentPhoneNumbers.add(phone_Number);
                        studentIDs.add(student_ID);
                    }
                    map.put("StudentNames", studentNames);
                    map.put("StudentPhoneNumbers", studentPhoneNumbers);
                    map.put("Student_IDs", studentIDs);
                    System.out.println("Map is:- "+map);
                    setMap(map);
                    map1 = map;
                }

                @Override
                public void onCancelled(@NonNull @NotNull DatabaseError databaseError) {
                    alertOrToastMsg.ToastMsg(databaseError.toString());
                }
            });
        System.out.println("Map are:- "+map);
        System.out.println("New Map is:- "+map1);
        return map;
    }

    public void setMap(Map<String, List<String>> map){
        map1 = map;
    }

    public Map<String, List<String>> getMap(){
        return map1;
    }

    @Override
    public void onClick(int position) {
        String ids = all_student_IDs.get(position);
        //alertOrToastMsg.ToastMsg(ids);
        String task = getIntent().getStringExtra("keyword");
        if(task.equals("assignTask")){
            intent = new Intent(this, Teacher_task.class);
            intent.putExtra("studentID", ids);
            startActivity(intent);
        }
        else {
            intent = new Intent(this, StudentDetailsActivity.class);
            intent.putExtra("studentID", ids);
            startActivity(intent);
        }
    }

    @Override
    public void onBackPressed() {
        finish();
    }
}