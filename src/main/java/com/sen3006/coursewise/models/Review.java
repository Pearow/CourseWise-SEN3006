package com.sen3006.coursewise.models;

public class Review {
    private Course course;
    private String comment;
    private int course_id;
    private User user;
    private int rating;

    public Review(Course course, String comment, User user, int rating) {
        this.course = course;
        this.comment = comment;
        this.user = user;
        this.rating = rating;
        course.addRating(rating);
    }

    public Course getCourse() {
        return course;
    }

    public void setCourse(Course course) {
        this.course = course;
    }

    public String getComment() {
        return comment;
    }

    public void setComment(String comment) {
        this.comment = comment;
    }

    public int getCourse_id() {
        return course_id;
    }

    public void setCourse_id(int course_id) {
        this.course_id = course_id;
    }

    public User getUser() {
        return user;
    }

    public void setUser(User user) {
        this.user = user;
    }

    public int getRating() {
        return rating;
    }

    public void setRating(int rating) {
        course.updateRating(this.rating, rating);
        this.rating = rating;
    }
}
