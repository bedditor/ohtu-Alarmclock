package ohtu.beddit.activity;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.preference.Preference;
import android.text.AndroidCharacter;
import android.text.Html;
import android.util.Log;
import android.view.Display;
import android.view.View;
import android.view.WindowManager;
import android.webkit.*;
import android.widget.Toast;
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
 * To change this template use File | Settings | File Templates.
 */
public class AuthActivity extends Activity implements TokenListener {
    WebViewClient wvc;
    WebView webview;
    private final String TAG = "AuthActivity";


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);    //To change body of overridden methods use File | Settings | File Templates.
        setContentView(R.layout.webview);

        //WebView webview = new WebView(this);
        webview = (WebView) findViewById(R.id.webLayout);
        webview.clearHistory();
        CookieSyncManager cookieMonster = CookieSyncManager.createInstance(webview.getContext());
        CookieManager.getInstance().removeSessionCookie();
        CookieManager.getInstance().removeAllCookie();
        cookieMonster.sync();


        WebSettings settings = webview.getSettings();
        webview.setInitialScale(1);
        settings.setSavePassword(false);
        settings.setCacheMode(WebSettings.LOAD_NO_CACHE);
        settings.setAppCacheEnabled(false);
        settings.setUseWideViewPort(true);
        settings.setLoadWithOverviewMode(true);
        settings.setDefaultZoom(WebSettings.ZoomDensity.FAR);


        wvc = new AmazingWebClient(this, webview);
        webview.setWebViewClient(wvc);
        Log.v("AuthActivity", FileHandler.getClientId(this) + " secret: " + FileHandler.getClientSecret(this));
        webview.loadUrl("https://api.beddit.com/api/oauth/authorize?client_id="+ FileHandler.getClientId(this) + "&redirect_uri=https://peach-app.appspot.com/oauth&response_type=code");
    }



    @Override
    public void onTokenReceived(String token) {
        //Toasts don't work in webview
        Pattern S = Pattern.compile("https...peach.app.appspot.com.oauth.code=(.+)");
        Matcher supah = S.matcher(token);
        Log.v(TAG, "Trying to match: " + token);
        if (supah.matches()) {
            Log.v(TAG, "Matches: " + token);
            token = "https://api.beddit.com/api/oauth/access_token?client_id="+ FileHandler.getClientId(this) + "&redirect_uri=https://peach-app.appspot.com/oauth&client_secret="+ FileHandler.getClientSecret(this) + "&grant_type=code&code="+supah.group(1);
            String result = OAuthHandling.getAccessToken(this, token);
            if (result.equalsIgnoreCase("error"))
                Log.v(TAG, "Something went wrong while getting access token from correct url. *pfft*");
            PreferenceService.setSetting(this, R.string.pref_key_userToken, result);
            Log.v("Toukenizer:", PreferenceService.getSettingString(this, R.string.pref_key_userToken));
            saveUsername();
            finish();
        }
    }


    private void saveUsername() {
        BedditWebConnector webConnector = new BedditWebConnector();
        String usernameJson = webConnector.getUsernameJson(this);
        Log.v("AuthActivity","got json: "+usernameJson);
        BedditApiController apiController = new BedditApiController();
        String username = apiController.getUsername(usernameJson, 0);
        PreferenceService.setSetting(this, R.string.pref_key_username, username);
    }


    @Override
    public void onBackPressed() {
        Intent resultIntent = new Intent((String) null);
        setResult(Activity.RESULT_OK, resultIntent);
        finish();
    }

    @Override
    public void finish() {
        webview.clearCache(true);
        webview.clearView();
        webview.clearHistory();
        super.finish();    //To change body of overridden methods use File | Settings | File Templates.
    }
}
