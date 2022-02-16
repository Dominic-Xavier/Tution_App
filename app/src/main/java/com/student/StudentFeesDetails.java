package com.student;

import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.graphics.Color;
import android.graphics.Typeface;
import android.os.Bundle;
import android.text.InputType;
import android.view.ContextThemeWrapper;
import android.view.View;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.common.AlertOrToastMsg;
import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.google.android.material.textfield.TextInputEditText;
import com.google.android.material.textfield.TextInputLayout;
import com.tutionapp.R;

public class StudentFeesDetails extends AppCompatActivity {

    private FloatingActionButton floatingActionButton;
    private AlertOrToastMsg alertOrToastMsg = new AlertOrToastMsg(this);
    private Intent intent;
    private AlertDialog.Builder builder;
    private AlertDialog alertDialog;
    private LinearLayout linearLayout;
    private TextInputLayout forDate, forFees;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_fees_details);

        floatingActionButton = findViewById(R.id.floating_button);
        intent = getIntent();
        String Stu_id = intent.getStringExtra("Stu_id");
        alertOrToastMsg.ToastMsg("Student Id:"+Stu_id);

        floatingActionButton.setOnClickListener((View view) -> {
            forDate = new TextInputLayout(this);
            forFees = new TextInputLayout(this);
            linearLayout = new LinearLayout(this);
            linearLayout.setOrientation(LinearLayout.VERTICAL);
            TextInputEditText getFees = addFeeDetails();
            TextInputEditText getDate = addDate();
            forDate.addView(getDate);
            forDate.setBoxBackgroundColor(Color.WHITE);
            forFees.addView(getFees);
            forFees.setBoxBackgroundColor(Color.WHITE);
            linearLayout.addView(displayDate());
            linearLayout.addView(forDate);
            linearLayout.addView(displayfees());
            linearLayout.addView(forFees);
            builder = new AlertDialog.Builder(new ContextThemeWrapper(StudentFeesDetails.this,
                    R.style.Widget_AppCompat_ActionBar))
                    .setTitle("Enter Fees")
                    .setPositiveButton("Ok", (dialogInterface, i) -> {
                        String date = getDate.getText().toString();
                        String fees = getFees.getText().toString();
                        if(!date.isEmpty() && !fees.isEmpty())
                            alertOrToastMsg.ToastMsg("Date "+date+" Fees "+fees);
                        linearLayout.removeAllViews();
                    })
                    .setNegativeButton("Calcel", (dialogInterface, i) -> {
                        linearLayout.removeAllViews();
                    });
            alertDialog = builder.create();
            alertDialog.setView(linearLayout);
            alertDialog.setCancelable(false);
            alertDialog.setCanceledOnTouchOutside(false);
            alertDialog.show();
        });
    }

    private TextView displayDate(){
        TextView tv = new TextView(this);
        tv.setText("Date");
        tv.setTextSize(20);
        tv.setTypeface(Typeface.defaultFromStyle(Typeface.BOLD));
        return tv;
    }

    private TextView displayfees(){
        TextView tv = new TextView(this);
        tv.setText("Fees");
        tv.setTextSize(20);
        tv.setTypeface(Typeface.defaultFromStyle(Typeface.BOLD));
        return tv;
    }

    public TextInputEditText addFeeDetails(){
        TextInputEditText textInputEditText = new TextInputEditText(new ContextThemeWrapper(StudentFeesDetails.this,
                R.style.Widget_MaterialComponents_TextInputLayout_OutlinedBox_Dense));
        textInputEditText.setHint("Enter Fees");
        textInputEditText.setTextSize(20);
        textInputEditText.setInputType(InputType.TYPE_CLASS_NUMBER);
        return textInputEditText;
    }

    public TextInputEditText addDate(){
        TextInputEditText textInputEditText = new TextInputEditText(new ContextThemeWrapper(StudentFeesDetails.this,
                R.style.Widget_MaterialComponents_TextInputLayout_OutlinedBox_Dense));
        textInputEditText.setHint("Enter Date");
        textInputEditText.setTextSize(20);
        textInputEditText.setInputType(InputType.TYPE_CLASS_DATETIME);
        return textInputEditText;
    }

    @Override
    public void onBackPressed() {
        finish();
    }
}