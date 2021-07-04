package com.register_Login;

import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.View;
import android.widget.Button;
import android.widget.RadioButton;
import android.widget.RadioGroup;

import com.alertOrToast.AlertOrToastMsg;
import com.dataHelper.CatcheData;
import com.dataHelper.Register_LoginOperations;
import com.dataHelper.Node;
import com.google.android.material.textfield.TextInputEditText;
import com.google.android.material.textfield.TextInputLayout;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.tutionapp.R;
import com.tutionapp.TutionActivity;

import androidx.appcompat.app.AppCompatActivity;

public class Login extends AppCompatActivity {

    private Button register, login;
    private TextInputEditText username, password, student_ID;
    private String user, pass, studentID;
    private RadioGroup radioGroup;
    private RadioButton radioButton, defaultButton;
    private AlertOrToastMsg alertOrToastMsg;
    private Register_LoginOperations dataManipulation = new Register_LoginOperations(this);
    private TextInputLayout textInputLayout, parentTextInputLayout;
    private Handler handler;
    private FirebaseDatabase rootNode = FirebaseDatabase.getInstance("https://tutor-project-1cc32-default-rtdb.firebaseio.com/");
    private DatabaseReference reference = rootNode.getReference().child(Node.Institutes.toString());

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.login);

        register = findViewById(R.id.register);
        login = findViewById(R.id.btn_login);
        username = (TextInputEditText)findViewById(R.id.txt_user);
        password = (TextInputEditText)findViewById(R.id.txt_Pass);
        radioGroup = findViewById(R.id.category);
        alertOrToastMsg = new AlertOrToastMsg(this);
        textInputLayout = findViewById(R.id.student_login_id);
        parentTextInputLayout = findViewById(R.id.parentsID);
        defaultButton = findViewById(R.id.Teacher);
        student_ID = findViewById(R.id.student_login_ID);

        radioGroup.check(defaultButton.getId());
        if(radioGroup.getCheckedRadioButtonId()==defaultButton.getId()){
            parentTextInputLayout.setVisibility(View.INVISIBLE);
            textInputLayout.setVisibility(View.INVISIBLE);
        }

        handler = new Handler();
        handler.postAtFrontOfQueue(() -> {
            String data = CatcheData.getData("Ins_id", this);
            if(data!=null){
                startActivity(new Intent(Login.this, TutionActivity.class));
                finish();
            }
        });

        username.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2) {

            }

            @Override
            public void onTextChanged(CharSequence charSequence, int i, int i1, int i2) {

            }

            @Override
            public void afterTextChanged(Editable editable) {
                if(editable.toString().trim().contains("Ins_id_"))
                    username.setError(null);
                else
                    username.setError("Invalid Institute ID");
            }
        });

        register.setOnClickListener((view) -> {
            startActivity(new Intent(Login.this, WhoAreYou.class));
            finish();
        });

        radioGroup.setOnCheckedChangeListener(new RadioGroup.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(RadioGroup radioGroup, int i) {
                int id = radioGroup.getCheckedRadioButtonId();
                radioButton = findViewById(id);
                String radiobuttonText = radioButton.getText().toString();
                if(radioGroup==null || radiobuttonText.equals("") || radiobuttonText==null || radiobuttonText.equals("Teacher")){
                    textInputLayout.setVisibility(View.INVISIBLE);
                    parentTextInputLayout.setVisibility(View.INVISIBLE);
                }
                else if(radiobuttonText.equals("Student")){
                    textInputLayout.setVisibility(View.VISIBLE);
                    parentTextInputLayout.setVisibility(View.INVISIBLE);
                }
                else if(radiobuttonText.equals("Parents")){
                    textInputLayout.setVisibility(View.VISIBLE);
                    parentTextInputLayout.setVisibility(View.VISIBLE);
                }
            }
        });

        login.setOnClickListener((view) ->{
            user = username.getText().toString();
            pass = password.getText().toString();

            int id = radioGroup.getCheckedRadioButtonId();
            radioButton = findViewById(id);
            if(!user.equals("") && !pass.equals("") && radioButton!=null){
                switch (radioButton.getText().toString()){

                    case "Teacher":{
                        dataManipulation.validateUserCredentials(user, pass);
                        break;
                    }
                    case "Student":{
                        studentID = student_ID.getText().toString();
                        dataManipulation.validateUserCredentials(user, studentID, pass);
                        break;
                    }
                    case "Parents":{
                        alertOrToastMsg.ToastMsg("It is under development");
                        break;
                    }

                    default:{
                        alertOrToastMsg.ToastMsg("Select an option");
                    }
                }
            }
            else {
                alertOrToastMsg.showAlert("Error","Username or password is empty or radio button is not selected");
            }
        });
    }

    private void checkedButton(View v){
        int id = radioGroup.getCheckedRadioButtonId();
        radioButton = findViewById(id);

    }

    @Override
    public void onBackPressed() {
        finish();
    }
}
