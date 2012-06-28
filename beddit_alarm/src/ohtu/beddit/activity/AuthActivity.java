package ohtu.beddit.activity;

import android.app.Activity;
import android.content.Intent;
import android.content.res.Configuration;
import android.os.Bundle;
import android.util.Log;
import android.webkit.CookieManager;
import android.webkit.CookieSyncManager;
import android.webkit.WebSettings;
import android.webkit.WebView;
import ohtu.beddit.R;
import ohtu.beddit.api.ApiController;
import ohtu.beddit.api.jsonparser.classimpl.ApiControllerClassImpl;
import ohtu.beddit.api.jsonparser.classimpl.UserData;
import ohtu.beddit.io.FileHandler;
import ohtu.beddit.io.PreferenceService;
import ohtu.beddit.web.*;

import java.util.regex.Matcher;
import java.util.regex.Pattern;

/**
 * Activity that is called to handle Oauth2 process and to set the Token to make calls to beddit.
 * If cancelled, also designed to return Intent with resultcode which should be handled.
 */
public class AuthActivity extends Activity implements UrlListener {
    private WebView webview;
    private final String TAG = "AuthActivity";
    private FileHandler fileHandler;

    public static final int RESULT_CANCELLED = 101;
    public static final int RESULT_FAILED = 102;

    //Replace this url with the url setted for this application. Doesnt need to be url.
    public static final String REDIRECT_URI = "https://peach-app.appspot.com";

    /**
     * Creates the view and initializes FileHandler.
     * @param savedInstanceState
     */
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        Log.v(TAG, "OnCreate");
        setContentView(R.layout.webview);

        fileHandler = new FileHandler(this);
    }

    /**
     * Creates webview if it was null. Initializes afterwards.
     */
    @Override
    protected void onResume() {
        if (webview == null) {
            webview = (WebView) findViewById(R.id.webLayout);
            webview.clearHistory();
            removeCookies();
            setSettings();
            openAuthBrowser();
        }
        super.onResume();
    }

    /**
     * Handles the url's we get from UrlListener.
     * If correct url we will also try to get the token and finish afterwards.
     * @param url Recieved from UrlListener. Is Regexped to check for correct url in case for "oauth?code=".
     */
    @Override
    public void onUrlReceived(String url) {
        // Seeing if we got the right url
        Matcher code = Pattern.compile("\\Q" + REDIRECT_URI + "/oauth?code=\\E(.+)").matcher(url);
        Matcher error = Pattern.compile("\\Q" + REDIRECT_URI + "/oauth?error=\\E(.+)").matcher(url);

        Log.v(TAG, "Trying to match url: " + url);
        if (code.matches()) {
            Log.v(TAG, "Code: " + code.group(1));
            // We got the right url so we construct our https get url and give it to
            // BedditConnector which will get the access token by https connection
            url = "https://api.beddit.com/api/oauth/access_token?client_id=" +
                    fileHandler.getClientInfo(FileHandler.CLIENT_ID) +
                    "&redirect_uri=" + REDIRECT_URI + "/oauth&client_secret=" +
                    fileHandler.getClientInfo(FileHandler.CLIENT_SECRET) +
                    "&grant_type=code&code=" + code.group(1);

            // If we get error, well you shouldn't. We close the program because we won't get correct access_token. Breaks other code?
            try {
                ApiController apiController = new ApiControllerClassImpl();
                String token = apiController.getAccessToken(url);
                Log.v(TAG, "result: " + token);
                // We put the correct access token to safe and be happy. User is allowed to use the program now.
                PreferenceService.setToken(this, token);
                saveUserData();
                finish();
            }
            catch (BedditException e) {
                Log.v(TAG, "BedditException in onUrlReceived");
                fail(false);
            }
        }
        // If user doesn't allow the program to access, we simply terminate the program.
        else if (error.matches()) {
            Log.v(TAG, "Error: " + error.group(1));
            fail(true);
        }
    }

    /**
     * This method sets the resultIntent that is returned to Activity from where this Activity was created.
     * Uses the classes own RESULT_CANCELLED and RESULT_FAILED codes.Finishes the activity afterwards.
     *
     * @param cancelledByUser if user cancelled then True.
     */
    private void fail(boolean cancelledByUser) {
        Log.v(TAG, "fail called");
        Intent resultIntent = new Intent((String) null);
        if (cancelledByUser) {
            setResult(RESULT_CANCELLED, resultIntent);
        } else {
            setResult(RESULT_FAILED, resultIntent);
        }
        finish();
    }

    /**
     * Saves the user data to PreferenceService so that we can use them in other activities. Will call fail in case of
     * exception.
     */
    private void saveUserData() {
        Log.v(TAG, "saving user data");
        try {
            ApiController apiController = new ApiControllerClassImpl();
            apiController.updateUserData(this); //updates the info in api controller for lines below:
            UserData userData = apiController.getUserData(this);
            PreferenceService.setUsername(this, userData.getUsername());
            PreferenceService.setFirstName(this, userData.getFirstName());
            PreferenceService.setLastName(this, userData.getLastName());
        } catch (Exception e) {
            Log.v(TAG, "saving user data failed");
            fail(false);
        }
    }

    /**
     * Clears browsers of all data. Super finishes.
     */
    @Override
    public void finish() {
        Log.v(TAG, "finishing");
        webview.clearCache(true);
        webview.clearView();
        webview.clearHistory();
        super.finish();
    }

    /**
     * Sets the browser settings so that we don't save anything unnecessary and the beddit login page is shown nicely.
     */
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

    /**
     * Initializes the BedditLoginClient and opens the oAuth2 url to begin the login process.
     */
    private void openAuthBrowser() {
        // Initialize the webClient
        BedditLoginClient client = new BedditLoginClient(this);
        client.addUrlListener(this);
        webview.setWebViewClient(client);
        Log.v(TAG, fileHandler.getClientInfo(FileHandler.CLIENT_ID) + " secret: " +
                fileHandler.getClientInfo(FileHandler.CLIENT_SECRET));
        webview.loadUrl("https://api.beddit.com/api/oauth/authorize?client_id=" +
                fileHandler.getClientInfo(FileHandler.CLIENT_ID) +
                "&redirect_uri=" + REDIRECT_URI + "/oauth&response_type=code");
    }

    /**
     * Removes the cookies from browser.
     */
    private void removeCookies() {
        CookieSyncManager cookieMonster = CookieSyncManager.createInstance(webview.getContext());
        CookieManager.getInstance().removeSessionCookie();
        CookieManager.getInstance().removeAllCookie();
        cookieMonster.sync();
    }

    /**
     * Back button is considered as "login process cancelled by user". We call "fail(true)". See "fail" for more info.
     */
    @Override
    public void onBackPressed() {
        Log.v(TAG, "BACK PRESSED");
        fail(true);
    }

    /**
     * Do nothing special on configuration change.
     * Prevents the activity from restarting on hardware keyboard
     * slide out and orientation change.
     * @param newConfig
     */
    @Override
    public void onConfigurationChanged(Configuration newConfig) {
        super.onConfigurationChanged(newConfig);
    }
}
