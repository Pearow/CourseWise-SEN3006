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
import java.util.Observable;
import java.util.Observer;

// API class for handling all the data
// TODO: Add observer pattern to all models
// TODO: Add reviews
public class API implements Observer {
    private static API instance;
    private final Gson gson;

    private Hashtable<String, Classroom> classrooms;
    private Hashtable<String, Course> courses;
    private Hashtable<Integer, UserPassword> users;
    private Hashtable<Integer, Department> departments;
    private Hashtable<Integer, Section> sections;
    private Hashtable<Integer, Professor> professors;

    // Singleton pattern to ensure only one instance of API exists
    private API() {
        gson = new GsonBuilder()
                .registerTypeAdapter(LocalTime.class, new LocalTimeAdapter())
                .registerTypeAdapter(Duration.class, new DurationAdapter())
                .registerTypeAdapter(Classroom.class, new ClassroomAdapter())
                .registerTypeAdapter(Course.class, new CourseAdapter())
                .registerTypeAdapter(UserPassword.class, new UserAdapter())
                .registerTypeAdapter(Department.class, new DepartmentAdapter())
                .registerTypeAdapter(Section.class, new SectionAdapter())
                .registerTypeAdapter(Professor.class, new ProfessorAdapter())
                .create();
        instance = this;
        loadAll();
    }

