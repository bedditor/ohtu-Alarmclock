package ohtu.beddit.json;

public interface BedditJsonParser {

    Night getNight(String json);

    Users getUsers(String json);

    QueueData getQueueData(String json);
}
