package com.codepath.apps.restclienttemplate.Activity;

import androidx.appcompat.app.ActionBar;
import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.FragmentManager;

import android.content.Intent;
import android.os.Bundle;
import android.text.format.DateUtils;
import android.util.Log;
import android.view.View;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.bumptech.glide.Glide;
import com.codepath.apps.restclienttemplate.ComposeDialogFragment;
import com.codepath.apps.restclienttemplate.R;
import com.codepath.apps.restclienttemplate.TwitterApp;
import com.codepath.apps.restclienttemplate.TwitterClient;
import com.codepath.apps.restclienttemplate.databinding.ActivityTweetDetailBinding;
import com.codepath.apps.restclienttemplate.models.Tweet;
import com.codepath.asynchttpclient.callback.JsonHttpResponseHandler;

import org.parceler.Parcels;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Locale;

import okhttp3.Headers;

public class TweetDetailActivity extends AppCompatActivity {

    public static final String TAG = "TweetDetailActivity";

    TwitterClient client;

    ImageView ivDetailMediaImage;
    ImageView ivDetailProfileImage;
    TextView tvDetailBody;
    TextView tvDetailScreenName;
    TextView tvDetailCreatedAt;

    Tweet tweet;

    ImageButton ibFavorite;
    ImageButton ibRetweet;
    ImageButton ibReply;
    ImageButton ibPerson;

    private ActivityTweetDetailBinding binding;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = ActivityTweetDetailBinding.inflate(getLayoutInflater());
        View view = binding.getRoot();
        setContentView(view);

        getSupportActionBar().setDisplayOptions(ActionBar.DISPLAY_SHOW_CUSTOM);
        getSupportActionBar().setCustomView(R.layout.action_bar_icon);

        client = TwitterApp.getRestClient(this);

        ivDetailMediaImage = findViewById(R.id.ivDetailMediaImage);
        ivDetailProfileImage = findViewById(R.id.ivDetailProfileImage);
        tvDetailBody = findViewById(R.id.tvDetailBody);
        tvDetailScreenName = findViewById(R.id.tvDetailScreenName);
        tvDetailCreatedAt = findViewById(R.id.tvDetailCreatedAt);
        ibFavorite = findViewById(R.id.ibFavorite);
        ibReply = findViewById(R.id.ibReply);
        ibRetweet = findViewById(R.id.ibRetweet);
        ibPerson = findViewById(R.id.ibPerson);

        Intent intent = getIntent();
        tweet = Parcels.unwrap(intent.getParcelableExtra("tweet"));

        if (!tweet.mediaUrl.equals("")) {
            Glide.with(this).load(tweet.mediaUrl).into(ivDetailMediaImage);
        } else {
            ivDetailMediaImage.setVisibility(View.GONE);
        }

        tvDetailBody.setText(tweet.body);
        tvDetailScreenName.setText(tweet.user.screenName);
        Glide.with(this).load(tweet.user.profileImageUrl).into(ivDetailProfileImage);

        if (tweet.liked) {
            ibFavorite.setImageResource(R.mipmap.filled_heart_foreground);
            ibFavorite.setTag(R.mipmap.filled_heart_foreground);
        } else {
            ibFavorite.setImageResource(R.mipmap.empty_heart_foreground);
            ibFavorite.setTag(R.mipmap.empty_heart_foreground);
        }

        tvDetailCreatedAt.setText(getRelativeTimeAgo(tweet.createdAt));

        ibFavorite.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if ((int) ibFavorite.getTag() == R.mipmap.empty_heart_foreground) {
                    client.likeTweet(tweet.uid, new JsonHttpResponseHandler() {
                        @Override
                        public void onSuccess(int statusCode, Headers headers, JSON json) {
                            Log.i(TAG, "Liked Success!");
                            ibFavorite.setImageResource(R.mipmap.filled_heart_foreground);
                            ibFavorite.setTag(R.mipmap.filled_heart_foreground);
                        }

                        @Override
                        public void onFailure(int statusCode, Headers headers, String response, Throwable throwable) {
                            Log.d(TAG, "Liked Failed!", throwable);
                        }
                    });
                } else {
                    client.unlikeTweet(tweet.uid, new JsonHttpResponseHandler() {
                        @Override
                        public void onSuccess(int statusCode, Headers headers, JSON json) {
                            Log.i(TAG, "Unliked Success!");
                            ibFavorite.setImageResource(R.mipmap.empty_heart_foreground);
                            ibFavorite.setTag(R.mipmap.empty_heart_foreground);
                        }

                        @Override
                        public void onFailure(int statusCode, Headers headers, String response, Throwable throwable) {
                            Log.d(TAG, "Unliked Failed!", throwable);
                        }
                    });
                }
            }
        });

        ibReply.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                /*
                Intent intentReply = new Intent(TweetDetailActivity.this, ComposeActivity.class);
                intentReply.putExtra("name", tweet.user.screenName);
                intentReply.putExtra("code", 25);
                startActivity(intentReply);
                finish();
                 */

                showComposeDialog();
            }
        });

        ibRetweet.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Toast.makeText(TweetDetailActivity.this, "Retweet Success!", Toast.LENGTH_SHORT).show();
            }
        });

        ibPerson.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intentFF = new Intent(TweetDetailActivity.this, ffActivity.class);
                intentFF.putExtra("id", tweet.user.id);
                startActivity(intentFF);
            }
        });
    }

    // getRelativeTimeAgo("Mon Apr 01 21:16:23 +0000 2014");
    public String getRelativeTimeAgo(String rawJsonDate) {
        String twitterFormat = "EEE MMM dd HH:mm:ss ZZZZZ yyyy";
        SimpleDateFormat sf = new SimpleDateFormat(twitterFormat, Locale.ENGLISH);
        sf.setLenient(true);

        String relativeDate = "";
        try {
            long dateMillis = sf.parse(rawJsonDate).getTime();
            relativeDate = DateUtils.getRelativeTimeSpanString(dateMillis,
                    System.currentTimeMillis(), DateUtils.SECOND_IN_MILLIS).toString();
        } catch (ParseException e) {
            e.printStackTrace();
        }

        return relativeDate;
    }

    public void showComposeDialog() {
        FragmentManager fm = getSupportFragmentManager();
        ComposeDialogFragment composeDialogFragment = ComposeDialogFragment.newInstance("Retweet Compose", tweet.user.screenName);
        composeDialogFragment.show(fm, "fragment_compose");
    }
}