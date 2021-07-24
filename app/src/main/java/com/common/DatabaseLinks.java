package com.common;

import com.dataHelper.Node;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

public class DatabaseLinks {
    private static final DatabaseReference baseReference = FirebaseDatabase.getInstance().getReference("https://tutor-project-1cc32-default-rtdb.firebaseio.com/");
    public static final DatabaseReference instituteReference = baseReference.child(Node.Institutes.toString());
    public static final DatabaseReference studentReference = instituteReference.child(Node.Student.toString());
}
