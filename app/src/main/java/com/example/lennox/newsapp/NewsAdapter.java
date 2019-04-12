package com.example.lennox.newsapp;

import android.content.Context;
import android.graphics.drawable.GradientDrawable;
import android.support.annotation.NonNull;
import android.support.v4.content.ContextCompat;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import java.util.List;

public class NewsAdapter extends RecyclerView.Adapter<NewsAdapter.NewsViewsHolder> {

    private Context mContext;
    private List<News> newsList;

    public NewsAdapter(Context mContext, List<News> newsList) {
        this.mContext = mContext;
        this.newsList = newsList;
    }

    private int getSectionColor(String sectionName) {
        int sectionColorResourceId;
        switch (sectionName) {
            case "Football":
            case "Sports":
                sectionColorResourceId = R.color.football;
                break;
            case "Business":
                sectionColorResourceId = R.color.business;
                break;
            case "Politics":
                sectionColorResourceId = R.color.politics;
                break;
            case "World news":
                sectionColorResourceId = R.color.worldNews;
                break;
            case "US news":
            case "UK news":
                sectionColorResourceId = R.color.usnews;
                break;
            case "Opinion":
                sectionColorResourceId = R.color.opinion;
                break;
            case "Education":
            case "Books":
                sectionColorResourceId = R.color.education;
                break;
            case "Television":
            case "Film":
                sectionColorResourceId = R.color.books;
                break;
            case "Society":
                sectionColorResourceId = R.color.society;
                break;
            default:
                sectionColorResourceId = R.color.unknown;
                break;
        }
        return ContextCompat.getColor(mContext, sectionColorResourceId);
    }


    private String formatTime(String datePublished, String category) {
        String separator = "T";
        String date = "";
        if (datePublished.contains(separator)) {
            String[] parts = datePublished.split(separator);
            if (category.equals("date")) {
                date += parts[0];
            } else {
                date += parts[1];
            }
        }
        return date;
    }

    @NonNull
    @Override
    public NewsViewsHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(mContext).inflate(R.layout.recycler_view_news,parent,false);
        return new NewsViewsHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull NewsViewsHolder holder, int position) {
        News news= newsList.get(position);
        holder.tvNewsHeadline.setText(news.getArticleName());

        holder.tvNewsSection.setText(news.getSectionName());
        GradientDrawable sectionCircle = (GradientDrawable) holder.tvNewsSection.getBackground();

        // Get the appropriate background color based on the current earthquake magnitude
        int sectionColor = getSectionColor(news.getSectionName());

        // Set the color on the magnitude circle
        sectionCircle.setColor(sectionColor);

        holder.tvNewsTime.setText(formatTime(news.getDatePublished(), "time"));
        holder.tvNewsDate.setText(formatTime(news.getDatePublished(), "date"));

        holder.tvNewsAuthor.setText("Author: "+ news.getAuthor());
    }

    @Override
    public int getItemCount() {
        return newsList.size();
    }

    public class NewsViewsHolder extends RecyclerView.ViewHolder{
        private TextView tvNewsSection;
        private TextView tvNewsHeadline;
        private TextView tvNewsDate;
        private TextView tvNewsTime;
        private TextView tvNewsAuthor;

        public NewsViewsHolder(View itemView) {
            super(itemView);
            tvNewsAuthor = itemView.findViewById(R.id.author);
            tvNewsHeadline = itemView.findViewById(R.id.news_headline);
            tvNewsDate = itemView.findViewById(R.id.date);
            tvNewsTime = itemView.findViewById(R.id.time);
            tvNewsSection = itemView.findViewById(R.id.news_section);

        }
    }//end inner class
}//end class NewsAdpater
