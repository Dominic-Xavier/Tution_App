package com.tutionapp;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.AppCompatSpinner;

import android.content.DialogInterface;
import android.graphics.Color;
import android.os.Bundle;
import android.os.Handler;
import android.text.InputType;
import android.view.ContextThemeWrapper;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.AutoCompleteTextView;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.ProgressBar;

import com.common.AlertOrToastMsg;
import com.dataHelper.CatcheData;
import com.dataHelper.DatabaseLinks;
import com.dataHelper.Node;
import com.dd.CircularProgressButton;
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
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;

public class AddSubject extends AppCompatActivity {

    private AutoCompleteTextView selectClass, selectSubject;
    private String strClassName, strSubjectName;
    private AlertOrToastMsg alertOrToastMsg = new AlertOrToastMsg(this);
    private String [] listOfClassNames, listOfSubjects;
    private AlertDialog.Builder alertDialog;
    private AlertDialog dialog;
    private LinearLayout linearLayout;
    private static List<String> allclassNameList, allsubjectNameList;
    private TextInputLayout textInputLayout;
    private DatabaseReference instutiteRef = DatabaseLinks.baseReference;
    private Map<String, Object> map = new HashMap<>();
    private static String Institute_ID;
    private TextInputEditText classes, subject;
    private TextInputEditText text;
    private Handler handler;
    private static final Map<String, List<String>> classSub = new HashMap<>();
    private static Map<String, List<String>> subDetails;
    private CircularProgressButton circularProgressButton;
    private ProgressBar progressBar;
    private static final List<AllClasses> allClassNames = new ArrayList<>();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_add_subject);

        Institute_ID = CatcheData.getData("Ins_id", this);

        selectClass = findViewById(R.id.filled_exposed_dropdown);
        selectSubject = findViewById(R.id.selectSubject);
        circularProgressButton = findViewById(R.id.progress_circular);
        progressBar = findViewById(R.id.subProgress);

        listOfClassNames = getResources().getStringArray(R.array.className);
        listOfSubjects = getResources().getStringArray(R.array.subjectName);

        allclassNameList = new ArrayList<>(Arrays.asList(listOfClassNames));
        allsubjectNameList = new ArrayList<>(Arrays.asList(listOfSubjects));
        fillAutoCompleteText();

        ArrayAdapter<String> classSelection = new ArrayAdapter<String>(this, android.R.layout.simple_list_item_1, allclassNameList);

        //AutoCompleteTextViewDesign autoCompleteTextViewDesign = new AutoCompleteTextViewDesign(this,R.layout.add_subject_design, allClassNames);

        ArrayAdapter<String> subjectSelection = new ArrayAdapter<String>(this, R.layout.support_simple_spinner_dropdown_item, allsubjectNameList);

        selectClass.setAdapter(classSelection);
        selectSubject.setAdapter(subjectSelection);

        handler = new Handler();
        handler.postAtFrontOfQueue(() -> {
            progressBar.setVisibility(View.VISIBLE);
            subDetails = getSubjectDetails();
        });

        /*allsubjectNameList.removeAll(allsubjectNameList);
        allsubjectNameList.add("Select Subject");
        allsubjectNameList.add("Add Subject");
        if(subDetails!=null)
            allsubjectNameList.addAll(subDetails.get(selectClass.getText().toString()));*/

        selectClass.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> adapterView, View view, int i, long l) {
                strClassName = adapterView.getItemAtPosition(i).toString();
                //alertOrToastMsg.ToastMsg(strClassName);
                if (strClassName != "Select Class") {
                    strSubjectName = "Select Subject";
                    subjectSelection.notifyDataSetChanged();
                }
                List<String> list1 = subDetails.get(strClassName);

                if (subDetails != null && list1 != null && !strClassName.equals("Add Class") && !strClassName.equals("Select Class")) {
                    allsubjectNameList.removeAll(allsubjectNameList);
                    allsubjectNameList.add("Select Subject");
                    allsubjectNameList.add("Add Subject");
                    for (String str : allsubjectNameList) {
                        allClassNames.add(new AllClasses(str));
                    }
                    allsubjectNameList.addAll(subDetails.get(strClassName));
                } else if (!strClassName.equals("Add Class") && !strClassName.equals("Select Class")) {
                    allsubjectNameList.removeAll(allsubjectNameList);
                    allsubjectNameList.add("Select Subject");
                    allsubjectNameList.add("Add Subject");
                }

                if (strClassName.equals("Add Class")) {
                    classes = addClasses();
                    linearLayout = new LinearLayout(AddSubject.this);
                    linearLayout.setOrientation(LinearLayout.VERTICAL);
                    textInputLayout = new TextInputLayout(AddSubject.this);
                    textInputLayout.setBoxBackgroundColor(Color.WHITE);
                    textInputLayout.addView(classes);
                    linearLayout.addView(textInputLayout);
                    alertDialog = new AlertDialog.Builder(new ContextThemeWrapper(AddSubject.this, R.style.Widget_AppCompat_ActionBar))
                            .setTitle("Add Class Name")
                            .setPositiveButton("Ok", (dialogInterface, which) -> {
                                String className = classes.getText().toString();
                                if (!className.isEmpty()) {
                                    boolean isPresent = false;
                                    for (String classNames:allclassNameList) {
                                        if(className.equals(classNames)){
                                            alertOrToastMsg.ToastMsg("Task is already present...!");
                                            isPresent = true;
                                            break;
                                        }
                                    }
                                    if(!isPresent)
                                        allclassNameList.add(className);
                                }
                            })
                            .setNegativeButton("Cancel", (DialogInterface dialogInterface, int j) -> {
                                linearLayout.removeAllViews();
                                textInputLayout.removeAllViews();
                            });
                    dialog = alertDialog.create();
                    dialog.setView(linearLayout);
                    dialog.setCancelable(false);
                    dialog.setCanceledOnTouchOutside(false);
                    dialog.show();
                    classSelection.notifyDataSetChanged();
                }
            }
        });

        selectSubject.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> adapterView, View view, int i, long l) {
                strSubjectName = adapterView.getItemAtPosition(i).toString();

                if (strSubjectName.equals("Add Subject")) {
                    subject = addSubjects();
                    alertDialog = new AlertDialog.Builder(getApplicationContext());
                    linearLayout = new LinearLayout(getApplicationContext());
                    linearLayout.setOrientation(LinearLayout.VERTICAL);
                    textInputLayout = new TextInputLayout(AddSubject.this);
                    textInputLayout.setBoxBackgroundColor(Color.WHITE);
                    textInputLayout.addView(subject);
                    linearLayout.addView(textInputLayout);
                    alertDialog = new AlertDialog.Builder(new ContextThemeWrapper(AddSubject.this, R.style.Widget_AppCompat_ActionBar))
                        .setTitle("Add Subject Name")
                        .setPositiveButton("Ok", (dialogInterface, which) -> {
                            String subName = subject.getText().toString();
                            if (!subName.equals("")){
                                boolean isPresent = false;
                                for (String  subjects : allsubjectNameList) {
                                    if(subjects.equals(subName)){
                                        alertOrToastMsg.ToastMsg("Subject Already Present...!");
                                        isPresent = true;
                                        break;
                                    }
                                }
                                if(!isPresent)
                                    allsubjectNameList.add(subName);
                            }
                            linearLayout.removeAllViews();
                        })
                        .setNegativeButton("Cancel", (dialogInterface, which) -> {
                            linearLayout.removeAllViews();
                        });
                    dialog = alertDialog.create();
                    dialog.setView(linearLayout);
                    dialog.setCancelable(false);
                    dialog.setCanceledOnTouchOutside(false);
                    dialog.show();
                }
            }
        });

        circularProgressButton.setOnClickListener((view) -> {

            strClassName = selectClass.getText().toString();
            strSubjectName = selectSubject.getText().toString();

            if (strClassName.equals("Select Class") || strSubjectName.equals("Select Subject")
                    || strClassName.equals("Add Class") || strSubjectName.equals("Add Subject")
                    ||strClassName.equals("") || strSubjectName.equals("")) {
                alertOrToastMsg.ToastMsg("Select Class and Subject");
                return;
            }

            circularProgressButton.setIndeterminateProgressMode(true); // turn on indeterminate progress
            circularProgressButton.setProgress(50); // set progress > 0 & < 100 to display indeterminate progress
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
                                circularProgressButton.setProgress(50); // set progress > 0 & < 100 to display indeterminate progress
                                circularProgressButton.setProgress(100); // set progress to 100 or -1 to indicate complete or error state
                                if (dataSnapshot.hasChild(Node.Subject.toString())) {
                                    map.put(strSubjectName, fees);
                                    instutiteRef.child(Institute_ID).child(Node.Subject.toString()).child(strClassName).updateChildren(map,
                                            (@Nullable @org.jetbrains.annotations.Nullable DatabaseError databaseError, @NonNull @NotNull DatabaseReference databaseReference) -> {
                                                if (databaseReference.child(strSubjectName).getKey().equals(strSubjectName)
                                                        && databaseReference.getKey().equals(strClassName)) {
                                                    circularProgressButton.setProgress(100); // set progress to 100 or -1 to indicate complete or error state
                                                    dialogInterface.dismiss();
                                                    linearLayout.removeAllViews();
                                                    alertOrToastMsg.ToastMsg("Values Inserted Successfully...!");
                                                    try {
                                                        Thread.sleep(1000);
                                                    } catch (InterruptedException e) {
                                                        e.printStackTrace();
                                                    }
                                                    circularProgressButton.setProgress(0); // set progress to 0 to switch back to normal state
                                                } else {
                                                    circularProgressButton.setProgress(-1); // set progress to 100 or -1 to indicate complete or error state
                                                    try {
                                                        Thread.sleep(1000);
                                                    } catch (InterruptedException e) {
                                                        e.printStackTrace();
                                                    }
                                                    dialogInterface.dismiss();
                                                    linearLayout.removeAllViews();
                                                    alertOrToastMsg.ToastMsg("Error in inserting values");
                                                    circularProgressButton.setProgress(0); // set progress to 0 to switch back to normal state
                                                }
                                            });
                                    map.clear();
                                } else {
                                    dialogInterface.dismiss();
                                    linearLayout.removeAllViews();
                                    DoInitialProcess(strSubjectName, strClassName, fees);
                                }
                            }

                            @Override
                            public void onCancelled(@NonNull @NotNull DatabaseError databaseError) {
                                alertOrToastMsg.showAlert("Error", databaseError.toString());
                                circularProgressButton.setProgress(-1); // set progress to 100 or -1 to indicate complete or error state
                                circularProgressButton.setImeOptions(5);
                                circularProgressButton.setProgress(0); // set progress to 0 to switch back to normal state
                            }
                        });
                    })
                    .setNegativeButton("Cancel", (dialogInterface, i) -> {
                        circularProgressButton.setProgress(-1); // set progress to 100 or -1 to indicate complete or error state
                        linearLayout.removeAllViews();
                        circularProgressButton.setImeOptions(View.TEXT_DIRECTION_INHERIT);
                        circularProgressButton.setProgress(0); // set progress to 0 to switch back to normal state
                        strClassName = "Select Class";
                        classSelection.notifyDataSetChanged();
                    });
            dialog = alertDialog.create();
            dialog.setView(linearLayout);
            dialog.setCanceledOnTouchOutside(false);
            dialog.setCancelable(false);
            dialog.show();

            alertOrToastMsg.ToastMsg("Class Name:" + strClassName + " Subject Name:" + strSubjectName);
        });
    }

    private Map<String, List<String>> getSubjectDetails(){
        instutiteRef.child(Institute_ID).addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                if(dataSnapshot.hasChild(Node.Subject.toString())){
                    for (DataSnapshot classNames : dataSnapshot.child(Node.Subject.toString()).getChildren()) {
                        String className = classNames.getKey();
                        allclassNameList.add(className);
                        allClassNames.add(new AllClasses(className));
                        List<String> subNames = new ArrayList<>();
                        for (DataSnapshot sub : dataSnapshot.child(Node.Subject.toString()).child(className).getChildren()) {
                            String subName = sub.getKey();
                            subNames.add(subName);
                            classSub.put(className, subNames);
                        }
                    }
                    System.out.println("Subject Details are "+classSub);
                }
                progressBar.setVisibility(View.GONE);
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {
                alertOrToastMsg.showAlert("Error", databaseError.toString());
            }
        });
        return classSub;
    }

    private void DoInitialProcess(String subjectName, String className, String fees) {
        instutiteRef.child(Institute_ID).addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull @NotNull DataSnapshot dataSnapshot) {
                if(!dataSnapshot.hasChild(Node.Subject.toString())){
                    map.put(subjectName,fees);
                    instutiteRef.child(Institute_ID).child(Node.Subject.toString()).child(className).setValue(map,
                            (@Nullable @org.jetbrains.annotations.Nullable DatabaseError databaseError, @NonNull @NotNull DatabaseReference databaseReference) -> {
                        if(databaseReference.getKey().equals(map.get("subjectName"))){
                            alertOrToastMsg.ToastMsg("Values Inserted Successfully...!"+databaseReference.toString());
                            circularProgressButton.setProgress(100); // set progress to 100 or -1 to indicate complete or error state
                            circularProgressButton.setProgress(0); // set progress to 0 to switch back to normal state
                        }

                        if(databaseError!=null){
                            circularProgressButton.setProgress(-1); // set progress to 100 or -1 to indicate complete or error state
                            alertOrToastMsg.showAlert("Error", databaseError.toString());
                            circularProgressButton.setProgress(0); // set progress to 0 to switch back to normal state
                        }
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

    private void fillAutoCompleteText(){
        allClassNames.add(new AllClasses("Select Class"));
        allClassNames.add(new AllClasses("Add Class"));
    }

    @Override
    public void onBackPressed() {
        finish();
    }
}