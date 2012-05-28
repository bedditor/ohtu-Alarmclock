package ohtu.beddit.alarm;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import ohtu.beddit.activity.AlarmActivity;


public class AlarmReceiver extends BroadcastReceiver {
    @Override
    public void onReceive(Context context, Intent intent)
    {
        Intent newintent = new Intent(context, AlarmActivity.class);
        newintent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_NO_USER_ACTION);
        context.startActivity(newintent);
    }

}