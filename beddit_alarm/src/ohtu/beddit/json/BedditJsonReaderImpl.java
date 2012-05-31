package ohtu.beddit.json;

import com.google.gson.Gson;
import com.google.gson.JsonElement;
import com.google.gson.stream.JsonReader;

import java.io.StringReader;

public class BedditJsonReaderImpl implements BedditJsonReader {
    @Override
    public Night getNight(String json){
        Gson gson = new Gson();
        JsonReader jsonReader = new JsonReader(new StringReader(json));
        jsonReader.setLenient(true);

        try {
            return gson.fromJson(jsonReader, Night.class);
        }
        catch (Exception e){
            System.out.println("BedditJsonReader: "+e.getMessage());
            return null;
        }

    }

}
