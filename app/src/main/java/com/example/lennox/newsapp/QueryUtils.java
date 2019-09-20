package com.example.lennox.newsapp;

import android.content.Context;
import android.text.TextUtils;
import android.util.Log;
import android.widget.Toast;

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
            Log.d(LOG, "Data is ready to be read");
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
        //articleName,sectionName,datePublished,newsURL,author,headLinePictureURL,bodyText,newsRating,authorImage
        try {
            JSONObject root = new JSONObject(jsonResponse);
            JSONObject response = root.getJSONObject("response");
            JSONArray results = response.getJSONArray("results");
            for (int i = 0; i < results.length(); i++){
                JSONObject currentNews = results.getJSONObject(i);

                //extract the data values
                String article = currentNews.getString("webTitle");
                String section = currentNews.getString("sectionName");
                String datePublished = currentNews.getString("webPublicationDate");
                String webURL = currentNews.getString("webUrl");

                // extracting from the fields object
                JSONObject fields = currentNews.getJSONObject("fields");

                String newsImage = fields.getString("thumbnail");
                String bodyText = fields.getString("bodyText");

                String starRating = "0";
                if(fields.has("starRating")){
                    starRating = fields.getString("starRating");
                }

                String contributor = "Anonymous";
                String contributorProfile = "Not available";
                String authorImage = "empty";
                String twitterHandle = "Not available";

                if(currentNews.length()>0){
                    JSONArray tags = currentNews.getJSONArray("tags");
                    //extracting from the tags array
                    JSONObject firstAuthor = tags.getJSONObject(0);
                    contributor = firstAuthor.getString("webTitle");
                    contributorProfile = firstAuthor.getString("webUrl");

                    if(firstAuthor.has("bylineImageUrl")){
                        authorImage = firstAuthor.getString("bylineImageUrl");
                    }
                    if(firstAuthor.has("twitterHandle")){
                        twitterHandle = firstAuthor.getString("twitterHandle");
                    }
                }

                News news = new News(article, section, datePublished, webURL, contributor,
                        contributorProfile, authorImage, twitterHandle, newsImage,bodyText, starRating);
                newsList.add(news);

                Log.d(LOG, "JSON extracted");
            }//end outer for
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
            urlConnection.setConnectTimeout(2000);
            urlConnection.connect();
            inputStream = urlConnection.getInputStream();
            jsonResponse = readFromStream(inputStream);
        }catch (Exception e){
            Log.e(LOG+ ": No connection","Api could not be connected "+ e);
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
