package ohtu.beddit.web;

import android.content.Context;
import android.graphics.Bitmap;
import android.util.Log;
import android.webkit.WebView;
import android.webkit.WebViewClient;
import ohtu.beddit.R;
import ohtu.beddit.activity.AuthActivity;

import java.util.LinkedList;
import java.util.List;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

/**
 * Created with IntelliJ IDEA.
 * User: juho
 * Date: 29.5.2012
 * Time: 13:56
 * To change this template use File | Settings | File Templates.
 */
public class AmazingWebClient extends WebViewClient {
    private List<UrlListener> listeners = new LinkedList<UrlListener>();
    private LoadingDialog dialog;
    private String[] blacklist = {"http://www.beddit.com/",
            "https://www.beddit.com/login",
            "http://www.beddit.com/sleep",
            "https://api.beddit.com/reset_password",
            "mailto:support@beddit.com",
            "https://api.beddit.com/signup",
            "http://www.cs.helsinki.fi/",
            "http://www.cs.helsinki.fi/home/",
            "https://api.beddit.com/newbeddit/",
            "https://api.beddit.com/login"};

    private static final String TAG = "AmazingWebClient";

    public AmazingWebClient(Context context) {
        dialog = new LoadingDialog(context);
    }

    public void addUrlListener(UrlListener l) {
        listeners.add(l);
    }

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
        if (isRedirect.matcher(url).matches())
            return true;

        return false;
    }

    @Override
    public void onLoadResource(WebView view, String url) {

        Log.v(TAG, "onLoadResource " + url);
        super.onLoadResource(view, url);    //To change body of overridden methods use File | Settings | File Templates.
    }

    @Override
    public void onPageStarted(WebView view, String url, Bitmap favicon) {
        dialog.show();
        Log.v(TAG, "onPageStarted " + url);
        super.onPageStarted(view, url, favicon);    //To change body of overridden methods use File | Settings | File Templates.
    }

    @Override
    public void onPageFinished(WebView view, String url) {
        dialog.dismiss();
        super.onPageFinished(view, url);    //To change body of overridden methods use File | Settings | File Templates.
    }
}
