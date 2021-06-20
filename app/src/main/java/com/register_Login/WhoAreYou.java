package com.register_Login;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;

import com.tutionapp.R;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

public class WhoAreYou extends AppCompatActivity {

    Button Teacher, Student, Parents;
    Intent intent;

    @Override
    protected void onCreate(@Nullable @org.jetbrains.annotations.Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.who_are_you);

        Teacher = findViewById(R.id.btn_teacher);
        Student = findViewById(R.id.btn_student);
        Parents = findViewById(R.id.btn_parents);

        Teacher.setOnClickListener((view) -> {
            intent = new Intent(this, Teacher_Registration.class);
            startActivity(intent);
            finish();
        });

        Student.setOnClickListener((view) -> {
            intent = new Intent(this, Student_Registration.class);
            startActivity(intent);
            finish();
        });

        Parents.setOnClickListener((view) -> {
            intent = new Intent(this, Registration_Parents.class);
            startActivity(intent);
            finish();
        });
    }

    @Override
    public void onBackPressed() {
        startActivity(new Intent(this, Login.class));
        finish();
    }
}
