package ohtu.beddit.web;

import android.app.ProgressDialog;
import android.content.Context;
import android.graphics.Bitmap;
import android.view.View;
import android.webkit.WebView;
import android.webkit.WebViewClient;
import ohtu.beddit.R;

import java.util.LinkedList;
import java.util.List;

/**
 * Created with IntelliJ IDEA.
 * User: juho
 * Date: 29.5.2012
 * Time: 13:56
 * To change this template use File | Settings | File Templates.
 */
public class AmazingWebClient extends WebViewClient {
    Context context;
    List<TokenListener> listeners = new LinkedList<TokenListener>();
    String[] blacklist = {"http://www.beddit.com/", "http://www.beddit.com/sleep", "https://api.beddit.com/reset_password", "mailto:support@beddit.com", "https://api.beddit.com/signup", "http://www.cs.helsinki.fi/","http://www.cs.helsinki.fi/home/"};
    public AmazingWebClient(Context context) {
        this.context = context;
        dialog = ProgressDialog.show(context, "",
                context.getString(R.string.page_loading), true);
    }

    public void addTokenListener(TokenListener l) {
        listeners.add(l);
    }

    @Override
    public boolean shouldOverrideUrlLoading(WebView view, String url) {
        for (String filter: blacklist) {
            if (url.equalsIgnoreCase(filter)) {
                for (TokenListener l : listeners)
                    l.onTokenReceived("Not Supported");
                return true;
            }
        }

        for (TokenListener l : listeners)
            l.onTokenReceived(url);
        return false;
    }

    ProgressDialog dialog;


    @Override
    public void onPageFinished(WebView view, String url) {
        super.onPageFinished(view, url);    //To change body of overridden methods use File | Settings | File Templates.
        dialog.dismiss();
    }


}
