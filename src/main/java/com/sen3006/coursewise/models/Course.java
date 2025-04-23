package com.sen3006.coursewise.models;
import com.sen3006.coursewise.enums.Type;

public class Course
{
    private String course_id;
    private String course_name;
    private Department department;
    private Type type;
    private  int course_rating_count;
    private int course_rating;
    private int total_course_rating;

    public Course(String course_id, String course_name, Department department, String type)
    {
        this.course_id = course_id;
        this.course_name = course_name;
        this.department = department;
        this.type = Type.fromString(type);
        this.course_rating_count = 0;
        this.course_rating = 0;
        this.total_course_rating = 0;
    }

    public boolean addRating(int newRating) {
        if (newRating < 0 || newRating > 100) {
            System.out.println("Invalid rating: " + newRating);
            return false;
        }
        this.total_course_rating += newRating;
        this.course_rating_count++;
        this.course_rating = Math.round((float) total_course_rating / course_rating_count);
        return true;
    }

    public int getAvgRating() {
        return this.course_rating;
    }

    public int getRatingCount() {
        return this.course_rating_count;
    }


    public Type getType() {
        return type;
    }

    public void setType(String type) {
        this.type = Type.fromString(type);
    }

    public Department getDepartment() {
        return department;
    }

    public void setDepartment(Department department) {
        this.department = department;
    }

    public String getCourse_name() {
        return course_name;
    }

    public void setCourse_name(String course_name) {
        this.course_name = course_name;
    }

    public String getCourse_id() {
        return course_id;
    }


    public void setCourse_id(String course_id) {
        this.course_id = course_id;
    }
    public void show_course_info() {
        System.out.println("Course Name : " + course_name);
        System.out.println("Course Id : " + course_id);
        System.out.println("Department Name : " + department.getDepartment_name());
        System.out.println("Lesson Type : " + this.type);
        System.out.println("Course Rating is " + this.course_rating + "% based on " + this.course_rating_count + " votes.");
    }

}
