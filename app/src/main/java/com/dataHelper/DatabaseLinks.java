package com.dataHelper;

import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;

public class DatabaseLinks {
    public static final DatabaseReference baseReference = FirebaseDatabase.getInstance("https://tutor-project-1cc32-default-rtdb.firebaseio.com").
            getReference("Institutes");
    public static final StorageReference storageReference = FirebaseStorage.getInstance("gs://tutor-project-1cc32.appspot.com").getReference();
    public static DatabaseReference getAttendanceLink(String ins_id){
        DatabaseReference attendanceRef = baseReference.child(ins_id).child(Node.Attendance.toString());
        return attendanceRef;
    }

    public static final StorageReference getInstituteRef(String ins_id){
        StorageReference storageRef = storageReference.child(ins_id);
        return storageRef;
    }
}
