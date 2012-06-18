package ohtu.beddit.web;

import android.content.Context;
import android.util.Log;
import com.google.gson.JsonParser;
import ohtu.beddit.io.PreferenceService;

import javax.net.ssl.HttpsURLConnection;
import java.io.IOException;
import java.io.InputStream;
import java.net.URL;
import java.util.Scanner;

public class BedditWebConnector implements BedditConnector {

    private static final String TAG = "BedditWebConnector";


    public String getUserJson(Context context) throws BedditConnectionException {
        return getSomeJson(context, "", false);
    }


    public String getWakeUpJson(Context context,String date) throws BedditConnectionException {
        String username = PreferenceService.getUsername(context);
        String query = username+"/"+date+"/sleep";
        return getSomeJson(context, query, false); //add
    }

    public String getQueueStateJson(Context context, String date) throws BedditConnectionException {
        String username = PreferenceService.getUsername(context);
        String query = username+"/"+date+"/sleep/queue_for_analysis";
        return getSomeJson(context, query, false); //add
    }

    //need to check if POSTing actually works as it is:
    public String requestDataAnalysis(Context context, String date) throws BedditConnectionException {
        String username = PreferenceService.getUsername(context);
        String query = username+"/"+date+"/sleep/queue_for_analysis";
        return getSomeJson(context, query, true); //add
    }

    public String getSomeJson(Context context, String query, boolean do_post) throws BedditConnectionException {
        String response = "";

        HttpsURLConnection connection = null;
        InputStream inputStream = null;
        try {
            connection = connect(context, query, connection,do_post);
            inputStream = connection.getInputStream();
            response = readFromStream(inputStream);
        }
        catch (Throwable e) {
            Log.e(TAG, Log.getStackTraceString(e));
            Log.v("testi22", "jee");
            throw new BedditConnectionException();
        }
        finally {
            closeConnections(connection, inputStream);
            if(response.equals("")) {
                Log.v(TAG, "Empty response");
                throw new BedditConnectionException();
            }
        }
        return response;
    }

    @Override
    public String getAccessToken(String url) throws BedditConnectionException {
        Log.v(TAG, "Trying to get access token from " + url);
        String token = null;
        HttpsURLConnection connection = null;
        InputStream inputStream = null;
        try {
            URL address = new URL(url);
            Log.v(TAG,"url: "+address);

            System.setProperty("http.keepAlive", "false");
            connection = (HttpsURLConnection) address.openConnection();

            connection.connect();
            Log.v(TAG, "responseCode: " + connection.getResponseCode());

            inputStream = connection.getInputStream();
            String stash = readFromStream(inputStream);
            Log.v(TAG,"stash: "+stash);

            token = new JsonParser().parse(stash).getAsJsonObject().get("access_token").getAsString();
            Log.v(TAG,"AccessToken = \""+token+"\"");

        } catch (Throwable e) {
            Log.e(TAG, Log.getStackTraceString(e));  //To change body of catch statement use File | Settings | File Templates.
            throw new BedditConnectionException();
        }
        finally {
            closeConnections(connection, inputStream);
        }
        return token;
    }

    private HttpsURLConnection connect(Context context, String query, HttpsURLConnection connection, boolean do_post) throws IOException {
        String token = PreferenceService.getToken(context);
        URL url = new URL("https://api.beddit.com/api2/user/"+query+"?access_token="+token);
        Log.v(TAG, "GET Token: " + url);
        connection = (HttpsURLConnection) url.openConnection();
        if(do_post){
            connection.setRequestMethod("POST");
            connection.setDoOutput(true);
        }else{
            connection.setRequestMethod("GET");
            connection.setDoOutput(false);
        }
        connection.connect();
        return connection;
    }

    private String readFromStream(InputStream inputStream){
        String response = "";
        Scanner scanner = new Scanner(inputStream);
        while(scanner.hasNext())
            response += scanner.nextLine();
        return response;
    }

    private void closeConnections(HttpsURLConnection connection, InputStream inputStream) {
        if (connection != null){
            connection.disconnect();
        }
        if (inputStream != null) {
            try {
                inputStream.close();
            } catch (IOException e) {
                Log.e(TAG, Log.getStackTraceString(e));
            }
        }
    }


}
