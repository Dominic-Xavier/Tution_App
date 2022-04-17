package com.recyclerViewAdapters;

import android.content.ContentResolver;
import android.content.Context;
import android.graphics.Bitmap;
import android.media.ThumbnailUtils;
import android.net.Uri;
import android.provider.MediaStore;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.webkit.MimeTypeMap;
import android.widget.ImageView;

import androidx.annotation.NonNull;
import androidx.appcompat.widget.AppCompatImageButton;
import androidx.appcompat.widget.AppCompatTextView;
import androidx.constraintlayout.widget.ConstraintLayout;
import androidx.recyclerview.widget.RecyclerView;

import com.common.AlertOrToastMsg;
import com.nostra13.universalimageloader.core.ImageLoader;
import com.nostra13.universalimageloader.core.assist.FailReason;
import com.nostra13.universalimageloader.core.listener.ImageLoadingListener;
import com.tutionapp.R;
import com.tutionapp.StudyMaterials;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

public class FolderAdapter extends RecyclerView.Adapter<FolderAdapter.ViewHolder> {

    List<Uri> uris;
    Context context;
    //List<String> folderNames;
    AlertOrToastMsg alertOrToastMsg;
    FileListener fileListener;

    public FolderAdapter(Context context, List<Uri> uris, FileListener fileListener){
        this.uris = uris;
        this.context = context;
        alertOrToastMsg = new AlertOrToastMsg(context);
        this.fileListener = fileListener;
        //this.folderNames = folderNames;
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.file_upload_design, parent, false);
        FolderAdapter.ViewHolder holder = new FolderAdapter.ViewHolder(view);
        return holder;
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int position) {
        Uri uri = uris.get(position);
        String uriParse = uri.toString();
        String videoIcon, pdfIcon;
        AppCompatImageButton imageButton = holder.imageButton;
        if(uri!=null){
            //imageButton.setImageURI(uri);
            holder.textView.setText(uri.getPath());
            //String format = getMimeType(uri);
            //alertOrToastMsg.ToastMsg(format);
                ImageLoader.getInstance().displayImage(uriParse, imageButton);
            }
    }

    @Override
    public int getItemCount() {
        return uris.size();
    }

    class ViewHolder extends RecyclerView.ViewHolder {

        AppCompatImageButton imageButton;
        AppCompatTextView textView;

        public ViewHolder(@NonNull View itemView) {
            super(itemView);

            imageButton = itemView.findViewById(R.id.folder_image_icon);
            textView = itemView.findViewById(R.id.displayFolderName);
            imageButton.setScaleType(ImageView.ScaleType.CENTER_CROP);
            imageButton.setLayoutParams(new ConstraintLayout.LayoutParams(500,500));
            imageButton.setOnClickListener((v) -> {
                fileListener.onClick(uris.get(getAdapterPosition()));
            });
        }
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
}