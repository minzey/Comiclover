package com.example.swati.comiclover;


import android.app.SearchManager;
import android.content.Context;
import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.SearchView;
import android.support.v7.widget.StaggeredGridLayoutManager;
import android.util.Log;
import android.view.GestureDetector;
import android.view.Menu;
import android.view.MenuItem;
import android.view.MotionEvent;
import android.view.View;
import android.view.inputmethod.InputMethodManager;
import android.widget.AbsListView;
import android.widget.Toast;


import com.android.volley.Cache;
import com.android.volley.Request;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonObjectRequest;
import com.example.swati.comiclover.Networking.AppController;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.UnsupportedEncodingException;
import java.util.ArrayList;

public class MainActivity extends AppCompatActivity {
    StaggeredGridLayoutManager mLayout;
    SuperheroListAdapter mAdapter;
    final String TAG="MAIN ACTIVITY";
    private int offset=0;
    int visibleItemCount;
    int totalItemCount;
    int pastVisibleItems;

    private boolean flag_loading=false;
    private ArrayList<SuperheroItem> superhero_items= new ArrayList<SuperheroItem>();
    private String CHARACTER_URL="http://gateway.marvel.com/v1/public/characters?limit=100&ts=1&apikey=98c280ca7e792decd643df89d62fcd61&hash=1bef2374a9d247fde0ddbf8d89492267";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        setTitle("Comic Lover");
        RecyclerView mView = (RecyclerView) findViewById(R.id.list);
        if(getResources().getConfiguration().orientation==getResources().getConfiguration().ORIENTATION_LANDSCAPE)
            mLayout = new StaggeredGridLayoutManager(3, 1);
        else
            mLayout= new StaggeredGridLayoutManager(2, 1);


        mView.setLayoutManager(mLayout);
        mAdapter = new SuperheroListAdapter(getApplicationContext(),superhero_items);
        mView.setAdapter(mAdapter);


        mView.setItemAnimator(null);
        mView.addOnItemTouchListener(
                new RecyclerItemClickListener(getApplicationContext(), new RecyclerItemClickListener.OnItemClickListener() {
                    @Override
                    public void onItemClick(View view, int position) {

                        Log.d(TAG, "item clicked!");

                        SuperheroItem temp = new SuperheroItem(superhero_items.get(position));
                        Intent i = new Intent(getApplicationContext(), ComicActivity.class);
                        i.putExtra("intent_id", String.valueOf(temp.getId()));

                        Log.d(TAG, "Character id clicked = " + String.valueOf(temp.getId()));

                        startActivity(i);
                    }
                })
        );



