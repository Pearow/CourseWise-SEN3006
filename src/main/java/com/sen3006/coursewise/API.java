package com.sen3006.coursewise;
import com.google.gson.Gson;
import com.sen3006.coursewise.models.*;

import java.io.*;
import java.nio.charset.StandardCharsets;
import java.time.LocalTime;
import java.util.ArrayList;
import java.util.Hashtable;

// API class for handling all the data
public class API {
    private static API instance;

    private Hashtable<String, Classroom> classrooms;
    private Hashtable<String, Course> courses;
    private Hashtable<Integer, User> users;
    private Hashtable<Integer, Department> departments;
    private Hashtable<Integer, Section> sections;
    private Hashtable<Integer, Professor> professors;

    // Singleton pattern to ensure only one instance of API exists
    private API() {
        fetchPseudoTables();

    }

    public static API getInstance() {
        if (instance == null) {
            instance = new API();
        }
        return instance;
    }

    private void fetchPseudoTables() {
        classrooms = new Hashtable<>();
        courses = new Hashtable<>();
        users = new Hashtable<>();
        departments = new Hashtable<>();
        sections = new Hashtable<>();
        professors = new Hashtable<>();
        try {
            for (CourseSection cSection : CourseSection.getAllCourseSections()) {
                // Create and add objects to the respective arrays
                // Use umis data to populate these objects

                if (!classrooms.containsKey(cSection.classroom_name)) {
                    classrooms.put(cSection.classroom_name, new Classroom(Campus.fromString(cSection.campus_name).Campus(), cSection.classroom_name)); // Campus won't be found
                }

                if (!courses.containsKey(cSection.course_code)) {
                    courses.put(cSection.course_code, new Course(cSection.course_code, cSection.course_name, null, 0));
                }

                if (!professors.containsKey(cSection.instructor_id)) {
                    professors.put(cSection.instructor_id, new Professor(cSection.instructor_id, cSection.instructor_name, cSection.instructor_surname, ""));
                }

                if (!sections.containsKey(cSection.section_id)) {
                    sections.put(cSection.section_id, new Section(LocalTime.parse(cSection.start_time), LocalTime.parse(cSection.end_time), cSection.day, getClassroom(cSection.classroom_name), getCourse(cSection.course_code)));
                }
            }
        } catch (IOException e) {
            //TODO: Add a proper error message
            e.printStackTrace();
        }
    }

    private void fetchUsers() throws IOException {
        BufferedReader reader = new BufferedReader(new InputStreamReader(new FileInputStream("src/main/java/resources/users.json"), StandardCharsets.UTF_8));
        String line;
        StringBuilder sb = new StringBuilder();
        while ((line = reader.readLine()) != null) {
            sb.append(line);
        }
        String json = sb.toString();
        Gson gson = new Gson();
        User[] userArray = gson.fromJson(json, User[].class);
        for (User user : userArray) {
            users.put(user.getId(), user);
        }
    }

    public Classroom getClassroom(String id) {
        return classrooms.get(id);
    }

    public Course getCourse(String id) {
        return courses.get(id);
    }

    public User getUser(int id) {
        return users.get(id);
    }

    public Department getDepartment(int id) {
        return departments.get(id);
    }

    public Section getSection(int id, String courseId) {
        return sections.get(id);
    }

    public Professor getProfessor(int id) {
        return professors.get(id);
    }

    public Classroom[] getClassrooms(int count) {
        Classroom[] result = new Classroom[count];
        classrooms.values().toArray(result);
        return result;
    }

    public Course[] getCourses(int count) {
        Course[] result = new Course[count];
        courses.values().toArray(result);
        return result;
    }

    public User[] getUsers(int count) {
        User[] result = new User[count];
        users.values().toArray(result);
        return result;
    }

    public Department[] getDepartments() {
        Department[] result = new Department[departments.size()];
        departments.values().toArray(result);
        return result;
    }

    public Section[] getSections(String courseId) {
        ArrayList<Section> results = new ArrayList<>();
        for (Section section : sections.values()) {
            if (section.getCourse().getCourse_id().contentEquals(courseId)) {
                results.add(section);
            }
        }
        Section[] result = new Section[results.size()];
        results.toArray(result);
        return result;
    }

    public Professor[] getProfessors(int count) {
        Professor[] result = new Professor[count];
        professors.values().toArray(result);
        return result;
    }

    public String[] getCredentials(String email) {
        try {
            String[] result = new String[2];
            Gson gson = new Gson();
            BufferedReader reader = new BufferedReader(new InputStreamReader(new FileInputStream("src/main/resources/users.json"), StandardCharsets.UTF_8));
            String line;
            StringBuilder sb = new StringBuilder();
            while ((line = reader.readLine()) != null) {
                sb.append(line);
            }
            String json = sb.toString();
            UserPassword[] userArray = gson.fromJson(json, UserPassword[].class);
            for (UserPassword user : userArray) {
                if (user.getEmail().contentEquals(email)) {
                    result[0] = String.valueOf(user.getId());
                    result[1] = String.valueOf(user.getEmail());
                    break;
                }
            }
            return result;
        }catch (IOException e) {
            e.printStackTrace();
            return null;
        }
    }

