package com.example.swati.comiclover;

import android.app.SearchManager;
import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.webkit.WebSettings;
import android.webkit.WebView;
import android.webkit.WebViewClient;

public class SearchResultsActivity extends AppCompatActivity {
    final String TAG="SEARCH RESULT ACTIVITY";
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_search_results);
        handleIntent(getIntent());
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_search_results, menu);
        return true;
    }

    @Override
    protected void onNewIntent(Intent intent) {

        handleIntent(intent);
    }

    private void handleIntent(Intent intent) {
        Log.d(TAG, "intent recognised!");
        if (Intent.ACTION_SEARCH.equals(intent.getAction())) {
            String query = intent.getStringExtra(SearchManager.QUERY);
            setTitle("More Awesomeness");
            query.replace(" ", "+");
            String searchURL="https://www.google.com/m?q="+query;
            WebView myWebView = (WebView) findViewById(R.id.webview);
            myWebView.getSettings().setJavaScriptEnabled( true );
            myWebView.getSettings().setCacheMode(WebSettings.LOAD_DEFAULT);
            myWebView.setWebViewClient(new WebViewClient());

            myWebView.loadUrl(searchURL);
            //use the query to search your data somehow
        }
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
