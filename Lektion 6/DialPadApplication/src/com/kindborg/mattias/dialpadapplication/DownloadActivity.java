package com.kindborg.mattias.dialpadapplication;

import java.io.*;
import java.net.*;

import android.app.*;
import android.os.*;
import android.webkit.*;
import android.widget.*;

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

    /**
     * Class responsible for loading URLs in the WebView.
     */
    private class Callback extends WebViewClient {

        @Override
        public boolean shouldOverrideUrlLoading(WebView view, String url) {
            String fileName = URLUtil.guessFileName(url, null, null);
            String absoluteFileName = ExternalStorage.combine(targetDirectory, fileName);

            if (!ExternalStorage.fileExists(absoluteFileName)) {
                new DownloadTask(absoluteFileName).execute(url);
            }

            return true;
        }
    }

    /**
     * Task responsible for downloading a file from Internet.
     */
    private class DownloadTask extends AsyncTask<String, Void, Boolean> {

        private final ProgressDialog progressDialog;
        private final String targetFileName;

        public DownloadTask(String targetFileName) {
            this.targetFileName = targetFileName;
            progressDialog = new ProgressDialog(DownloadActivity.this);
            progressDialog.setMessage(DownloadActivity.this.getText(R.string.downloadactivity_download_message));
            progressDialog.setCancelable(false);
        }

        @Override
        protected void onPreExecute() {
            progressDialog.show();
        }

        @Override
        protected void onPostExecute(Boolean result) {
            progressDialog.dismiss();

            if (!result) {
                Toast
                    .makeText(DownloadActivity.this, R.string.downloadactivity_download_errormessage, Toast.LENGTH_SHORT)
                    .show();
            }
        }

        @Override
        protected Boolean doInBackground(String... urls) {
            InputStream input = null;
            OutputStream output = null;
            HttpURLConnection connection = null;

            try {
                URL url = new URL(urls[0]);
                connection = (HttpURLConnection) url.openConnection();
                connection.connect();

                // Handle HTTP failures
                if (connection.getResponseCode() != HttpURLConnection.HTTP_OK) {
                    return false;
                }

                // Download the file
                input = connection.getInputStream();
                output = new FileOutputStream(targetFileName);

                byte data[] = new byte[4096];
                int count;
                while ((count = input.read(data)) != -1) {
                    output.write(data, 0, count);
                }
            } catch (Exception e) {
                return false;
            } finally {
                try {
                    if (output != null) {
                        output.close();
                    }

                    if (input != null) {
                        input.close();
                    }

                } catch (IOException ignored) {
                }

                if (connection != null)
                    connection.disconnect();
            }

            return true;
        }
    }
}