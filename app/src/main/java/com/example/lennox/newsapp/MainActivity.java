package com.example.lennox.newsapp;

import android.app.LoaderManager;
import android.app.LoaderManager.LoaderCallbacks;
import android.content.Context;
import android.content.Intent;
import android.content.Loader;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.net.Uri;
import android.os.AsyncTask;
import android.os.Parcel;
import android.os.Parcelable;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import java.util.ArrayList;
import java.util.List;

public class MainActivity extends AppCompatActivity implements LoaderCallbacks<List<News>> {

    private static final String LOG = MainActivity.class.getSimpleName();
    private static final String NEWS_URL = "https://content.guardianapis.com/search?";
    private static NewsAdapter newsAdapter;
    private static String orderBy = "newest";
    private static String section = "";
    private static String useDate = "published";
    private static String showTags = "contributor";
    private static final String API_KEY = "1aefe6a9-8256-4ed1-9705-078617ffba7b";
    private static View loading;
    MyParceable myClass;

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

    //Used to prevent calling onCreate() after screen rotation
    @Override
    protected void onSaveInstanceState(Bundle outState) {
        super.onSaveInstanceState(outState);
        outState.putParcelable("obj", myClass);
    }

    //Used to prevent calling onCreate() after screen rotation
    @Override
    protected void onRestoreInstanceState(Bundle savedInstanceState) {
        super.onRestoreInstanceState(savedInstanceState);
        myClass = savedInstanceState.getParcelable("obj");
    }

    public static class MyParceable implements Parcelable{
        private int mData;

        @Override
        public int describeContents() {
            return 0;
        }

        // save object in parcel
        @Override
        public void writeToParcel(Parcel out, int flags) {
            out.writeInt(mData);
        }

        public static final Parcelable.Creator<MyParceable> CREATOR =
                new Parcelable.Creator<MyParceable>(){
                    @Override
                    public MyParceable createFromParcel(Parcel in) {
                        return new MyParceable(in);
                    }

                    @Override
                    public MyParceable[] newArray(int size) {
                        return new MyParceable[size];
                    }
                };
        // recreate object from parcel
        private MyParceable(Parcel in){
            mData = in.readInt();
        }
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
            //initialize the loader to retrieve data
            LoaderManager loaderManager = getLoaderManager();
            loaderManager.initLoader(1, null, this);
        }else{
            //If there is no network
            loading.setVisibility(View.GONE);
            emptyState.setText("No internet connection!!!");
        }
        newsView.setAdapter(newsAdapter);
    }

    @Override
    public Loader<List<News>> onCreateLoader(int i, Bundle bundle) {
        //Create the query parameters
        Uri baseUri = Uri.parse(NEWS_URL);
        Uri.Builder uriBuilder = baseUri.buildUpon();

        uriBuilder.appendQueryParameter("use-date", useDate);
        uriBuilder.appendQueryParameter("order-by", orderBy);
        // uriBuilder.appendQueryParameter("section", section);
        uriBuilder.appendQueryParameter("show-tags", showTags);
        uriBuilder.appendQueryParameter("api-key", API_KEY);
        Log.d(LOG, "The query has been built");

        return new NewsLoader(MainActivity.this, uriBuilder.toString());
    }

    @Override
    public void onLoadFinished(Loader<List<News>> loader, List<News> news) {
        if (news != null && !news.isEmpty()) {
            loading.setVisibility(View.GONE);
            newsAdapter.addAll(news);
        }
    }

    @Override
    public void onLoaderReset(Loader<List<News>> loader) {
        newsAdapter.clear();
    }
}
