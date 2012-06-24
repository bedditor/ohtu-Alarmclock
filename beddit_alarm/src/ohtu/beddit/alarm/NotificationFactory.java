package ohtu.beddit.alarm;

import android.app.Notification;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.content.Context;
import android.content.Intent;
import ohtu.beddit.R;
import ohtu.beddit.activity.MainActivity;
import ohtu.beddit.utils.TimeUtils;


/*
 * This class is used for making notification on notification bar when the alarm is set. Since we want the user
 * to be able to always check the wake up time, the notification is not removable by others than the application itself.
 */

public class NotificationFactory {

    private final Context context;
    private final NotificationManager notificationManager;

    // Since we are using only a single notification, the ID is always the same. If updated to show multiple
    // notifications, ID's should be unique and handled so that resetNotification will delete correct notification.
    private static final int ID = 1;

    public NotificationFactory(Context context) {
        this.context = context.getApplicationContext();
        this.notificationManager = (NotificationManager) context.getSystemService(Context.NOTIFICATION_SERVICE);
    }

    public NotificationFactory(Context context, NotificationManager notificationManager) {
        this.context = context.getApplicationContext();
        this.notificationManager = notificationManager;
    }

    public void setNotification(int startHours, int startMinutes, int endHours, int endMinutes) {
        Notification notification = new Notification(R.drawable.alarm_notification, "", 0);
        notification.flags = Notification.FLAG_NO_CLEAR;

        // The notification will have time displayed either "8:15 AM" or "8:00 AM - 8:15 AM",
        // depending on if there's interval or not.

        String print = TimeUtils.timeAsString(startHours, startMinutes, context);
        if (startHours != endHours || startMinutes != endMinutes) {
            print += " - " + TimeUtils.timeAsString(endHours, endMinutes, context);
        }

        Intent intent = new Intent(context, MainActivity.class);
        PendingIntent pendIntent = PendingIntent.getActivity(context, 0, intent, 0);
        notification.setLatestEventInfo(context, context.getString(R.string.notification_text), print, pendIntent);
        notificationManager.notify(ID, notification);
    }

    public void resetNotification() {
        notificationManager.cancel(ID);
    }


}
