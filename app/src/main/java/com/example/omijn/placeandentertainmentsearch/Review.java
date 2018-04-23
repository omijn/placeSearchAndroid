package com.example.omijn.placeandentertainmentsearch;

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

    // TODO: 4/22/18 Convert timestamp to YYYY-MM-DD HH:MM:SS
    public String getTimestamp() {
        return timestamp;
    }

    public float getRating() {
        return Float.valueOf(Double.toString(rating));
    }
}
