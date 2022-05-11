package com.tutionapp;

import androidx.activity.result.ActivityResultLauncher;
import androidx.activity.result.contract.ActivityResultContracts;
import androidx.annotation.AnyRes;
import androidx.annotation.NonNull;
import androidx.annotation.RequiresApi;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.app.DownloadManager;
import android.content.ContentResolver;
import android.content.Context;
import android.content.Intent;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.os.Handler;

import android.view.View;
import android.webkit.MimeTypeMap;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.LinearLayout;
import android.widget.ProgressBar;

import com.common.AlertOrToastMsg;
import com.dataHelper.CatcheData;
import com.dataHelper.FolderCRUDOperations;
import com.fileViewer.ImageViewer;
import com.fileViewer.PDFViewer;
import com.fileViewer.VideoViewer;
import com.google.android.material.bottomsheet.BottomSheetDialog;

import com.gridViewAdapter.Folder;
import com.gridViewAdapter.FolderClass;
import com.nostra13.universalimageloader.cache.memory.impl.WeakMemoryCache;
import com.nostra13.universalimageloader.core.DisplayImageOptions;
import com.nostra13.universalimageloader.core.ImageLoader;
import com.nostra13.universalimageloader.core.ImageLoaderConfiguration;
import com.nostra13.universalimageloader.core.assist.ImageScaleType;
import com.nostra13.universalimageloader.core.display.FadeInBitmapDisplayer;
import com.recyclerViewAdapters.FileListener;
import com.recyclerViewAdapters.FolderAdapter;
import com.tuann.floatingactionbuttonexpandable.FloatingActionButtonExpandable;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class StudyMaterials extends AppCompatActivity implements Folder, FileListener {

    private FloatingActionButtonExpandable actionButtonExpandable;
    private AlertDialog.Builder builder;
    private LinearLayout linearLayout;
    private static RecyclerView gridView;
    private List<String> folderNAme;
    private static AlertOrToastMsg alertOrToastMsg;
    private static final Map<String, List<String>> folders = new HashMap<>();
    private Handler handler;
    ActivityResultLauncher mGetContent;
    private static final int ImageForLoading = R.drawable.icons8_file_512;
    private static FolderCRUDOperations folderCRUDOperations;
    private static GridLayoutManager gridLayoutManager;
    private ProgressBar progressBar;

    @RequiresApi(api = Build.VERSION_CODES.M)
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.studymaterials);

        actionButtonExpandable = findViewById(R.id.createFolder);
        gridView = findViewById(R.id.recycler_Grid_View);
        progressBar = findViewById(R.id.loadFiles);
        alertOrToastMsg = new AlertOrToastMsg(this);
        gridView.setVisibility(View.GONE);

        handler = new Handler();
        handler.postAtFrontOfQueue(() -> {
            //Load the folder details
            folderCRUDOperations = new FolderCRUDOperations(StudyMaterials.this,
                    CatcheData.getData("Ins_id", StudyMaterials.this), progressBar);
            ImageLoader.getInstance().init(getConfig());
            progressBar.setVisibility(View.VISIBLE);
            folderCRUDOperations.getAllFiles(gridView);
        });

        mGetContent = registerForActivityResult(
            new ActivityResultContracts.GetContent(),
            result-> {
                progressBar.setVisibility(View.VISIBLE);
                folderCRUDOperations.insertData(result, gridView);
                //This commented code is for GridView Adapter
                /*folderAdapter = new FolderAdapter(StudyMaterials.this, folderNAme, StudyMaterials.this, folders, uriList);
                gridView.setAdapter(folderAdapter);*/

            });

        String folder = getIntent().getStringExtra("FolderNAme");
        folderNAme = new ArrayList<>();
        String folderPath = null;
        if(folder!=null){
            folderPath = FolderClass.getFolderPath();
            alertOrToastMsg.ToastMsg("Folder Path: "+folderPath);
        }

        List<String> listFolder = FolderClass.getSubFolder(folder);
        if(listFolder!=null)
            folderNAme.addAll(listFolder);

        gridView.addOnScrollListener(new RecyclerView.OnScrollListener() {
            @Override
            public void onScrolled(@NonNull RecyclerView recyclerView, int dx, int dy) {
            if(dy>0)
                actionButtonExpandable.collapse(true);
            else
                actionButtonExpandable.expand(true);
            }
        });

        actionButtonExpandable.setOnClickListener((v) -> {

            BottomSheetDialog bottomView = new BottomSheetDialog(StudyMaterials.this);
            bottomView.setContentView(R.layout.bottom_view);
            ImageButton uploadFolder = bottomView.findViewById(R.id.create_folder);
            ImageButton uploadFile = bottomView.findViewById(R.id.uploadFile);
            uploadFolder.setOnClickListener((vi) -> {
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
                            else{
                                recyclerViewAdapter(StudyMaterials.this, getUriToDrawable(StudyMaterials.this, R.drawable.ic_baseline_folder_64),
                                        folderName, StudyMaterials.this::onClick, gridView);
                            }
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
                bottomView.cancel();
            });

            uploadFile.setOnClickListener((View view) -> {
                mGetContent.launch("*/*");
                bottomView.cancel();
            });
            bottomView.show();
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

    public ImageLoaderConfiguration getConfig(){
        DisplayImageOptions defaultOptions = new DisplayImageOptions.Builder()
                .showImageOnLoading(ImageForLoading)
                .cacheOnDisk(true).cacheInMemory(true)
                .cacheOnDisk(true).resetViewBeforeLoading(true)
                .imageScaleType(ImageScaleType.EXACTLY)
                .displayer(new FadeInBitmapDisplayer(300)).build();
        ImageLoaderConfiguration configuration = new ImageLoaderConfiguration.Builder(getApplicationContext())
                .defaultDisplayImageOptions(defaultOptions)
                .memoryCache(new WeakMemoryCache())
                .diskCacheSize(100 * 1024 * 1024)
                .build();
        return configuration;
    }

    @Override
    public void onBackPressed() {
        super.onBackPressed();
        finish();
    }

    /**
     * get uri to drawable or any other resource type if u wish
     * @param context - context
     * @param drawableId - drawable res id
     * @return - uri
     */
    public static final Uri getUriToDrawable(@NonNull Context context,
                                             @AnyRes int drawableId) {
        Uri imageUri = Uri.parse(ContentResolver.SCHEME_ANDROID_RESOURCE
                + "://" + context.getResources().getResourcePackageName(drawableId)
                + '/' + context.getResources().getResourceTypeName(drawableId)
                + '/' + context.getResources().getResourceEntryName(drawableId) );
        return imageUri;
    }



    public static void recyclerViewAdapter(Context context, Uri list, String FileName, FileListener fileListener, RecyclerView recyclerView){

        FolderAdapter folderAdapter  = FolderAdapter.getInstance(context, list, FileName, fileListener);
        recyclerView.setAdapter(folderAdapter);
        gridLayoutManager = new GridLayoutManager(context, 2, GridLayoutManager.VERTICAL, false);
        recyclerView.setLayoutManager(gridLayoutManager);
    }

    @Override
    public void onClick(Uri uri) {

    }
}