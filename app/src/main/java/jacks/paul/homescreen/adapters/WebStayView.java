package jacks.paul.homescreen.adapters;

import android.webkit.WebView;
import android.webkit.WebViewClient;

/**
 * Created by User on 2015-10-10.
 */
public class WebStayView extends WebViewClient {
    @Override
    public boolean shouldOverrideUrlLoading(WebView view, String url) {
        view.loadUrl(url);
        return true;
    }
}
