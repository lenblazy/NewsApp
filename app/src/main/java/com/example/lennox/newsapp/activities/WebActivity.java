package com.example.lennox.newsapp.activities;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.webkit.WebView;

import com.example.lennox.newsapp.R;

public class WebActivity extends AppCompatActivity {

    String url = "http://www.example.com";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_web);

        Intent i = getIntent();
        url = i.getExtras().getString("url");

        WebView myWebView = (WebView) findViewById(R.id.webview);
        myWebView.loadUrl(url);
    }
}
