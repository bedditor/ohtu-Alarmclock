package ohtu.beddit.api.jsonclassimpl;

import android.util.Log;
import com.google.gson.Gson;
import com.google.gson.JsonParseException;
import com.google.gson.stream.JsonReader;
import ohtu.beddit.web.BedditException;
import ohtu.beddit.web.UnauthorizedException;

import java.io.StringReader;

class BedditJsonParserImpl implements BedditJsonParser {
    private static final String TAG = "BedditJsonParser";

    @Override
    public SleepData getSleepData(String json) throws BedditException {
        return getObject(json, SleepData.class);
    }

    @Override
    public UserData getUserData(String json) throws BedditException {
        return getObject(json, UserData.class);
    }

    @Override
    public QueueData getQueueData(String json) throws BedditException {
        return getObject(json, QueueData.class);
    }

    private <T> T getObject(String json, Class<T> type) throws BedditException {
        checkForError(json);
        if(UserData.class.isAssignableFrom(type)){
            json = nameTable(json);
        }
        JsonReader jsonReader = getNewJsonReader(json);
        try{
            Gson gson = new Gson();
            return gson.fromJson(jsonReader, type);
        }
        catch (JsonParseException e){
            throw new InvalidJsonException("InvalidJsonException");
        }
    }

    private void checkForError(String json) throws BedditException {
        if(!json.contains("\"error\":")){
            return;
        }
        JsonReader jsonReader = getNewJsonReader(json);
        Gson gson = new Gson();
        try{ //got error message
            ErrorJson error = gson.fromJson(jsonReader, ErrorJson.class);
            Log.v(TAG, error.getError()+": "+error.getErrorMessage());
            if(error.getError().equals("unauthorized")){
                throw new UnauthorizedException(error.getErrorMessage());
            }
            else{
                throw new BedditException(error.getErrorMessage());
            }
        }
        catch (JsonParseException e){
            throw new InvalidJsonException(e.getMessage());
        }
    }

    private String nameTable(String json){
        return "{\"users\": " + json + "}";
    }

    private JsonReader getNewJsonReader(String json) {
        JsonReader jsonReader = new JsonReader(new StringReader(json));
        jsonReader.setLenient(true);
        return jsonReader;
    }
}

