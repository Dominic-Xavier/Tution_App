package com.common;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;

import com.dataHelper.CatcheData;
import com.register_Login.Login;
import com.student.Student;
import com.tutionapp.R;
import com.tutionapp.TutionActivity;

public class SplastScreen extends AppCompatActivity {

    private Handler handler;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        handler = new Handler();
        handler.postAtFrontOfQueue(() -> {
            String data = CatcheData.getData("Ins_id", this);
            if(data!=null && data.contains("Ins_id")){
                startActivity(new Intent(SplastScreen.this, TutionActivity.class));
                finish();
            }
            else if(data!=null && data.contains("Stu_id")){
                startActivity(new Intent(SplastScreen.this, Student.class));
                finish();
            }
            else {
                startActivity(new Intent(SplastScreen.this, Login.class));
                finish();
            }
        });
    }
}