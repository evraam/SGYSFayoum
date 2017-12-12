package com.example.andrew.goodreads;

/**
 * Created by andrew on 12/5/2017.
 */

public class blog {
    String postTitle,postImage,postTime;

    public blog() {

    }


    public blog(String postTitle, String postImage, String postTime) {
        this.postTitle = postTitle;
        this.postImage = postImage;
        this.postTime=postTime;

    }

    public String getPostTitle() {
        return postTitle;
    }

    public void setPostTitle(String postTitle) {
        this.postTitle = postTitle;
    }

    public String getPostImage() {
        return postImage;
    }

    public void setPostImage(String postImage) {
        this.postImage = postImage;
    }
    public String getPostTime() {
        return postTime;
    }

    public void setPostTime(String postTime) {
        this.postTime = postTime;
    }

}
