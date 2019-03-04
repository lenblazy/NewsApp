package com.example.lennox.newsapp;

import android.util.Log;

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

        return null;
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
            Log.e("No connection","Error occurred "+ e);
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
