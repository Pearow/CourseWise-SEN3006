package com.sen3006.coursewise.models;
import com.sen3006.coursewise.API;
import com.sen3006.coursewise.enums.Type;
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
    private Type type;

    public Section(int section_id, LocalTime start_time, LocalTime end_time, int section_day, Classroom classroom, Course course, Professor professor, int type) {
        this.section_id = section_id;
        this.start_time = start_time;
        this.end_time = end_time;
        this.section_day = Weekday.fromIndex(section_day);
        this.classroom = classroom;
        this.course = course;
        this.lesson_duration = Duration.between(start_time,end_time);
        this.professor = professor;
        this.type = Type.fromIndex(type);

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
        LocalTime oldEndTime = this.end_time;
        this.end_time = end_time;

        if (oldEndTime != null && !oldEndTime.equals(end_time)) {
            // Notify observers about the change
            setChanged();
            notifyObservers();
        }
    }

    public LocalTime getStart_time() {
        return start_time;
    }

    public void setStart_time(LocalTime start_time) {
        LocalTime oldStartTime = this.start_time;
        this.start_time = start_time;

        if (oldStartTime != null && !oldStartTime.equals(start_time)) {
            // Notify observers about the change
            setChanged();
            notifyObservers();
        }
    }

    public Weekday getSection_day() {
        return section_day;
    }

    public void setSection_day(int section_day) {
        int oldSectionDay = this.section_day.getIntWeekday();
        this.section_day = Weekday.fromIndex(section_day);

        if (oldSectionDay != this.section_day.getIntWeekday()) {
            // Notify observers about the change
            setChanged();
            notifyObservers();
        }
    }

    public Classroom getClassroom() {
        return classroom;
    }

    public void setClassroom(Classroom classroom) {
        Classroom oldClassroom = this.classroom;
        this.classroom = classroom;

        if (oldClassroom != null && !oldClassroom.getClass_id().equals(classroom.getClass_id())) {
            // Notify observers about the change
            setChanged();
            notifyObservers();
        }
    }

    public Course getCourse() {
        return course;
    }

    public void setCourse(Course course) {
        Course oldCourse = this.course;
        this.course = course;

        // Notify observers about the change
        if (oldCourse != null && !oldCourse.getCourse_id().equals(course.getCourse_id())) {
            setChanged();
            notifyObservers();
        }
    }

    public Professor getProfessor() {
        return professor;
    }

    public Professor setProfessor(Professor professor) {
        Professor oldProfessor = this.professor;
        this.professor = professor;

        if (oldProfessor != null && !oldProfessor.getName().equals(professor.getName())) {
            // Notify observers about the change
            setChanged();
            notifyObservers();
        }
        return professor;
    }

    public int getRating() {
        return Math.round((float) (professor.getAvgRating() + course.getAvgRating())/2);
    }

    public Type getType() {
        return type;
    }

    public void setType(Type type) {
        Type oldType = this.type;
        this.type = type;

        // Notify observers about the change
        if (oldType != null && !(oldType.getIntType() == type.getIntType())) {
            setChanged();
            notifyObservers();
        }
    }

    public void  show_section_info()
    {
        System.out.println("Class : " + classroom.getClass_id());
        System.out.println("The lesson starts on " + this.section_day + " at " + start_time);
        System.out.println("The lesson ends on " + this.section_day + " at " + end_time);
        System.out.println("Duration (minutes): " + lesson_duration.toMinutes());

    }

}
