package com.register_Login;

import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.Toast;

import com.alertOrToast.AlertOrToastMsg;
import com.dataHelper.CatcheData;
import com.dataHelper.Data_Manipulation;
import com.google.android.material.textfield.TextInputEditText;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.tutionapp.R;
import com.tutionapp.TutionActivity;

import org.jetbrains.annotations.NotNull;
import org.json.JSONException;
import org.json.JSONObject;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

public class Login extends AppCompatActivity {

    private Button register, login;
    private TextInputEditText username, password;
    private String user, pass;
    private RadioGroup radioGroup;
    private RadioButton radioButton;
    private AlertOrToastMsg alertOrToastMsg;
    private Data_Manipulation dataManipulation = new Data_Manipulation(this);
    private Handler handler;

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

        handler = new Handler();
        handler.postAtFrontOfQueue(() -> {
            String data = CatcheData.getData("Ins_id", this);
            if(data!=null){
                startActivity(new Intent(Login.this, TutionActivity.class));
                finish();
            }
        });

        register.setOnClickListener((view) -> {
            startActivity(new Intent(Login.this, WhoAreYou.class));
            finish();
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
                        alertOrToastMsg.ToastMsg("It is undr development");
                        break;
                    }
                    case "Parents":{
                        alertOrToastMsg.ToastMsg("It is undr development");
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
}
