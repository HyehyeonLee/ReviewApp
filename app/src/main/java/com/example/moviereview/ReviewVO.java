package com.example.moviereview;

import android.os.Parcel;

public class ReviewVO {
    private int id;
    private String title;
    private String date;
    private double rating;
    private String memo;
    private String image;

    public ReviewVO(String title, String date, double rating, String memo, String image) {
        super();
        this.title = title;
        this.date = date;
        this.rating = rating;
        this.memo = memo;
        this.image = image;
    }

    public ReviewVO() {
        super();
    }


    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getDate() {
        return date;
    }

    public void setDate(String date) {
        this.date = date;
    }

    public double getRating() {
        return rating;
    }

    public void setRating(double rating) {
        this.rating = rating;
    }

    public String getMemo() {
        return memo;
    }

    public void setMemo(String memo) {
        this.memo = memo;
    }

    public String getImage() {
        return image;
    }

    public void setImage(String image) {
        this.image = image;
    }

    public void setId(int id){
        this.id = id;
    }

    public int getId(){
        return id;
    }

    @Override
    public String toString() {
        return "Movie [title=" + title + ", date=" + date + ", rating=" + rating + ", memo=" + memo + "]";
    }

}
