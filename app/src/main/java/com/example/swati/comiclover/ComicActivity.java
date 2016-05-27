package com.example.swati.comiclover;

import android.app.ProgressDialog;
import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.StaggeredGridLayoutManager;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.AbsListView;
import android.widget.AdapterView;
import android.widget.ListAdapter;
import android.widget.ListView;
import android.widget.Toast;

import com.android.volley.Request;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonObjectRequest;
import com.example.swati.comiclover.Networking.AppController;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;

public class ComicActivity extends AppCompatActivity {
    private String character_id;
    final String TAG="COMIC_ACTIVITY";
    private LinearLayoutManager mLayout;
    private ComicListAdapter mAdapter;
    private ProgressDialog pdialog;
    private ArrayList<ComicItem> comicItems;
    private String COMICURL = "http://gateway.marvel.com/v1/public/characters/1009150/comics?format=comic&hasDigitalIssue=true&apikey=98c280ca7e792decd643df89d62fcd61";
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_comic);
        Bundle extras= getIntent().getExtras();
        character_id=extras.getString("intent_id");
        Log.d(TAG, character_id);
        comicItems = new ArrayList<ComicItem>();

        pdialog = new ProgressDialog(this);
        //pdialog.setProgressStyle(ProgressDialog.STYLE_SPINNER);
        //pdialog.setTitle("Loading");
        pdialog.setMessage("Loading digital comics...");
        COMICURL="http://gateway.marvel.com/v1/public/characters/"+character_id+"/comics?format=comic&hasDigitalIssue=true&ts=1&apikey=98c280ca7e792decd643df89d62fcd61&hash=1bef2374a9d247fde0ddbf8d89492267";
        mLayout=new LinearLayoutManager(getApplicationContext());
        RecyclerView mView = (RecyclerView) findViewById(R.id.comic_list);
        mView.setLayoutManager(mLayout);
        mAdapter = new ComicListAdapter(getApplicationContext(),comicItems);
        mView.setAdapter(mAdapter);
        graburl(COMICURL);

        mView.addOnItemTouchListener(
                new RecyclerItemClickListener(getApplicationContext(), new RecyclerItemClickListener.OnItemClickListener() {
                    @Override
                    public void onItemClick(View view, int position) {

                        Log.d(TAG, "item clicked!");

                        ComicItem temp = new ComicItem(comicItems.get(position));
                        Intent i = new Intent(getApplicationContext(), ComicDetailActivity.class);
                        i.putExtra("intent_digitalurl", temp.getDigitalurl());
                        startActivity(i);

                    }
                })
        );



    }

    public void graburl(String COMICURL){
        JsonObjectRequest jsObjRequest = new JsonObjectRequest
                (Request.Method.GET, COMICURL, null, new Response.Listener<JSONObject>() {

                    @Override
                    public void onResponse(JSONObject response) {

                        Log.d(TAG, response.toString());
                        showDialog();
                        parsejsonfeed(response);
                    }
                }, new Response.ErrorListener() {

                    @Override
                    public void onErrorResponse(VolleyError error) {

                        Log.d(TAG,error.toString());
                        Toast.makeText(getApplicationContext(),error.toString(),Toast.LENGTH_SHORT).show();

                    }
                });
        AppController.getInstance().addToRequestQueue(jsObjRequest);

    }

    private void showDialog() {
        if (!pdialog.isShowing())
            pdialog.show();
    }

    private void hideDialog() {
        if (pdialog.isShowing())
            pdialog.dismiss();
    }

    private void parsejsonfeed(JSONObject response) {
        try {
            //superhero_items.clear();
            JSONObject dataObject = response.getJSONObject("data");
            JSONArray resultsArray = dataObject.getJSONArray("results");
            Log.d(TAG, "results array retrieved");
            for (int i = 0; i < resultsArray.length(); i++) {
                JSONObject feedObj = (JSONObject) resultsArray.get(i);
                ComicItem item = new ComicItem();
                //Log.d(TAG, feedObj.getString("title"));
                item.setId(feedObj.getInt("id"));
                item.setDigitalid(feedObj.getInt("digitalId"));
                item.setTitle(feedObj.getString("title"));
                item.setDes(feedObj.getString("description"));
                JSONObject imageObj = (JSONObject)feedObj.get("thumbnail");
                String imageurl=imageObj.getString("path")+"."+imageObj.getString("extension");
                //if(imageurl.equals("http://i.annihil.us/u/prod/marvel/i/mg/b/40/image_not_available.jpg")){item.setImage(null);}
                //else{
                item.setImage(imageurl);
                /*JSONArray urlArray = (JSONArray)feedObj.get("urls");
                for(int j=0;j<urlArray.length();j++){
                    JSONObject urlObj = (JSONObject) urlArray.get(j);
                    if(urlObj.getString("type").equals("reader")){
                        item.setDigitalurl(urlObj.getString("url"));
                    }
                }*/
                item.setDigitalurl("http://read.marvel.com/#/book/"+feedObj.getInt("digitalId"));


                comicItems.add(item);
                //Log.d(TAG, "id: " + feedObj.getInt("id") + " name: " + feedObj.getString("name") + " image: " + imageObj.getString("path") + "." + imageObj.getString("extension"));

            }

            mAdapter.notifyDataSetChanged();
            hideDialog();
            //flag_loading=false;
            //swipeRefreshLayout.setRefreshing(false);
        } catch (JSONException e) {
            e.printStackTrace();
        }
    }



    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_comic, menu);
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
