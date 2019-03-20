package com.example.lennox.newsapp;

import android.text.TextUtils;
import android.util.Log;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;
import java.nio.charset.Charset;
import java.util.ArrayList;
import java.util.List;

public class QueryUtils {

    private static final String LOG = QueryUtils.class.getSimpleName();

    public static List<News> fetchEarthquakeData(String url) {
        //Create a URL OBJECT
        URL newsURL = createURL(url);
        String jsonResponse = "";

        //Create a HTTP connection
        try {
            jsonResponse = makeHttpRequest(newsURL);
        } catch (IOException e) {
            e.printStackTrace();
        }
        //Parse the data from JSON FORMAT To string
        return extractFeatureFromJson(jsonResponse);
    }

    private static List<News> extractFeatureFromJson(String jsonResponse) {
        //check is json passed is null and return early
        if(TextUtils.isEmpty(jsonResponse)){
            return null;
        }

        List<News> newsList = new ArrayList<>();

        try {

            JSONObject root = new JSONObject(jsonResponse);
            JSONObject response = root.getJSONObject("response");
            JSONArray results = response.getJSONArray("results");
            for (int i = 0; i < results.length(); i++){
                //get the author
                //1st will be just passing null
                JSONObject currentNews = results.getJSONObject(i);

                //extract the data values
                String article = currentNews.getString("webTitle");
                String section = currentNews.getString("sectionName");
                String datePublished = currentNews.getString("webPublicationDate");
                String webURL = currentNews.getString("webUrl");

                News news = new News(article,section,datePublished,webURL);
                newsList.add(news);
            }//end for
        } catch (JSONException e) {
            Log.e(LOG, "Error parsing json objects", e);
        }
        return newsList;
    }

    private static String makeHttpRequest(URL newsURL) throws IOException {
        String jsonResponse = "";
        if (newsURL == null){
            return jsonResponse;
        }

        HttpURLConnection urlConnection = null;
        InputStream inputStream = null;

        try{
            urlConnection = (HttpURLConnection) newsURL.openConnection();
            urlConnection.setRequestMethod("GET");
            urlConnection.setReadTimeout(1000);
            urlConnection.setConnectTimeout(1500);
            urlConnection.connect();
            inputStream = urlConnection.getInputStream();
            jsonResponse = readFromStream(inputStream);
        }catch (Exception e){
            Log.e(LOG+ ": No connection","Error occurred "+ e);
        }finally {
            if(urlConnection != null){
                urlConnection.disconnect();
            }
            if(inputStream != null){
                inputStream.close();
            }
        }
        return jsonResponse;
    }

    private static String readFromStream(InputStream inputStream) {
        StringBuilder output = new StringBuilder();
        if(inputStream != null){
            InputStreamReader isr = new InputStreamReader(inputStream, Charset.forName("UTF-8"));
            BufferedReader br = new BufferedReader(isr);
            try {
                String line = br.readLine();
                while (line != null) {
                    output.append(line);
                    line = br.readLine();
                }
                return output.toString();
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
        return null;
    }

    private static URL createURL(String url) {
        try {
            return new URL(url);
        } catch (MalformedURLException e) {
            return null;
        }
    }
}
