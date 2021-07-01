package com.dataHelper;

import android.app.Activity;
import android.app.AlertDialog;
import android.content.Context;
import android.content.Intent;

import com.alertOrToast.AlertOrToastMsg;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.Query;
import com.google.firebase.database.ValueEventListener;
import com.register_Login.Login;
import com.tutionapp.TutionActivity;

import org.jetbrains.annotations.NotNull;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.Map;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;

public class Data_Manipulation {
    private AlertOrToastMsg alertOrToastMsg;
    private static long noOfChildNodes, noOfInstituteNodes = 0;
    private FirebaseDatabase rootNode;
    private DatabaseReference reference, Institute_Reference;
    private Map<String, Object> map;
    private Context context;
    private String MobileNumber, OwnerName, Institute_Name, ins_id, Password;
    private boolean verifyHasNode = false;

    public Data_Manipulation(Context context){
        this.context = context;
        alertOrToastMsg = new AlertOrToastMsg(context);
        rootNode = FirebaseDatabase.getInstance("https://tutor-project-1cc32-default-rtdb.firebaseio.com/");
        reference = rootNode.getReference();
        Institute_Reference = rootNode.getReference().child("Institutes");
    }

    public void fetchNoOfCurrentNode(Node node, Map<String, Object> map){

        this.map = map;

        switch (node){

            case Institutes:{
                noOfInstitutesCount(map);
                break;
            }

            case Parent:{
                createChildNode("", Node.Parent);
                break;
            }

            case Teacher:{
                createChildNode("", Node.Teacher);
                break;
            }

            case Student:{
                createChildNode(map.get("Ins_ID").toString(), Node.Student);
                break;
            }

            default:{
                throw new RuntimeException("You have selected the wrong option");
            }
        }
    }

    private void createChildNode(String ins_id, Node nodeName) {
        Institute_Reference.child(ins_id).child(nodeName.toString()).addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull @NotNull DataSnapshot dataSnapshot) {
                if (dataSnapshot.hasChild("Stu_id_1")){
                    noOfChildNodes = dataSnapshot.getChildrenCount();
                    map.put("Student_ID", "Stu_id_"+noOfChildNodes);
                    Institute_Reference.child("").setValue(map, new DatabaseReference.CompletionListener() {
                        @Override
                        public void onComplete(@Nullable @org.jetbrains.annotations.Nullable DatabaseError databaseError, @NonNull @NotNull DatabaseReference databaseReference) {
                            if(databaseReference.getKey().equals(map.get("Student_ID")))
                                alertOrToastMsg.ToastMsg("Values inserted successfully");
                        }
                    });
                }
                else
                    DoinitialPreocess(Node.Student, ins_id);

            }

