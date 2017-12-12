package com.example.andrew.goodreads;

/**
 * Created by andrew on 10/27/2017.
 */

public class comment {
    String user;
    String comment;
    String userImage;

    comment(){

    }

    public comment(String userImage) {
        this.userImage = userImage;
    }

    public String getUserImage() {
        return userImage;
    }

    public void setUserImage(String userImage) {
        this.userImage = userImage;
    }

    public comment(String user, String comment, String userImage) {
        this.user= user;
        this.comment = comment;
        this.userImage = userImage;

    }

    public String getUser() {
        return user;
    }

    public void setUser(String userName) {
        this.user = userName;
    }

    public String getComment() {
        return comment;
    }

    public void setComment(String comment) {
        this.comment = comment;
    }
}
