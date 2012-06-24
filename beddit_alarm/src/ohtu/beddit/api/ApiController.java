package ohtu.beddit.api;

import android.content.Context;
import ohtu.beddit.api.jsonclassimpl.QueueData;
import ohtu.beddit.api.jsonclassimpl.SleepData;
import ohtu.beddit.api.jsonclassimpl.UserData;
import ohtu.beddit.web.BedditException;

public interface ApiController {
    void updateUserData(Context context) throws BedditException;

    void updateSleepData(Context context) throws BedditException;

    void updateQueueData(Context context) throws BedditException;

    void requestInfoUpdate(Context context) throws BedditException;

    String getAccessToken(Context context, String url) throws BedditException;

    QueueData getQueueData(Context context) throws BedditException;

    UserData getUserData(Context context) throws BedditException;

    SleepData getSleepData(Context context) throws BedditException;

    boolean hasUserChanged(Context context);

    boolean isSleepInfoOutdated();

}
