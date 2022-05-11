package com.tutionapp;

import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.AppCompatButton;
import androidx.appcompat.widget.AppCompatEditText;

import android.os.Bundle;
import android.view.View;

import com.common.AlertOrToastMsg;
import com.dataHelper.CatcheData;

import org.jitsi.meet.sdk.JitsiMeet;
import org.jitsi.meet.sdk.JitsiMeetActivity;
import org.jitsi.meet.sdk.JitsiMeetConferenceOptions;
import org.jitsi.meet.sdk.JitsiMeetUserInfo;

import java.net.MalformedURLException;
import java.net.URL;

public class LiveClasses extends AppCompatActivity {

    AppCompatButton join;
    AppCompatEditText meetingCode;
    AlertOrToastMsg alertOrToastMsg = new AlertOrToastMsg(this);

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_live_classes);

        meetingCode = findViewById(R.id.meetingID);
        join = findViewById(R.id.joinMeeting);

        URL server;
        try {
            server = new URL("https://meet.jit.si");
            JitsiMeetConferenceOptions conferenceOptions = new JitsiMeetConferenceOptions.Builder()
                    .setServerURL(server)
                    .build();
            JitsiMeet.setDefaultConferenceOptions(conferenceOptions);
        }
        catch (MalformedURLException e){
            alertOrToastMsg.showAlert("Error", e.getMessage());
        }

        join.setOnClickListener((v) -> {
            String meeting_code = meetingCode.getText().toString();
            if(meeting_code.isEmpty()){
                alertOrToastMsg.ToastMsg("Enter a value");
                return;
            }

            JitsiMeetUserInfo userInfo = new JitsiMeetUserInfo();
            String ownerName = getResources().getString(R.string.OwnerName);
            userInfo.setDisplayName(CatcheData.getData(ownerName, getApplicationContext()));
            JitsiMeetConferenceOptions conferenceOptions = new JitsiMeetConferenceOptions.Builder()
                    .setRoom(meeting_code)
                    .setFeatureFlag("pip.enabled",true)
                    .setUserInfo(userInfo)
                    .build();
            JitsiMeetActivity.launch(getApplicationContext(),conferenceOptions);
        });
    }
}