package com.dataHelper;

import com.dataHelper.Node;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

public class DatabaseLinks {
    public static final DatabaseReference baseReference = FirebaseDatabase.getInstance("https://tutor-project-1cc32-default-rtdb.firebaseio.com").getReference("Institutes");
}
