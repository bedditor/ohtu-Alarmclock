package ohtu.beddit.activity;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.webkit.*;
import ohtu.beddit.R;
import ohtu.beddit.io.FileHandler;
import ohtu.beddit.io.PreferenceService;
import ohtu.beddit.json.BedditApiController;
import ohtu.beddit.web.AmazingWebClient;
import ohtu.beddit.web.BedditWebConnector;
import ohtu.beddit.web.OAuthHandling;
import ohtu.beddit.web.TokenListener;

import java.util.regex.Matcher;
import java.util.regex.Pattern;

/**
 * Created with IntelliJ IDEA.
 * User: juho
 * Date: 29.5.2012
 * Time: 13:42
 */
public class AuthActivity extends Activity implements TokenListener {
    WebView webview;
    private final String TAG = "AuthActivity";


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.webview);

        webview = (WebView) findViewById(R.id.webLayout);
        webview.clearHistory();
        removeCookies();
        setSettings();
        openAuthBrowser();
    }



    @Override
    public void onTokenReceived(String token) {
        // Seeing if we get the right url
        Pattern code = Pattern.compile("\\Qhttps://peach-app.appspot.com/oauth?code=\\E(.+)");
        Pattern error = Pattern.compile("\\Qhttps://peach-app.appspot.com/oauth?error=access_denied\\E");
        Matcher match = code.matcher(token);
        Matcher problem = error.matcher(token);

        Log.v(TAG, "Trying to match url: " + token);
        if (match.matches()) {
            Log.v(TAG, "Success");
            // We got the right url so we construct our https get url and give it to OAuthHandling class which will get the access token by https connection
            token = "https://api.beddit.com/api/oauth/access_token?client_id="+ FileHandler.getClientInfo(this, FileHandler.CLIENT_ID) + "&redirect_uri=https://peach-app.appspot.com/oauth&client_secret="+ FileHandler.getClientInfo(this, FileHandler.CLIENT_SECRET) + "&grant_type=code&code="+match.group(1);
            String result = OAuthHandling.getAccessToken(this, token);
            // If we get error, well you shouldn't. We close the program because we won't get correct access_token. Breaks other code?
            if (result.equalsIgnoreCase("error")) {
                Log.v(TAG, "Something went wrong while getting access token from correct url. *pfft*");
                fail();
            }
            // We put the correct access token to safe and be happy. User is allowed to use the program now.
            PreferenceService.setSetting(this, R.string.pref_key_userToken, result);
            Log.v("Tokenizer:", PreferenceService.getSettingString(this, R.string.pref_key_userToken));
            saveUsername();
            finish();
        }
        // If user doesn't allow the program to access, we simply terminate the program.
        if (problem.matches()) {
            fail();
        }
    }

    private void fail(){
        Intent resultIntent = new Intent((String) null);
        setResult(Activity.RESULT_OK, resultIntent);
        PreferenceService.setSetting(this, R.string.pref_key_userToken, "");
        PreferenceService.setSetting(this, R.string.pref_key_username, "");
        finish();
    }

    private void saveUsername() {
        BedditWebConnector webConnector = new BedditWebConnector();
        String usernameJson = webConnector.getUsernameJson(this);
        Log.v(TAG,"got username json: "+usernameJson);
        BedditApiController apiController = new BedditApiController();
        String username = apiController.getUsername(usernameJson, 0);
        PreferenceService.setSetting(this, R.string.pref_key_username, username);
    }

    @Override
    public void onBackPressed() {
        //If we stop before getting access token, we will terminate the program.
        Intent resultIntent = new Intent((String) null);
        setResult(Activity.RESULT_OK, resultIntent);
        finish();
    }

    @Override
    public void finish() {
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
        client.addTokenListener(this);
        webview.setWebViewClient(client);
        Log.v(TAG, FileHandler.getClientInfo(this, FileHandler.CLIENT_ID) + " secret: " + FileHandler.getClientInfo(this, FileHandler.CLIENT_SECRET));
        webview.loadUrl("https://api.beddit.com/api/oauth/authorize?client_id="+ FileHandler.getClientInfo(this, FileHandler.CLIENT_ID) + "&redirect_uri=https://peach-app.appspot.com/oauth&response_type=code");
    }

    private void removeCookies() {
        // Removes all cookies that the webviewclient or webview has.
        CookieSyncManager cookieMonster = CookieSyncManager.createInstance(webview.getContext());
        CookieManager.getInstance().removeSessionCookie();
        CookieManager.getInstance().removeAllCookie();
        cookieMonster.sync();
    }
}
