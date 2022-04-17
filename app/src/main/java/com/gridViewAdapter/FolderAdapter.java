package com.gridViewAdapter;

import android.content.Context;
import android.content.Intent;
import android.graphics.Bitmap;
import android.net.Uri;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;

import androidx.appcompat.widget.AppCompatImageButton;
import androidx.appcompat.widget.AppCompatTextView;
import androidx.constraintlayout.widget.ConstraintLayout;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentTransaction;

import com.common.AlertOrToastMsg;
import com.nostra13.universalimageloader.core.ImageLoader;
import com.nostra13.universalimageloader.core.assist.FailReason;
import com.nostra13.universalimageloader.core.listener.ImageLoadingListener;
import com.nostra13.universalimageloader.core.listener.SimpleImageLoadingListener;
import com.tutionapp.R;
import com.tutionapp.StudyMaterials;

import java.util.List;
import java.util.Map;

public class FolderAdapter extends BaseAdapter {
    List<String> folderName;
    Context context;
    LayoutInflater layoutInflater;
    AlertOrToastMsg alertOrToastMsg;
    Folder folder;
    Intent intent;
    Map<String, List<String>> map;
    List<Uri> uri;
    public FolderAdapter(Context context, List<String> folderName, Folder folder, Map<String, List<String>> map, List<Uri> uri){
        this.folderName = folderName;
        this.context = context;
        this.folder = folder;
        alertOrToastMsg = new AlertOrToastMsg(context);
        this.map = map;
        this.uri = uri;
    }
    @Override
    public int getCount() {
        return uri.size();
    }

    @Override
    public Object getItem(int i) {
        return null;
    }

    @Override
    public long getItemId(int i) {
        return 0;
    }

    @Override
    public View getView(int i, View view, ViewGroup viewGroup) {
        if(layoutInflater==null)
            layoutInflater = (LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        if(view==null)
            view = layoutInflater.inflate(R.layout.file_upload_design, null);
        AppCompatTextView textView = view.findViewById(R.id.folderName);
        AppCompatImageButton imageButton = view.findViewById(R.id.folder_image_icon);
        Uri uris = uri.get(i);
        //imageButton.setImageURI(uris);
        imageButton.setScaleType(ImageView.ScaleType.CENTER_CROP);
        imageButton.setLayoutParams(new ConstraintLayout.LayoutParams(500,500));

        ImageLoader.getInstance().displayImage(uris.toString(), imageButton, new ImageLoadingListener() {
            @Override
            public void onLoadingStarted(String imageUri, View view) {
                alertOrToastMsg.ToastMsg("Loading started...!");
            }

            @Override
            public void onLoadingFailed(String imageUri, View view, FailReason failReason) {
                alertOrToastMsg.ToastMsg("Loading failed...!");
            }

            @Override
            public void onLoadingComplete(String imageUri, View view, Bitmap loadedImage) {
                imageButton.setImageURI(Uri.parse(imageUri));
            }

            @Override
            public void onLoadingCancelled(String imageUri, View view) {
                alertOrToastMsg.ToastMsg("Loading cancelled....!");
            }
        });

        //imageButton.setBackgroundResource(R.drawable.ic_baseline_folder_100);
        imageButton.setOnClickListener((v) -> {
            /*String folderNames = folderName.get(i);
            alertOrToastMsg.ToastMsg("Clicked "+folderNames);
            FolderClass.getFolderPath(folderNames);
            folder.getFolderItemPosition(i);
            List<String> list = map.get(folderName.get(i));
            folder.getListOfFolders(list);
            intent = new Intent(context, StudyMaterials.class);
            intent.putExtra("FolderNAme", folderNames);
            context.startActivity(intent);*/
        });

        textView.setText(uris.getPath());
        return view;
    }
}