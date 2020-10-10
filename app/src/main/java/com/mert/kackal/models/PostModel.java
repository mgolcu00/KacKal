package com.mert.kackal.models;

import android.graphics.Bitmap;

public class PostModel {
    private String dateTime;
    private String authorName;
    private String authorImageUrl;
    private String text;
    private String imageUrl;
    private int likeCount;
    private Bitmap post_image;
    private String post_id;

    public PostModel(String dateTime, String authorName, String text, String imageUrl) {
        this.dateTime = dateTime;
        this.authorName = authorName;
        this.text = text;
        this.imageUrl = imageUrl;
        likeCount = 0;
    }

    public PostModel(String dateTime, String authorName, String text) {
        this.dateTime = dateTime;
        this.authorName = authorName;
        this.text = text;
        this.imageUrl = null;
        likeCount = 0;
    }

    public PostModel() {
    }



    public PostModel(String dateTime, String authorName, String authorImageUrl, String text, String imageUrl, int likeCount, Bitmap post_image, String post_id) {
        this.dateTime = dateTime;
        this.authorName = authorName;
        this.authorImageUrl = authorImageUrl;
        this.text = text;
        this.imageUrl = imageUrl;
        this.likeCount = likeCount;
        this.post_image = post_image;
        this.post_id = post_id;
    }

    public void likePost() {
        likeCount++;
    }

    public void dislikePost() {
        if (likeCount > 0)
            likeCount--;
    }

    public String getDateTime() {
        return dateTime;
    }

    public void setDateTime(String dateTime) {
        this.dateTime = dateTime;
    }

    public String getAuthorName() {
        return authorName;
    }

    public void setAuthorName(String authorName) {
        this.authorName = authorName;
    }

    public String getAuthorImageUrl() {
        return authorImageUrl;
    }

    public void setAuthorImageUrl(String authorImageUrl) {
        this.authorImageUrl = authorImageUrl;
    }

    public String getText() {
        return text;
    }

    public void setText(String text) {
        this.text = text;
    }

    public String getImageUrl() {
        return imageUrl;
    }

    public void setImageUrl(String imageUrl) {
        this.imageUrl = imageUrl;
    }

    public int getLikeCount() {
        return likeCount;
    }

    public void setLikeCount(int likeCount) {
        this.likeCount = likeCount;
    }

    public Bitmap getPost_image() {
        return post_image;
    }

    public void setPost_image(Bitmap post_image) {
        this.post_image = post_image;
    }

    public String getPost_id() {
        return post_id;
    }

    public void setPost_id(String post_id) {
        this.post_id = post_id;
    }
}
