package ohtu.beddit.web;

import android.view.View;
import android.webkit.WebView;
import android.webkit.WebViewClient;

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
    TokenListener listener;

    String[] blacklist = {"http://www.beddit.com/", "http://www.beddit.com/sleep", "https://api.beddit.com/reset_password", "mailto:support@beddit.com", "https://api.beddit.com/signup", "http://www.cs.helsinki.fi/","http://www.cs.helsinki.fi/home/"};
    public AmazingWebClient(TokenListener listener, WebView view) {
        view.setVisibility(View.INVISIBLE);
        this.listener = listener;
    }

    @Override
    public boolean shouldOverrideUrlLoading(WebView view, String url) {
        for (String filter: blacklist) {
            if (url.equalsIgnoreCase(filter)) {
                listener.onTokenReceived("Not Supported");
                return true;
            }
        }
        Pattern S = Pattern.compile("https...api.beddit.com.api.oauth.access_token.client_id.*redirect_uri.https...peach-app.appspot.com.oauth.client_secret.*grant_type.code.code.*");
        Matcher match = S.matcher(url);
        if (match.matches())
            return true;
        listener.onTokenReceived(url);
        return false;
    }

    @Override
    public void onPageFinished(WebView view, String url) {
        super.onPageFinished(view, url);    //To change body of overridden methods use File | Settings | File Templates.
        view.setVisibility(View.VISIBLE);
    }
}
