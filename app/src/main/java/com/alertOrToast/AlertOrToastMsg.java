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
    private LinearLayout linearLayout;
    private EditText editText;
    private String hint;

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

    public String showAlertEditText(String title, String hint){
        this.hint = hint;
        String[] text = new String[1];
        linearLayout = new LinearLayout(context);
        alertDialog = new AlertDialog.Builder(context);
        editText = alertText();
        linearLayout.addView(editText);
        alertDialog.setTitle(title)
                .setPositiveButton("Ok",(dialogInterface, num)-> {
                    text[0] = editText.getText().toString();
                    if(text[0].isEmpty() || text[0]==null)
                        ToastMsg("Type Something...!");
                })
                .setNegativeButton("Cancel",(dialogInterface, i) -> {
                    linearLayout.removeAllViews();
                });
        dialog = alertDialog.create();
        dialog.setView(linearLayout);
        dialog.setCancelable(false);
        dialog.setCanceledOnTouchOutside(false);
        dialog.show();

        return text[0];
    }

    private TextInputEditText alertText(){
        TextInputEditText editText = new TextInputEditText(context);
        editText.setHint(hint);
        editText.setInputType(InputType.TYPE_CLASS_NUMBER);
        editText.setTextSize(15);
        return editText;
    }
}
