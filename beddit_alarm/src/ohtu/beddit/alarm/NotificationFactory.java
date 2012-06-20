package ohtu.beddit.alarm;

import android.app.Notification;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.content.Context;
import android.content.Intent;
import ohtu.beddit.R;
import ohtu.beddit.activity.MainActivity;
import ohtu.beddit.utils.TimeUtils;

public class NotificationFactory {

    private Context context;
    private NotificationManager notificationManager;
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
