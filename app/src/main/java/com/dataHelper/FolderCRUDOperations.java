package com.dataHelper;

import android.annotation.SuppressLint;
import android.content.ContentResolver;
import android.content.Context;
import android.content.Intent;
import android.database.Cursor;
import android.net.Uri;
import android.provider.OpenableColumns;
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

    public void insertData(List<Uri> uris, RecyclerView recyclerView){
        StorageReference storageReference = DatabaseLinks.getInstituteRef(ins_id);

        if(uris!=null){
            recyclerView.setVisibility(View.GONE);
            for (Uri uri : uris) {
                String imageName = getFileName(uri);
                storageReference.child(imageName).putFile(uri).addOnSuccessListener((UploadTask.TaskSnapshot taskSnapshot) -> {
                    getAllFiles(recyclerView);
                    alertOrToastMsg.ToastMsg("File saved Successfully....!");
                }).addOnFailureListener((@NonNull Exception e) -> {
                    alertOrToastMsg.showAlert("Error", e.getMessage());
                });
                progressBar.setVisibility(View.GONE);
            }

        }
        else
            progressBar.setVisibility(View.GONE);

    }

    public void insertData(Uri uri, RecyclerView recyclerView){
        StorageReference storageReference = DatabaseLinks.getInstituteRef(ins_id);

        if(uri!=null){
            recyclerView.setVisibility(View.GONE);
            String imageName = getFileName(uri);
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
            for (StorageReference fileRef : listResult.getItems()) {
                recyclerView.setVisibility(View.VISIBLE);
                fileRef.getDownloadUrl().addOnSuccessListener((Uri uri) -> {
                    String name = DatabaseLinks.getStorageRef().getReferenceFromUrl(String.valueOf(uri)).getName();
                    StudyMaterials.recyclerViewAdapter(context, uri, name, FolderCRUDOperations.this::onClick, recyclerView);
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

    public void getAllFiles(String path, RecyclerView recyclerView){
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
        String fileType = getMimeType(uri).split("/")[0];
        String fileType1 = getMimeType(uri).split("/")[1];
        if(fileType.equals("image")){
            alertOrToastMsg.ToastMsg("URI Clicked"+fileType);
            intent = new Intent(context, ImageViewer.class);
            intent.putExtra("image", uri.toString());
            context.startActivity(intent);
        }
        else if(fileType.equals("video")){
            alertOrToastMsg.ToastMsg("URI Clicked"+fileType);
            intent = new Intent(context, VideoViewer.class);
            intent.putExtra("video", uri.toString());
            context.startActivity(intent);
        }

        else if(fileType1.equals("pdf")){
            alertOrToastMsg.ToastMsg("PDF Clicked"+fileType1);
            intent = new Intent(context, PDFViewer.class);
            intent.putExtra("pdf", uri.toString());
            context.startActivity(intent);
        }
    }

    public String getFileName(Uri uri) {
        String result = null;
        if (uri.getScheme().equals("content")) {
            Cursor cursor = context.getContentResolver().query(uri, null, null, null, null);
            try {
                if (cursor != null && cursor.moveToFirst()) {
                    result = cursor.getString(cursor.getColumnIndex(OpenableColumns.DISPLAY_NAME));
                }
            } finally {
                cursor.close();
            }
        }
        if (result == null) {
            result = uri.getPath();
            int cut = result.lastIndexOf('/');
            if (cut != -1) {
                result = result.substring(cut + 1);
            }
        }
        return result;
    }
}