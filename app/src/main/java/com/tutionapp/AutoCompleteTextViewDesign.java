package com.tutionapp;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ImageButton;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.widget.AppCompatImageButton;
import androidx.appcompat.widget.AppCompatTextView;

import com.common.AlertOrToastMsg;

import java.util.List;

public class AutoCompleteTextViewDesign extends ArrayAdapter<AllClasses> {

    List<AllClasses> list;
    AlertOrToastMsg alertOrToastMsg;
    int resource;
    public AutoCompleteTextViewDesign(@NonNull Context context,int resource, List<AllClasses> list) {
        super(context,resource, list);
        this.list = list;
        this.resource = resource;
        alertOrToastMsg = new AlertOrToastMsg(context);
    }

    @Nullable
    @Override
    public AllClasses getItem(int position) {
        return list.get(position);
    }

    @Override
    public int getCount() {
        return list.size();
    }

    @NonNull
    @Override
    public View getView(int position, @Nullable View convertView, @NonNull ViewGroup parent) {
        if(convertView==null)
            convertView = LayoutInflater.from(getContext()).inflate(resource, parent, false);
        TextView text = convertView.findViewById(R.id.sub_Or_Class_Name);
        AllClasses all = getItem(position);
        if(all!=null)
            text.setText(all.getClassNames());
        return convertView;
    }

    @Override
    public View getDropDownView(int position, @Nullable View convertView, @NonNull ViewGroup parent) {
        if(convertView==null)
            convertView = LayoutInflater.from(getContext()).inflate(resource, parent, false);
        TextView text = convertView.findViewById(R.id.sub_Or_Class_Name);
        ImageButton imageButton = convertView.findViewById(R.id.delete_subject_or_class);
        AllClasses all = getItem(position);
        if(all!=null){
            text.setText(all.getClassNames());
            imageButton.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    alertOrToastMsg.ToastMsg("Clicked "+all.getClassNames());
                }
            });
        }
        return convertView;
    }
}