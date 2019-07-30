package com.codepath.apps.restclienttemplate;

import android.os.Bundle;
import android.support.v4.widget.SwipeRefreshLayout;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;

import com.codepath.apps.restclienttemplate.models.Tweet;
import com.loopj.android.http.JsonHttpResponseHandler;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.List;

import cz.msebera.android.httpclient.Header;

public class TimelineActivity extends AppCompatActivity {
    TwitterClient client;
    RecyclerView rvTweet;
    TweetsAdapter adapter;
    List<Tweet> tweets;

    LinearLayoutManager linearLayoutManager;
    // Store a member variable for the listener
    // Store a member variable for the listener
    private EndlessRecyclerViewScrollListener scrollListener;


    private SwipeRefreshLayout swipeContainer;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_timeline);

        client = TwitterApp.getRestClient(this);
        // Rcycleview
        swipeContainer = (SwipeRefreshLayout) findViewById(R.id.swipeContainer);
        rvTweet= findViewById(R.id.rvTweets);
        tweets = new ArrayList<>();
        adapter = new TweetsAdapter(this,tweets);
        linearLayoutManager = new LinearLayoutManager(this);
        rvTweet.setLayoutManager(linearLayoutManager);
        rvTweet.setAdapter(adapter);




        //
        populateHomeTimeline();

        swipeContainer.setOnRefreshListener(new SwipeRefreshLayout.OnRefreshListener() {
            @Override
            public void onRefresh() {
                Log.d("TwitterClient","this has been refreshed");
                // Your code to refresh the list here.
                // Make sure you call swipeContainer.setRefreshing(false)
                // once the network request has completed successfully.
                //fetchTimelineAsync(0);
                populateHomeTimeline();
            }
        });
        // Configure the refreshing colors
        swipeContainer.setColorSchemeResources(android.R.color.holo_blue_bright,
                android.R.color.holo_green_light,
                android.R.color.holo_orange_light,
                android.R.color.holo_red_light);

        // Retain an instance so that you can call `resetState()` for fresh searches
        scrollListener = new EndlessRecyclerViewScrollListener(linearLayoutManager) {
            @Override
            public void onLoadMore(int page, int totalItemsCount, RecyclerView view) {
                // Triggered only when new data needs to be appended to the list
                // Add whatever code is needed to append new items to the bottom of the list
                loadMoreData(page);
            }
        };
        // Adds the scroll listener to RecyclerView
        rvTweet.addOnScrollListener(scrollListener);


    }

    public void loadMoreData(final int page) {
       client.getHomeTimelineLoadMore(page,new JsonHttpResponseHandler(){
          // Log.d("")
           @Override
           public void onSuccess(int statusCode, Header[] headers, JSONArray response) {
               Log.d("TwitterClient",response.toString());
              // Log.e("TwitterClient Load More",response.toString()+" ==> "+String.valueOf(page));
               List<Tweet> tweetToAdd = new ArrayList<>();
               for(int i=0; i<response.length();i++){
                   try {
                       JSONObject jsonObject = response.getJSONObject(i);
                       Tweet tweet= Tweet.fromJson(jsonObject);
                       tweetToAdd.add(tweet);
                       // tweets.add(tweet);
                       //adapter.notifyItemInserted(tweets.size()-1);
                   } catch (JSONException e) {
                       e.printStackTrace();
                       Log.e("TwitterClient Load More",e.getMessage());
                       //Toast.makeText(this,e.getMessage(),Toast.)
                   }
                   adapter.clear();
                   adapter.addTweets(tweetToAdd);
                   adapter.notifyDataSetChanged();
                   swipeContainer.setRefreshing(false);
               }
           }

           @Override
           public void onFailure(int statusCode, Header[] headers, Throwable throwable, JSONObject errorResponse) {
               // super.onFailure(statusCode, headers, throwable, errorResponse);
               Log.e("TwitterClient Load More",errorResponse.toString());
           }

           @Override
           public void onFailure(int statusCode, Header[] headers, String responseString, Throwable throwable) {
               //super.onFailure(statusCode, headers, responseString, throwable);
               Log.e("TwitterClient Load More",responseString);
           }
       });

        // 1. Send an API request to retrieve appropriate paginated data
        // 2. Deserialize and construct new model objects from the API response
        // 3. Append the new data objects to the existing set of items inside the array of items
        // 4. Notify the adapter of the new items made with `notifyItemRangeInserted()`
    }

    private void populateHomeTimeline() {
        client.getHomeTimeline(new JsonHttpResponseHandler(){
            @Override
            public void onSuccess(int statusCode, Header[] headers, JSONArray response) {
                Log.d("TwitterClient",response.toString());
                List<Tweet> tweetToAdd = new ArrayList<>();
                for(int i=0; i<response.length();i++){
                    try {
                        JSONObject jsonObject = response.getJSONObject(i);
                        Tweet tweet= Tweet.fromJson(jsonObject);
                        Log.d("Tweet Value ==>", String.valueOf(tweet.uid));
                        tweetToAdd.add(tweet);
                       // tweets.add(tweet);
                        //adapter.notifyItemInserted(tweets.size()-1);
                    } catch (JSONException e) {
                        e.printStackTrace();
                        Log.e("TwitterClient",e.getMessage());
                        //Toast.makeText(this,e.getMessage(),Toast.)
                    }
                    adapter.clear();
                    adapter.addTweets(tweetToAdd);
                    swipeContainer.setRefreshing(false);
                }
            }

            @Override
            public void onFailure(int statusCode, Header[] headers, Throwable throwable, JSONObject errorResponse) {
               // super.onFailure(statusCode, headers, throwable, errorResponse);
                Log.e("TwitterClient",errorResponse.toString());
            }

            @Override
            public void onFailure(int statusCode, Header[] headers, String responseString, Throwable throwable) {
                //super.onFailure(statusCode, headers, responseString, throwable);
                Log.e("TwitterClient",responseString);
            }
        });
    }
}
