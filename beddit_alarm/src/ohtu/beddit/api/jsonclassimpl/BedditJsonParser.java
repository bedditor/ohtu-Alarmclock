package ohtu.beddit.api.jsonclassimpl;

interface BedditJsonParser {

    Night getNight(String json) throws InvalidJsonException;

    Users getUsers(String json) throws InvalidJsonException;

    QueueData getQueueData(String json) throws InvalidJsonException;
}
