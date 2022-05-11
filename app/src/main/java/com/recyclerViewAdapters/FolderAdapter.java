package com.recyclerViewAdapters;

import static android.os.Environment.DIRECTORY_DOWNLOADS;

import android.annotation.SuppressLint;
import android.app.DownloadManager;
import android.content.ContentResolver;
import android.content.Context;
import android.graphics.Color;
import android.net.Uri;
import android.os.Environment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.webkit.MimeTypeMap;
import android.widget.ImageView;

import androidx.annotation.NonNull;
import androidx.appcompat.widget.AppCompatImageButton;
import androidx.appcompat.widget.AppCompatTextView;
import androidx.appcompat.widget.PopupMenu;
import androidx.cardview.widget.CardView;
import androidx.constraintlayout.widget.ConstraintLayout;
import androidx.recyclerview.widget.RecyclerView;

import com.common.AlertOrToastMsg;
import com.nostra13.universalimageloader.core.ImageLoader;
import com.tutionapp.R;

import java.io.File;
import java.util.ArrayList;
import java.util.List;

public class FolderAdapter extends RecyclerView.Adapter<FolderAdapter.ViewHolder> {

    List<Uri> uris;
    Context context;
    //List<String> folderNames;
    static AlertOrToastMsg alertOrToastMsg;
    FileListener fileListener;
    List<String> FileNames;
    static FolderAdapter folderAdapter;
    private static final List<Uri> uriList = new ArrayList<>();
    private static final List<String> fleNames = new ArrayList<>();
    private static final List<String> selectedFileNames = new ArrayList<>();
    SelectedItems selectedItems;

    public static FolderAdapter getInstance(Context context, Uri uris, String fileNames, FileListener fileListener){
        uriList.add(uris);
        fleNames.add(fileNames);
        if(folderAdapter==null)
            folderAdapter = new FolderAdapter(context, uriList, fleNames, fileListener);
        return folderAdapter;
    }

    public FolderAdapter(Context context, List<Uri> uris, List<String> FileNames, FileListener fileListener){
        this.uris = uris;
        this.context = context;
        alertOrToastMsg = new AlertOrToastMsg(context);
        this.fileListener = fileListener;
        this.FileNames = FileNames;
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
    public void onBindViewHolder(@NonNull ViewHolder holder, @SuppressLint("RecyclerView") int position) {
        Uri uri = uris.get(position);
        String FieName = FileNames.get(position);
        String uriParse = uri.toString();
        String videoIcon, pdfIcon, folderIcon;
        AppCompatImageButton imageButton = holder.imageButton;
        videoIcon = "drawable://"+R.drawable.icons8_video_100;
        pdfIcon = "drawable://"+R.drawable.icons8_export_pdf_90;
        folderIcon = "drawable://"+R.drawable.ic_baseline_folder_100;
        if(uri!=null){
                //imageButton.setImageURI(uri);
                holder.textView.setText(FieName);
                try {
                    String format = getMimeType(uri);
                    String formatType = format.split("/")[0];
                    String formatType1 = format.split("/")[1];
                    if(formatType.equals("video"))
                        ImageLoader.getInstance().displayImage(videoIcon, imageButton);
                    else if(formatType1.equals("pdf"))
                        ImageLoader.getInstance().displayImage(pdfIcon, imageButton);
                    else if(formatType.equals("image"))
                        ImageLoader.getInstance().displayImage(uriParse, imageButton);
                    else
                        ImageLoader.getInstance().displayImage(folderIcon, imageButton);
                }
                catch (Exception e){
                    ImageLoader.getInstance().displayImage(folderIcon, imageButton);
                }

            }
        imageButton.setOnLongClickListener((w) -> {
            alertOrToastMsg.ToastMsg("Long pressed...!");
            holder.cardView.setBackgroundColor(Color.GRAY);
            /*if(){
                holder.cardView.setBackgroundColor(Color.GRAY);
                selectedFileNames.add(fleNames.get(position));
            }
            else{
                holder.cardView.setBackgroundColor(Color.WHITE);
                selectedFileNames.remove(fleNames.get(position));
            }
            selectedItems.getItems(selectedFileNames);*/
            return true;
        });
        PopupMenu popupMenu = new PopupMenu(context, holder.textViewOptions);
        popupMenu.inflate(R.menu.options_menu);
        holder.textViewOptions.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                popupMenu.setOnMenuItemClickListener((item) -> {
                    switch (item.getItemId()){
                        case R.id.rename:
                            alertOrToastMsg.ToastMsg("Edit clicked");
                        return true;

                        case R.id.deleteData:
                            alertOrToastMsg.ToastMsg("Delete clicked");
                        return true;

                        case R.id.downloadData:
                            //alertOrToastMsg.ToastMsg("Download clicked");
                            downloadFile(context, DIRECTORY_DOWNLOADS, FileNames.get(position),uri);
                        return true;

                        default:
                            return false;
                    }
                });
                popupMenu.show();
            }
        });

    }

    @Override
    public int getItemCount() {
        return uris.size();
    }

    class ViewHolder extends RecyclerView.ViewHolder {

        AppCompatImageButton imageButton;
        AppCompatTextView textView, textViewOptions;
        CardView cardView;

        public ViewHolder(@NonNull View itemView) {
            super(itemView);

            imageButton = itemView.findViewById(R.id.folder_image_icon);
            textView = itemView.findViewById(R.id.displayFolderName);
            textViewOptions = itemView.findViewById(R.id.textViewOptions);
            cardView = itemView.findViewById(R.id.cardView);
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

    public void downloadFile(Context context, String destinationDirectory, String fileName, Uri uri){
        //createFolder(destinationDirectory);
        DownloadManager downloadManager = (DownloadManager) context.getSystemService(Context.DOWNLOAD_SERVICE);
        DownloadManager.Request request = new DownloadManager.Request(uri);

        request.setNotificationVisibility(DownloadManager.Request.VISIBILITY_VISIBLE_NOTIFY_COMPLETED);
        request.setDestinationInExternalFilesDir(context, destinationDirectory, fileName);

        downloadManager.enqueue(request);
    }

    public void createFolder(String destinationDirectory){
        File mediaStorageDir = new File(Environment.getExternalStorageDirectory()+"/"+destinationDirectory);
        if (!mediaStorageDir.exists()) {
            if (!mediaStorageDir.mkdirs()) {
                alertOrToastMsg.ToastMsg("Failed to create directory...!");
            }
            else
                alertOrToastMsg.ToastMsg("Folder created successfully....!");
        }
    }

    interface SelectedItems{
        void getItems(List<String> fileNames);
    }
}