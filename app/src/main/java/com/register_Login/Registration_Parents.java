package com.register_Login;

import android.content.Intent;
import android.os.Bundle;

import com.tutionapp.R;

import androidx.appcompat.app.AppCompatActivity;

public class Registration_Parents extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.parent_registration);


    }

    @Override
    public void onBackPressed() {
        startActivity(new Intent(this, WhoAreYou.class));
        finish();
    }
}
