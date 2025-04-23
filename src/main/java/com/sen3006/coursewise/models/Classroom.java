package com.sen3006.coursewise.models;
import com.sen3006.coursewise.enums.Campus;

public class Classroom
{
    private Campus campus;
    private String class_id;

    public Classroom(String campus, String class_id)
    {
        this.campus = Campus.fromString(campus);
        this.class_id = class_id;
    }

    public Campus getCampus() {
        return campus;
    }

    public void setCampus(Campus campus) {
        this.campus = campus;
    }

    public String getClass_id() {
        return class_id;
    }

    public void setClass_id(String class_id) {
        this.class_id = class_id;
    }

    public void show_class_info()
    {
        System.out.println("Class : "+ class_id);
        System.out.println("Campus : " + this.campus);
    }
}
