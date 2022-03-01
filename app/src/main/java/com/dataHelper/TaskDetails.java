package com.dataHelper;

import android.content.Context;
import android.os.Build;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.annotation.RequiresApi;

import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;

public class TaskDetails {

    private Context context;
    private static DatabaseReference studentReference = DatabaseLinks.baseReference;
    private static String insID;
    private static String topicID;
    private Map<String, Object> map;

    public TaskDetails(){
        insID = CatcheData.getData("Ins_id",context);
    }

    public TaskDetails(Context context, Map<String, Object> map){
        this.context = context;
        insID = CatcheData.getData("Ins_id",context);
        System.out.println("Ins_id is: "+insID);
        this.map = map;
    }

    public void addTask(String student_id){
        studentReference.child(insID).child(Node.Student.toString()).child(student_id)
                .addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {

                String value = null;
                if(dataSnapshot.hasChild("Task")){
                    for (DataSnapshot snapshot : dataSnapshot.getChildren()){
                        value = snapshot.child("Task").getKey();
                    }
                    if(value!=null){
                        String split = value.split("Topic_id_")[1];
                        System.out.println("List of values are: "+value);
                        System.out.println("List of values are: "+split);
                        int topicCount = Integer.parseInt(split)+1;
                        topicID = "Topic_id_"+topicCount;
                        System.out.println("New Topic id is "+topicID);
                    }
                    studentReference.child(insID).child(Node.Student.toString()).child(student_id).child("Task")
                        .child(topicID).updateChildren(map, (@Nullable DatabaseError databaseError, @NonNull DatabaseReference databaseReference) -> {
                        Toast.makeText(context, "values inserted successfully....!", Toast.LENGTH_SHORT).show();
                    });
                }
                else{
                    studentReference.child(insID).child(Node.Student.toString()).child(student_id).child("Task")
                        .child("Topic_id_1").setValue(map, (@Nullable DatabaseError databaseError, @NonNull DatabaseReference databaseReference) -> {
                        Toast.makeText(context, "values inserted successfully....!", Toast.LENGTH_SHORT).show();
                    });
                }
                topicID = "Topic_id_1";
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {
                Toast.makeText(context, databaseError.toString(), Toast.LENGTH_SHORT).show();
            }
        });
    }

    public Boolean validformat(String date) {
        Boolean b;
        try {
            Date d;
            final String Dateformat = "dd-mm-yyyy";
            DateFormat df = new SimpleDateFormat(Dateformat);
            df.setLenient(false);
            d = df.parse(date);
            String split[] = date.split("-");
            int dte = Integer.parseInt(split[0]);
            int month = Integer.parseInt(split[1]);
            int year = Integer.parseInt(split[2]);
            if (date.equals(df.format(d)) && dte<=31 && month<=12 && year<9999)
                b = true;
            else
                b = false;
        } catch (ParseException e) {
            b = false;
        }
        return b;
    }

    public void getTasks(String stu_id){
        studentReference.child(insID).child(Node.Student.toString()).child(stu_id)
                .addListenerForSingleValueEvent(new ValueEventListener() {
                    @Override
                    public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                        if(dataSnapshot.hasChild("Tasks")){

                        }
                    }

                    @Override
                    public void onCancelled(@NonNull DatabaseError databaseError) {

                    }
                });
    }
}