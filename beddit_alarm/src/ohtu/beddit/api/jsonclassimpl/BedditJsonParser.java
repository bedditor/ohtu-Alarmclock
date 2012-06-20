package ohtu.beddit.api.jsonclassimpl;

import ohtu.beddit.web.BedditException;

interface BedditJsonParser {

    SleepData getSleepData(String json) throws BedditException;

    UserData getUserData(String json) throws BedditException;

    QueueData getQueueData(String json) throws BedditException;
}
