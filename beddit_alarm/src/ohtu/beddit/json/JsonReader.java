package ohtu.beddit.json;

import com.google.gson.JsonElement;

/**
 * Created with IntelliJ IDEA.
 * User: joosakur
 * Date: 31.5.2012
 * Time: 13:46
 * To change this template use File | Settings | File Templates.
 */
public interface JsonReader {
    Night getNight(String json);
    Night getNight(JsonElement json);

}
