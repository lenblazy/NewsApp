package com.example.lennox.newsapp;

import android.content.Context;
import android.support.annotation.NonNull;
import android.support.v4.content.ContextCompat;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.RatingBar;
import android.widget.TextView;
import android.widget.Toast;

import com.squareup.picasso.Picasso;

import java.util.List;

public class NewsAdapter extends RecyclerView.Adapter<NewsAdapter.NewsViewsHolder> {

    private Context mContext;
    private List<News> newsList;

    public NewsAdapter(Context mContext, List<News> newsList) {
        this.mContext = mContext;
        this.newsList = newsList;
    }

    //Todo: move different news sections to different fragments
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

    //Todo: Extract time such that time of post is relevant to the current time
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
        View view = LayoutInflater.from(mContext).inflate(R.layout.recycler_view_news, parent, false);

        //Todo: set up an onclick listener for the app

        return new NewsViewsHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull NewsViewsHolder holder, int position) {
        News news = newsList.get(position);
        holder.tvNewsHeadline.setText(news.getArticleName());

        //Get image url and load it using picasso
        if(news.getAuthorImage()!= null){
            Picasso.get().load(news.getAuthorImage()).into(holder.ivAuthorImage);
        }else{
            Picasso.get().load(R.drawable.default_user).into(holder.ivAuthorImage);
        }

        holder.tvAuthorName.setText(news.getAuthor());

        //Todo: Extract this date and relate it to current time
        holder.tvTime.setText(news.getDatePublished());

        Picasso.get().load(R.drawable.more).into(holder.ivMore);

        holder.tvNewsBody.setText(news.getBodyText());

        holder.newsRating.setRating(Integer.parseInt(news.getNewsRating()));

        Picasso.get().load(news.getHeadLinePictureURL()).into(holder.ivNewsImage);


    }

    @Override
    public int getItemCount() {
        return newsList.size();
    }

    public class NewsViewsHolder extends RecyclerView.ViewHolder {
        private ImageView ivAuthorImage;
        private TextView tvAuthorName;
        private TextView tvTime;
        private ImageView ivMore;
        private TextView tvNewsHeadline;
        private TextView tvNewsBody;
        private RatingBar newsRating;
        private ImageView ivNewsImage;

        public NewsViewsHolder(View itemView) {
            super(itemView);

            ivAuthorImage = itemView.findViewById(R.id.iv_author_image);
            tvAuthorName = itemView.findViewById(R.id.tv_author_name);
            tvTime = itemView.findViewById(R.id.time);
            ivMore = itemView.findViewById(R.id.iv_more);
            tvNewsHeadline = itemView.findViewById(R.id.tv_news_headline);
            tvNewsBody = itemView.findViewById(R.id.tv_news_body);
            newsRating = itemView.findViewById(R.id.news_rating);
            ivNewsImage = itemView.findViewById(R.id.iv_news_image);


        }
    }//end inner class
}//end class NewsAdapter
