package com.example.lennox.newsapp;

public class News {
    private String articleName;
    private String sectionName;
    private String datePublished;
    private String newsURL;
    private String author;
    private String contributorProfile;
    private String authorImage;
    private String twitterHandle;
    private String headLinePictureURL;
    private String bodyText;
    private String newsRating;

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

    public String getAuthor() {
        return author;
    }

    public String getContributorProfile() {
        return contributorProfile;
    }

    public String getAuthorImage() {
        return authorImage;
    }

    public String getTwitterHandle() {
        return twitterHandle;
    }

    public String getHeadLinePictureURL() {
        return headLinePictureURL;
    }

    public String getBodyText() {
        return bodyText;
    }

    public String getNewsRating() {
        return newsRating;
    }

    public News(String articleName, String sectionName, String datePublished, String newsURL,
                String author, String contributorProfile, String authorImage, String twitterHandle,
                String headLinePictureURL, String bodyText, String newsRating) {
        this.articleName = articleName;
        this.sectionName = sectionName;
        this.datePublished = datePublished;
        this.newsURL = newsURL;
        this.author = author;
        this.contributorProfile = contributorProfile;
        this.authorImage = authorImage;
        this.twitterHandle = twitterHandle;
        this.headLinePictureURL = headLinePictureURL;
        this.bodyText = bodyText;
        this.newsRating = newsRating;
    }
}
