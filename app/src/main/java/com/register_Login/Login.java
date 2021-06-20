package com.register_Login;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.google.android.material.textfield.TextInputEditText;
import com.tutionapp.R;

import androidx.appcompat.app.AppCompatActivity;

public class Login extends AppCompatActivity {

    private Button register, login;
    private TextInputEditText username, password;
    private String user, pass;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.login);

        register = findViewById(R.id.register);
        login = findViewById(R.id.btn_login);
        username = (TextInputEditText)findViewById(R.id.txt_user);
        password = (TextInputEditText)findViewById(R.id.txt_Pass);

        register.setOnClickListener((view) -> {
            startActivity(new Intent(Login.this, WhoAreYou.class));
            finish();
        });

        login.setOnClickListener((view) ->{
            user = username.getText().toString();
            pass = password.getText().toString();
            if(!user.equals("") || !pass.equals("")){
                Toast.makeText(Login.this,"User Name and Password not empty..!",Toast.LENGTH_SHORT).show();
            }
            else {
                Toast.makeText(Login.this,"User Name or Password is empty..!",Toast.LENGTH_SHORT).show();
            }
        });
    }
}
