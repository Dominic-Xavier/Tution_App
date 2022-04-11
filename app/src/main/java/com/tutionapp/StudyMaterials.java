package com.tutionapp;

import androidx.annotation.NonNull;
import androidx.annotation.RequiresApi;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;

import android.content.DialogInterface;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.os.Handler;
import android.view.View;
import android.widget.AbsListView;
import android.widget.EditText;
import android.widget.GridView;
import android.widget.LinearLayout;

import com.common.AlertOrToastMsg;
import com.dataHelper.CatcheData;
import com.dataHelper.DatabaseLinks;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.storage.StorageReference;
import com.google.firebase.storage.UploadTask;
import com.gridViewAdapter.Folder;
import com.gridViewAdapter.FolderAdapter;
import com.gridViewAdapter.FolderClass;
import com.tuann.floatingactionbuttonexpandable.FloatingActionButtonExpandable;

import java.io.File;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class StudyMaterials extends AppCompatActivity implements Folder {

    private FloatingActionButtonExpandable actionButtonExpandable;
    private AlertDialog.Builder builder;
    private LinearLayout linearLayout;
    private GridView gridView;
    private FolderAdapter folderAdapter;
    private List<String> folderNAme;

    int myLastVisiblePos;
    private AlertOrToastMsg alertOrToastMsg;
    private static final Map<String, List<String>> folders = new HashMap<>();
    private Handler handler;
    private StorageReference reference;

    @RequiresApi(api = Build.VERSION_CODES.M)
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.studymaterials);

        alertOrToastMsg = new AlertOrToastMsg(this);
        gridView = findViewById(R.id.grid_view);

        String folder = getIntent().getStringExtra("FolderNAme");
        folderNAme = new ArrayList<>();
        if(folder!=null){
            FolderClass.loadData(folder, folderNAme);
            FolderClass.folderPath(folder);
        }

        List<String> listFolder = FolderClass.getSubFolder(folder);
        if(listFolder!=null)
            folderNAme.addAll(listFolder);

        actionButtonExpandable = findViewById(R.id.createFolder);

        alertOrToastMsg.ToastMsg("Folder "+folder);

        folderAdapter = new FolderAdapter(this, folderNAme, StudyMaterials.this, folders);
        gridView.setAdapter(folderAdapter);
        gridView.setOnScrollListener(new AbsListView.OnScrollListener() {
            @Override
            public void onScrollStateChanged(AbsListView absListView, int i) {

            }

            @Override
            public void onScroll(AbsListView absListView, int firstVisibleItem, int visibleItemCount, int totalItemCount) {
                int currentFirstVisPos = absListView.getFirstVisiblePosition();
                if(currentFirstVisPos > myLastVisiblePos)
                    //scroll down
                    actionButtonExpandable.collapse(true);
                if(currentFirstVisPos < myLastVisiblePos)
                    //scroll up
                    actionButtonExpandable.expand(true);
                myLastVisiblePos = currentFirstVisPos;
            }
        });

        actionButtonExpandable.setOnClickListener((v) -> {
            linearLayout = new LinearLayout(StudyMaterials.this);
            linearLayout.setOrientation(LinearLayout.VERTICAL);
            EditText editText = getFolderName();
            linearLayout.addView(editText);
            builder = new AlertDialog.Builder(StudyMaterials.this);
            builder.setTitle("Create Folder")
                    .setPositiveButton("Create", (dialogInterface, i) -> {
                            String folderName = editText.getText().toString();
                            if(folderName.isEmpty() || folderName==null)
                                alertOrToastMsg.ToastMsg("Folder cannot be empty...!");
                            else
                                folderNAme.add(folderName);
                            //folderAdapter = new FolderAdapter(StudyMaterials.this, folderNAme);
                            linearLayout.removeAllViews();
                    })
                    .setNegativeButton("Cancel", (dialogInterface, i) -> {
                        linearLayout.removeAllViews();
                        linearLayout = null;
                    });
            AlertDialog dialog = builder.create();
            dialog.setView(linearLayout);
            dialog.setCancelable(false);
            dialog.setCanceledOnTouchOutside(false);
            dialog.show();
        });
    }

    private EditText getFolderName(){
        EditText editText = new EditText(this);
        editText.setHint("Folder Name");
        editText.setTextSize(15);
        return editText;
    }

    @Override
    public void getFolderItemPosition(int position) {
        String folder = folderNAme.get(position);
        List<String> folderName = new ArrayList<>();
        folderName.addAll(folderName);
        folders.put(folder, folderName);
        folderName.addAll(folders.get(folder));
    }

    @Override
    public void getListOfFolders(List<String> foldersList) {
        folderNAme.addAll(foldersList);
    }

    @Override
    public void onBackPressed() {
        super.onBackPressed();
        finish();
    }
}