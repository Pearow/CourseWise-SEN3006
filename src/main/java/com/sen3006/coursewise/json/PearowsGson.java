package com.sen3006.coursewise.json;


import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.sen3006.coursewise.models.*;

public class PearowsGson {
    private PearowsGson() {}
    public static Gson getGson() {
        return new GsonBuilder()
                .registerTypeAdapter(Classroom.class, new ClassroomAdapter())
                .registerTypeAdapter(Course.class, new CourseAdapter())
                .registerTypeAdapter(UserPassword.class, new UserAdapter())
                .registerTypeAdapter(Department.class, new DepartmentAdapter())
                .registerTypeAdapter(Professor.class, new ProfessorAdapter())
                .registerTypeAdapter(Section.class, new SectionAdapter())
                .registerTypeAdapter(Review.class, new ReviewAdapter())
                .create();
    }
}