    public static API getInstance() {
        if (instance == null) {
            new API();
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
                if (!departments.containsKey(dep.hashCode())) {
                    departments.put(dep.hashCode(), new Department(dep.hashCode(), dep, null));
                }
            }
        } catch (IOException e) {
            //TODO: Add a proper error message
            e.printStackTrace();
        }
    }
    private void saveAll() {
        Hashtable[] tables = {classrooms, courses, departments, sections, professors};
        String[] names = {"classrooms", "courses", "departments", "sections", "professors"};

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

    public void syncClassrooms() {
        try {
            BufferedWriter writer = new BufferedWriter(new FileWriter("src/main/resources/classrooms.json"));
            Classroom[] table = new Classroom[classrooms.size()];
            classrooms.values().toArray(table);

            String json = gson.toJson(table, Classroom[].class);
            writer.write(json);
            writer.flush();
            writer.close();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public void syncCourses() {
        try {
            BufferedWriter writer = new BufferedWriter(new FileWriter("src/main/resources/courses.json"));
            Course[] table = new Course[courses.size()];
            courses.values().toArray(table);

            String json = gson.toJson(table, Course[].class);
            writer.write(json);
            writer.flush();
            writer.close();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public void syncUsers() {
        try {
            BufferedWriter writer = new BufferedWriter(new FileWriter("src/main/resources/users.json"));
            UserPassword[] table = new UserPassword[users.size()];
            users.values().toArray(table);

            String json = gson.toJson(table, UserPassword[].class);
            writer.write(json);
            writer.flush();
            writer.close();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public void syncDepartments() {
        try {
            BufferedWriter writer = new BufferedWriter(new FileWriter("src/main/resources/departments.json"));
            Department[] table = new Department[departments.size()];
            departments.values().toArray(table);

            String json = gson.toJson(table, Department[].class);
            writer.write(json);
            writer.flush();
            writer.close();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public void syncSections() {
        try {
            BufferedWriter writer = new BufferedWriter(new FileWriter("src/main/resources/sections.json"));
            Section[] table = new Section[sections.size()];

            sections.values().toArray(table);

            String json = gson.toJson(table, Section[].class);
            writer.write(json);
            writer.flush();
            writer.close();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public void syncProfessors() {
        try {
            BufferedWriter writer = new BufferedWriter(new FileWriter("src/main/resources/professors.json"));
            Professor[] table = new Professor[professors.size()];
            professors.values().toArray(table);

            String json = gson.toJson(table, Professor[].class);
            writer.write(json);
            writer.flush();
            writer.close();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    private void loadAll() {
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

        } catch (IOException e) {
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
        String[] result = {"", ""}; // id, password TODO: Use a class instead
        try {
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
                    result[1] = String.valueOf(user.getPassword());
                    break;
                }
            }
            return result;
        } catch (IOException e) {
            e.printStackTrace();
            return result;
        }
    }

    public void update(Observable o, Object arg) {
        if (o instanceof Classroom) {
            Classroom classroom = (Classroom) o;
            if (!classrooms.containsValue(classroom)) {
                System.out.println("Classroom not found in API");
            }
            syncClassrooms();
        }
        if (o instanceof Course) {
            Course course = (Course) o;
            if (!courses.containsValue(course)) {
                System.out.println("Course not found in API");
            }
            syncCourses();
        }
        if (o instanceof User) {
            User user = (User) o;
            if (!users.containsValue(user)) {
                System.out.println("User not found in API");
            }
            syncUsers();
        }
        if (o instanceof Department) {
            Department department = (Department) o;
            if (!departments.containsValue(department)) {
                System.out.println("Department not found in API");
            }
            syncDepartments();
        }
        if (o instanceof Section) {
            Section section = (Section) o;
            if (!sections.containsValue(section)) {
                System.out.println("Section not found in API");
            }
            syncSections();
        }
        if (o instanceof Professor) {
            Professor professor = (Professor) o;
            if (!professors.containsValue(professor)) {
                System.out.println("Professor not found in API");
            }
            syncProfessors();
        }
    }

    public static void main(String[] args) throws IOException {
        API api = API.getInstance();
//        api.fetchPseudoTables();
        System.out.println("OK");
    }
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

class LocalTimeAdapter implements JsonSerializer<LocalTime>, JsonDeserializer<LocalTime> {
    @Override
    public JsonElement serialize(LocalTime localTime, Type typeOfSrc, JsonSerializationContext context) {
        return new JsonPrimitive(localTime.toString());
    }

    @Override
    public LocalTime deserialize(JsonElement json, Type typeOfT, JsonDeserializationContext context) throws JsonParseException {
        return LocalTime.parse(json.getAsString());
    }
}

class DurationAdapter implements JsonSerializer<Duration>, JsonDeserializer<Duration> {
    @Override
    public JsonElement serialize(Duration duration, Type typeOfSrc, JsonSerializationContext context) {
        return new JsonPrimitive(duration.toString());
    }
    @Override
    public Duration deserialize(JsonElement json, Type typeOfT, JsonDeserializationContext context) throws JsonParseException {
        return Duration.parse(json.getAsString());
    }
}

class ClassroomAdapter implements JsonSerializer<Classroom>, JsonDeserializer<Classroom> {
    @Override
    public JsonElement serialize(Classroom classroom, Type typeOfSrc, JsonSerializationContext context) {
        JsonObject jsonObject = new JsonObject();
        jsonObject.addProperty("campus", classroom.getCampus().getIntCampus());
        jsonObject.addProperty("class_id", classroom.getClass_id());
        return jsonObject;
    }

    @Override
    public Classroom deserialize(JsonElement json, Type typeOfT, JsonDeserializationContext context) throws JsonParseException {
        JsonObject jsonObject = json.getAsJsonObject();
        return new Classroom(Campus.fromString(jsonObject.get("campus").getAsString()).getIntCampus(), jsonObject.get("class_id").getAsString());
    }
}
class CourseAdapter implements JsonSerializer<Course>, JsonDeserializer<Course> {
    @Override
    public JsonElement serialize(Course course, Type typeOfSrc, JsonSerializationContext context) {
        JsonObject jsonObject = new JsonObject();
        jsonObject.addProperty("course_id", course.getCourse_id());
        jsonObject.addProperty("course_name", course.getCourse_name());
        return jsonObject;
    }

    @Override
    public Course deserialize(JsonElement json, Type typeOfT, JsonDeserializationContext context) throws JsonParseException {
        JsonObject jsonObject = json.getAsJsonObject();
        return new Course(jsonObject.get("course_id").getAsString(), jsonObject.get("course_name").getAsString(), null, 0);
    }
}
class UserAdapter implements JsonSerializer<UserPassword>, JsonDeserializer<UserPassword> {
    @Override
    public JsonElement serialize(UserPassword user, Type typeOfSrc, JsonSerializationContext context) {
        JsonObject jsonObject = new JsonObject();
        jsonObject.addProperty("user_id", user.getId());
        jsonObject.addProperty("name", user.getName());
        jsonObject.addProperty("surname", user.getSurname());
        jsonObject.addProperty("email", user.getEmail());
        jsonObject.addProperty("password", user.getPassword());
        return jsonObject;
    }

    @Override
    public UserPassword deserialize(JsonElement json, Type typeOfT, JsonDeserializationContext context) throws JsonParseException {
        JsonObject jsonObject = json.getAsJsonObject();
        return new UserPassword(jsonObject.get("user_id").getAsInt(), jsonObject.get("name").getAsString(), jsonObject.get("surname").getAsString(), jsonObject.get("email").getAsString(), jsonObject.get("password").getAsString());
    }
}
class DepartmentAdapter implements JsonSerializer<Department>, JsonDeserializer<Department> {
    @Override
    public JsonElement serialize(Department department, Type typeOfSrc, JsonSerializationContext context) {
        JsonObject jsonObject = new JsonObject();
        jsonObject.addProperty("department_id", department.getDepartment_id());
        jsonObject.addProperty("department_name", department.getDepartment_name());
        return jsonObject;
    }

    @Override
    public Department deserialize(JsonElement json, Type typeOfT, JsonDeserializationContext context) throws JsonParseException {
        JsonObject jsonObject = json.getAsJsonObject();
        return new Department(jsonObject.get("department_id").getAsInt(), jsonObject.get("department_name").getAsString(), null);
    }
}
class SectionAdapter implements JsonSerializer<Section>, JsonDeserializer<Section> {
    @Override
    public JsonElement serialize(Section section, Type typeOfSrc, JsonSerializationContext context) {
        JsonObject jsonObject = new JsonObject();
        jsonObject.addProperty("section_id", section.getSection_id());
        jsonObject.addProperty("course_id", section.getCourse().getCourse_id());
        jsonObject.addProperty("classroom_name", section.getClassroom().getClass_id());
        return jsonObject;
    }

    @Override
    public Section deserialize(JsonElement json, Type typeOfT, JsonDeserializationContext context) throws JsonParseException {
        JsonObject jsonObject = json.getAsJsonObject();
        return new Section(0, LocalTime.now(), LocalTime.now(), 0, null, null);
    }
}
class ProfessorAdapter implements JsonSerializer<Professor>, JsonDeserializer<Professor> {
    @Override
    public JsonElement serialize(Professor professor, Type typeOfSrc, JsonSerializationContext context) {
        JsonObject jsonObject = new JsonObject();
        jsonObject.addProperty("prof_id", professor.getId());
        jsonObject.addProperty("name", professor.getName());
        jsonObject.addProperty("surname", professor.getSurname());
        return jsonObject;
    }

    @Override
    public Professor deserialize(JsonElement json, Type typeOfT, JsonDeserializationContext context) throws JsonParseException {
        JsonObject jsonObject = json.getAsJsonObject();
        return new Professor(jsonObject.get("prof_id").getAsInt(), jsonObject.get("name").getAsString(), jsonObject.get("surname").getAsString(), null);
    }
}