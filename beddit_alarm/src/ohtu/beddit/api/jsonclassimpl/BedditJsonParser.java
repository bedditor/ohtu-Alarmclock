package ohtu.beddit.api.jsonclassimpl;

import ohtu.beddit.web.BedditException;
import ohtu.beddit.web.UnauthorizedException;

interface BedditJsonParser {

    Night getNight(String json) throws BedditException, UnauthorizedException;

    Users getUsers(String json) throws BedditException, UnauthorizedException;

    QueueData getQueueData(String json) throws BedditException, UnauthorizedException;
}
