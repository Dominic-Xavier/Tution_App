package com.dataHelper;

import android.content.Context;
import android.os.Build;
import android.view.View;
import android.widget.ProgressBar;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.annotation.RequiresApi;
import androidx.recyclerview.widget.RecyclerView;

import com.common.AlertOrToastMsg;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.recyclerViewAdapters.TeacherRecyclerAdapter;
import com.recyclerViewAdapters.TopicListener;
import com.tutionapp.Teacher_task;

import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class TaskDetails implements TeacherRecyclerAdapter.OnTask, TopicListener {

    private Context context;
    private static DatabaseReference studentReference = DatabaseLinks.baseReference;
    private static String insID, topicID;
    private Map<String, Object> map;
    private List<String> topicIDs;
    private static List<String> listOfSelectedIds;
    private static final Map<String, List<String>> taskDetails = new HashMap<>();
    private AlertOrToastMsg alertOrToastMsg;
    private static ProgressBar progressBar;

    public TaskDetails(Context context){
        this.context = context;
        insID = CatcheData.getData("Ins_id",context);
        alertOrToastMsg = new AlertOrToastMsg(context);
    }

    public TaskDetails(Context context, ProgressBar progressBar){
        insID = CatcheData.getData("Ins_id",context);
        alertOrToastMsg = new AlertOrToastMsg(context);
        this.context = context;
        this.progressBar = progressBar;
    }

    public TaskDetails(Context context, Map<String, Object> map){
        this.context = context;
        alertOrToastMsg = new AlertOrToastMsg(context);
        insID = CatcheData.getData("Ins_id",context);
        System.out.println("Ins_id is: "+insID);
        this.map = map;
    }

    public static List<String> getListOfSelectedIds() {
        return listOfSelectedIds;
    }

    public void setListOfSelectedIds(List<String> listOfSelectedIds) {
        this.listOfSelectedIds = listOfSelectedIds;
    }

    public void addTask(String student_id, RecyclerView recyclerView){

        studentReference.child(insID).child(Node.Student.toString()).child(student_id).orderByChild("Task")
                .addListenerForSingleValueEvent(new ValueEventListener() {

            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                List<Integer> topicIds = new ArrayList<>();
                if(dataSnapshot.hasChild("Task")){
                    for (DataSnapshot snapshot : dataSnapshot.child("Task").getChildren()) {
                        String topic_ID = snapshot.getKey();
                        int topicNumber = Integer.parseInt(topic_ID.split("_")[2]);
                        topicIds.add(topicNumber);
                    }
                    Collections.sort(topicIds);
                    long topicCount = 0;
                    topicCount = topicIds.get(topicIds.size()-1);
                    if(topicCount!=0 && topicCount!=-1){
                        topicCount++;
                        topicID = "Topic_id_"+topicCount;
                        System.out.println("New Topic id is "+topicID);
                    }
                    studentReference.child(insID).child(Node.Student.toString()).child(student_id).child("Task")
                        .child(topicID).updateChildren(map, (@Nullable DatabaseError databaseError, @NonNull DatabaseReference databaseReference) -> {
                        Toast.makeText(context, "values inserted successfully....!", Toast.LENGTH_SHORT).show();

                        getTaskDetails(student_id, recyclerView);
                    });
                }
                else{
                    studentReference.child(insID).child(Node.Student.toString()).child(student_id).child("Task")
                        .child("Topic_id_1").setValue(map, (@Nullable DatabaseError databaseError, @NonNull DatabaseReference databaseReference) -> {
                        getTaskDetails(student_id, recyclerView);
                        Toast.makeText(context, "values inserted successfully....!", Toast.LENGTH_SHORT).show();
                    });
                }
                topicID = "Topic_id_1";
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {
                alertOrToastMsg.showAlert("Error", databaseError.toString());
            }
        });
    }

    public Map<String, List<String>> getTaskDetails(String stu_id, RecyclerView recyclerView){

        studentReference.child(insID).child(Node.Student.toString()).child(stu_id)
                .addListenerForSingleValueEvent(new ValueEventListener() {

            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                //progressBar.setVisibility(View.VISIBLE);
                List<String> allDates = new ArrayList<>();
                List<String> subject = new ArrayList<>();
                List<String> topic = new ArrayList<>();
                if(dataSnapshot.hasChild("Task")){
                    topicIDs = new ArrayList<>();
                    allDates.removeAll(allDates);
                    subject.removeAll(subject);
                    topic.removeAll(topic);
                    topicIDs.removeAll(topicIDs);
                    List<String> allDescriptions = new ArrayList<>();
                    for (DataSnapshot snapshot : dataSnapshot.child("Task").getChildren()) {
                        String topicID = snapshot.getKey();
                        System.out.println("Topic id is "+topicID);
                        topicIDs.add(topicID);
                        String Subject = snapshot.child("Subject").getValue(String.class);
                        Object date = snapshot.child("Date").getValue();
                        String Title = snapshot.child("Topic").getValue(String.class);
                        String description = snapshot.child("Description").getValue(String.class);
                        allDates.add(date.toString());
                        subject.add(Subject);
                        topic.add(Title);
                        allDescriptions.add(description);
                    }
                    taskDetails.put("Dates", allDates);
                    taskDetails.put("Subjects", subject);
                    taskDetails.put("Title", topic);
                    taskDetails.put("TopicID", topicIDs);
                    taskDetails.put("Description", allDescriptions);
                    Teacher_task.recyclerView(context, allDates, subject, topic,topicIDs, recyclerView,
                            TaskDetails.this::onClick, TaskDetails.this::selectTopic);
                }
                else{
                    Teacher_task.recyclerView(context, allDates, subject, topic,topicIDs, recyclerView,
                            TaskDetails.this::onClick, TaskDetails.this::selectTopic);
                    alertOrToastMsg.ToastMsg("No Task present for this student....!");
                }

                progressBar.setVisibility(View.GONE);
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {
                alertOrToastMsg.showAlert("Error", databaseError.toString());
            }
        });
        return taskDetails;
    }

    public void updateFinishedTask(String stu_id, List<String> list){
        studentReference.child(insID).child(Node.Student.toString()).child(stu_id).child("Task")
                .orderByKey().addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                List<Integer> topicIds = new ArrayList<>();
                for (DataSnapshot snapshot : dataSnapshot.getChildren()){
                    String key = snapshot.getKey();
                    topicIds.add(Integer.parseInt(key.split("_")[2]));
                }
                Collections.sort(topicIds);
                System.out.println("Data snapshot is "+topicIds);
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {
                alertOrToastMsg.showAlert("Error", databaseError.toString());
            }
        });
    }

    public void deleteTasks(List<String> topicIds, String stu_id, RecyclerView recyclerView){
        studentReference.child(insID).child(Node.Student.toString()).child(stu_id).child("Task").
                addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                for (DataSnapshot snapshot : dataSnapshot.getChildren()){
                    for (String ids:topicIds) {
                        if(ids.equals(snapshot.getKey())){
                            snapshot.getRef().removeValue(new DatabaseReference.CompletionListener() {
                                @Override
                                public void onComplete(@Nullable DatabaseError databaseError, @NonNull DatabaseReference databaseReference) {
                                    alertOrToastMsg.ToastMsg("Deleted "+ids+" Successfully...!");
                                }
                            });
                        }
                    }
                    getTaskDetails(stu_id, recyclerView);
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {
                alertOrToastMsg.showAlert("Error", databaseError.toString());
            }
        });
    }

    @Override
    public void onClick(int position) {
        String id = topicIDs.get(position);
        alertOrToastMsg.ToastMsg(id);
        System.out.println("Clicked Topic ID "+id);
    }

    @Override
    public void selectTopic(List<String> list) {
        setListOfSelectedIds(list);
    }
}