package com.dataHelper;

import android.content.Context;
import android.os.Build;

import androidx.annotation.NonNull;
import androidx.annotation.RequiresApi;

import com.common.AlertOrToastMsg;
import com.common.DateFunctions;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.ValueEventListener;

import java.util.Map;

public class AttendanceDetails {

    Context context;
    private AlertOrToastMsg alertOrToastMsg;

    public AttendanceDetails(Context context){
        this.context = context;
        alertOrToastMsg = new AlertOrToastMsg(context);
    }

    public void addAttendance(Map<String, Object> present, Map<String, Object> absent, Map<String, Object> late){
        DatabaseReference instituteRef = DatabaseLinks.getAttendanceLink(CatcheData.getData("Ins_id", context));
        instituteRef.addListenerForSingleValueEvent(new ValueEventListener() {
            @RequiresApi(api = Build.VERSION_CODES.O)
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                dataSnapshot.child(DateFunctions.getCurrentDate()).child("Present").getRef().setValue(present)
                        .addOnSuccessListener(new OnSuccessListener<Void>() {
                            @Override
                            public void onSuccess(Void unused) {
                                alertOrToastMsg.ToastMsg("Present Details saved successfully...!");
                            }
                        });
                dataSnapshot.child(DateFunctions.getCurrentDate()).child("Absent").getRef().setValue(absent)
                        .addOnSuccessListener(new OnSuccessListener<Void>() {
                            @Override
                            public void onSuccess(Void unused) {
                                alertOrToastMsg.ToastMsg("Absent Details saved successfully...!");
                            }
                        });
                dataSnapshot.child(DateFunctions.getCurrentDate()).child("Late").getRef().setValue(late)
                        .addOnSuccessListener(new OnSuccessListener<Void>() {
                            @Override
                            public void onSuccess(Void unused) {
                                alertOrToastMsg.ToastMsg("Late Details saved successfully...!");
                            }
                        });
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {
                alertOrToastMsg.showAlert("Error", databaseError.toString());
            }
        });
    }
}
