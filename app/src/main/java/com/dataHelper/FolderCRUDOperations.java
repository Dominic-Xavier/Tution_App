package com.dataHelper;

import android.content.ContentResolver;
import android.content.Context;
import android.content.Intent;
import android.net.Uri;
import android.view.View;
import android.webkit.MimeTypeMap;
import android.widget.ProgressBar;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.common.AlertOrToastMsg;
import com.fileViewer.ImageViewer;
import com.fileViewer.PDFViewer;
import com.fileViewer.VideoViewer;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.storage.ListResult;
import com.google.firebase.storage.StorageReference;
import com.google.firebase.storage.UploadTask;
import com.recyclerViewAdapters.FileListener;
import com.tutionapp.StudyMaterials;

import java.util.ArrayList;
import java.util.List;

public class FolderCRUDOperations implements FileListener {

    Context context;
    String ins_id;
    AlertOrToastMsg alertOrToastMsg;
    static List<Uri> uriList;
    Intent intent;
    ProgressBar progressBar;

    public FolderCRUDOperations(Context context, String ins_id, ProgressBar progressBar){
        this.context = context;
        this.ins_id = ins_id;
        this.progressBar = progressBar;
        alertOrToastMsg = new AlertOrToastMsg(context);
    }

    public void insertData(String path, String imageName, Uri uri){
        StorageReference storageReference = DatabaseLinks.getInstituteRef(ins_id);
        storageReference.child(path).child(imageName).putFile(uri).addOnSuccessListener((UploadTask.TaskSnapshot taskSnapshot) -> {
            alertOrToastMsg.ToastMsg("File saved Successfully....!");
        });
    }

    public void insertData(Uri uri, RecyclerView recyclerView){
        StorageReference storageReference = DatabaseLinks.getInstituteRef(ins_id);
        long milliSec =  System.currentTimeMillis();

        if(uri!=null){
            recyclerView.setVisibility(View.GONE);
            String imageName = milliSec+"."+getFileExtension(uri);
            storageReference.child(imageName).putFile(uri).addOnSuccessListener((UploadTask.TaskSnapshot taskSnapshot) -> {
                getAllFiles(recyclerView);
                alertOrToastMsg.ToastMsg("File saved Successfully....!");
            }).addOnFailureListener((@NonNull Exception e) -> {
                alertOrToastMsg.showAlert("Error", e.getMessage());
                progressBar.setVisibility(View.GONE);
            });
        }
        else
            progressBar.setVisibility(View.GONE);

    }

    public void getAllFiles(RecyclerView recyclerView){
        StorageReference storageReference = DatabaseLinks.getInstituteRef(ins_id);
        storageReference.listAll().addOnSuccessListener((ListResult listResult) -> {
            uriList = new ArrayList<>();
            for (StorageReference fileRef : listResult.getItems()) {
                recyclerView.setVisibility(View.VISIBLE);
                fileRef.getDownloadUrl().addOnSuccessListener((Uri uri) -> {
                    uriList.add(uri);
                    StudyMaterials.recyclerViewAdapter(context, uriList, FolderCRUDOperations.this::onClick, recyclerView);
                });
            }
            //StudyMaterials.recyclerViewAdapter(context, uriList, FolderCRUDOperations.this::onClick);
        }).addOnSuccessListener((listResult)-> {
            //StudyMaterials.recyclerViewAdapter(context, uriList, FolderCRUDOperations.this::onClick);
            progressBar.setVisibility(View.GONE);
        }).addOnFailureListener((@NonNull Exception e) -> {
            alertOrToastMsg.showAlert("Error Occurred", e.getMessage());
            progressBar.setVisibility(View.GONE);
        });
    }

    public void getAllFiles(String path){
        StorageReference storageReference = DatabaseLinks.getInstituteRef(ins_id);
    }

    private String getFileExtension(Uri image) {
        ContentResolver contentResolver = context.getContentResolver();

        MimeTypeMap typeMap = MimeTypeMap.getSingleton();

        return typeMap.getExtensionFromMimeType(contentResolver.getType(image));
    }

    public String getMimeType(Uri uri) {
        String mimeType = null;
        if (ContentResolver.SCHEME_CONTENT.equals(uri.getScheme())) {
            ContentResolver cr = context.getContentResolver();
            mimeType = cr.getType(uri);
        } else {
            String fileExtension = MimeTypeMap.getFileExtensionFromUrl(uri
                    .toString());
            mimeType = MimeTypeMap.getSingleton().getMimeTypeFromExtension(
                    fileExtension.toLowerCase());
        }
        return mimeType;
    }

    @Override
    public void onClick(Uri uri) {
        if(getMimeType(uri).split("/")[0].equals("image")){
            alertOrToastMsg.ToastMsg("URI Clicked"+uri.toString());
            intent = new Intent(context, ImageViewer.class);
            intent.putExtra("image", uri.toString());
            context.startActivity(intent);
        }
        else if(getMimeType(uri).split("/")[0].equals("video")){
            alertOrToastMsg.ToastMsg("URI Clicked"+uri.toString());
            intent = new Intent(context, VideoViewer.class);
            intent.putExtra("video", uri.toString());
            context.startActivity(intent);
        }

        else if(getMimeType(uri).split("/")[1].equals("pdf")){
            alertOrToastMsg.ToastMsg("PDF Clicked"+uri.toString());
            intent = new Intent(context, PDFViewer.class);
            intent.putExtra("pdf", uri.toString());
            context.startActivity(intent);
        }
    }
}