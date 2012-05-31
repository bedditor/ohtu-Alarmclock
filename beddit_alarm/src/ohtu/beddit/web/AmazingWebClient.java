package ohtu.beddit.web;

import android.webkit.WebView;
import android.webkit.WebViewClient;

/**
 * Created with IntelliJ IDEA.
 * User: juho
 * Date: 29.5.2012
 * Time: 13:56
 * To change this template use File | Settings | File Templates.
 */
public class AmazingWebClient extends WebViewClient {
    TokenListener listener;
    public AmazingWebClient(TokenListener listener) {
        this.listener = listener;
    }
    @Override
    public boolean shouldOverrideUrlLoading(WebView view, String url) {
        listener.onTokenRecieved(url);
        return false;
    }
}
