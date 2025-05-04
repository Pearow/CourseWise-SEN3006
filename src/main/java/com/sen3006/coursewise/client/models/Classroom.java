package com.sen3006.coursewise.client.models;
import com.sen3006.coursewise.client.API;
import com.sen3006.coursewise.client.enums.Campus;

import java.util.Observable;

public class Classroom extends Observable
{
    private Campus campus;
    private String class_id;

    public Classroom(int campus, String class_id)
    {
        this.campus = Campus.fromIndex(campus);
        this.class_id = class_id;

        // Register this classroom as an observable to the API
        this.addObserver(API.getInstance());
    }

    public Campus getCampus() {
        return campus;
    }

    public void setCampus(int campus) {
        int oldCampus = this.campus.getIntCampus();
        this.campus = Campus.fromIndex(campus);

        if (oldCampus != this.campus.getIntCampus()) {
            // Notify observers about the change
            setChanged();
            notifyObservers();
        }
    }

    public String getClass_id() {
        return class_id;
    }

    private void setClass_id(String class_id) {
        this.class_id = class_id;
    }

    public void show_class_info()
    {
        System.out.println("Class : "+ class_id);
        System.out.println("Campus : " + this.campus);
    }
}
