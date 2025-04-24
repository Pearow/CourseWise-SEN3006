package com.sen3006.coursewise.models;
import com.sen3006.coursewise.enums.Campus;

public class Classroom
{
    private Campus campus;
    private String class_id;

    public Classroom(int campus, String class_id)
    {
        this.campus = Campus.fromIndex(campus);
        this.class_id = class_id;
    }

    public Campus getCampus() {
        return campus;
    }

    public void setCampus(int campus) {
        this.campus = Campus.fromIndex(campus);
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
