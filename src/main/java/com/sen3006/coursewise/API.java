package com.sen3006.coursewise;
import com.sen3006.coursewise.models.*;

public class API {
    private static API instance;
    private API() {}

    public static API getInstance() {
        if (instance == null) {
            instance = new API();
        }
        return instance;
    }

    public Classroom getClassroom(String id){
        return null;
    }

    public Course getCourse(String id){
        return null;
    }

    public User getUser(int id){
        return null;
    }

    public Department getDepartment(int id){
        return null;
    }

    public Section getSection(int id, String courseId){
        return null;
    }

    public Professor getProfessor(int id){
        return null;
    }


    public Classroom[] getClassrooms(int count){
        return null;
    }

    public Course[] getCourses(int count){
        return null;
    }

    public User[] getUsers(int count){
        return null;
    }

    public Department[] getDepartments(){
        return null;
    }

    public Section[] getSections(String courseId){
        return null;
    }

    public Professor[] getProfessors(int count){
        return null;
    }

    public String[] getCredentials(String email){
        return new String[]{"0", ""};
    }

}
