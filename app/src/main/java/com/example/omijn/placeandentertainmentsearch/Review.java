package com.example.omijn.placeandentertainmentsearch;

import java.text.SimpleDateFormat;
import java.util.Date;

public class Review {
    private int index; // used to determine order in which reviews were retrieved from Google, so we can do a default sort later
    private String review;
    private String author;
    private String reviewLink;
    private String authorPhoto;
    private String timestamp;
    private Double rating;

    public Review(int i, String review, String author, String reviewLink, String authorPhoto, String timestamp, Double rating) {
        this.index = i;
        this.review = review;
        this.author = author;
        this.reviewLink = reviewLink;
        this.authorPhoto = authorPhoto;
        this.timestamp = timestamp;
        this.rating = rating;
    }

    public int getIndex() {
        return index;
    }

    public String getReview() {
        return review;
    }

    public String getAuthor() {
        return author;
    }

    public String getReviewLink() {
        return reviewLink;
    }

    public String getAuthorPhoto() {
        return authorPhoto;
    }

    public String getTimestamp() {
        Date date = new Date(Long.parseLong(timestamp) * 1000);
        SimpleDateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
        return dateFormat.format(date);
    }

    public Long getUnixTimestamp() {
        return Long.parseLong(timestamp);
    }

    public float getRating() {
        return Float.valueOf(Double.toString(rating));
    }
}
