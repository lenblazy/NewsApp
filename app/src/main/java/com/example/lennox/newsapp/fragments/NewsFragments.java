package com.example.lennox.newsapp.fragments;

import android.content.Context;
import android.content.SharedPreferences;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.net.Uri;
import android.os.Bundle;
import android.preference.PreferenceManager;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v4.app.LoaderManager;
import android.support.v4.content.Loader;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.Toast;

import com.example.lennox.newsapp.HomePageActivity;
import com.example.lennox.newsapp.News;
import com.example.lennox.newsapp.adapters.NewsAdapter;
import com.example.lennox.newsapp.NewsLoader;
import com.example.lennox.newsapp.R;

import java.util.ArrayList;
import java.util.List;

import static com.example.lennox.newsapp.HomePageActivity.API_KEY;

public class NewsFragments extends Fragment implements LoaderManager.LoaderCallbacks<List<News>>, View.OnClickListener {

    private RecyclerView recyclerNews;
    private List<News> sectionNewsList;
    private NewsAdapter newsAdapter;
    private static String TAG = NewsFragments.class.getSimpleName();
    private static String section;
    private RelativeLayout btnContainer;
    private Button btnNextPage, btnPrevPage;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        section = getArguments().getString("section");
        return inflater.inflate(R.layout.fragment_news, container, false);
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        sectionNewsList = new ArrayList<News>();

        newsAdapter = new NewsAdapter(getActivity(), sectionNewsList);

        recyclerNews = view.findViewById(R.id.rv_news);
        recyclerNews.setLayoutManager(new LinearLayoutManager(getActivity()));

        btnContainer = view.findViewById(R.id.btn_container);
        view.findViewById(R.id.btn_next).setOnClickListener(this);
        view.findViewById(R.id.btn_previous).setOnClickListener(this);

        recyclerNews.addOnScrollListener(new RecyclerView.OnScrollListener() {
            @Override
            public void onScrollStateChanged(RecyclerView recyclerView, int newState) {
                super.onScrollStateChanged(recyclerView, newState);

                if (!recyclerView.canScrollVertically(1)) {
                    btnContainer.setVisibility(View.VISIBLE);
                } else if (recyclerView.canScrollVertically(1)) {
                    btnContainer.setVisibility(View.GONE);
                }else{
                    btnContainer.setVisibility(View.VISIBLE);
                }
            }
        });

        ConnectivityManager conMgr = (ConnectivityManager) getActivity().getSystemService(Context.CONNECTIVITY_SERVICE);
        NetworkInfo netInfo = conMgr.getActiveNetworkInfo();

        if (netInfo != null && netInfo.isConnected()) {
            //initialize the loader to retrieve data
            getLoaderManager().initLoader(1, null, this);
            recyclerNews.setAdapter(newsAdapter);
        } else {
            //If there is no network
            //Todo: Display an empty state
            Toast.makeText(getActivity(), "No internet connection", Toast.LENGTH_SHORT).show();
        }
    }//end onViewCreated

    @Override
    public Loader<List<News>> onCreateLoader(int i, Bundle bundle) {
        // Call the preferences to help build the uri
        SharedPreferences sharedPreferences = PreferenceManager.getDefaultSharedPreferences(getActivity());

        String newsOrder = sharedPreferences.getString(
                getString(R.string.settings_order_by_key),
                getString(R.string.settings_order_by_default));

        String newsLimit = sharedPreferences.getString(
                getString(R.string.settings_limit_news_key),
                getString(R.string.settings_limit_news_default));

        //Create the query parameters
        Uri baseUri = Uri.parse(HomePageActivity.NEWS_URL);
        Uri.Builder uriBuilder = baseUri.buildUpon();

        //http://content.guardianapis.com/search?page=20&api-key=1aefe6a9-8256-4ed1-9705-078617ffba7b&show-fields=thumbnail
        //https://content.guardianapis.com/search?q=12%20years%20a%20slave&format=json&tag=film/film,tone/reviews&from-date=2010-01-01&show-tags=contributor&show-fields=starRating,headline,thumbnail,short-url&order-by=relevance&api-key=test

        uriBuilder.appendQueryParameter("section", section);
        uriBuilder.appendQueryParameter("page-size", "10");
        uriBuilder.appendQueryParameter("order-by", newsOrder);
        uriBuilder.appendQueryParameter("use-date", "published");
        uriBuilder.appendQueryParameter("show-tags", "contributor");
        uriBuilder.appendQueryParameter("show-fields", "headline,bodyText,thumbnail,starRating");
        uriBuilder.appendQueryParameter("api-key", API_KEY);
        Log.i(TAG, uriBuilder.toString());
        return new NewsLoader(getContext(), uriBuilder.toString());
    }

    @Override
    public void onLoadFinished(Loader<List<News>> loader, List<News> news) {
        if (news != null && !news.isEmpty()) {
            //add data to the recycler view
            sectionNewsList.addAll(news);
            newsAdapter.notifyDataSetChanged();
        } else {
            //Todo: display and empty view
            Toast.makeText(getActivity(), "No results found", Toast.LENGTH_SHORT).show();
        }
    }


    @Override
    public void onLoaderReset(Loader<List<News>> loader) {
        sectionNewsList.clear();
        newsAdapter.notifyDataSetChanged(); // let your adapter know about the changes and reload view.
    }

    @Override
    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.btn_next:
                Toast.makeText(getActivity(), "NEXT PAGE PLEASE", Toast.LENGTH_SHORT).show();
                break;
            case R.id.btn_previous:
                Toast.makeText(getActivity(), "PREVIOUS PAGE PLEASE", Toast.LENGTH_SHORT).show();
                break;
            default:
                break;
        }
    }

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
}