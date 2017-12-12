package com.example.andrew.goodreads;

/**
 * Created by andrew on 10/13/2017.
 */

public class book {
    String bookTitle,bookDes,bookImage,UserName,userImage,bookRate;



    public book() {

    }

    public book(String bName, String bDes, String bImage, String uName,String userImage,String bRate) {
        this.bookTitle = bName;
        this.bookDes = bDes;
        this.bookImage = bImage;
        this.UserName=uName;
        this.userImage = userImage;
        this.bookRate=bRate;
    }

    public String getUserName() {
        return UserName;
    }

    public void setUserName(String userName) {
        UserName = userName;
    }

    public String getbookTitle() {
        return bookTitle;
    }

    public void setbookTitle(String bName) {
        this.bookTitle = bName;
    }

    public String getbookDes() {
        return bookDes;
    }

    public void setbookDes(String bDes) {
        this.bookDes = bDes;
    }

    public String getbookImage() {
        return bookImage;
    }

    public void setbookImage(String bImage) {
        this.bookImage = bImage;
    }
    public String getUserImage() {
        return userImage;
    }

    public void setUserImage(String userImage) {
        this.userImage = userImage;
    }

    public String getBookRate() {
        return bookRate;
    }

    public void setBookRate(String bookRate) {
        this.bookRate = bookRate;
    }
}
