package com.register_Login;

import android.content.Intent;
import android.os.Bundle;
import android.widget.Button;

import com.dataHelper.Register_LoginOperations;
import com.dataHelper.Node;
import com.google.android.material.textfield.TextInputEditText;
import com.tutionapp.R;

import java.util.HashMap;
import java.util.Map;

import androidx.appcompat.app.AppCompatActivity;

public class Registration_Parents extends AppCompatActivity {

    private TextInputEditText ParentName, PhoneNumber, InsId, Student_id, password, confirmPassword;
    private Button parent_register;
    private String str_ParentName, str_PhoneNumber, str_InsId, str_Student_id, str_password, str_confirmPassword;
    private Map<String, Object> map = new HashMap<>();
    private Register_LoginOperations dataManipulation = new Register_LoginOperations(this);

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.parent_registration);

        ParentName = findViewById(R.id.parent_name);
        PhoneNumber = findViewById(R.id.parent_phone_number);
        InsId = findViewById(R.id.parent_instute_id);
        Student_id = findViewById(R.id.parent_student_id);
        password = findViewById(R.id.parent_password);
        confirmPassword = findViewById(R.id.parent_confirm_password);
        parent_register = findViewById(R.id.registerParent);

        parent_register.setOnClickListener((v) -> {
            str_ParentName = ParentName.getText().toString();
            str_PhoneNumber = PhoneNumber.getText().toString();
            str_InsId = InsId.getText().toString();
            str_Student_id = Student_id.getText().toString();
            str_password = password.getText().toString();
            str_confirmPassword = confirmPassword.getText().toString();
            if(str_password.equals(str_confirmPassword)){
                if(str_PhoneNumber.length()==10){
                    map.put("parentName",str_ParentName);
                    map.put("parentPhoneNumber",str_PhoneNumber);
                    map.put("parentInsID",str_InsId);
                    map.put("parentStudentID",str_Student_id);
                    map.put("ParentPassword",str_password);
                    PhoneNumber.setError(null);
                    dataManipulation.fetchNoOfCurrentNode(Node.Parent, map);
                }
                else
                    PhoneNumber.setError("Invalid Number");
            }
        });
    }

    @Override
    public void onBackPressed() {
        startActivity(new Intent(this, WhoAreYou.class));
        finish();
    }
}