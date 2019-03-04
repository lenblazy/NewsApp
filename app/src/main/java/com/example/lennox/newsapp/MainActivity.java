package com.example.lennox.newsapp;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.widget.ListView;
import android.widget.TextView;

public class MainActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        ListView newsList = findViewById(R.id.news_list);
        TextView emptyState = findViewById(R.id.empty_view);
        newsList.setEmptyView(emptyState);
    }
}
