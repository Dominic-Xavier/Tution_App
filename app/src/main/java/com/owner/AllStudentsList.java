package com.owner;

import android.os.Bundle;

import com.alertOrToast.AlertOrToastMsg;
import com.dataHelper.CatcheData;
import com.dataHelper.Node;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.tutionapp.R;

import org.jetbrains.annotations.NotNull;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

public class AllStudentsList extends AppCompatActivity implements RecyclerViewAdapter.OnStudentListner {

    private RecyclerView recyclerView;
    private RecyclerViewAdapter recyclerViewAdapter;
    private static Set<String> studentNames = new HashSet<>();
    private static Set<String> studentPhoneNumbers = new HashSet<>();
    private static AlertOrToastMsg alertOrToastMsg;
    private static Map<String, Set<String>> map = new HashMap<>();
    private List<String> stu_Names;
    private static final DatabaseReference reference = FirebaseDatabase.getInstance("https://tutor-project-1cc32-default-rtdb.firebaseio.com/").getReference()
            .child(Node.Institutes.toString());

    @Override
    protected void onCreate(@Nullable @org.jetbrains.annotations.Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.all_students_list);

        String ins_id = CatcheData.getData("Ins_id", this);
        alertOrToastMsg = new AlertOrToastMsg(this);

        Map<String, Set<String>> maps = getNamesAndPhoneNumbers(ins_id);
        Set<String> names = maps.get("StudentNames");
        Set<String> phoneNumbers = maps.get("StudentPhoneNumbers");

        recyclerView = findViewById(R.id.recyclerView);

        if(names!=null && phoneNumbers!=null){
            recyclerViewAdapter = new RecyclerViewAdapter(this, names, phoneNumbers, this::onClick);
            recyclerView.setAdapter(recyclerViewAdapter);
            recyclerView.setLayoutManager(new LinearLayoutManager(this));
            stu_Names = new ArrayList<>(names);
        }
    }

    private static Map<String, Set<String>> getNamesAndPhoneNumbers(String ins_id){

        reference.child(ins_id).child(Node.Student.toString()).child("")
                .addListenerForSingleValueEvent(new ValueEventListener() {
                    @Override
                    public void onDataChange(@NonNull @NotNull DataSnapshot dataSnapshot) {
                        int count = 0;
                        JSONObject jsonObject;
                        System.out.println("Student Lists:-"+dataSnapshot.getValue());
                        for(DataSnapshot snapshot : dataSnapshot.getChildren()){
                            Object allStudentData = snapshot.getValue();
                            try {
                                jsonObject = new JSONObject(allStudentData.toString());
                                String Names = jsonObject.getString("StudentName");
                                String phone_Number = jsonObject.getString("PhoneNumber");
                                studentPhoneNumbers.add(phone_Number);
                                studentNames.add(Names);
                            }
                            catch (NullPointerException | JSONException n){
                                alertOrToastMsg.showAlert("Exception", n.getMessage());
                                break;
                            }
                        }
                        map.put("StudentNames", studentNames);
                        map.put("StudentPhoneNumbers", studentPhoneNumbers);
                        System.out.println("Map is:-"+map);
                    }

                    @Override
                    public void onCancelled(@NonNull @NotNull DatabaseError databaseError) {
                        alertOrToastMsg.ToastMsg(databaseError.toString());
                    }
                });
        System.out.println("Map are:-"+map);
        return map;
    }

    @Override
    public void onClick(int position) {
        String Name = stu_Names.get(position);
        alertOrToastMsg.ToastMsg("Clicked "+Name);
    }

    @Override
    public void onBackPressed() {
        finish();
    }
}
