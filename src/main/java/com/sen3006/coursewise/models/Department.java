package com.sen3006.coursewise.models;

public class Department
{
    private int department_id;
    private String department_name;
    private String faculty_name;

    public Department(int department_id, String department_name, String faculty_name) {
        this.department_id = department_id;
        this.department_name = department_name;
        this.faculty_name = faculty_name;
    }

    public int getDepartment_id() {
        return department_id;
    }

    public void setDepartment_id(int department_id) {
        this.department_id = department_id;
    }

    public String getDepartment_name() {
        return department_name;
    }

    public void setDepartment_name(String department_name) {
        this.department_name = department_name;
    }

    public String getFaculty_name() {
        return faculty_name;
    }

    public void setFaculty_name(String faculty_name) {
        this.faculty_name = faculty_name;
    }

    public void show_department_info()
    {
        System.out.println("Faculty name : "+ faculty_name);
        System.out.println("Department ıd : " + department_id);
        System.out.println("Department name " + department_name);

    }

}
