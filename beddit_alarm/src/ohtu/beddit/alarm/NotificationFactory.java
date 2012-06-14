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
    private NotificationManager notfManager;
    private static final int ID = 1;

    public NotificationFactory(Context context){
        this.context = context.getApplicationContext();
        this.notfManager = (NotificationManager)context.getSystemService(Context.NOTIFICATION_SERVICE);
    }

    public NotificationFactory(Context context, NotificationManager notfManager){
        this.context = context.getApplicationContext();
        this.notfManager = notfManager;
    }

    public void setNotification(int startHours, int startMinutes, int endHours, int endMinutes){
        Notification notification = new Notification(R.drawable.alarm_notification,"",0);
        notification.flags = Notification.FLAG_NO_CLEAR;

        String print = TimeUtils.timeAsString(startHours, startMinutes, context);
        if (startHours != endHours || startMinutes != endMinutes){
            print += " - " + TimeUtils.timeAsString(endHours, endMinutes, context);;
        }
        
        Intent intent = new Intent(context, MainActivity.class);
        PendingIntent pendIntent = PendingIntent.getActivity(context, 0, intent, 0);
        notification.setLatestEventInfo(context, context.getString(R.string.notification_text),print,pendIntent);
        notfManager.notify(ID, notification);
    }

    public void resetNotification(){
        notfManager.cancel(ID);
    }


}
