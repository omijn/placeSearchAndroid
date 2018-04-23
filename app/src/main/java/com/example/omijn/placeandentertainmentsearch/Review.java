package com.example.omijn.placeandentertainmentsearch;

import java.text.SimpleDateFormat;
import java.util.Date;

public class Review {
    private String review;
    private String author;
    private String reviewLink;
    private String authorPhoto;
    private String timestamp;
    private Double rating;

    public Review(String review, String author, String reviewLink, String authorPhoto, String timestamp, Double rating) {
        this.review = review;
        this.author = author;
        this.reviewLink = reviewLink;
        this.authorPhoto = authorPhoto;
        this.timestamp = timestamp;
        this.rating = rating;
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

    public float getRating() {
        return Float.valueOf(Double.toString(rating));
    }
}
