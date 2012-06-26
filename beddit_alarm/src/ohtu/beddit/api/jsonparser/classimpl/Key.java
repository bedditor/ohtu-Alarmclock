package ohtu.beddit.api.jsonparser.classimpl;

/**
 * Encapsulates json data of a temporary access key as an object.
 */
public class Key {

    private String expires;
    private String key;

    public String getExpires() {
        return expires;
    }

    public String getKey() {
        return key;
    }
}