package com.example.lennox.newsapp;

import android.support.v4.content.AsyncTaskLoader;
import android.content.Context;

import java.util.List;

public class NewsLoader extends AsyncTaskLoader {

    private static String LOG = NewsLoader.class.getSimpleName();
    private static String mUrl;
    private static Context mContext;

    public NewsLoader(Context context, String url) {
        super(context);
        mContext = context;
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
        if (mUrl == null) {
            return null;
        }

        //This returns a list
        return QueryUtils.fetchEarthquakeData(mUrl);
    }
}
