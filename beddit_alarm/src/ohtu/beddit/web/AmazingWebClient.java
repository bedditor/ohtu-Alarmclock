package ohtu.beddit.web;

import android.app.Dialog;
import android.content.Context;
import android.util.Log;
import android.webkit.WebView;
import android.webkit.WebViewClient;
import ohtu.beddit.R;

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
    List<UrlListener> listeners = new LinkedList<UrlListener>();
    LoadingDialog dialog;
    String[] blacklist = {"http://www.beddit.com/","https://www.beddit.com/login", "http://www.beddit.com/sleep", "https://api.beddit.com/reset_password", "mailto:support@beddit.com", "https://api.beddit.com/signup", "http://www.cs.helsinki.fi/","http://www.cs.helsinki.fi/home/", "https://api.beddit.com/newbeddit/", "https://api.beddit.com/login"};

    private static final String TAG = "AmazingWebClient";

    public AmazingWebClient(Context context) {
        dialog = new LoadingDialog(context, R.style.CustomDialogTheme);
        dialog.setContentView(R.layout.loading_dialog);
        dialog.show();
    }

    public void addUrlListener(UrlListener l) {
        listeners.add(l);
    }

    @Override
    public boolean shouldOverrideUrlLoading(WebView view, String url) {
        Log.v(TAG, "shouldOverrideUrlLoading: " + url);
        for (String filter: blacklist) {
            if (url.equalsIgnoreCase(filter)) {
                for (UrlListener l : listeners)
                    l.onUrlReceived("Not Supported");
                return true;
            }
        }
        Pattern S = Pattern.compile("\\Qhttps://api.beddit.com/api/oauth/access_token?client_id=\\E.*\\Q&redirect_uri=https://peach-app.appspot.com/oauth&client_secret=\\E.*\\Q&grant_type=code&code=\\E.*");
        Matcher match = S.matcher(url);
        if (match.matches())
            return true;
        for (UrlListener l : listeners)
            l.onUrlReceived(url);
        return false;
    }

    @Override
    public void onPageFinished(WebView view, String url) {
        super.onPageFinished(view, url);    //To change body of overridden methods use File | Settings | File Templates.
        dialog.dismiss();
    }
}
