package ohtu.beddit.api.jsonclassimpl;

interface BedditJsonParser {

    Night getNight(String json);

    Users getUsers(String json);

    QueueData getQueueData(String json);
}
