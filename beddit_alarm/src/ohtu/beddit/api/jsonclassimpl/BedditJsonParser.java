package ohtu.beddit.api.jsonclassimpl;

import ohtu.beddit.web.BedditException;
import ohtu.beddit.web.UnauthorizedException;

interface BedditJsonParser {

    SleepData getSleepData(String json) throws BedditException, UnauthorizedException;

    UserData getUserData(String json) throws BedditException, UnauthorizedException;

    QueueData getQueueData(String json) throws BedditException, UnauthorizedException;
}
