package com.example.andrew.goodreads;

/**
 * Created by andrew on 10/30/2017.
 */

public class pdf {
    String bookUri;
    String bookName;
    public pdf(){

    }

    public pdf(String bookUri,String bookName) {
        this.bookUri = bookUri;
        this.bookName=bookName;
    }

    public String getBookUri() {
        return bookUri;
    }

    public void setBookUri(String bookUri) {
        this.bookUri = bookUri;
    }

    public String getBookName() {
        return bookName;
    }

    public void setBookName(String bookName) {
        this.bookName = bookName;
    }
}
