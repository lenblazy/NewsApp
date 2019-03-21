package com.example.lennox.newsapp;

import android.app.Activity;
import android.content.Context;
import android.graphics.drawable.GradientDrawable;
import android.support.v4.content.ContextCompat;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import org.w3c.dom.Text;

import java.util.ArrayList;

public class NewsAdapter extends ArrayAdapter<News> {
    public NewsAdapter(Activity context, ArrayList<News> newsList) {
        super(context,0, newsList);
    }

    @Override
    public View getView(int position, View view, ViewGroup parent) {
        View listItemView = view;
        if(listItemView == null){
            listItemView = LayoutInflater.from(getContext()).inflate(R.layout.item_list, parent, false);
        }

        News currentNews = getItem(position);

        TextView newsHeadline = listItemView.findViewById(R.id.news_headline);
        newsHeadline.setText(currentNews.getArticleName());

        TextView newsSection = (TextView) listItemView.findViewById(R.id.news_section);
        newsSection.setText(currentNews.getSectionName());

        GradientDrawable sectionCircle = (GradientDrawable) newsSection.getBackground();

        // Get the appropriate background color based on the current earthquake magnitude
        int sectionColor = getSectionColor(currentNews.getSectionName());

        // Set the color on the magnitude circle
        sectionCircle.setColor(sectionColor);

        TextView datePublished = (TextView) listItemView.findViewById(R.id.date);
        TextView timePublished = (TextView) listItemView.findViewById(R.id.time);

        //separating the date and time and format them
        datePublished.setText(formatTime(currentNews.getDatePublished(), "date"));
        timePublished.setText(formatTime(currentNews.getDatePublished(),"time"));

        TextView author = (TextView) listItemView.findViewById(R.id.author);
        //to change this later after being able to extract author
        author.setText(R.string.author + currentNews.getAuthor());

        return listItemView;
    }

    private int getSectionColor(String sectionName) {
        int sectionColorResourceId;
        switch(sectionName){
            case "sports":
                sectionColorResourceId = R.color.sports;
                break;
            case "finance":
                sectionColorResourceId = R.color.finance;
                break;
            case "politics":
                sectionColorResourceId = R.color.politics;
                break;
            case "technology":
                sectionColorResourceId = R.color.technology;
                break;
            case "health":
                sectionColorResourceId = R.color.health;
                break;
            default:
                sectionColorResourceId = R.color.unknown;
                break;
        }
        return ContextCompat.getColor(getContext(), sectionColorResourceId);
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
