package com.example.lennox.newsapp.adapters;

import android.content.Context;
import android.content.Intent;
import android.net.Uri;
import android.support.annotation.NonNull;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.RatingBar;
import android.widget.Spinner;
import android.widget.TextView;

import com.example.lennox.newsapp.News;
import com.example.lennox.newsapp.R;
import com.squareup.picasso.Picasso;

import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;
import java.util.TimeZone;

public class NewsAdapter extends RecyclerView.Adapter<NewsAdapter.NewsViewsHolder> {

    private Context mContext;
    private List<News> newsList;
    private int itemPosition;

    public NewsAdapter(Context mContext, List<News> newsList) {
        this.mContext = mContext;
        this.newsList = newsList;
    }

    @NonNull
    @Override
    public NewsViewsHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(mContext).inflate(R.layout.recycler_view_news, parent, false);
        return new NewsViewsHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull final NewsViewsHolder holder, int position) {
        News news = newsList.get(holder.getAdapterPosition());
        itemPosition = holder.getAdapterPosition();

        long MIN_MILLIS = 1000 * 60;
        long HR_MILLIS = 60 * MIN_MILLIS;
        long DAY_MILLIS = 24 * HR_MILLIS;
        long dateMillis;
        String date = "";
        long now = System.currentTimeMillis();
        Date guardianDate;
        SimpleDateFormat sDateFormat = new SimpleDateFormat("dd MMM");

        holder.tvNewsHeadline.setText(news.getArticleName());

        //Get image url and load it using picasso
        if (!news.getAuthorImage().equals("empty")) {
            Picasso.get().load(news.getAuthorImage()).into(holder.ivAuthorImage);
        } else {
            holder.ivAuthorImage.setImageResource(R.drawable.default_user);
        }

        holder.tvAuthorName.setText(news.getAuthor());

        try {
            DateFormat guardianDateFormat = new SimpleDateFormat("yyyy-MM-dd'T'HH:mm:ss'Z'");
            guardianDateFormat.setTimeZone(TimeZone.getTimeZone("UTC"));

            guardianDate = guardianDateFormat.parse(news.getDatePublished());
            dateMillis = guardianDate.getTime();

            if (now - dateMillis < (DAY_MILLIS)) {
                if (now - dateMillis < (HR_MILLIS)) {
                    long mins = Math.round((now - dateMillis) / MIN_MILLIS);
                    if (String.valueOf(mins).equals("1")) {
                        date = mins + " min ago";
                    }else if(String.valueOf(mins).equals("0")){
                        date = "Now...";
                    }else {
                        date = mins + " mins ago";
                    }

                } else {
                    long mins = Math.round((now - dateMillis) / HR_MILLIS);
                    if (String.valueOf(mins).equals("1")) {
                        date = mins + " hr ago";
                    } else {
                        date = mins + " hrs ago";
                    }

                }
            } else {
                Date dateDate = new Date(dateMillis);
                date = sDateFormat.format(dateDate);
            }
        } catch (ParseException e) {
            e.printStackTrace();
        }

        //add a dot to the date string
        date = "\u2022" + date;

        holder.tvTime.setText(date);
        //todo: use spinner to allow users to follow or view author's profile

        holder.tvNewsBody.setText(news.getBodyText());

        holder.newsRating.setRating(Integer.parseInt(news.getNewsRating()));

        Picasso.get().load(news.getHeadLinePictureURL()).into(holder.ivNewsImage);

        holder.itemView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                News currentNews = newsList.get(holder.getAdapterPosition());
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
        private TextView tvNewsHeadline;
        private TextView tvNewsBody;
        private RatingBar newsRating;
        private ImageView ivNewsImage;
        private Spinner spinner;

        public NewsViewsHolder(View itemView) {
            super(itemView);

            ivAuthorImage = itemView.findViewById(R.id.iv_author_image);
            tvAuthorName = itemView.findViewById(R.id.tv_author_name);
            tvTime = itemView.findViewById(R.id.time);
            spinner = itemView.findViewById(R.id.spinner);
            tvNewsHeadline = itemView.findViewById(R.id.tv_news_headline);
            tvNewsBody = itemView.findViewById(R.id.tv_news_body);
            newsRating = itemView.findViewById(R.id.news_rating);
            ivNewsImage = itemView.findViewById(R.id.iv_news_image);
        }
    }//end inner class
}//end class NewsAdapter
