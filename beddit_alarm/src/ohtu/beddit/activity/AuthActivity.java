package ohtu.beddit.activity;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.webkit.*;
import ohtu.beddit.R;
import ohtu.beddit.io.FileHandler;
import ohtu.beddit.io.PreferenceService;
import ohtu.beddit.api.ApiController;
import ohtu.beddit.api.jsonclassimpl.ApiControllerClassImpl;
import ohtu.beddit.web.*;

import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class AuthActivity extends Activity implements UrlListener {
    WebView webview;
    private final String TAG = "AuthActivity";
    private FileHandler fileHandler;

    public static final int RESULT_CANCELLED = 101;
    public static final int RESULT_FAILED = 102;

    public static final String REDIRECT_URI = "https://peach-app.appspot.com";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        Log.v(TAG, "OnCreate");
        setContentView(R.layout.webview);

        fileHandler = new FileHandler(this);
        webview = (WebView) findViewById(R.id.webLayout);
        webview.clearHistory();
        removeCookies();
        setSettings();
        openAuthBrowser();
    }

    @Override
    public void onUrlReceived(String url) {
        // Seeing if we got the right url
        Matcher code = Pattern.compile("\\Q"+REDIRECT_URI+"/oauth?code=\\E(.+)").matcher(url);
        Matcher error = Pattern.compile("\\Q"+REDIRECT_URI+"/oauth?error=\\E(.+)").matcher(url);

        Log.v(TAG, "Trying to match url: " + url);
        if (code.matches()) {
            Log.v(TAG, "Code: "+code.group(1));
            // We got the right url so we construct our https get url and give it to BedditConnector which will get the access token by https connection
            url = "https://api.beddit.com/api/oauth/access_token?client_id=" +
                    fileHandler.getClientInfo(FileHandler.CLIENT_ID) +
                    "&redirect_uri="+REDIRECT_URI+"/oauth&client_secret=" +
                    fileHandler.getClientInfo(FileHandler.CLIENT_SECRET) +
                    "&grant_type=code&code="+code.group(1);

            // If we get error, well you shouldn't. We close the program because we won't get correct access_token. Breaks other code?
            try{
                BedditConnector bedditConnector = new BedditWebConnector();
                String token = bedditConnector.getAccessToken(url);
                Log.v(TAG, "result: "+token);
                // We put the correct access token to safe and be happy. User is allowed to use the program now.
                PreferenceService.setToken(this, token);
                saveUserData();
                finish();
            } catch (BedditConnectionException e){
                Log.v(TAG, "BedditConnectionException in onUrlReceived");
                fail(false);
            }
        }
        // If user doesn't allow the program to access, we simply terminate the program.
        else if (error.matches()) {
            Log.v(TAG, "Error: "+error.group(1));
            fail(true);
        }
    }

    private void fail(boolean cancelledByUser){
        Log.v(TAG, "fail called");
        Intent resultIntent = new Intent((String) null);
        if(cancelledByUser){
            setResult(RESULT_CANCELLED, resultIntent);
        }
        else {
            setResult(RESULT_FAILED, resultIntent);
        }
        PreferenceService.setUsername(this, "");
        PreferenceService.setToken(this, "");
        finish();
    }

    private void saveUserData() {
        Log.v(TAG, "saving user data");
        try{
            ApiController apiController = new ApiControllerClassImpl();
            apiController.updateUserInfo(this); //updates the info in apicontroller for lines below:
            PreferenceService.setUsername(this, apiController.getUsername(this, 0));
            PreferenceService.setFirstName(this, apiController.getFirstName(this, 0));
            PreferenceService.setLastName(this, apiController.getLastName(this, 0));
        }
        catch (Exception e){
            Log.v(TAG, "saving user data failed");
            fail(false);
        }
    }

    @Override
    public void finish() {
        Log.v(TAG, "finishing");
        webview.clearCache(true);
        webview.clearView();
        webview.clearHistory();
        super.finish();
    }

    private void setSettings() {
        // Here we set various settings regarding browser experience.
        WebSettings settings = webview.getSettings();
        webview.setInitialScale(1);
        settings.setSavePassword(false);
        settings.setCacheMode(WebSettings.LOAD_NO_CACHE);
        settings.setAppCacheEnabled(false);
        settings.setUseWideViewPort(true);
        settings.setLoadWithOverviewMode(true);
        settings.setDefaultZoom(WebSettings.ZoomDensity.FAR);
    }

    private void openAuthBrowser() {
        // Initialize the webclient and open it to this webview.
        AmazingWebClient client = new AmazingWebClient(this);
        client.addUrlListener(this);
        webview.setWebViewClient(client);
        Log.v(TAG, fileHandler.getClientInfo(FileHandler.CLIENT_ID) + " secret: " + fileHandler.getClientInfo(FileHandler.CLIENT_SECRET));
        webview.loadUrl("https://api.beddit.com/api/oauth/authorize?client_id=" +
                fileHandler.getClientInfo(FileHandler.CLIENT_ID) +
                "&redirect_uri="+REDIRECT_URI+"/oauth&response_type=code");
    }

    private void removeCookies() {
        CookieSyncManager cookieMonster = CookieSyncManager.createInstance(webview.getContext());
        CookieManager.getInstance().removeSessionCookie();
        CookieManager.getInstance().removeAllCookie();
        cookieMonster.sync();
    }

    @Override
    public void onBackPressed() {
        Log.v(TAG, "BACK PRESSED");
        fail(true);
    }
}
