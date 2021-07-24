package com.register_Login;

import android.content.Intent;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.widget.Button;

import com.alertOrToast.AlertOrToastMsg;
import com.dataHelper.Register_LoginOperations;
import com.dataHelper.Node;
import com.google.android.material.textfield.TextInputEditText;
import com.tutionapp.R;

import java.util.HashMap;
import java.util.Map;

import androidx.appcompat.app.AppCompatActivity;

public class Student_Registration extends AppCompatActivity {

    private TextInputEditText student_name, Ins_id, PhoneNumber, ClassName, SchoolName, Password, confirm_Password;
    private Button student_registration;
    private String str_student_name, str_Ins_id, str_PhoneNumber, str_ClassName, str_SchoolName, str_Password, str_confirm_Password;
    private Map<String, Object> map = new HashMap<>();
    private Register_LoginOperations dataManipulation = new Register_LoginOperations(this);
    private Intent intent;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.student_registration);

        student_name = findViewById(R.id.student_name);
        Ins_id = findViewById(R.id.student_institute);
        PhoneNumber = findViewById(R.id.student_phone_number);
        ClassName = findViewById(R.id.student_class_or_college);
        SchoolName = findViewById(R.id.school_name);
        Password = findViewById(R.id.student_password);
        confirm_Password = findViewById(R.id.student_confirm_password);
        student_registration = findViewById(R.id.student_register_button);

        //Must write code do the OTP Validation
        intent = new Intent(this, OTPValidation.class);

        Ins_id.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2) {

            }

            @Override
            public void onTextChanged(CharSequence charSequence, int i, int i1, int i2) {

            }

            @Override
            public void afterTextChanged(Editable editable) {
                if(editable.toString().contains("Ins_id_"))
                    Ins_id.setError(null);
                else
                    Ins_id.setError("Invalid Institude ID");
            }
        });

        student_registration.setOnClickListener((v) -> {
            str_student_name = student_name.getText().toString();
            str_Ins_id = Ins_id.getText().toString();
            str_PhoneNumber = PhoneNumber.getText().toString();
            str_ClassName = ClassName.getText().toString();
            str_SchoolName = SchoolName.getText().toString();
            str_Password = Password.getText().toString();
            str_confirm_Password = confirm_Password.getText().toString();

            if(str_Password.equals(str_confirm_Password)){
                if(str_PhoneNumber.length()==10){
                    PhoneNumber.setError(null);
                    Ins_id.setError(null);
                    map.put("StudentName", str_student_name);
                    map.put("Ins_ID", str_Ins_id);
                    map.put("PhoneNumber", str_PhoneNumber);
                    map.put("ClassName", str_ClassName);
                    map.put("SchoolName", str_SchoolName);
                    map.put("Password", str_Password);

                    dataManipulation.fetchNoOfCurrentNode(Node.Student,map);
                }
                else
                    PhoneNumber.setError("10 numbers required");
            }
        });
    }

    @Override
    public void onBackPressed() {
        startActivity(new Intent(this, WhoAreYou.class));
        finish();
    }
}
