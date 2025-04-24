package com.sen3006.coursewise.models;
import com.sen3006.coursewise.enums.Weekday;

import java.time.LocalTime;
import java.time.Duration;

public class Section
{
    private LocalTime start_time;
    private LocalTime end_time;
    private Weekday section_day;
    private Classroom classroom;
    private final Duration lesson_duration;

    public Section(LocalTime start_time, LocalTime end_time, int section_day, Classroom classroom) {
        this.start_time = start_time;
        this.end_time = end_time;
        this.section_day = Weekday.fromIndex(section_day);
        this.classroom = classroom;
        this.lesson_duration = Duration.between(start_time,end_time);
    }


    public LocalTime getEnd_time() {
        return end_time;
    }

    public void setEnd_time(LocalTime end_time) {
        this.end_time = end_time;
    }

    public LocalTime getStart_time() {
        return start_time;
    }

    public void setStart_time(LocalTime start_time) {
        this.start_time = start_time;
    }

    public Weekday getSection_day() {
        return section_day;
    }

    public void setSection_day(int section_day) {
        this.section_day = Weekday.fromIndex(section_day);
    }

    public Classroom getClassroom() {
        return classroom;
    }

    public void setClassroom(Classroom classroom) {
        this.classroom = classroom;
    }

    public void  show_section_info()
    {
        System.out.println("Class : " + classroom.getClass_id());
        System.out.println("The lesson starts on " + this.section_day + " at " + start_time);
        System.out.println("The lesson ends on " + this.section_day + " at " + end_time);
        System.out.println("Duration (minutes): " + lesson_duration.toMinutes());

    }

}
