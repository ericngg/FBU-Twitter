package com.codepath.apps.restclienttemplate.Activity;

import androidx.annotation.Nullable;
import androidx.appcompat.app.ActionBar;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import androidx.swiperefreshlayout.widget.SwipeRefreshLayout;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;

import com.codepath.apps.restclienttemplate.EndlessRecyclerViewScrollListener;
import com.codepath.apps.restclienttemplate.R;
import com.codepath.apps.restclienttemplate.adapters.TweetsAdapter;
import com.codepath.apps.restclienttemplate.TwitterApp;
import com.codepath.apps.restclienttemplate.TwitterClient;
import com.codepath.apps.restclienttemplate.models.ComposeActivity;
import com.codepath.apps.restclienttemplate.models.Tweet;
import com.codepath.asynchttpclient.callback.JsonHttpResponseHandler;

import org.json.JSONArray;
import org.json.JSONException;
import org.parceler.Parcels;

import java.util.ArrayList;
import java.util.List;

import okhttp3.Headers;

public class TimelineActivity extends AppCompatActivity implements TweetsAdapter.ViewHolder.onTweetListener {

    public static final String TAG = "TimelineActivity";
    public static final int REQUEST_CODE = 20;

    Long lowestMaxId;

    TwitterClient client;
    RecyclerView rvTweets;
    List<Tweet> tweets;
    TweetsAdapter adapter;
    SwipeRefreshLayout srlContainer;

    MenuItem miActionProgressItem;

    private EndlessRecyclerViewScrollListener scrollListener;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_timeline);

        getSupportActionBar().setDisplayOptions(ActionBar.DISPLAY_SHOW_CUSTOM);
        getSupportActionBar().setCustomView(R.layout.action_bar_icon);

        client = TwitterApp.getRestClient(this);
        rvTweets = findViewById(R.id.rvTweets);
        srlContainer = findViewById(R.id.srlContainer);

        tweets = new ArrayList<>();
        adapter = new TweetsAdapter(this, tweets, this);
        LinearLayoutManager linearLayoutManager = new LinearLayoutManager(this);
        rvTweets.setLayoutManager(linearLayoutManager);
        rvTweets.setAdapter(adapter);
        populateHomeTimeline();

        srlContainer.setOnRefreshListener(new SwipeRefreshLayout.OnRefreshListener() {
            @Override
            public void onRefresh() {
                showProgressBar();
                fetchTimelineAsync(0);
            }
        });

        // Configure the refreshing colors
        srlContainer.setColorSchemeResources(android.R.color.holo_blue_bright,
                android.R.color.holo_green_light,
                android.R.color.holo_orange_light,
                android.R.color.holo_red_light);

        scrollListener = new EndlessRecyclerViewScrollListener(linearLayoutManager) {
            @Override
            public void onLoadMore(int page, int totalItemsCount, RecyclerView view) {
                loadNextDataFromApi(page);
            }
        };

        rvTweets.addOnScrollListener(scrollListener);
    }

    // Append the next page of data into the adapter
    // This method probably sends out a network request and appends new data items to your adapter.
    public void loadNextDataFromApi(int offset) {
        // Send an API request to retrieve appropriate paginated data
        //  --> Send the request including an offset value (i.e `page`) as a query parameter.
        //  --> Deserialize and construct new model objects from the API response
        //  --> Append the new data objects to the existing set of items inside the array of items
        //  --> Notify the adapter of the new items made with `notifyItemRangeInserted()`

        showProgressBar();

        client.getHomeTimeline2(lowestMaxId, new JsonHttpResponseHandler() {
            @Override
            public void onSuccess(int statusCode, Headers headers, JSON json) {
                Log.i(TAG, "Sucess! Another 25");
                JSONArray jsonArray = json.jsonArray;

                try {
                    tweets.addAll(Tweet.fromJsonArray(jsonArray));
                    adapter.notifyDataSetChanged();
                } catch (JSONException e) {
                    e.printStackTrace();
                }
            }

            @Override
            public void onFailure(int statusCode, Headers headers, String response, Throwable throwable) {
                Log.d(TAG, "Failure! No 25!", throwable);
            }
        });

        hideProgressBar();
    }

    public void fetchTimelineAsync(int page) {
        client.getHomeTimeline(new JsonHttpResponseHandler() {
            @Override
            public void onSuccess(int statusCode, Headers headers, JSON json) {
                adapter.clear();
                JSONArray jsonArray = json.jsonArray;

                try {
                    tweets.addAll(Tweet.fromJsonArray(jsonArray));
                    adapter.notifyDataSetChanged();
                    rvTweets.scrollToPosition(0);
                } catch (JSONException e) {
                    e.printStackTrace();
                }

                hideProgressBar();
                srlContainer.setRefreshing(false);
            }

            @Override
            public void onFailure(int statusCode, Headers headers, String response, Throwable throwable) {
                Log.d(TAG, "Fetch timeline Error: " + throwable.toString());
                srlContainer.setRefreshing(false);
                hideProgressBar();
            }
        });
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        miActionProgressItem = menu.findItem(R.id.miActionProgress);
        getMenuInflater().inflate(R.menu.menu_main, menu);
        return true;
    }

    @Override
    public boolean onPrepareOptionsMenu(Menu menu) {
        miActionProgressItem = menu.findItem(R.id.miActionProgress);

        return super.onPrepareOptionsMenu(menu);
    }

    public void showProgressBar() {
        // Show progress item
        miActionProgressItem.setVisible(true);
    }

    public void hideProgressBar() {
        // Hide progress item
        miActionProgressItem.setVisible(false);
    }


    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        if (item.getItemId() == R.id.compose) {
            Intent intent = new Intent(this, ComposeActivity.class);
            intent.putExtra("code", 20);
            startActivityForResult(intent, REQUEST_CODE);
            return true;
        }
        return super.onOptionsItemSelected(item);
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        if (requestCode == REQUEST_CODE && resultCode == RESULT_OK) {
            // Get data from the intent (tweet)
            Tweet tweet = Parcels.unwrap(data.getParcelableExtra("tweet"));
            // Update the RV with the tweet
            // Modify data source of tweets
            tweets.add(0, tweet);
            // Update the adapter
            adapter.notifyItemInserted(0);
            rvTweets.smoothScrollToPosition(0);
        }
        super.onActivityResult(requestCode, resultCode, data);
    }

    private void populateHomeTimeline() {
        client.getHomeTimeline(new JsonHttpResponseHandler() {
            @Override
            public void onSuccess(int statusCode, Headers headers, JSON json) {
                JSONArray jsonArray = json.jsonArray;
                try {
                    tweets.addAll(Tweet.fromJsonArray(jsonArray));

                    lowestMaxId = tweets.get(tweets.size() - 1).uid;
                    adapter.notifyDataSetChanged();
                } catch (JSONException e) {
                    e.printStackTrace();
                }
            }

            @Override
            public void onFailure(int statusCode, Headers headers, String response, Throwable throwable) {
                Log.e(TAG, "onFailure! Status code: " + statusCode, throwable);
            }
        });
    }

    @Override
    public void onTweetClick(int position) {
        Tweet tweet = tweets.get(position);
        Intent intent = new Intent(TimelineActivity.this, TweetDetailActivity.class);
        intent.putExtra("tweet", Parcels.wrap(tweet));
        startActivity(intent);
    }
}
