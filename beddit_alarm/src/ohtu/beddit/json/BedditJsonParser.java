package ohtu.beddit.json;

public interface BedditJsonParser {

    Night getNight(String json);

    User getUser(String json);

}