    public static void main(String[] args) throws IOException{
        UserPassword[] users;
        Gson gson = new Gson();
        BufferedReader reader = new BufferedReader(new InputStreamReader(new FileInputStream("src/main/resources/users.json")));
        String line;
        StringBuilder sb = new StringBuilder();
        while ((line = reader.readLine()) != null) {
            sb.append(line);
        }
        String json = sb.toString();
        users = gson.fromJson(json, UserPassword[].class);
    }

    //Create json objects
//    public static void main(String[] args) throws IOException {
//        FileOutputStream fos = new FileOutputStream("src/main/resources/users.json");
//        BufferedWriter writer = new BufferedWriter(new OutputStreamWriter(fos, StandardCharsets.UTF_8));
//        Gson gson = new Gson();
//        UserPassword[] users = new UserPassword[3];
//        users[0] = new UserPassword(2200900, "Salim Mert", "Uçar", "salim.ucar@bahcesehir.edu.tr", String.valueOf("123".hashCode()));
//        users[1] = new UserPassword(2200780, "Azizcan", "Tam", "azizcan.tam@bahcesehir.edu.tr", String.valueOf("223".hashCode()));
//        users[2] = new UserPassword(2200870, "Murat Kerem", "Serter", "murat.serter@bahcesehir.edu.tr", String.valueOf("323".hashCode()));
//
//        String json = gson.toJson(users, User[].class);
//        writer.write(json);
//        writer.flush();
//        writer.close();
//    }
}

// Object for handling umis data
class CourseSection {
    public int id;
    public int section_id;
    public int semester_id;
    public String academic_year;
    public String semester_name;
    public String semester_name_en;
    public int course_id;
    public String course_code;
    public String course_name;
    public int section;
    public int instructor_id;
    public int classroom_id;
    public int day;
    public String day_name;
    public String day_name_en;
    public String start_time;
    public String end_time;
    public String instructor_name;
    public String instructor_surname;
    public String classroom_name;
    public String building_name;
    public String building_name_en;
    public String campus_name;
    public String campus_name_en;
    public int building_id;
    public int campus_id;

    CourseSection() {
    }

    CourseSection(int id, int section_id, int semester_id, String academic_year, String semester_name, String semester_name_en, int course_id, String course_code, String course_name, int section, int instructor_id, int classroom_id, int day, String day_name, String day_name_en, String start_time, String end_time, String instructor_name, String instructor_surname, String classroom_name, String building_name, String building_name_en, String campus_name, String campus_name_en, int building_id, int campus_id) {
        this.id = id;
        this.section_id = section_id;
        this.semester_id = semester_id;
        this.academic_year = academic_year;
        this.semester_name = semester_name;
        this.semester_name_en = semester_name_en;
        this.course_id = course_id;
        this.course_code = course_code;
        this.course_name = course_name;
        this.section = section;
        this.instructor_id = instructor_id;
        this.classroom_id = classroom_id;
        this.day = day;
        this.day_name = day_name;
        this.day_name_en = day_name_en;
        this.start_time = start_time;
        this.end_time = end_time;
        this.instructor_name = instructor_name;
        this.instructor_surname = instructor_surname;
        this.classroom_name = classroom_name;
        this.building_name = building_name;
        this.building_name_en = building_name_en;
        this.campus_name = campus_name;
        this.campus_name_en = campus_name_en;
        this.building_id = building_id;
        this.campus_id = campus_id;
    }

    static CourseSection[] getAllCourseSections(String fileName) throws IOException {
        Gson gson = new Gson();
        BufferedReader br = new BufferedReader(new InputStreamReader(new FileInputStream(fileName), StandardCharsets.UTF_8));
        StringBuilder sb = new StringBuilder();
        String line;
        while ((line = br.readLine()) != null) {
            sb.append(line);
        }
        String json = sb.toString();
        return gson.fromJson(json, CourseSection[].class);
    }
    static CourseSection[] getAllCourseSections() throws IOException {
        return getAllCourseSections("src/main/resources/all_course_schedules.json");
    }
}

class UserPassword {
    int id;
    String name;
    String surname;
    String email;
    String password;

    public UserPassword(int id, String name, String surname, String email, String password) {
        this.id = id;
        this.name = name;
        this.surname = surname;
        this.email = email;
        this.password = password;
    }

    public UserPassword(int id, String email, String password) {
        this.id = id;
        this.email = email;
        this.password = password;
    }

    public int getId() {
        return id;
    }

    public String getName() {
        return name;
    }

    public String getSurname() {
        return surname;
    }

    public String getEmail() {
        return email;
    }

    public String getPassword() {
        return password;
    }
}

