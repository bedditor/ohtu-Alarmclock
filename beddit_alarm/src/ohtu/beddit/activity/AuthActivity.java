package ohtu.beddit.activity;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.KeyEvent;
import android.view.WindowManager;
import android.webkit.*;
import ohtu.beddit.R;
import ohtu.beddit.io.FileHandler;
import ohtu.beddit.io.PreferenceService;
import ohtu.beddit.api.ApiController;
import ohtu.beddit.api.jsonclassimpl.ApiControllerClassImpl;
import ohtu.beddit.web.*;

import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class AuthActivity extends Activity implements TokenListener {
    WebView webview;
    private final String TAG = "AuthActivity";
    private FileHandler fileHandler;

    public static final int RESULT_CANCELLED = 101;
    public static final int RESULT_FAILED = 102;

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
    public void onTokenReceived(String url) {
        // Seeing if we got the right url
        Pattern code = Pattern.compile("\\Qhttps://peach-app.appspot.com/oauth?code=\\E(.+)");
        Pattern error = Pattern.compile("\\Qhttps://peach-app.appspot.com/oauth?error=access_denied\\E");
        Matcher match = code.matcher(url);
        Matcher problem = error.matcher(url);

        Log.v(TAG, "Trying to match url: " + url);
        if (match.matches()) {
            Log.v(TAG, "Success");
            // We got the right url so we construct our https get url and give it to BedditConnector which will get the access token by https connection
            url = "https://api.beddit.com/api/oauth/access_token?client_id="+ fileHandler.getClientInfo(FileHandler.CLIENT_ID) + "&redirect_uri=https://peach-app.appspot.com/oauth&client_secret="+ fileHandler.getClientInfo(FileHandler.CLIENT_SECRET) + "&grant_type=code&code="+match.group(1);

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
                Log.v(TAG, "Something went wrong while getting access token from correct url. *pfft*");
                fail(false);
            }
        }
        // If user doesn't allow the program to access, we simply terminate the program.
        else if (problem.matches()) {
            Log.v(TAG, "Access denied");
            fail(true);
        }
    }

    /*
    public void derpDialog(){
        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        builder.setMessage("CRASH!");
        builder.setCancelable(false);
        builder.setPositiveButton("D:", new DialogInterface.OnClickListener() {
            public void onClick(DialogInterface dialog, int id) {
                BedditWebConnector blob = new BedditWebConnector();
                try{
                    Log.v("derp", "tila on: "+blob.getQueueStateJson(AuthActivity.this, TimeUtils.getTodayAsQueryDateString()));
                }catch (Exception e){

                }
            }
        });
        AlertDialog alert = builder.create();
        Log.v("dialogi", "nakyy: "+alert.isShowing());
        alert.show();
        Log.v("dialogi", "nakyy: "+alert.isShowing());
    }
    */

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
        client.addTokenListener(this);
        webview.setWebViewClient(client);
        Log.v(TAG, fileHandler.getClientInfo(FileHandler.CLIENT_ID) + " secret: " + fileHandler.getClientInfo(FileHandler.CLIENT_SECRET));
        webview.loadUrl("https://api.beddit.com/api/oauth/authorize?client_id=" + fileHandler.getClientInfo(FileHandler.CLIENT_ID) + "&redirect_uri=https://peach-app.appspot.com/oauth&response_type=code");
    }

    private void removeCookies() {
        // Removes all cookies that the webviewclient or webview has.
        CookieSyncManager cookieMonster = CookieSyncManager.createInstance(webview.getContext());
        CookieManager.getInstance().removeSessionCookie();
        CookieManager.getInstance().removeAllCookie();
        cookieMonster.sync();
    }

    @Override
    public void onAttachedToWindow() {
        Log.v(TAG, "SETTING KEYGUARD ON");
        Log.v(TAG, "onAttachedToWindow");
        this.getWindow().setType(WindowManager.LayoutParams.TYPE_KEYGUARD);
        super.onAttachedToWindow();    //To change body of overridden methods use File | Settings | File Templates.
    }

    @Override
    public void onBackPressed() {
        Log.v(TAG, "BACK PRESSED");
        setResult(MainActivity.RESULT_KILL);
        finish();
    }

    @Override
    public boolean onKeyDown(int keyCode, KeyEvent event) {
        if (keyCode == KeyEvent.KEYCODE_HOME) {
            Log.v(TAG, "HOME PRESSED");
            setResult(MainActivity.RESULT_HOME_BUTTON_KILL);
            finish();
            return true;
        }

        if (keyCode == KeyEvent.KEYCODE_CALL) {
            Log.v(TAG, "CALL PRESSED");
            setResult(MainActivity.RESULT_CALL_BUTTON_KILL);
            finish();
            return true;
        }

        return super.onKeyDown(keyCode, event);
    }
}
