package com.sen3006.coursewise;
import com.google.gson.*;
import com.sen3006.coursewise.enums.Campus;
import com.sen3006.coursewise.models.*;

import java.io.*;
import java.lang.reflect.Type;
import java.nio.charset.StandardCharsets;
import java.time.Duration;
import java.time.LocalTime;
import java.util.ArrayList;
import java.util.Hashtable;

// API class for handling all the data
// TODO: Add observer pattern to all models
public class API {
    private static API instance;

    private Hashtable<String, Classroom> classrooms;
    private Hashtable<String, Course> courses;
    private Hashtable<Integer, UserPassword> users;
    private Hashtable<Integer, Department> departments;
    private Hashtable<Integer, Section> sections;
    private Hashtable<Integer, Professor> professors;

    // Singleton pattern to ensure only one instance of API exists
    private API() {
        loadAll();
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

                if (!classrooms.containsKey(cSection.classroom_name) && !cSection.classroom_name.contentEquals("MS TEAMS") && !cSection.classroom_name.contentEquals("ITSLEARNING1")) {
                    classrooms.put(cSection.classroom_name, new Classroom(Campus.fromString(cSection.campus_name).getIntCampus(), cSection.classroom_name)); // Campus won't be found
                }

                if (!courses.containsKey(cSection.course_code)) {
                    courses.put(cSection.course_code, new Course(cSection.course_code, cSection.course_name, null, 0));
                }

                if (!professors.containsKey(cSection.instructor_id)) {
                    professors.put(cSection.instructor_id, new Professor(cSection.instructor_id, cSection.instructor_name, cSection.instructor_surname, ""));
                }

                if (!sections.containsKey(cSection.section_id) && !cSection.classroom_name.contentEquals("ITSLEARNING1")) {
                    sections.put(cSection.section_id, new Section(cSection.section, LocalTime.parse(cSection.start_time), LocalTime.parse(cSection.end_time), cSection.day - 1, getClassroom(cSection.classroom_name), getCourse(cSection.course_code)));
                }

                //Extract department abbreviation
                String dep = cSection.course_code.replaceAll("[0-9]", "");
                if (!departments.containsKey(dep.hashCode())){
                    departments.put(dep.hashCode(), new Department(dep.hashCode(), dep, null));
                }
            }
        } catch (IOException e) {
            //TODO: Add a proper error message
            e.printStackTrace();
        }
    }

    //TODO: Add save class by class
    private void saveAll(){
        Hashtable[] tables = {classrooms, courses, departments, sections, professors};
        String[] names = {"classrooms", "courses", "departments", "sections", "professors"};

        GsonBuilder gsonBuilder = new GsonBuilder();
        gsonBuilder.registerTypeAdapter(LocalTime.class, new LocalTimeSerializer());
        gsonBuilder.registerTypeAdapter(Duration.class, new DurationSerializer());

        Gson gson = gsonBuilder.create();
        for (int i = 0; i < tables.length; i++) {
            try {
                FileOutputStream fos = new FileOutputStream("src/main/resources/" + names[i] + ".json");
                BufferedWriter writer = new BufferedWriter(new OutputStreamWriter(fos, StandardCharsets.UTF_8));

                Object[] table = new Object[tables[i].size()];
                tables[i].values().toArray(table);

                String json = gson.toJson(table, table.getClass());
                writer.write(json);
                writer.flush();
                writer.close();
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
    }

    private void loadAll(){
        Gson gson = new GsonBuilder().registerTypeAdapter(LocalTime.class, new LocalTimeDeserializer()).registerTypeAdapter(Duration.class, new DurationDeserializer()).create();
        try {
            Classroom[] classrooms;
            Course[] courses;
            UserPassword[] users;
            Department[] departments;
            Section[] sections;
            Professor[] professors;

            classrooms = gson.fromJson(new BufferedReader(new InputStreamReader(new FileInputStream("src/main/resources/classrooms.json"), StandardCharsets.UTF_8)).readLine(), Classroom[].class);
            courses = gson.fromJson(new BufferedReader(new InputStreamReader(new FileInputStream("src/main/resources/courses.json"), StandardCharsets.UTF_8)).readLine(), Course[].class);
            users = gson.fromJson(new BufferedReader(new InputStreamReader(new FileInputStream("src/main/resources/users.json"), StandardCharsets.UTF_8)).readLine(), UserPassword[].class);
            departments = gson.fromJson(new BufferedReader(new InputStreamReader(new FileInputStream("src/main/resources/departments.json"), StandardCharsets.UTF_8)).readLine(), Department[].class);
            sections = gson.fromJson(new BufferedReader(new InputStreamReader(new FileInputStream("src/main/resources/sections.json"), StandardCharsets.UTF_8)).readLine(), Section[].class);
            professors = gson.fromJson(new BufferedReader(new InputStreamReader(new FileInputStream("src/main/resources/professors.json"), StandardCharsets.UTF_8)).readLine(), Professor[].class);
            this.classrooms = new Hashtable<>();
            this.courses = new Hashtable<>();
            this.users = new Hashtable<>();
            this.departments = new Hashtable<>();
            this.sections = new Hashtable<>();
            this.professors = new Hashtable<>();

            for (Classroom classroom : classrooms) {
                this.classrooms.put(classroom.getClass_id(), classroom);
            }
            for (Course course : courses) {
                this.courses.put(course.getCourse_id(), course);
            }
            for (UserPassword user : users) {
                this.users.put(user.getId(), user);
            }
            for (Department department : departments) {
                this.departments.put(department.getDepartment_id(), department);
            }
            for (Section section : sections) {
                this.sections.put(section.getSection_id() + section.getCourse().getCourse_id().hashCode(), section);
            }
            for (Professor professor : professors) {
                this.professors.put(professor.getId(), professor);
            }

        }catch (IOException e){
            e.printStackTrace();
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

    //TODO: Fix
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
        String[] result = {"", ""}; // id, email TODO: Use a class instead
        try {
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
            return result;
        }
    }

    public static void main(String[] args) throws IOException{
        API api = API.getInstance();
        System.out.println("OK");
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

class UserPassword extends User{
    String password;

    public UserPassword(int id, String name, String surname, String email, String password) {
        super(id, name, surname, email);
        this.password = password;
    }

    public String getPassword() {
        return password;
    }
}

class LocalTimeSerializer implements JsonSerializer<LocalTime> {
    @Override
    public JsonElement serialize(LocalTime localTime, Type typeOfSrc, JsonSerializationContext context) {
        return new JsonPrimitive(localTime.toString());
    }
}

class LocalTimeDeserializer implements JsonDeserializer<LocalTime> {
    @Override
    public LocalTime deserialize(JsonElement json, Type typeOfT, JsonDeserializationContext context) throws JsonParseException {
        return LocalTime.parse(json.getAsString());
    }
}

class DurationSerializer implements JsonSerializer<Duration> {
    @Override
    public JsonElement serialize(Duration duration, Type typeOfSrc, JsonSerializationContext context) {
        return new JsonPrimitive(duration.toString());
    }
}

class DurationDeserializer implements JsonDeserializer<Duration> {
    @Override
    public Duration deserialize(JsonElement json, Type typeOfT, JsonDeserializationContext context) throws JsonParseException {
        return Duration.parse(json.getAsString());
    }
}