        mView.addOnScrollListener(new RecyclerView.OnScrollListener() {
            @Override
            public void onScrolled(RecyclerView recyclerView, int dx, int dy) {
                if (dy > 0) //check for scroll down
                {

                    visibleItemCount = mLayout.getChildCount();
                    totalItemCount = mLayout.getItemCount();
                    int[] firstPos = new int[mLayout.getSpanCount()];
                    pastVisibleItems = mLayout.findFirstVisibleItemPositions(firstPos)[0];


                    if (flag_loading == false) {
                        if ((visibleItemCount + pastVisibleItems) >= totalItemCount) {
                            Log.d(TAG, "scrolled to the bottom");
                            flag_loading = true;
                            Toast.makeText(getApplicationContext(), "Loading more characrters", Toast.LENGTH_SHORT).show();
                            offset = offset + 100;
                            Log.d(TAG, "offset value= " + offset);
                            graburl(offset);
                            //Do pagination.. i.e. fetch new data
                        }
                    }
                }
            }
        });
        graburl(offset);




    }


    public void graburl(int offset){
        String CHARACTER_URL_WITHOFFSET=CHARACTER_URL+"&offset="+offset;

        Cache cache = AppController.getInstance().getRequestQueue().getCache();
        Cache.Entry entry = cache.get(CHARACTER_URL_WITHOFFSET);
        if (entry != null) {
            // fetch the data from cache
            Log.d(TAG, "loading cached data");
            try {
                String data = new String(entry.data, "UTF-8");
                try {
                    parsejsonfeed(new JSONObject(data));
                } catch (JSONException e) {
                    e.printStackTrace();
                }
            } catch (UnsupportedEncodingException e) {
                e.printStackTrace();
            }
        }
        else{
            Log.d(TAG,"No cached data, fresh volley call!");

            JsonObjectRequest jsObjRequest = new JsonObjectRequest
                    (Request.Method.GET, CHARACTER_URL_WITHOFFSET, null, new Response.Listener<JSONObject>() {

                        @Override
                        public void onResponse(JSONObject response) {

                            Log.d(TAG, response.toString());
                            parsejsonfeed(response);
                        }
                    }, new Response.ErrorListener() {

                        @Override
                        public void onErrorResponse(VolleyError error) {

                            Log.d(TAG,error.toString());

                        }
                    });
            AppController.getInstance().addToRequestQueue(jsObjRequest);


        }



    }

    private void parsejsonfeed(JSONObject response) {
        try {
            //superhero_items.clear();
            JSONObject dataObject = response.getJSONObject("data");
            JSONArray resultsArray = dataObject.getJSONArray("results");
            Log.d(TAG, "results array retrieved");
            for (int i = 0; i < resultsArray.length(); i++) {
                JSONObject feedObj = (JSONObject) resultsArray.get(i);
                SuperheroItem item = new SuperheroItem();
                //Log.d(TAG, feedObj.getString("title"));
                item.setId(feedObj.getInt("id"));
                item.setName(feedObj.getString("name"));
                JSONObject imageObj = (JSONObject)feedObj.get("thumbnail");
                String imageurl=imageObj.getString("path")+"."+imageObj.getString("extension");
                //if(imageurl.equals("http://i.annihil.us/u/prod/marvel/i/mg/b/40/image_not_available.jpg")){item.setImage(null);}
                //else{
                item.setImage(imageurl);

                superhero_items.add(item);
                Log.d(TAG, "id: " + feedObj.getInt("id") + " name: " + feedObj.getString("name") + " image: " + imageObj.getString("path") + "." + imageObj.getString("extension"));

            }

            mAdapter.notifyDataSetChanged();
            flag_loading=false;
            Toast.makeText(getApplicationContext(),"100 characters loaded",Toast.LENGTH_SHORT).show();
            //swipeRefreshLayout.setRefreshing(false);
        } catch (JSONException e) {
            e.printStackTrace();
        }
    }




    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_main, menu);
        SearchManager searchManager =
                (SearchManager) getSystemService(Context.SEARCH_SERVICE);
        SearchView searchView =
                (SearchView) menu.findItem(R.id.search).getActionView();
        searchView.setSearchableInfo(
                searchManager.getSearchableInfo(getComponentName()));
        searchView.requestFocus();



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


class RecyclerItemClickListener implements RecyclerView.OnItemTouchListener {
    private OnItemClickListener mListener;

    public interface OnItemClickListener {
        public void onItemClick(View view, int position);
    }

    GestureDetector mGestureDetector;

    public RecyclerItemClickListener(Context context, OnItemClickListener listener) {
        mListener = listener;
        mGestureDetector = new GestureDetector(context, new GestureDetector.SimpleOnGestureListener() {
            @Override public boolean onSingleTapUp(MotionEvent e) {
                return true;
            }
        });
    }

    @Override public boolean onInterceptTouchEvent(RecyclerView view, MotionEvent e) {
        View childView = view.findChildViewUnder(e.getX(), e.getY());
        if (childView != null && mListener != null && mGestureDetector.onTouchEvent(e)) {
            mListener.onItemClick(childView, view.getChildPosition(childView));
            return true;
        }
        return false;
    }

    @Override public void onTouchEvent(RecyclerView view, MotionEvent motionEvent) { }

    @Override
    public void onRequestDisallowInterceptTouchEvent (boolean disallowIntercept){}
}
