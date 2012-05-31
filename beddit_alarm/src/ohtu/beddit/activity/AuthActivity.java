package ohtu.beddit.activity;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.preference.Preference;
import android.text.AndroidCharacter;
import android.util.Log;
import android.view.Display;
import android.view.WindowManager;
import android.webkit.WebSettings;
import android.webkit.WebView;
import android.webkit.WebViewClient;
import android.widget.Toast;
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
        setContentView(R.layout.webview);

        //WebView webview = new WebView(this);
        WebView webview = (WebView) findViewById(R.id.webLayout);
        WebSettings settings = webview.getSettings();
        webview.setInitialScale(1);
        settings.setSavePassword(false);
        settings.setCacheMode(WebSettings.LOAD_NO_CACHE);
        settings.setAppCacheEnabled(false);
        settings.setUseWideViewPort(true);
        settings.setLoadWithOverviewMode(true);
        settings.setDefaultZoom(WebSettings.ZoomDensity.FAR);

        wvc = new AmazingWebClient(this);
        webview.setWebViewClient(wvc);
        webview.loadUrl("http://peach-app.appspot.com/testi");
    }

    @Override
    public void onTokenRecieved(String token) {
        if (token.equals("Not Supported")) {
            Toast.makeText(this, token, Toast.LENGTH_SHORT);
            return;
        }
        Pattern p = Pattern.compile(".*code=(.{24})");
        Matcher m = p.matcher(token);
        Log.v("AuthActivity", "Trying to match: " + token);
        if (m.matches()) {
            Log.v("AuthActivity", "Matches: " + m.group(1));
            PreferenceService.setSetting(this, R.string.pref_key_userToken, m.group(1));

            Log.v("Toukenizer:", PreferenceService.getSettingString(this, R.string.pref_key_userToken));
            super.finish();
        }
    }

    @Override
    public void onBackPressed() {
        Intent resultIntent = new Intent((String) null);
        setResult(Activity.RESULT_OK, resultIntent);
        super.finish();
    }
}
