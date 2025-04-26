package com.sen3006.coursewise.models;

import com.sen3006.coursewise.API;

public class Professor extends User
{
    private int prof_id;
    private int prof_rating;
    private int prof_rating_count;
    private int total_rating;
    private String prof_name;

    public Professor(int prof_id,String prof_name, String prof_surname, String prof_email)
    {
        super(prof_id, prof_name, prof_surname, prof_email);
        this.prof_id = prof_id;
        this.prof_rating = 0;
        this.prof_rating_count = 0;
        this.total_rating = 0;

        // Register this classroom as an observable to the API
        this.addObserver(API.getInstance());
    }

    public String getProf_name() {
        return prof_name;
    }

    public void setProf_name(String prof_name) {
        this.prof_name = prof_name;

        // Notify observers about the change
        setChanged();
        notifyObservers();
    }

    public int getProf_id() {
        return prof_id;
    }

    private void setProf_id(int prof_id) {
        this.prof_id = prof_id;
    }

    public boolean addRating(int newRating) {
        if (newRating <= 0 || newRating > 10) {
            System.out.println("Invalid rating: " + newRating);
            return false;
        }
        this.total_rating += newRating;
        this.prof_rating_count++;
        this.prof_rating = Math.round((float) total_rating / prof_rating_count);

        // Notify observers about the change
        setChanged();
        notifyObservers();

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
        System.out.println("Professor Rating is " + this.prof_rating + "/10 based on " + this.prof_rating_count + " votes.");

    }

}
