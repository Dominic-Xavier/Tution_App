package com.alertOrToast;

import android.app.AlertDialog;
import android.content.Context;
import android.widget.Toast;

public class AlertOrToastMsg {

    AlertDialog.Builder alertDialog;
    AlertDialog dialog;
    Context context;

    public AlertOrToastMsg(Context context){
        this.context = context;
    }

    public void ToastMsg(String Message){
        Toast.makeText(context, Message, Toast.LENGTH_SHORT).show();
    }

    public void showAlert(String title, String message){
        alertDialog = new AlertDialog.Builder(context);
        alertDialog.setTitle(title)
                .setMessage(message)
                .setPositiveButton("Ok",null);
        dialog = alertDialog.create();
        dialog.show();
    }
}
