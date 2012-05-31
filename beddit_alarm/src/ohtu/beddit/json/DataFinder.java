package ohtu.beddit.json;

import java.util.Calendar;

public class DataFinder {
    public char getLastSleepStage(String json){
        BedditJsonReader jsonReader = new BedditJsonReaderImpl();
        return jsonReader.getNight(json).getLastSleepStage();
    }

    public Calendar getTimeOfLastSleepStage(String json){
        BedditJsonReader jsonReader = new BedditJsonReaderImpl();
        return jsonReader.getNight(json).getTimeOfLastSleepStage();
    }
}
