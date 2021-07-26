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
            String ins_id = CatcheData.getData("Ins_id", this);
            String stu_id = CatcheData.getData("Stu_id", this);
            if(ins_id!=null && ins_id.contains("Ins_id")){
                startActivity(new Intent(SplastScreen.this, TutionActivity.class));
                finish();
            }
            else if(stu_id!=null && stu_id.contains("Stu_id")){
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