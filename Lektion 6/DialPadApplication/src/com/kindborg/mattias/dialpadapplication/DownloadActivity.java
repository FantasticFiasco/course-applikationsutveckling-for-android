package com.kindborg.mattias.dialpadapplication;

import android.os.*;
import android.webkit.*;

public class DownloadActivity extends BaseActivity {

    public static final String EXTRA_SOURCEURL = "extra_sourceurl";
    public static final String EXTRA_TARGETDIRECTORY = "extra_targetdirectory";

    private String targetDirectory;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_download);

        // Get data from extra
        String sourceUrl = getExtra(EXTRA_SOURCEURL);
        targetDirectory = getExtra(EXTRA_TARGETDIRECTORY);

        // Initiate web view
        WebView webView = (WebView) findViewById(R.id.downloadactivity_webview);
        webView.loadUrl(sourceUrl);
        webView.setWebViewClient(new Callback());
    }

    private static class Callback extends WebViewClient {

        @Override
        public boolean shouldOverrideUrlLoading(WebView view, String url) {
            // TODO: Implement
            return true;
        }
    }
}