package ohtu.beddit.utils;

import android.app.Activity;
import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;

import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;

public class Utils {

    public static Calendar bedditTimeStringToCalendar(String timeString){
        timeString = timeString.replaceAll("T", " ");
        Date date;
        try {
            DateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
            date = (Date)dateFormat.parse(timeString);
        } catch (ParseException e) {
            System.out.println("Night: "+e.getMessage());
            return null;
        }

        Calendar calendar = Calendar.getInstance();
        calendar.setTime(date);
        return calendar;
    }

    public static String getTodayAsQueryDateString(){
        Calendar kalenteri = Calendar.getInstance();
        int year = kalenteri.get(Calendar.YEAR);
        int month = kalenteri.get(Calendar.MONTH);
        int day = kalenteri.get(Calendar.DAY_OF_MONTH);
        return year+"/"+(month+1)+"/"+day; //because the fucking months start from 0
    }

    public static void createOkDialog(final Activity context, String message){
        AlertDialog.Builder builder = new AlertDialog.Builder(context);
        builder.setMessage(message);
        builder.setCancelable(false);
        builder.setPositiveButton("Ok", new DialogInterface.OnClickListener() {
            public void onClick(DialogInterface dialog, int id) {
                context.finish();
            }
        });
        AlertDialog alert = builder.create();
        alert.show();
    }

}
