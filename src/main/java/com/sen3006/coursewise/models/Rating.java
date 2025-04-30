package com.sen3006.coursewise.models;

import com.sen3006.coursewise.API;

import java.util.Observable;

public class Rating extends Observable {
    private Professor professor;
    private User user;
    private int rating;

    public Rating(Professor professor, User user, int rating, boolean isNew) {
        this.professor = professor;
        this.user = user;
        this.rating = rating;
        if (isNew) {
            professor.addRating(rating);
        }

        addObserver(API.getInstance());
    }

    public Rating(Professor professor, User user, int rating) {
        this(professor, user, rating, true);
    }

    public Professor getProfessor() {
        return professor;
    }

    public User getUser() {
        return user;
    }

    public int getRating() {
        return rating;
    }

    public void setRating(int rating) {
        professor.updateRating(this.rating, rating);
        this.rating = rating;
        setChanged();
        notifyObservers();
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
