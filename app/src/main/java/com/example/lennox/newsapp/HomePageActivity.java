package com.example.lennox.newsapp;

import android.app.LoaderManager.LoaderCallbacks;
import android.content.Context;
import android.content.Loader;
import android.content.SharedPreferences;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.net.Uri;
import android.os.Parcel;
import android.os.Parcelable;
import android.preference.PreferenceManager;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.MotionEvent;
import android.view.View;
import android.view.inputmethod.InputMethodManager;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.Toast;

import java.util.ArrayList;
import java.util.List;

public class HomePageActivity extends AppCompatActivity implements LoaderCallbacks<List<News>> {

    private RecyclerView recyclerNews;
    private ImageView drNavImage;
    private EditText etNewsSearch;
    private List<News> newsList;
    private NewsAdapter newsAdapter;
    private static final String API_KEY = "1aefe6a9-8256-4ed1-9705-078617ffba7b";
    private static final String NEWS_URL = "https://content.guardianapis.com/search?";
    private static final String TAG = HomePageActivity.class.getSimpleName();
    private MyParceable myClass;
    String queryText;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_home_page);

        recyclerNews = findViewById(R.id.rv_news);
        recyclerNews.setLayoutManager(new LinearLayoutManager(getApplicationContext()));

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

        drNavImage = findViewById(R.id.iv_nav_drawer);
        etNewsSearch = findViewById(R.id.et_search_news);

        // A drawable click listener
        etNewsSearch.setOnTouchListener(new View.OnTouchListener() {
            @Override
            public boolean onTouch(View view, MotionEvent event) {
                final int DRAWABLE_RIGHT = 2;

                if (event.getAction() == MotionEvent.ACTION_UP) {
                    if (event.getRawX() >= (etNewsSearch.getRight() - etNewsSearch.getCompoundDrawables()[DRAWABLE_RIGHT].getBounds().width())) {
                        queryText = etNewsSearch.getText().toString().trim();
                        etNewsSearch.setText("");
                        // Check if no view has focus:
                        //View v = HomePageActivity.this.getCurrentFocus();
                        if (view!= null) {
                            InputMethodManager imm = (InputMethodManager)getSystemService(Context.INPUT_METHOD_SERVICE);
                            imm.hideSoftInputFromWindow(view.getWindowToken(), 0);
                            view.clearFocus();
                        }
                        refresh();
                        return true;
                    }
                }
                return false;
            }
        });

        newsList = new ArrayList<News>();

        newsAdapter = new NewsAdapter(this, newsList);

        ConnectivityManager conMgr = (ConnectivityManager) getSystemService(Context.CONNECTIVITY_SERVICE);
        NetworkInfo netInfo = conMgr.getActiveNetworkInfo();

        if (netInfo != null && netInfo.isConnected()) {
            //initialize the loader to retrieve data
            getLoaderManager().initLoader(1, null, this);
            recyclerNews.setAdapter(newsAdapter);
        } else {
            //If there is no network
            //Todo: Display an empty state
            Toast.makeText(this, "No internet connection", Toast.LENGTH_SHORT).show();
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

        //http://content.guardianapis.com/search?page=20&api-key=1aefe6a9-8256-4ed1-9705-078617ffba7b&show-fields=thumbnail
        //https://content.guardianapis.com/search?q=12%20years%20a%20slave&format=json&tag=film/film,tone/reviews&from-date=2010-01-01&show-tags=contributor&show-fields=starRating,headline,thumbnail,short-url&order-by=relevance&api-key=test
        if (!newsSection.equals("All")) {
            uriBuilder.appendQueryParameter("section", newsSection);
        }

        if (queryText != null) {
            uriBuilder.appendQueryParameter("q", queryText);
        }

        uriBuilder.appendQueryParameter("page-size", "10");
        uriBuilder.appendQueryParameter("order-by", newsOrder);
        uriBuilder.appendQueryParameter("use-date", "published");
        uriBuilder.appendQueryParameter("show-tags", "contributor");
        uriBuilder.appendQueryParameter("show-fields", "headline,bodyText,thumbnail,starRating");
        uriBuilder.appendQueryParameter("api-key", API_KEY);
        Log.i(TAG, uriBuilder.toString());
        return new NewsLoader(HomePageActivity.this, uriBuilder.toString());
    }

    @Override
    public void onLoadFinished(Loader<List<News>> loader, List<News> news) {
        if (news != null && !news.isEmpty()) {
            //add data to the recycler view
            newsList.addAll(news);
            newsAdapter.notifyDataSetChanged();
        } else {
            //Todo: display and empty view
            Toast.makeText(HomePageActivity.this, "No results found", Toast.LENGTH_SHORT).show();
        }
    }

    @Override
    public void onLoaderReset(Loader<List<News>> loader) {
        newsList.clear();
        newsAdapter.notifyDataSetChanged(); // let your adapter know about the changes and reload view.
    }

    //reset the adapter and add new data
    private void refresh() {
        Toast.makeText(getApplicationContext(), queryText, Toast.LENGTH_SHORT).show();
        newsList.clear();

        //emptyState.setVisibility(View.GONE);
        //loading.setVisibility(View.VISIBLE);
        ConnectivityManager conMgr = (ConnectivityManager) getSystemService(Context.CONNECTIVITY_SERVICE);
        NetworkInfo netInfo = conMgr.getActiveNetworkInfo();
        if (netInfo != null && netInfo.isConnected()) {
            getLoaderManager().restartLoader(1, null, this);
        } else {
            //If there is no network
            // loading.setVisibility(View.GONE);
            // emptyState.setVisibility(View.VISIBLE);
        }
          recyclerNews.setAdapter(newsAdapter);
    }//end method refresh

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

}
