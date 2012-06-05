package ohtu.beddit.alarm;

import android.app.Notification;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.content.ContentProviderOperation;
import android.content.Context;
import android.content.Intent;
import android.util.Log;
import ohtu.beddit.R;
import ohtu.beddit.activity.MainActivity;

import java.util.Calendar;

/**
 * Created with IntelliJ IDEA.
 * User: tahuomo
 * Date: 29.5.2012
 * Time: 13:23
 * To change this template use File | Settings | File Templates.
 */
public class Notifications {

    public static int setNotification(int ID, int startHours, int startMinutes, int endHours, int endMinutes ,Context context){
        Notification notification = new Notification(R.drawable.alarm_notification,"",0);
        notification.flags = Notification.FLAG_NO_CLEAR;

        String intervalBegin = timeAsString(startHours, startMinutes);
        String intervalEnd = timeAsString(endHours, endMinutes);


        String print  = "";
        if (intervalBegin.equals(intervalEnd)){
            print = intervalBegin;
        } else {
            print = intervalBegin + " - " + intervalEnd;
        }

        Intent intention = new Intent(context, MainActivity.class);
        PendingIntent pendIntent = PendingIntent.getActivity(context, 0,intention,0);
        notification.setLatestEventInfo(context, "Alarm set at ",print,pendIntent);
        NotificationManager notificationman= (NotificationManager)context.getSystemService(Context.NOTIFICATION_SERVICE);
        notificationman.notify(ID, notification);


        return ID;
    }

    public static void resetNotification(int ID, Context context){
        NotificationManager notifications = (NotificationManager)context.getSystemService(Context.NOTIFICATION_SERVICE);
        notifications.cancel(ID);
    }


    public static String timeAsString(int hour, int minute){
        String hourString = "";
        String minuteString = "";
        if (hour < 10)
            hourString += "0";
        if (minute < 10)
            minuteString += "0";
        hourString += hour;
        minuteString += minute;
        return hourString+":"+minuteString;
    }
}
