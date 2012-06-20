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
        String editedJson = "{\"users\": "+json+"}";
        Log.v("JsonParser",editedJson);
        return getObject(editedJson, UserData.class);
    }

    @Override
    public QueueData getQueueData(String json) throws BedditException {
        return getObject(json, QueueData.class);
    }



    private <T extends Object> T getObject(String json, Class<T> type) throws BedditException {
        JsonReader jsonReader = getNewJsonReader(json);
        try{
            Gson gson = new Gson();
            return gson.fromJson(jsonReader, type);
        }
        catch (JsonParseException e){
            checkForError(jsonReader);
            return null;
        }
    }

    private void checkForError(JsonReader jsonReader) throws BedditException {
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
        } //no error message
        catch (JsonParseException e){
            throw new InvalidJsonException();
        }
    }


    private JsonReader getNewJsonReader(String json){

        JsonReader jsonReader = new JsonReader(new StringReader(json));
        jsonReader.setLenient(true);
        return jsonReader;
    }


}

