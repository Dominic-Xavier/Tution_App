package com.tutionapp;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.AppCompatSpinner;

import android.content.DialogInterface;
import android.graphics.Color;
import android.os.Bundle;
import android.text.InputType;
import android.view.ContextThemeWrapper;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.LinearLayout;

import com.common.AlertOrToastMsg;
import com.dataHelper.CatcheData;
import com.dataHelper.Node;
import com.google.android.material.textfield.TextInputEditText;
import com.google.android.material.textfield.TextInputLayout;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import org.jetbrains.annotations.NotNull;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class AddSubject extends AppCompatActivity {

    private AppCompatSpinner selectClass, selectSubject;
    private Button Submit;
    private String strClassName, strSubjectName;
    private AlertOrToastMsg alertOrToastMsg = new AlertOrToastMsg(this);
    private String [] listOfClassNames, listOfSubjects;
    private AlertDialog.Builder alertDialog;
    private AlertDialog dialog;
    private LinearLayout linearLayout;
    private List<String> allclassNameList, allsubjectNameList;
    private TextInputLayout textInputLayout;
    private DatabaseReference instutiteRef = FirebaseDatabase.getInstance("https://tutor-project-1cc32-default-rtdb.firebaseio.com/").getReference()
            .child(Node.Institutes.toString());
    private Map<String, String> map = new HashMap<>();
    private static String Institute_ID;
    private TextInputEditText classes, subject;
    private TextInputEditText text;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_add_subject);

        Institute_ID = CatcheData.getData("Ins_id", this);

        selectClass = findViewById(R.id.selectClass);
        selectSubject = findViewById(R.id.selectSubject);
        Submit = findViewById(R.id.submitSubject);
        listOfClassNames = getResources().getStringArray(R.array.className);
        listOfSubjects = getResources().getStringArray(R.array.subjectName);

        allclassNameList = new ArrayList<>(Arrays.asList(listOfClassNames));
        allsubjectNameList = new ArrayList<>(Arrays.asList(listOfSubjects));

        ArrayAdapter<String> classSelection = new ArrayAdapter<String>(this,R.layout.support_simple_spinner_dropdown_item, allclassNameList);

        ArrayAdapter<String> subjectSelection = new ArrayAdapter<String>(this,R.layout.support_simple_spinner_dropdown_item, allsubjectNameList);

        selectClass.setAdapter(classSelection);
        selectSubject.setAdapter(subjectSelection);

        selectClass.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> adapterView, View view, int i, long l) {
                strClassName = adapterView.getItemAtPosition(i).toString();
                if(strClassName.equals("Add Class")){
                    classes = addClasses();
                    linearLayout = new LinearLayout(AddSubject.this);
                    linearLayout.setOrientation(LinearLayout.VERTICAL);
                    textInputLayout = new TextInputLayout(AddSubject.this);
                    textInputLayout.setBoxBackgroundColor(Color.WHITE);
                    textInputLayout.addView(classes);
                    linearLayout.addView(textInputLayout);
                    alertDialog = new AlertDialog.Builder(new ContextThemeWrapper(AddSubject.this,R.style.Widget_AppCompat_ActionBar))
                            .setTitle("Add Class Name")
                            .setPositiveButton("Ok",(dialogInterface, which) -> {
                                String className = classes.getText().toString();
                                if(!className.isEmpty())
                                    allclassNameList.add(className);
                            })
                            .setNegativeButton("Cancel", new DialogInterface.OnClickListener() {
                                @Override
                                public void onClick(DialogInterface dialogInterface, int i) {
                                    linearLayout.removeAllViews();
                                    textInputLayout.removeAllViews();
                                }
                            });
                    dialog = alertDialog.create();
                    dialog.setView(linearLayout);
                    dialog.setCancelable(false);
                    dialog.setCanceledOnTouchOutside(false);
                    dialog.show();
                }
            }

            @Override
            public void onNothingSelected(AdapterView<?> adapterView) {
                alertOrToastMsg.ToastMsg("Please Select any option");
            }
        });

        selectSubject.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> adapterView, View view, int i, long l) {
                strSubjectName = adapterView.getItemAtPosition(i).toString();
                if(strSubjectName.equals("Add Subject")){
                    subject = addSubjects();
                    alertDialog = new AlertDialog.Builder(getApplicationContext());
                    linearLayout = new LinearLayout(getApplicationContext());
                    linearLayout.setOrientation(LinearLayout.VERTICAL);
                    textInputLayout = new TextInputLayout(AddSubject.this);
                    textInputLayout.setBoxBackgroundColor(Color.WHITE);
                    textInputLayout.addView(subject);
                    linearLayout.addView(textInputLayout);
                    alertDialog = new AlertDialog.Builder(new ContextThemeWrapper(AddSubject.this,R.style.Widget_AppCompat_ActionBar))
                            .setTitle("Add Subject Name")
                            .setPositiveButton("Ok",(dialogInterface, which) -> {
                                String subName = subject.getText().toString();
                                if(!subName.equals(""))
                                    allsubjectNameList.add(subName);
                                linearLayout.removeAllViews();
                            })
                            .setNegativeButton("Calcel", (dialogInterface, which)-> {
                                linearLayout.removeAllViews();
                            });
                    dialog = alertDialog.create();
                    dialog.setView(linearLayout);
                    dialog.setCancelable(false);
                    dialog.setCanceledOnTouchOutside(false);
                    dialog.show();
                }
            }

            @Override
            public void onNothingSelected(AdapterView<?> adapterView) {
                alertOrToastMsg.ToastMsg("Please Select any option");
            }
        });

        Submit.setOnClickListener((view) -> {

            if(strClassName.equals("Select Class") || strSubjectName.equals("Select Subject") || strClassName.equals("Add Class") || strSubjectName.equals("Add Subject")){
                alertOrToastMsg.ToastMsg("Select Class or Subject");
                return;
            }

            linearLayout = new LinearLayout(this);
            linearLayout.setOrientation(LinearLayout.VERTICAL);
            text = addFeeDetails();
            linearLayout.addView(text);
            alertDialog = new AlertDialog.Builder(this)
                .setTitle("Select Fees")
                .setPositiveButton("Ok", (dialogInterface, i) -> {
                    String fees = text.getText().toString();
                        instutiteRef.child(Institute_ID).addListenerForSingleValueEvent(new ValueEventListener() {
                            @Override
                            public void onDataChange(@NonNull @NotNull DataSnapshot dataSnapshot) {
                                if(dataSnapshot.hasChild(Node.Subject.toString())){
                                    map.put(strSubjectName, fees);
                                    instutiteRef.child(Institute_ID).child(Node.Subject.toString()).child(strClassName).setValue(map,
                                            (@Nullable @org.jetbrains.annotations.Nullable DatabaseError databaseError, @NonNull @NotNull DatabaseReference databaseReference) -> {
                                                if(databaseReference.child(strSubjectName).getKey().equals(strSubjectName)
                                                        && databaseReference.getKey().equals(strClassName)){
                                                    dialogInterface.dismiss();
                                                    linearLayout.removeAllViews();
                                                    alertOrToastMsg.ToastMsg("Values Inserted Successfully...!");
                                                }
                                                else{
                                                    dialogInterface.dismiss();
                                                    linearLayout.removeAllViews();
                                                    alertOrToastMsg.ToastMsg("Error in inserting values");
                                                }
                                            });
                                    map.clear();
                                }
                                else{
                                    dialogInterface.dismiss();
                                    linearLayout.removeAllViews();
                                    DoInitialProcess(strSubjectName, strClassName, fees);
                                }
                            }

                            @Override
                            public void onCancelled(@NonNull @NotNull DatabaseError databaseError) {
                                alertOrToastMsg.showAlert("Error", databaseError.toString());
                            }
                        });
                    })
                    .setNegativeButton("Cancel", (dialogInterface, i) -> {
                        linearLayout.removeAllViews();
                    });
            dialog = alertDialog.create();
            dialog.setView(linearLayout);
            dialog.setCanceledOnTouchOutside(false);
            dialog.setCancelable(false);
            dialog.show();

            alertOrToastMsg.ToastMsg("Class Name:"+strClassName+" Subject Name:"+strSubjectName);
        });
    }

    private void DoInitialProcess(String subjectName, String className, String fees) {
        instutiteRef.child(Institute_ID).addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull @NotNull DataSnapshot dataSnapshot) {
                if(!dataSnapshot.hasChild(Node.Subject.toString())){
                    map.put(subjectName,fees);
                    instutiteRef.child(Institute_ID).child(Node.Subject.toString()).child(className).setValue(map,
                            (@Nullable @org.jetbrains.annotations.Nullable DatabaseError databaseError, @NonNull @NotNull DatabaseReference databaseReference) -> {
                        if(databaseReference.getKey().equals(map.get("subjectName")))
                            alertOrToastMsg.ToastMsg("Values Inserted Successfully...!"+databaseReference.toString());
                        if(databaseError!=null)
                            alertOrToastMsg.showAlert("Error", databaseError.toString());
                    });
                }
                map.clear();
            }

            @Override
            public void onCancelled(@NonNull @NotNull DatabaseError databaseError) {
                alertOrToastMsg.showAlert("Error", databaseError.toString());
            }
        });
    }


    public TextInputEditText addSubjects(){
        TextInputEditText textInputEditText = new TextInputEditText(new ContextThemeWrapper(AddSubject.this,
                R.style.Widget_MaterialComponents_TextInputLayout_OutlinedBox_Dense));
        textInputEditText.setHint("Add Subject");
        textInputEditText.setTextSize(20);
        return textInputEditText;
    }

    private TextInputEditText addClasses(){
        TextInputEditText textInputEditText = new TextInputEditText(new ContextThemeWrapper(AddSubject.this,
                R.style.Widget_MaterialComponents_TextInputLayout_OutlinedBox_Dense));
        textInputEditText.setHint("Class Name");
        textInputEditText.setTextSize(20);
        return textInputEditText;
    }

    public TextInputEditText addFeeDetails(){
        TextInputEditText textInputEditText = new TextInputEditText(new ContextThemeWrapper(AddSubject.this,
                R.style.Widget_MaterialComponents_TextInputLayout_OutlinedBox_Dense));
        textInputEditText.setHint("Enter Fees");
        textInputEditText.setTextSize(20);
        textInputEditText.setInputType(InputType.TYPE_CLASS_NUMBER);
        return textInputEditText;
    }

    @Override
    public void onBackPressed() {
        finish();
    }
}