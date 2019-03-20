package com.example.lennox.newsapp;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import org.w3c.dom.Text;

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

        TextView newsHeadline = (TextView) listItemView.findViewById(R.id.news_headline);
        newsHeadline.setText(currentNews.getArticleName());

        TextView newsSection = (TextView) listItemView.findViewById(R.id.news_section);
        newsSection.setText(currentNews.getSectionName().substring(1));

        TextView datePublished = (TextView) listItemView.findViewById(R.id.date);
        TextView timePublished = (TextView) listItemView.findViewById(R.id.time);

        //separating the date and time and format them
        datePublished.setText(formatTime(currentNews.getDatePublished(), "date"));
        timePublished.setText(formatTime(currentNews.getDatePublished(),"time"));

        TextView author = (TextView) listItemView.findViewById(R.id.author);
        //to change this later after being able to extract author
        author.setText("Author: " + "Unknown");

        return listItemView;
    }

    private String formatTime(String datePublished, String category) {
        String separator = "T";
        String date = "";
        if(datePublished.contains(separator)){
            String[] parts = datePublished.split(separator);
            if(category == "date"){
                date += parts[0];
            }else{
                date += parts[1];
            }
        }
        return date;
    }
}
