package ohtu.beddit.json;

import android.util.Log;
import com.google.gson.Gson;
import com.google.gson.stream.JsonReader;

import java.io.StringReader;

public class BedditJsonParserImpl implements BedditJsonParser {
    private static final String TAG = "BedditJsonParser";

    @Override
    public Night getNight(String json){
        return getObject(json, Night.class);
    }

    @Override
    public User getUser(String json){
        return getObject(json, User.class);
    }

    private <T extends JsonObject> T getObject(String json, Class<T> type){
        try {
            Gson gson = new Gson();
            return gson.fromJson(getNewJsonReader(json), type);
        }
        catch (Exception e){
            Log.v(TAG, "Error reading JSon: "+e.getMessage());
            return null;
        }
    }

    private JsonReader getNewJsonReader(String json){

        JsonReader jsonReader = new JsonReader(new StringReader(json));
        jsonReader.setLenient(true);
        return jsonReader;
    }

}

