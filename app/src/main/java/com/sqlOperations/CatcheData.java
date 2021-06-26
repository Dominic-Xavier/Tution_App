package com.sqlOperations;

import android.content.Context;
import android.content.SharedPreferences;
import android.preference.PreferenceManager;
import android.widget.Toast;

public class CatcheData {

    static Context context;
    private static SharedPreferences sharedPreferences;

    public CatcheData(Context context){
        this.context = context;
    }

    public static void setData(String key, String value){
        sharedPreferences = PreferenceManager.getDefaultSharedPreferences(context);
        SharedPreferences.Editor edit = sharedPreferences.edit();
        edit.putString(key,value);
        boolean save_data = edit.commit();
        if(save_data)
            Toast.makeText(context,"Data Saved Successfully...!", Toast.LENGTH_SHORT).show();
        else
            Toast.makeText(context,"Data Saving Failed...!", Toast.LENGTH_SHORT).show();
    }

    public static void delete_data(){
        sharedPreferences = PreferenceManager.getDefaultSharedPreferences(context);
        SharedPreferences.Editor edit = sharedPreferences.edit();
        edit.remove("u_id");
        boolean delete_data = edit.commit();
        if(delete_data)
            Toast.makeText(context,"Data deleted Successfully...!", Toast.LENGTH_SHORT).show();
        else
            Toast.makeText(context,"Data did not deleted...!", Toast.LENGTH_SHORT).show();
    }

    public static String getData(String key){
        sharedPreferences = PreferenceManager.getDefaultSharedPreferences(context);
        if(key.equals("u_id"))
            return sharedPreferences.getString("u_id",null);
        return null;
    }
}
