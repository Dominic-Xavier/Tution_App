package com.dataHelper;

import android.content.Context;
import android.net.Uri;

import com.common.AlertOrToastMsg;
import com.google.firebase.storage.StorageReference;
import com.google.firebase.storage.UploadTask;

public class FolderCRUDOperations {

    Context context;
    String ins_id;
    AlertOrToastMsg alertOrToastMsg;

    public FolderCRUDOperations(Context context, String ins_id){
        this.context = context;
        this.ins_id = ins_id;
        alertOrToastMsg = new AlertOrToastMsg(context);
    }

    public void createFolder(String path, Uri uri){
        StorageReference storageReference = DatabaseLinks.getInstituteRef(ins_id);
        storageReference.child(path).putFile(uri).addOnSuccessListener((UploadTask.TaskSnapshot taskSnapshot) -> {
            alertOrToastMsg.ToastMsg("File saved Successfully....!");
        });
    }
}