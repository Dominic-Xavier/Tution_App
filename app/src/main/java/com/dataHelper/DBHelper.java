package com.dataHelper;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

import androidx.annotation.Nullable;

import com.common.AlertOrToastMsg;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class DBHelper extends SQLiteOpenHelper {

    static Context contexts;
    private static DBHelper dbHelper;
    public static final String  TABLE_NAME = "Student_Fees";
    static AlertOrToastMsg alertOrToastMsg;
    static Map<String, List<String>> map;
    static List<String> dates, fees_Paid, fees_Balance, total_Fees;

    public static DBHelper getInstance(@Nullable Context context){
        contexts = context;
        if(dbHelper==null){
            dbHelper = new DBHelper(context);
            alertOrToastMsg = new AlertOrToastMsg(context);
            dates = new ArrayList<>();
            fees_Paid = new ArrayList<>();
            fees_Balance = new ArrayList<>();
            total_Fees = new ArrayList<>();
            map = new HashMap<>();
        }
        return dbHelper;
    }

    private DBHelper(@Nullable Context context) {
        super(context, "FeesDetails", null, 1);
    }

    @Override
    public void onCreate(SQLiteDatabase db) {
        db.execSQL("CREATE TABLE "+TABLE_NAME+" (dates Date, Fees_Paid int, Fees_Balance int, Total_Fees int)");
    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        db.execSQL("DROP TABLE IF EXISTS "+TABLE_NAME);
        onCreate(db);
    }

    public void insertData(List<String> dates, List<String> Fees_Paid, List<String> Fees_Balance, List<String> Total_Fees) {
        SQLiteDatabase db = this.getWritableDatabase();
        ContentValues cv = new ContentValues();
        boolean isInserted = false;
        for(int i=0; i<dates.size(); i++) {
            cv.put("Dates", dates.get(i));
            cv.put("Fees_Paid", Fees_Paid.get(i));
            cv.put("Fees_Balance", Fees_Balance.get(i));
            cv.put("Total_Fees", Total_Fees.get(i));
            long verifyInserted = db.insert(TABLE_NAME, null, cv);
            if (verifyInserted != -1)
                isInserted = true;
            else{
                isInserted = false;
                alertOrToastMsg.ToastMsg("Error in inserting values...!");
                return;
            }
        }
        if(isInserted)
            alertOrToastMsg.ToastMsg("Values inserted Successfully...!");
    }

    public Map<String, List<String>> getFeeDetails(){
        SQLiteDatabase db = this.getReadableDatabase();
        Cursor cursor = db.rawQuery("SELECT * FROM "+TABLE_NAME, null);
        while (cursor.moveToNext()){
            dates.add(cursor.getString(0));
            fees_Paid.add(cursor.getString(1));
            fees_Balance.add(cursor.getString(2));
            total_Fees.add(cursor.getString(3));
        }
        map.put("Dates", dates);
        map.put("fees_Paid", fees_Paid);
        map.put("fees_Balance", fees_Balance);
        map.put("total_Fees", total_Fees);
        return map;
    }
}
