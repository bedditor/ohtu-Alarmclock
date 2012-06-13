package ohtu.beddit.web;

import android.app.Dialog;
import android.app.ProgressDialog;
import android.content.Context;
import android.graphics.Bitmap;
import android.util.Log;
import android.view.View;
import android.webkit.WebView;
import android.webkit.WebViewClient;
import android.widget.ImageView;
import android.widget.TextView;
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
    List<TokenListener> listeners = new LinkedList<TokenListener>();
    Dialog dialog;
    String[] blacklist = {"http://www.beddit.com/","https://www.beddit.com/login", "http://www.beddit.com/sleep", "https://api.beddit.com/reset_password", "mailto:support@beddit.com", "https://api.beddit.com/signup", "http://www.cs.helsinki.fi/","http://www.cs.helsinki.fi/home/"};
    public AmazingWebClient(Context context) {
        dialog = new Dialog(context, R.style.CustomDialogTheme);
        dialog.setContentView(R.layout.loading_dialog);
        dialog.show();

    }

    public void addTokenListener(TokenListener l) {
        listeners.add(l);
    }

    @Override
    public boolean shouldOverrideUrlLoading(WebView view, String url) {
        Log.v("AmazingWebClient", "shouldOverrideUrlLoading: " + url);
        for (String filter: blacklist) {
            if (url.equalsIgnoreCase(filter)) {
                for (TokenListener l : listeners)
                    l.onTokenReceived("Not Supported");
                return true;
            }
        }
        Pattern S = Pattern.compile("\\Qhttps://api.beddit.com/api/oauth/access_token?client_id=\\E.*\\Q&redirect_uri=https://peach-app.appspot.com/oauth&client_secret=\\E.*\\Q&grant_type=code&code=\\E.*");
        Matcher match = S.matcher(url);
        if (match.matches())
            return true;
        for (TokenListener l : listeners)
            l.onTokenReceived(url);
        return false;
    }

    @Override
    public void onPageFinished(WebView view, String url) {
        super.onPageFinished(view, url);    //To change body of overridden methods use File | Settings | File Templates.
        dialog.dismiss();
    }
}
