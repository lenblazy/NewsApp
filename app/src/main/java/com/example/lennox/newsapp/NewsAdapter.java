package com.example.lennox.newsapp;

import android.content.Context;
import android.content.Intent;
import android.net.Uri;
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
    private int itemPosition;

    public NewsAdapter(Context mContext, List<News> newsList) {
        this.mContext = mContext;
        this.newsList = newsList;
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
    public NewsViewsHolder onCreateViewHolder( ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(mContext).inflate(R.layout.recycler_view_news, parent, false);
        return new NewsViewsHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull NewsViewsHolder holder, int position) {
        News news = newsList.get(holder.getAdapterPosition());
        itemPosition = position;

        holder.tvNewsHeadline.setText(news.getArticleName());

        //Get image url and load it using picasso
        if(!news.getAuthorImage().equals("empty")){
            Picasso.get().load(news.getAuthorImage()).into(holder.ivAuthorImage);
        }else{
            holder.ivAuthorImage.setImageResource(R.drawable.default_user);
        }

        holder.tvAuthorName.setText(news.getAuthor());

        //Todo: Extract this date and relate it to current time
        holder.tvTime.setText(news.getDatePublished());

        holder.ivMore.setImageResource(R.drawable.more);

        holder.tvNewsBody.setText(news.getBodyText());

        holder.newsRating.setRating(Integer.parseInt(news.getNewsRating()));

        Picasso.get().load(news.getHeadLinePictureURL()).into(holder.ivNewsImage);

        holder.itemView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                News currentNews = newsList.get(itemPosition);
                Uri newsUri = Uri.parse(currentNews.getNewsURL());
                view.getContext().startActivity(new Intent(Intent.ACTION_VIEW, newsUri));
            }
        });
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
