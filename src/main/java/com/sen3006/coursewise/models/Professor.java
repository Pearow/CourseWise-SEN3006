package com.sen3006.coursewise.models;

public class Professor
{
    private String prof_id;
    private int prof_rating;
    private int prof_rating_count;
    private int total_rating;
    private String prof_name;

    public Professor(String prof_id,String prof_name)
    {
        this.prof_id = prof_id;
        this.prof_name = prof_name;
        this.prof_rating = 0;
        this.prof_rating_count = 0;
        this.total_rating = 0;
    }

    public String getProf_name() {
        return prof_name;
    }

    public void setProf_name(String prof_name) {
        this.prof_name = prof_name;
    }

    public String getProf_id() {
        return prof_id;
    }

    public void setProf_id(String prof_id) {
        this.prof_id = prof_id;
    }

    public boolean addRating(int newRating) {
        if (newRating < 0 || newRating > 100) {
            System.out.println("Invalid rating: " + newRating);
            return false;
        }
        this.total_rating += newRating;
        this.prof_rating_count++;
        this.prof_rating = Math.round((float) total_rating / prof_rating_count);
        return true;
    }

    public int getAvgRating() {
        return this.prof_rating;
    }

    public int getRatingCount() {
        return this.prof_rating_count;
    }

    public void show_prof_info()
    {
        System.out.println("Professor Name : " + prof_name);
        System.out.println("Professor Id : " + prof_id);
        System.out.println("Professor Rating is " + this.prof_rating + "% based on " + this.prof_rating_count + " votes.");

    }

}
