package ohtu.beddit.alarm;

import android.app.Notification;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.content.ContentProviderOperation;
import android.content.Context;
import android.content.Intent;
import android.text.format.DateFormat;
import android.util.Log;
import ohtu.beddit.R;
import ohtu.beddit.activity.MainActivity;

import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;

public class Notifications {

    public static int setNotification(int ID, int startHours, int startMinutes, int endHours, int endMinutes, Context context){
        Notification notification = new Notification(R.drawable.alarm_notification,"",0);
        notification.flags = Notification.FLAG_NO_CLEAR;

        String print = timeAsString(startHours, startMinutes, context);
        if (startHours != endHours || startMinutes != endMinutes){
            print += " - " + timeAsString(endHours, endMinutes, context);;
        }
        
        Intent intent = new Intent(context, MainActivity.class);
        PendingIntent pendIntent = PendingIntent.getActivity(context, 0, intent, 0);
        notification.setLatestEventInfo(context, context.getString(R.string.notification_text),print,pendIntent);
        NotificationManager manager = (NotificationManager)context.getSystemService(Context.NOTIFICATION_SERVICE);
        manager.notify(ID, notification);

        return ID;
    }

    public static void resetNotification(int ID, Context context){
        NotificationManager manager = (NotificationManager)context.getSystemService(Context.NOTIFICATION_SERVICE);
        manager.cancel(ID);
    }


    public static String timeAsString(int hour, int minute, Context context){
        Date date = new Date();
        date.setHours(hour);
        date.setMinutes(minute);

        SimpleDateFormat dateFormat;
        if(DateFormat.is24HourFormat(context)){
            dateFormat = new SimpleDateFormat("HH:mm");
        }
        else{
            dateFormat = new SimpleDateFormat("h:mm a");
        }

        return dateFormat.format(date);
    }
}
