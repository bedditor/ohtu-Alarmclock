package ohtu.beddit.web;

/**
 * Interface which should be implemented. Mainly used as passage in AuthActivity to pass url from AmazingWebClient back
 * to the AuthActivity.
 */
public interface UrlListener {
    public void onUrlReceived(String url);
}
