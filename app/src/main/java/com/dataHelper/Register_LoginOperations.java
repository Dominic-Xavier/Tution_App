package com.dataHelper;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;

import com.alertOrToast.AlertOrToastMsg;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.student.Student;
import com.tutionapp.TutionActivity;

import org.jetbrains.annotations.NotNull;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.Map;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;

public class Register_LoginOperations {
    private AlertOrToastMsg alertOrToastMsg;
    private static long noOfChildNodes, noOfInstituteNodes = 0;
    private FirebaseDatabase rootNode;
    private DatabaseReference reference, Institute_Reference;
    private Map<String, Object> map;
    private Context context;
    private String MobileNumber, OwnerName, Institute_Name, ins_id, Password;
    private static boolean verifyHasNode = false;
    static String hasChild, keyValue;


    public Register_LoginOperations(Context context){
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
                createParentNode();
                break;
            }

            case Teacher:{
                createChildNode("", Node.Teacher, map);
                break;
            }

            case Student:{
                createChildNode(map.get("Ins_ID").toString(), Node.Student, map);
                break;
            }

            default:{
                throw new RuntimeException("You have selected the wrong option");
            }
        }
    }

    private void createChildNode(String ins_id, Node nodeName, Map<String, Object> map) {

        if(!validateIns_InstituteID(ins_id)){
            alertOrToastMsg.ToastMsg("Invalid Institute ID");
            return;
        }
        else
            alertOrToastMsg.ToastMsg("valid Institute ID");

        switch (nodeName){
            case Student:{
                hasChild = "Stu_id_1";
                keyValue = "Stu_id_";
                break;
            }

            case Teacher:{
                hasChild = "Tech_id_1";
                keyValue = "Tech_id_";
                break;
            }

            case Parent:{
                hasChild = "Par_id_1";
                keyValue = "Par_id_";
                break;
            }
        }

        Institute_Reference.child(ins_id).child(nodeName.toString()).addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull @NotNull DataSnapshot dataSnapshot) {
                if (dataSnapshot.hasChild(hasChild)){

                    noOfChildNodes = dataSnapshot.getChildrenCount();
                    long nodeCout = noOfChildNodes+1;
                    map.put("Student_ID", keyValue+nodeCout);
                    Institute_Reference.child(ins_id).child(nodeName.toString()).child(map.get("Student_ID").toString()).setValue(map, new DatabaseReference.CompletionListener() {
                        @Override
                        public void onComplete(@Nullable @org.jetbrains.annotations.Nullable DatabaseError databaseError, @NonNull @NotNull DatabaseReference databaseReference) {
                            if(databaseReference.getKey().equals(map.get("Student_ID")))
                                alertOrToastMsg.ToastMsg("Values inserted successfully");
                            else{
                                alertOrToastMsg.ToastMsg(databaseError.toString());
                                alertOrToastMsg.ToastMsg("Invalid Institute ID");
                            }
                        }
                    });
                }
                else
                    DoinitialPreocess(nodeName, ins_id, map);

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

    private void DoinitialPreocess(Node node, String Ins_id, Map<String, Object> map){

        Institute_Reference.child(Ins_id).addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull @NotNull DataSnapshot dataSnapshot) {
                map.put("Student_ID","Stu_id_1");
                for (DataSnapshot snapshot:dataSnapshot.getChildren()){
                    String keys = snapshot.getKey();
                    System.out.println("Student keys:-"+keys);
                    if(!keys.equals(Ins_id)){
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
                String Instutute_id = dataSnapshot.child("ins_id").getValue(String.class);
                String passWord = dataSnapshot.child("Password").getValue(String.class);
                System.out.println("Login Data:-"+Instutute_id+" : "+passWord);
                if(Instutute_id==null){
                    alertOrToastMsg.ToastMsg("User ID or Password id incorrect...!");
                    return;
                }

                if(password.equals(passWord) && userID.equals(Instutute_id)){
                    context.startActivity(new Intent(context, TutionActivity.class));
                    CatcheData.setData("Ins_id",Instutute_id, context);
                    ((Activity)context).finish();
                }
                else
                    alertOrToastMsg.ToastMsg("User ID or Password is incorrect...!");
                }

            @Override
            public void onCancelled(@NonNull @NotNull DatabaseError databaseError) {
                alertOrToastMsg.showAlert("Error",databaseError.toString());
            }
        });
    }

    private boolean validateIns_InstituteID(String ins_id){

        Institute_Reference.addListenerForSingleValueEvent(new ValueEventListener() {
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
                    else
                        verifyHasNode = false;
                }
            }

            @Override
            public void onCancelled(@NonNull @NotNull DatabaseError databaseError) {
                alertOrToastMsg.showAlert("validateIns_InstituteID Error", databaseError.toString());
                verifyHasNode = false;
            }

        });
        System.out.println("verifyHasNode:-"+verifyHasNode);
        return verifyHasNode;
    }

    private void createParentNode(){

        Institute_Reference.child(map.get("parentInsID").toString()).child(Node.Student.toString())
                .addListenerForSingleValueEvent(new ValueEventListener() {
                    @Override
                    public void onDataChange(@NonNull @NotNull DataSnapshot dataSnapshot) {
                        if(dataSnapshot.hasChild(map.get("parentStudentID").toString())){
                            long count = dataSnapshot.getChildrenCount()+1;
                            map.put("ParentID", "par_id_"+count);
                            alertOrToastMsg.ToastMsg("Valid Ins ID and student ID");
                            Institute_Reference.child(map.get("parentInsID").toString()).child(Node.Student.toString()).child(map.get("parentStudentID").toString())
                                    .updateChildren(map, new DatabaseReference.CompletionListener() {
                                        @Override
                                        public void onComplete(@Nullable @org.jetbrains.annotations.Nullable DatabaseError databaseError, @NonNull @NotNull DatabaseReference databaseReference) {
                                            if(databaseReference.getKey().equals(map.get("parentStudentID")))
                                                alertOrToastMsg.ToastMsg("Value is inserted successfully");
                                            else
                                                alertOrToastMsg.ToastMsg("Error in inserting value...!");
                                        }
                                    });
                        }
                        else
                            alertOrToastMsg.ToastMsg("Invalid Ins ID or Student ID");
                    }

                    @Override
                    public void onCancelled(@NonNull @NotNull DatabaseError databaseError) {
                        alertOrToastMsg.ToastMsg(databaseError.toString());
                    }
                });
    }

    public void validateUserCredentials(String ins_id, String Student_ID, String pass){

        Institute_Reference.child(ins_id).child(Node.Student.toString()).child(Student_ID).addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                Object obj = dataSnapshot.getValue();
                try {
                    String StudentID = dataSnapshot.child("Student_ID").getValue(String.class);
                    String Ins_ID = dataSnapshot.child("Ins_ID").getValue(String.class);
                    String passWord = dataSnapshot.child("Password").getValue(String.class);
                    System.out.println("");
                    if(Student_ID.equals(StudentID) && ins_id.equals(Ins_ID) && pass.equals(passWord)){
                        CatcheData.setData("Stu_id",StudentID, context);
                        context.startActivity(new Intent(context, Student.class));
                        ((Activity)context).finish();
                    }
                    else
                        alertOrToastMsg.ToastMsg("Invalid Stu ID or Ins ID or Password...!");
                }
                catch (Exception e){
                    alertOrToastMsg.showAlert("Error", e.getMessage());
                }
            }

            @Override
            public void onCancelled(@NonNull @NotNull DatabaseError databaseError) {
                alertOrToastMsg.ToastMsg(databaseError.toString());
            }
        });
    }
}