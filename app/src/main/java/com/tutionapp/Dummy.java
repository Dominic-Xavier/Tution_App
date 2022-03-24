package com.tutionapp;

import android.content.Context;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;

public class Dummy extends ArrayAdapter {
    public Dummy(@NonNull Context context, int resource) {
        super(context, resource);
    }

    @Override
    public int getPosition(@Nullable Object item) {
        return super.getPosition(item);
    }

    @Override
    public View getDropDownView(int position, @Nullable View convertView, @NonNull ViewGroup parent) {
        return super.getDropDownView(position, convertView, parent);

    }

    @Override
    public long getItemId(int position) {
        return super.getItemId(position);
    }
}
