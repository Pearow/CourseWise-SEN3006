package com.sen3006.coursewise.models;
import com.sen3006.coursewise.API;
import com.sen3006.coursewise.enums.Weekday;

import java.time.LocalTime;
import java.time.Duration;
import java.util.Observable;

public class Section extends Observable
{
    private int section_id;
    private LocalTime start_time;
    private LocalTime end_time;
    private Weekday section_day;
    private Classroom classroom;
    private Course course;
    private final Duration lesson_duration;
    private Professor professor;

    public Section(int section_id, LocalTime start_time, LocalTime end_time, int section_day, Classroom classroom, Course course, Professor professor) {
        this.section_id = section_id;
        this.start_time = start_time;
        this.end_time = end_time;
        this.section_day = Weekday.fromIndex(section_day);
        this.classroom = classroom;
        this.course = course;
        this.lesson_duration = Duration.between(start_time,end_time);
        this.professor = professor;

        // Register this classroom as an observable to the API
        this.addObserver(API.getInstance());
    }

    public int getSection_id() {
        return section_id;
    }

    private void setSection_id(int section_id) {
        this.section_id = section_id;
    }

    public LocalTime getEnd_time() {
        return end_time;
    }

    public void setEnd_time(LocalTime end_time) {
        this.end_time = end_time;

        // Notify observers about the change
        setChanged();
        notifyObservers();
    }

    public LocalTime getStart_time() {
        return start_time;
    }

    public void setStart_time(LocalTime start_time) {
        this.start_time = start_time;

        // Notify observers about the change
        setChanged();
        notifyObservers();
    }

    public Weekday getSection_day() {
        return section_day;
    }

    public void setSection_day(int section_day) {
        this.section_day = Weekday.fromIndex(section_day);

        // Notify observers about the change
        setChanged();
        notifyObservers();
    }

    public Classroom getClassroom() {
        return classroom;
    }

    public void setClassroom(Classroom classroom) {
        this.classroom = classroom;

        // Notify observers about the change
        setChanged();
        notifyObservers();
    }

    public Course getCourse() {
        return course;
    }

    public void setCourse(Course course) {
        this.course = course;

        // Notify observers about the change
        setChanged();
        notifyObservers();
    }

    public Professor getProfessor() {
        return professor;
    }

    public Professor setProfessor(Professor professor) {
        this.professor = professor;

        // Notify observers about the change
        setChanged();
        notifyObservers();
        return professor;
    }

    public int getRating() {
        return Math.round((float) (professor.getAvgRating() + course.getAvgRating())/2);
    }

    public void  show_section_info()
    {
        System.out.println("Class : " + classroom.getClass_id());
        System.out.println("The lesson starts on " + this.section_day + " at " + start_time);
        System.out.println("The lesson ends on " + this.section_day + " at " + end_time);
        System.out.println("Duration (minutes): " + lesson_duration.toMinutes());

    }

}
