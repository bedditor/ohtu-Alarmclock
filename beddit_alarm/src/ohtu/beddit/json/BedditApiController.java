package ohtu.beddit.json;

import java.util.Calendar;

public class BedditApiController {

    private static BedditJsonParser jsonParser = new BedditJsonParserImpl();

    public String getUsername(String json){
        return jsonParser.getUser(json).getUsername();
    }

    public char getLastSleepStage(String json){
        return jsonParser.getNight(json).getLastSleepStage();
    }

    public Calendar getTimeOfLastSleepStage(String json){
        return jsonParser.getNight(json).getLastSleepStageTime();
    }
}
