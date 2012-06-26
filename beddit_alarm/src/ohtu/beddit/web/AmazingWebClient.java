package ohtu.beddit.web;

import android.content.Context;
import android.graphics.Bitmap;
import android.util.Log;
import android.webkit.WebView;
import android.webkit.WebViewClient;
import ohtu.beddit.activity.AuthActivity;
import java.util.LinkedList;
import java.util.List;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

/**
 * This class handles Beddit oauth2 and mainly the website side of it. It is specifically designed to disable
 * all unnecessary links specific at Beddit login page and control so that the user can only login or cancel the login
 * process. This class is to be given to WebView.
 */
public class AmazingWebClient extends WebViewClient {
    private final List<UrlListener> listeners = new LinkedList<UrlListener>();
    private final LoadingDialog dialog;

    // Forbidden pages. Should be checked and update until
    // the login page is stripped-down of forbidden links
    private final String[] blacklist = {"http://www.beddit.com/",
            "https://www.beddit.com/login",
            "http://www.beddit.com/sleep",
            "https://api.beddit.com/reset_password",
            "mailto:support@beddit.com",
            "https://api.beddit.com/signup",
            "http://www.cs.helsinki.fi/",
            "http://www.cs.helsinki.fi/home/",
            "https://api.beddit.com/newbeddit/",
            "https://api.beddit.com/login",
            "https://api.beddit.com/logout_login"};

    private static final String TAG = "AmazingWebClient";

    public AmazingWebClient(Context context) {
        dialog = new LoadingDialog(context);
    }

    /**
     * This allows for data passage back to WebView and is required to be given as shouldOverrideUrlLoading method
     * expects that this is set correctly.
     * @param l The UrlListener.
     */
    public void addUrlListener(UrlListener l) {
        listeners.add(l);
    }


    /**
     * This method will deny the access to any other pages than the login pages. If it does pass all the check's then
     * we will also give the url through UrlListener. And to prevent double oauth for the correct address,
     * there is regexp that will prevent the oauth url to be loaded to this browser.
     * @param view Is given the current view.
     * @param url Is given the current url which the client is loading.
     * @return returns a boolean which decides wether to load the page or not.
     */
    @Override
    public boolean shouldOverrideUrlLoading(WebView view, String url) {
        Log.v(TAG, "shouldOverrideUrlLoading: " + url);
        for (String filter : blacklist) {
            if (url.equalsIgnoreCase(filter)) {
                for (UrlListener l : listeners)
                    l.onUrlReceived("Not Supported");
                return true;
            }
        }
        Pattern p = Pattern.compile("\\Qhttps://api.beddit.com/api/oauth/access_token?client_id=\\E.*\\Q&redirect_uri=" + AuthActivity.REDIRECT_URI + "/oauth&client_secret=\\E.*\\Q&grant_type=code&code=\\E.*");
        Matcher match = p.matcher(url);
        if (match.matches())
            return true;
        for (UrlListener l : listeners)
            l.onUrlReceived(url);

        Pattern isRedirect = Pattern.compile("^\\Q" + AuthActivity.REDIRECT_URI + "\\E.+");
        return isRedirect.matcher(url).matches();

    }

    @Override
    public void onLoadResource(WebView view, String url) {
        Log.v(TAG, "onLoadResource " + url);
        super.onLoadResource(view, url);
    }

    /**
     * We have overrided this method to show our own loading screen on top.
     */
    @Override
    public void onPageStarted(WebView view, String url, Bitmap favicon) {
        dialog.show();
        Log.v(TAG, "onPageStarted " + url);
        super.onPageStarted(view, url, favicon);
    }

    /**
     * We have overrided this method to dismiss our own loading screen to show the loaded webpage.
     */
    @Override
    public void onPageFinished(WebView view, String url) {
        dialog.dismiss();
        super.onPageFinished(view, url);
    }
}
