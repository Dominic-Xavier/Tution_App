package com.gridViewAdapter;

import android.content.Context;
import android.content.Intent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;

import androidx.appcompat.widget.AppCompatImageButton;
import androidx.appcompat.widget.AppCompatTextView;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentTransaction;

import com.common.AlertOrToastMsg;
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
    public FolderAdapter(Context context, List<String> folderName, Folder folder, Map<String, List<String>> map){
        this.folderName = folderName;
        this.context = context;
        this.folder = folder;
        alertOrToastMsg = new AlertOrToastMsg(context);
        this.map = map;
    }
    @Override
    public int getCount() {
        return folderName.size();
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
        AppCompatImageButton imageButton = view.findViewById(R.id.folder);
        imageButton.setBackgroundResource(R.drawable.ic_baseline_folder_100);
        imageButton.setOnClickListener((v) -> {
            String folderNames = folderName.get(i);
            alertOrToastMsg.ToastMsg("Clicked "+folderNames);
            FolderClass.folderPath(folderNames);
            folder.getFolderItemPosition(i);
            List<String> list = map.get(folderName.get(i));
            folder.getListOfFolders(list);
            intent = new Intent(context, StudyMaterials.class);
            intent.putExtra("FolderNAme", folderNames);
            context.startActivity(intent);
            String folderPath = FolderClass.getPath(folderNames);
            if(folderPath!=null)
                alertOrToastMsg.ToastMsg(folderPath);
            else
                alertOrToastMsg.ToastMsg("No Name...!");
        });
        textView.setText(folderName.get(i));
        return view;
    }
}