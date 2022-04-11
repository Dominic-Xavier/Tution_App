package com.common;

import android.os.Build;

import androidx.annotation.RequiresApi;

import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.Date;

public class DateFunctions {

    @RequiresApi(api = Build.VERSION_CODES.O)
    public static String getCurrentDate(){
        final DateTimeFormatter dtf = DateTimeFormatter.ofPattern("dd-MM-yyyy");
        LocalDateTime now = LocalDateTime.now();
        String currentDate = dtf.format(now);
        return currentDate;
    }

    public static Boolean validateDateFormat(String date) {
        Boolean b;
        try {
            Date d;
            final String DateFormat = "dd-mm-yyyy";
            DateFormat df = new SimpleDateFormat(DateFormat);
            df.setLenient(false);
            d = df.parse(date);
            String split[] = date.split("-");
            int dte = Integer.parseInt(split[0]);
            int month = Integer.parseInt(split[1]);
            int year = Integer.parseInt(split[2]);
            if (date.equals(df.format(d)) && dte<=31 && month<=12 && year<9999)
                b = true;
            else
                b = false;
        } catch (ParseException e) {
            b = false;
        }
        return b;
    }
}
