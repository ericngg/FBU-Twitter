package com.codepath.apps.restclienttemplate.Activity;

import androidx.appcompat.app.ActionBar;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;

import com.codepath.apps.restclienttemplate.R;
import com.codepath.apps.restclienttemplate.TwitterApp;
import com.codepath.apps.restclienttemplate.TwitterClient;
import com.codepath.apps.restclienttemplate.adapters.FollowersAdapter;
import com.codepath.apps.restclienttemplate.adapters.FollowsAdapter;
import com.codepath.apps.restclienttemplate.databinding.ActivityFfBinding;
import com.codepath.asynchttpclient.callback.JsonHttpResponseHandler;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.List;

import okhttp3.Headers;

public class ffActivity extends AppCompatActivity {

    public static final String TAG = "ffActivity";

    TwitterClient client;
    private long id;

    List<String> followers;
    List<String> follows;
    FollowersAdapter adapter1;
    FollowsAdapter adapter2;

    private ActivityFfBinding binding;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = binding.inflate(getLayoutInflater());
        View view = binding.getRoot();
        setContentView(view);


        getSupportActionBar().setDisplayOptions(ActionBar.DISPLAY_SHOW_CUSTOM);
        getSupportActionBar().setCustomView(R.layout.action_bar_icon);

        Intent intent = getIntent();
        followers = new ArrayList<>();
        follows = new ArrayList<>();
        id = intent.getLongExtra("id", 1);
        client = TwitterApp.getRestClient(this);

        follower();
        follows();
    }

    public void follower() {
        client.followers(id, new JsonHttpResponseHandler() {
            @Override
            public void onSuccess(int statusCode, Headers headers, JSON json) {
                Log.i(TAG, "Success: " + json.toString());
                JSONObject jsonObject = json.jsonObject;
                try {
                    JSONArray jsonArray = jsonObject.getJSONArray("users");
                    for (int i = 0; i < jsonArray.length(); i++) {
                        followers.add(jsonArray.getJSONObject(i).getString("name"));
                    }

                    adapter1 = new FollowersAdapter(followers);
                    binding.rvFollowers.setAdapter(adapter1);
                    binding.rvFollowers.setLayoutManager(new LinearLayoutManager(ffActivity.this));
                } catch (JSONException e) {
                    e.printStackTrace();
                }
            }

            @Override
            public void onFailure(int statusCode, Headers headers, String response, Throwable throwable) {
                Log.e(TAG, "Failed: " + statusCode + " Throwable:", throwable);
            }
        });
    }

    public void follows() {
        client.follows(id, new JsonHttpResponseHandler() {
            @Override
            public void onSuccess(int statusCode, Headers headers, JSON json) {
                Log.i(TAG, "Success: " + json.toString());
                JSONObject jsonObject = json.jsonObject;
                try {
                    JSONArray jsonArray = jsonObject.getJSONArray("users");
                    for (int i = 0; i < jsonArray.length(); i++) {
                        follows.add(jsonArray.getJSONObject(i).getString("name"));
                    }

                    Log.i(TAG, "onSuccess: " + follows.toString());

                    adapter2 = new FollowsAdapter(follows);
                    binding.rvFollows.setAdapter(adapter2);
                    binding.rvFollows.setLayoutManager(new LinearLayoutManager(ffActivity.this));
                } catch (JSONException e) {
                    e.printStackTrace();
                }
            }

            @Override
            public void onFailure(int statusCode, Headers headers, String response, Throwable throwable) {
                Log.e(TAG, "Failed: " + statusCode + " Throwable:", throwable);
            }
        });
    }
}