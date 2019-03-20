package com.example.lennox.newsapp;

import android.content.Intent;
import android.net.Uri;
import android.os.AsyncTask;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import java.util.ArrayList;
import java.util.List;

public class MainActivity extends AppCompatActivity {

    private static final String NEWS_URL = "https://content.guardianapis.com/search?";
    private static NewsAdapter newsAdapter;
    private static String orderBy = "newest";
    private static String section = "sports";
    private static String useDate = "published";
    private static String showElements = "image";
    private static String author = "author";
    private static final String API_KEY = "test";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        TextView emptyState = findViewById(R.id.empty_view);
        ListView newsView = findViewById(R.id.news_list);

        //Create the adapter
        newsAdapter = new NewsAdapter(this, new ArrayList<News>());

        //AsyncTask to retrieve data from a background thread
        new NewsAsyncTask().execute(NEWS_URL);

        newsView.setAdapter(newsAdapter);
        newsView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                //Get current position of news item
                //News currentNews = newsAdapter.getItem();
                //Uri newsUri = Uri.parse(currentNews.getUrl());
                //startActivity(new Intent(Intent.ACTION_VIEW, newsUri));
            }
        });

        //If there is no network
        newsView.setEmptyView(emptyState);
    }

    private static class NewsAsyncTask extends AsyncTask<String,Void, List<News>>{

        @Override
        protected List<News> doInBackground(String... urls) {
            if(urls == null){
                return null;
            }

            //Create the query parameters
            Uri baseUri = Uri.parse(NEWS_URL);
            Uri.Builder uriBuilder = baseUri.buildUpon();

            uriBuilder.appendQueryParameter("show-elements", showElements);
            uriBuilder.appendQueryParameter("use-date", useDate);
            uriBuilder.appendQueryParameter("section", section);
            uriBuilder.appendQueryParameter("order-by", orderBy);
            uriBuilder.appendQueryParameter("author", author);
            uriBuilder.appendQueryParameter("api-key", API_KEY);
            return QueryUtils.fetchEarthquakeData(uriBuilder.toString());
        }

        @Override
        protected void onPostExecute(List<News> news) {
            if (news != null && !news.isEmpty()) {
                newsAdapter.addAll(news);
            }
        }
    }

}