            @Override
            public void onCancelled(@NonNull @NotNull DatabaseError databaseError) {
                alertOrToastMsg.showAlert("Error", databaseError.toString());
            }
        });
    }

    private void noOfInstitutesCount(Map<String, Object> map){

        reference.child(Node.Institutes.toString()).addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull @NotNull DataSnapshot dataSnapshot) {
                if (dataSnapshot.hasChild("Ins_id_1")){
                    noOfInstituteNodes = dataSnapshot.getChildrenCount();
                    System.out.println("ins_id is:"+noOfInstituteNodes);
                    long institute_id = noOfInstituteNodes+1;
                    map.put("ins_id","Ins_id_"+institute_id);
                    Institute_Reference.child("Ins_id_"+institute_id).setValue(map, new DatabaseReference.CompletionListener() {
                        @Override
                        public void onComplete(@Nullable @org.jetbrains.annotations.Nullable DatabaseError databaseError, @NonNull @NotNull DatabaseReference databaseReference) {
                            if(databaseReference.getKey().equals("Ins_id_"+institute_id)){
                                alertOrToastMsg.ToastMsg("Value inserted successfully");
                                alertOrToastMsg.showAlert("Enter this id in login","Your ID is:- "+databaseReference.getKey());
                            }
                        }
                    });
                }
                else
                    DoinitialPreocess();
            }

            @Override
            public void onCancelled(@NonNull @NotNull DatabaseError databaseError) {
                alertOrToastMsg = new AlertOrToastMsg(context);
                alertOrToastMsg.showAlert("Error Occured","Error is:"+databaseError.toString());
            }
        });
    }

    private void DoinitialPreocess(){

        //Creating new node called Institutes and adding first time user
        reference.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull @NotNull DataSnapshot dataSnapshot) {
                if (!dataSnapshot.hasChild(Node.Institutes.toString())){
                    map.put("ins_id","Ins_id_1");
                    reference.child("Institutes").child("Ins_id_1").setValue(map, new DatabaseReference.CompletionListener() {
                        @Override
                        public void onComplete(@Nullable @org.jetbrains.annotations.Nullable DatabaseError databaseError, @NonNull @NotNull DatabaseReference databaseReference) {
                            System.out.println("Key:-"+databaseReference.getKey());
                            if(databaseReference.getKey().equals("Ins_id_1")){
                                alertOrToastMsg.ToastMsg("Value inserted successfully");
                                alertOrToastMsg.showAlert("Enter this id in login","Your ID is:- "+databaseReference.getKey());
                            }
                        }
                    });
                }
            }

            @Override
            public void onCancelled(@NonNull @NotNull DatabaseError databaseError) {
                alertOrToastMsg = new AlertOrToastMsg(context);
                alertOrToastMsg.showAlert("Error Occured","Error is:"+databaseError.toString());
            }
        });
    }

    private void DoinitialPreocess(Node node, String Ins_id){

        if(validateIns_InstituteID(Ins_id)==false){
            alertOrToastMsg.ToastMsg("Invalid Institute ID");
            return;
        }

        reference.child(Node.Institutes.toString()).child(Ins_id).addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull @NotNull DataSnapshot dataSnapshot) {
                map.put("Student_ID","Stu_id_1");
                for (DataSnapshot snapshot:dataSnapshot.getChildren()){
                    String keys = snapshot.getKey();
                    if(keys.equals(Ins_id)){
                        Institute_Reference.child(Ins_id).child(node.toString()).child("Stu_id_1").setValue(map, new DatabaseReference.CompletionListener() {
                            @Override
                            public void onComplete(@Nullable @org.jetbrains.annotations.Nullable DatabaseError databaseError, @NonNull @NotNull DatabaseReference databaseReference) {
                                alertOrToastMsg.ToastMsg("Value inserted successfully...!");
                            }
                        });
                        break;
                    }
                }
            }

            @Override
            public void onCancelled(@NonNull @NotNull DatabaseError databaseError) {
                alertOrToastMsg.showAlert("Error", databaseError.toString());
            }
        });
    }

    public void fetchData(String child){
        Institute_Reference.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull @NotNull DataSnapshot dataSnapshot) {
                dataSnapshot.getValue();
            }

            @Override
            public void onCancelled(@NonNull @NotNull DatabaseError databaseError) {

            }
        });
    }

    public void validateUserCredentials(String userID, String password){
        Institute_Reference.child(userID).addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull @NotNull DataSnapshot dataSnapshot) {
                Object data = dataSnapshot.getValue();
                System.out.println("Login Data:-"+data);
                if(data==null){
                    alertOrToastMsg.ToastMsg("User ID or Password id incorrect...!");
                    return;
                }
                try {
                    JSONObject jobj = new JSONObject(data.toString());
                    System.out.println("Login Data in json:-"+jobj);
                    MobileNumber = jobj.getString("MobileNumber");
                    OwnerName = jobj.getString("OwnerName");
                    Institute_Name = jobj.getString("Institute_Name");
                    ins_id = jobj.getString("ins_id");
                    Password = jobj.getString("Password");

                    if(password.equals(Password) && userID.equals(ins_id)){
                        context.startActivity(new Intent(context, TutionActivity.class));
                        CatcheData.setData("Ins_id",ins_id, context);
                        ((Activity)context).finish();
                    }
                    else
                        alertOrToastMsg.ToastMsg("User ID or Password id incorrect...!");
                } catch (JSONException e) {
                    e.printStackTrace();
                }
            }

            @Override
            public void onCancelled(@NonNull @NotNull DatabaseError databaseError) {
                alertOrToastMsg.showAlert("Error",databaseError.toString());
            }
        });
    }

    private boolean validateIns_InstituteID(String ins_id){

        Institute_Reference.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull @NotNull DataSnapshot dataSnapshot) {
                System.out.println("My Key is:-"+ins_id);
                for(DataSnapshot snapshot:dataSnapshot.getChildren()){
                    String key = snapshot.getKey();
                    System.out.println("All Keys:-"+key);
                    if(key.equals(ins_id)){
                        System.out.println("dataSnapshot Key is:-"+key);
                        verifyHasNode = true;
                        break;
                    }
                }

            }

            @Override
            public void onCancelled(@NonNull @NotNull DatabaseError databaseError) {
                alertOrToastMsg.showAlert("validateIns_InstituteID Error", databaseError.toString());
                verifyHasNode = false;
            }
        });
        return verifyHasNode;
    }
}