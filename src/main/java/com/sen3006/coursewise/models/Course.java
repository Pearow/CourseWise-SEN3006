package com.sen3006.coursewise.models;
import com.sen3006.coursewise.API;
import com.sen3006.coursewise.GUIController;
import com.sen3006.coursewise.enums.CourseType;

import java.util.Observable;

public class Course extends Observable
{
    private String course_id;
    private String course_name;
    private Department department;
    private CourseType type;
    private String lecturersNote;
    private  int course_rating_count;
    private int course_rating;
    private int total_course_rating;

    public Course(String course_id, String course_name, Department department, int type)
    {
        this(course_id, course_name, department, type, 0, 0, "");
    }

    public Course(String course_id, String course_name, Department department, int type, int total_rating, int course_rating_count, String lecturersNote) {
        this.course_id = course_id;
        this.course_name = course_name;
        this.department = department;
        this.type = CourseType.fromIndex(type);
        this.total_course_rating = total_rating;
        this.course_rating_count = course_rating_count;
        this.course_rating = Math.round((float) total_course_rating / course_rating_count);
        this.lecturersNote = lecturersNote;

        // Register this classroom as an observable to the API
        this.addObserver(API.getInstance());
        this.addObserver(GUIController.getInstance());
    }

    protected boolean addRating(int newRating) {
        if (newRating <= 0 || newRating > 10) {
            System.out.println("Invalid rating: " + newRating);
            return false;
        }
        this.total_course_rating += newRating;
        this.course_rating_count++;
        this.course_rating = Math.round((float) total_course_rating / course_rating_count);

        // Notify observers about the change
        setChanged();
        notifyObservers();

        return true;
    }

    public String getLecturersNote() {
        return lecturersNote;
    }

    public void setLecturersNote(String lecturersNote) {
        this.lecturersNote = lecturersNote;

        // Notify observers about the change
        setChanged();
        notifyObservers();
    }

    protected boolean updateRating(int oldRating, int newRating) {
        if (oldRating <= 0 || oldRating > 10 || newRating <= 0 || newRating > 10) {
            System.out.println("Invalid rating: " + oldRating + " or " + newRating);
            return false;
        }
        this.total_course_rating = this.total_course_rating - oldRating + newRating;
        this.course_rating = Math.round((float) total_course_rating / course_rating_count);

        // Notify observers about the change
        setChanged();
        notifyObservers();
        return true;
    }

    public int getAvgRating() {
        return this.course_rating;
    }

    public int getRatingCount() {
        return this.course_rating_count;
    }

    public int getTotalRating() {
        return this.total_course_rating;
    }

    public boolean revokeRating(int rating) {
        if (rating <= 0 || rating > 10) {
            System.out.println("Invalid rating: " + rating);
            return false;
        }
        this.total_course_rating -= rating;
        this.course_rating_count--;
        this.course_rating = Math.round((float) total_course_rating / course_rating_count);

        // Notify observers about the change
        setChanged();
        notifyObservers();

        return true;
    }


    public CourseType getType() {
        return type;
    }

    public void setType(int type) {
        this.type = CourseType.fromIndex(type);
        // Notify observers about the change
        setChanged();
        notifyObservers();
    }

    public Department getDepartment() {
        return department;
    }

    public void setDepartment(Department department) {
        this.department = department;

        // Notify observers about the change
        setChanged();
        notifyObservers();
    }

    public String getCourse_name() {
        return course_name;
    }

    public void setCourse_name(String course_name) {
        this.course_name = course_name;

        // Notify observers about the change
        setChanged();
        notifyObservers();
    }

    public String getCourse_id() {
        return course_id;
    }


    private void setCourse_id(String course_id) {
        this.course_id = course_id;
    }
    public void show_course_info() {
        System.out.println("Course Name : " + course_name);
        System.out.println("Course Id : " + course_id);
        System.out.println("Department Name : " + department.getDepartment_name());
        System.out.println("Lesson Type : " + this.type);
        System.out.println("Course Rating is " + this.course_rating + "/10 based on " + this.course_rating_count + " votes.");
    }

}
