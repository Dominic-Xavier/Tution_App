package com.dataHelper;

import android.content.Context;
import android.content.SharedPreferences;
import android.preference.PreferenceManager;
import android.widget.Toast;

public class CatcheData {

    private static SharedPreferences sharedPreferences;

    public static void setData(String key, String value, Context context){
        sharedPreferences = PreferenceManager.getDefaultSharedPreferences(context);
        SharedPreferences.Editor edit = sharedPreferences.edit();
        edit.putString(key,value);
        boolean save_data = edit.commit();
        if(save_data)
            Toast.makeText(context,"Data Saved in sharedpreference...!", Toast.LENGTH_SHORT).show();
        else
            Toast.makeText(context,"Data Saving Failed...!", Toast.LENGTH_SHORT).show();
    }

    public static boolean delete_data(Context context){
        sharedPreferences = PreferenceManager.getDefaultSharedPreferences(context);
        SharedPreferences.Editor edit = sharedPreferences.edit();
        edit.clear();
        boolean delete_data = edit.commit();
        if(delete_data){
            Toast.makeText(context,"Data deleted Successfully...!", Toast.LENGTH_SHORT).show();
            return delete_data;
        }
        else{
            Toast.makeText(context,"Data did not deleted...!", Toast.LENGTH_SHORT).show();
            return delete_data;
        }
    }

    public static String getData(String key, Context context){
        sharedPreferences = PreferenceManager.getDefaultSharedPreferences(context);
        String result = sharedPreferences.getString(key,null);
        return result;
    }
}
