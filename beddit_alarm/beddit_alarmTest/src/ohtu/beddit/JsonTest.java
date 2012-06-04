package ohtu.beddit;

import com.google.gson.JsonSyntaxException;
import ohtu.beddit.json.BedditJsonParser;
import ohtu.beddit.json.BedditJsonParserImpl;
import ohtu.beddit.json.Night;
import org.junit.After;
import org.junit.*;
import org.junit.Before;
import org.junit.Test;
//import junit.framework.TestCase;

import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.util.Calendar;

public class JsonTest {

    private final String TAG = "JsonTest";
    private BedditJsonParser bedditJsonParser;
    private String filename = "beddit_alarmTest/src/ohtu/beddit/test_files/night_json_test.txt";
    private Night night;

    @Before
    public void setUp() {
        bedditJsonParser = new BedditJsonParserImpl();
        try{
            night = bedditJsonParser.getNight(readStringFromFile(filename));
        }
        catch (JsonSyntaxException e){
            System.out.println(e.getMessage());
        }
    }

    @After
    public void tearDown() {

    }

    @Test
    public void testDate() {
        Assert.assertEquals("2012-03-14", night.getDate());
    }

    @Test
    public void testSleepStages() {
        Assert.assertEquals('M', night.getLastSleepStage());
        Assert.assertEquals(2012, night.getLastSleepStageTime().get(Calendar.YEAR));
        Assert.assertEquals(2, night.getLastSleepStageTime().get(Calendar.MONTH));
        Assert.assertEquals(13, night.getLastSleepStageTime().get(Calendar.DAY_OF_MONTH));
        Assert.assertEquals(23, night.getLastSleepStageTime().get(Calendar.HOUR_OF_DAY));
        Assert.assertEquals(58, night.getLastSleepStageTime().get(Calendar.MINUTE));
        Assert.assertEquals(0, night.getLastSleepStageTime().get(Calendar.SECOND));

    }

    private String readStringFromFile(String filename){
        String returnedString = "";
        FileInputStream fis = null;
        try{
            fis = new FileInputStream(filename);
        }catch(FileNotFoundException e){
            return "";
        }

        try{
            int readCharacter;
            while((readCharacter = fis.read()) != -1){
                returnedString += (char)readCharacter;
            }
            fis.close();
        }catch (IOException f){
            return "";
        }

        return returnedString;
    }
}
