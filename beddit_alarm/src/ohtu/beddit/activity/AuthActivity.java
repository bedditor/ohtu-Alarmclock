package ohtu.beddit.activity;

import android.app.Activity;
import android.os.Bundle;
import android.preference.Preference;
import android.util.Log;
import android.webkit.WebSettings;
import android.webkit.WebView;
import android.webkit.WebViewClient;
import ohtu.beddit.R;
import ohtu.beddit.io.FileHandler;
import ohtu.beddit.io.PreferenceService;
import ohtu.beddit.web.AmazingWebClient;
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
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);    //To change body of overridden methods use File | Settings | File Templates.

        WebView webview = new WebView(this);
        WebSettings settings = webview.getSettings();
        settings.setSavePassword(false);
        settings.setCacheMode(WebSettings.LOAD_NO_CACHE);
        settings.setAppCacheEnabled(false);
        setContentView(webview);
        wvc = new AmazingWebClient(this);
        webview.setWebViewClient(wvc);
        webview.loadUrl("http://peach-app.appspot.com/testi");
        lol = 0;
    }

    int lol;

    @Override
    public void onTokenRecieved(String token) {
        lol++;
        Pattern p = Pattern.compile(".*code=(.{24})");
        Matcher m = p.matcher(token);
        Log.v("AuthActivity", "Trying to match: " + token);
        if (m.matches()) {
            Log.v("AuthActivity", "Matches: " + m.group(1));
            PreferenceService.setSetting(this, R.string.pref_key_userToken, m.group(1));

            Log.v("Toukenizer:", PreferenceService.getSettingString(this, R.string.pref_key_userToken));
            super.finish();
        }
        if (lol == 666) //menit jonnekki muualle, kuole pois
            super.finish();
        //super.finish();
    }
}
