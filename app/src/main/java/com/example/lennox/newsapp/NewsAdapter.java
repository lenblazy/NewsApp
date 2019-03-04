package com.example.lennox.newsapp;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import java.util.ArrayList;

public class NewsAdapter extends ArrayAdapter<News> {
    public NewsAdapter(Context context, ArrayList<News> newsList) {
        super(context,0, newsList);
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        View listItemView = convertView;
        if(listItemView == null){
            convertView = LayoutInflater.from(getContext()).inflate(R.layout.item_list, parent, false);
        }
        News currentNews = getItem(position);
        ImageView newsImage = (ImageView) listItemView.findViewById(R.id.news_image);
        //setImage
        TextView newsHeadline = (TextView) listItemView.findViewById(R.id.news_headline);
        //setText
        TextView newsStatus = (TextView) listItemView.findViewById(R.id.news_status);
        //setText
        TextView viewers = (TextView) listItemView.findViewById(R.id.viewers);
        //setText
        ImageView comments = (ImageView) listItemView.findViewById(R.id.comments);
        //setImage

        return listItemView;
    }
}
