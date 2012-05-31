package ohtu.beddit.json;

import com.google.gson.Gson;
import com.google.gson.JsonElement;

public class JsonReaderImpl implements JsonReader {
    @Override
    public Night getNight(String json){
        Gson gson = new Gson();

        try {
            return gson.fromJson(json, Night.class);
        }
        catch (Exception e){
            return null;
        }

    }

    @Override
    public Night getNight(JsonElement json) {
        return getNight(json.getAsString());
    }
}
