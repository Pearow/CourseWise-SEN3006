package com.sen3006.coursewise.models;

import com.sen3006.coursewise.API;

import java.util.Observable;

public class Review extends Observable {
    private Course course;
    private String comment;
    private User user;
    private int rating;

    public Review(Course course, String comment, User user, int rating, boolean isNew) {
        this.course = course;
        this.comment = comment;
        this.user = user;
        this.rating = rating;
        if (isNew) {
            course.addRating(rating);
        }

        addObserver(API.getInstance());
    }

    public Review(Course course, String comment, User user, int rating) {
        this(course, comment, user, rating, true);
    }

    public Course getCourse() {
        return course;
    }
  
//    public void setCourse(Course newCourse) {
//        course.revokeRating(rating);
//        course = newCourse;
//        course.addRating(rating);
//
//        // Notify observers about the change
//        setChanged();
//        notifyObservers();
//    }

    public String getComment() {
        return comment;
    }

    public void setComment(String comment) {
        this.comment = comment;
      
        // Notify observers about the change
        setChanged();
        notifyObservers();
    }

    public User getUser() {
        return user;
    }

    public int getRating() {
        return rating;
    }

    public void setRating(int rating) {
        course.updateRating(this.rating, rating);
        this.rating = rating;
    }

//    public void revokeRating() {
//        if (rating == -1){
//            System.out.println("Already revoked");
//            return;
//        }
//        course.revokeRating(rating);
//        course = null;
//        rating = -1;
//
//        // Notify observers about the change
//        setChanged();
//        notifyObservers();
//    }
}
