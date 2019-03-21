package com.example.lennox.newsapp;

import android.content.Context;
import android.content.Intent;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.net.Uri;
import android.os.AsyncTask;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import java.util.ArrayList;
import java.util.List;

public class MainActivity extends AppCompatActivity {

    private static final String LOG = MainActivity.class.getSimpleName();
    private static final String NEWS_URL = "https://content.guardianapis.com/search?";
    private static NewsAdapter newsAdapter;
    private static String orderBy = "newest";
    private static String section = "politics";
    private static String useDate = "published";
    private static String showTags = "contributor";
    private static final String API_KEY = "1aefe6a9-8256-4ed1-9705-078617ffba7b";
    private static View loading;

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.main_menu, menu);
        return super.onCreateOptionsMenu(menu);
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch(item.getItemId()){
            case R.id.setting:
                //to do
                break;
            case R.id.refresh:
                //to do
                break;
            default:
                break;
        }
        return super.onOptionsItemSelected(item);
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        TextView emptyState = findViewById(R.id.empty_view);
        ListView newsView = findViewById(R.id.news_list);
        loading = findViewById(R.id.loading);
        newsView.setEmptyView(emptyState);

        //Create the adapter
        newsAdapter = new NewsAdapter(this, new ArrayList<News>());

        newsView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> adapterView, View view, int position, long l) {
                //Get current position of news item
                News currentNews = newsAdapter.getItem(position);
                Uri newsUri = Uri.parse(currentNews.getNewsURL());
                startActivity(new Intent(Intent.ACTION_VIEW, newsUri));
            }
        });

        ConnectivityManager conMgr = (ConnectivityManager) getSystemService(Context.CONNECTIVITY_SERVICE);
        NetworkInfo netInfo = conMgr.getActiveNetworkInfo();

        if(netInfo != null  && netInfo.isConnected()){
            //AsyncTask to retrieve data from a background thread
            new NewsAsyncTask().execute(NEWS_URL);
        }else{
            //If there is no network
            loading.setVisibility(View.GONE);
            emptyState.setText("No internet connection!!!");
        }
        newsView.setAdapter(newsAdapter);
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

            uriBuilder.appendQueryParameter("use-date", useDate);
            uriBuilder.appendQueryParameter("order-by", orderBy);
            uriBuilder.appendQueryParameter("section", section);
            uriBuilder.appendQueryParameter("show-tags", showTags);
            uriBuilder.appendQueryParameter("api-key", API_KEY);
            Log.d(LOG, "The query has been built");
            return QueryUtils.fetchEarthquakeData(uriBuilder.toString());
        }

        @Override
        protected void onPostExecute(List<News> news) {
            if (news != null && !news.isEmpty()) {
                loading.setVisibility(View.GONE);
                newsAdapter.addAll(news);
            }
        }
    }

}
