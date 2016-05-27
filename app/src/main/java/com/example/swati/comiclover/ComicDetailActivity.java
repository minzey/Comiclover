package com.example.swati.comiclover;

import android.app.SearchManager;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.webkit.WebView;
import android.webkit.WebViewClient;

public class ComicDetailActivity extends AppCompatActivity {
    final String TAG="COMICDETAIL";
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_comic_detail);
        Bundle extras=getIntent().getExtras();
        String comic_url=extras.getString("intent_digitalurl");
        Log.d(TAG, comic_url);
        WebView myWebView = (WebView) findViewById(R.id.webview);
        myWebView.getSettings().setJavaScriptEnabled(true);
        myWebView.setWebViewClient(new MyWebClient());
        myWebView.loadUrl(comic_url);
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_comic_detail, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();

        //noinspection SimplifiableIfStatement
        if (id == R.id.action_settings) {
            return true;
        }

        return super.onOptionsItemSelected(item);
    }
}

class MyWebClient extends WebViewClient {
    final String TAG="WEBCLIENT";
    /*@Override
    public boolean shouldOverrideUrlLoading(WebView view, String url) {
        view.loadUrl(url);
        return true;
    }*/

    @Override
    public void onPageFinished(WebView view, String url) {
        Log.d(TAG,"removing preview-box");
        view.loadUrl("javascript:(function() {document.getElementById('preview-box').style.display='none';})()");
    }
}