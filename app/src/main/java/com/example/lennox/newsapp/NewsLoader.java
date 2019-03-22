package com.example.lennox.newsapp;

import android.content.AsyncTaskLoader;
import android.content.Context;
import android.net.Uri;
import android.util.Log;

import java.util.List;

public class NewsLoader extends AsyncTaskLoader {

    private static String LOG = NewsLoader.class.getSimpleName();
    private static String mUrl;

    public NewsLoader(Context context, String url) {
        super(context);
        mUrl = url;
    }

    @Override
    protected void onStartLoading() {
        super.onStartLoading();
        //called by initLoader()
        forceLoad();
    }

    @Override
    public List<News> loadInBackground() {
        if(mUrl == null){
            return null;
        }

        //This returns a list
        return QueryUtils.fetchEarthquakeData(mUrl);
    }
}
