package com.register_Login;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.google.android.material.textfield.TextInputEditText;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.tutionapp.R;

import androidx.appcompat.app.AppCompatActivity;

public class Teacher_Registration extends AppCompatActivity {

    private Button register;
    private TextInputEditText teacherName, instute_name, mobileNumber, password, confirmPassword;
    private String str_teacherName, str_instudeName, str_mobileNumber, str_password, conf_password;
    private FirebaseDatabase rootNode;
    private DatabaseReference reference;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.teacher_registration);
        register = findViewById(R.id.teacher_reg_button);
        teacherName = findViewById(R.id.teacher_userName);
        instute_name = findViewById(R.id.teacher_ins_name);
        mobileNumber = findViewById(R.id.teacher_phone_number);
        password = findViewById(R.id.teacher_pass);
        confirmPassword = findViewById(R.id.teacher_confirm_pass);


        register.setOnClickListener((view)-> {
            str_teacherName = teacherName.getText().toString();
            str_instudeName = instute_name.getText().toString();
            str_mobileNumber = mobileNumber.getText().toString();
            str_password = password.getText().toString();
            conf_password = confirmPassword.getText().toString();
            rootNode = FirebaseDatabase.getInstance();
            reference = rootNode.getReference();

            if(str_password.equals(conf_password)) {
                Toast.makeText(this, "Password Matches", Toast.LENGTH_SHORT).show();
            }
            else
                Toast.makeText(this, "Password Dosen't match", Toast.LENGTH_SHORT).show();
        });
    }

    @Override
    public void onBackPressed() {
        startActivity(new Intent(this, WhoAreYou.class));
        finish();
    }
}
