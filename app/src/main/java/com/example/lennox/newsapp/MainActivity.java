package com.example.lennox.newsapp;

import android.app.LoaderManager;
import android.app.LoaderManager.LoaderCallbacks;
import android.app.SearchManager;
import android.content.Context;
import android.content.Intent;
import android.content.Loader;
import android.content.SharedPreferences;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.net.Uri;
import android.os.AsyncTask;
import android.os.Parcel;
import android.os.Parcelable;
import android.preference.PreferenceManager;
import android.support.v4.view.MenuItemCompat;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.SearchView;
import android.support.v7.widget.Toolbar;
import android.text.TextUtils;
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

public class MainActivity extends AppCompatActivity implements LoaderCallbacks<List<News>>, SearchView.OnQueryTextListener {

    private static final String LOG = MainActivity.class.getSimpleName();
    private static final String NEWS_URL = "https://content.guardianapis.com/search?";
    private static NewsAdapter newsAdapter;
    private static final String API_KEY = "1aefe6a9-8256-4ed1-9705-078617ffba7b";
    private static View loading;
    private MyParceable myClass;
    private TextView emptyState;
    private RecyclerView newsRecyclerView;
    String queryText;
    List<News> newsList;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState); //always call the superclass first
        setContentView(R.layout.activity_main);

        Toolbar myToolbar = findViewById(R.id.action_toolbar);
        setSupportActionBar(myToolbar);

        emptyState = findViewById(R.id.empty_view);
        newsRecyclerView = findViewById(R.id.news_list);
        newsRecyclerView.setLayoutManager(new LinearLayoutManager(getApplicationContext()));

        loading = findViewById(R.id.loading);

        newsList = new ArrayList<News>();

        //Create the adapter
        newsAdapter = new NewsAdapter(this, newsList);

/*
        newsRecyclerView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> adapterView, View view, int position, long l) {
                //Get current position of news item
                News currentNews = newsAdapter.getItem(position);
                Uri newsUri = Uri.parse(currentNews.getNewsURL());
                startActivity(new Intent(Intent.ACTION_VIEW, newsUri));
            }
        });*/

        ConnectivityManager conMgr = (ConnectivityManager) getSystemService(Context.CONNECTIVITY_SERVICE);
        NetworkInfo netInfo = conMgr.getActiveNetworkInfo();

        if (netInfo != null && netInfo.isConnected()) {
            //initialize the loader to retrieve data
            LoaderManager loaderManager = getLoaderManager();
            loaderManager.initLoader(1, null, this);
            emptyState.setVisibility(View.GONE);
            newsRecyclerView.setAdapter(newsAdapter);
        } else {
            //If there is no network
            loading.setVisibility(View.GONE);
            emptyState.setText("No internet connection!!!");
            newsRecyclerView.setVisibility(View.GONE);
        }
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.main_menu, menu);

        MenuItem searchItem = menu.findItem(R.id.search);
        SearchView searchView = (SearchView) MenuItemCompat.getActionView(searchItem);
        searchView.setOnQueryTextListener(this);

        return super.onCreateOptionsMenu(menu);
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case R.id.setting:
                startActivity(new Intent(MainActivity.this, Settings.class));
                break;
            case R.id.refresh:
                refresh();
                break;
            default:
                break;
        }
        return super.onOptionsItemSelected(item);
    }

    //reset the adapter and add new data
    private void refresh() {

        newsList.clear();

        emptyState.setVisibility(View.GONE);
        loading.setVisibility(View.VISIBLE);
        ConnectivityManager conMgr = (ConnectivityManager) getSystemService(Context.CONNECTIVITY_SERVICE);
        NetworkInfo netInfo = conMgr.getActiveNetworkInfo();
        if (netInfo != null && netInfo.isConnected()) {
            getLoaderManager().restartLoader(1, null, this);
        } else {
            //If there is no network
            loading.setVisibility(View.GONE);
            emptyState.setVisibility(View.VISIBLE);
        }
        newsRecyclerView.setAdapter(newsAdapter);
    }

    //Saves state of app
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

    @Override
    public boolean onQueryTextSubmit(String query) {
        getLoaderManager().restartLoader(1, null, this);
        supportInvalidateOptionsMenu();
        return true;
    }

    @Override
    public boolean onQueryTextChange(String newText) {
        queryText = !TextUtils.isEmpty(newText) ? newText : null;
        return true;
    }

    public static class MyParceable implements Parcelable {
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
                new Parcelable.Creator<MyParceable>() {
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
        private MyParceable(Parcel in) {
            mData = in.readInt();
        }
    }

    @Override
    public Loader<List<News>> onCreateLoader(int i, Bundle bundle) {

        // Call the preferences to help build the uri
        SharedPreferences sharedPreferences = PreferenceManager.getDefaultSharedPreferences(this);

        String newsSection = sharedPreferences.getString(
                getString(R.string.settings_sections_key),
                getString(R.string.settings_sections_default));

        String newsOrder = sharedPreferences.getString(
                getString(R.string.settings_order_by_key),
                getString(R.string.settings_order_by_default));

        String newsLimit = sharedPreferences.getString(
                getString(R.string.settings_limit_news_key),
                getString(R.string.settings_limit_news_default));

        //Create the query parameters
        Uri baseUri = Uri.parse(NEWS_URL);
        Uri.Builder uriBuilder = baseUri.buildUpon();

        if (!newsSection.equals("All")) {
            uriBuilder.appendQueryParameter("section", newsSection);
        }

        if(queryText != null){
            uriBuilder.appendQueryParameter("q", queryText);
        }

        uriBuilder.appendQueryParameter("page-size", newsLimit);
        uriBuilder.appendQueryParameter("order-by", newsOrder);
        uriBuilder.appendQueryParameter("use-date", "published");
        uriBuilder.appendQueryParameter("show-tags", "contributor");
        uriBuilder.appendQueryParameter("api-key", API_KEY);

        return new NewsLoader(MainActivity.this, uriBuilder.toString());
    }

    @Override
    public void onLoadFinished(Loader<List<News>> loader, List<News> news) {
        if (news != null && !news.isEmpty()) {
            loading.setVisibility(View.GONE);
            //add data to the recycler view
            newsList.addAll(news);
            newsAdapter.notifyDataSetChanged();
            Log.i(LOG, " news is not empty bro");
        } else {
            loading.setVisibility(View.GONE);
            emptyState.setText("No results found!!!");
            Log.i(LOG, "NULL-> news is empty bro");
        }
    }

    @Override
    public void onLoaderReset(Loader<List<News>> loader) {
        newsList.clear();
        newsAdapter.notifyDataSetChanged(); // let your adapter know about the changes and reload view.
    }
}
