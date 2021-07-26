package com.alertOrToast;

import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.text.InputType;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.Toast;

import com.google.android.material.textfield.TextInputEditText;

public class AlertOrToastMsg {

    private AlertDialog.Builder alertDialog;
    private AlertDialog dialog;
    private Context context;
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
