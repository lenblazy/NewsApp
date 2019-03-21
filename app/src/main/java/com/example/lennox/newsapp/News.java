package com.example.lennox.newsapp;

public class News {
    private String articleName;
    private String sectionName;
    private String datePublished;
    private String newsURL;
    private String author;

    public News(String articleName, String sectionName, String datePublished,String author, String newsURL) {
        this.articleName = articleName;
        this.sectionName = sectionName;
        this.datePublished = datePublished;
        this.newsURL = newsURL;
        this.author = author;
    }

    public String getAuthor() {
        return articleName;
    }

    public String getArticleName() {
        return articleName;
    }

    public String getSectionName() {
        return sectionName;
    }

    public String getDatePublished() {
        return datePublished;
    }

    public String getNewsURL() {
        return newsURL;
    }
}
