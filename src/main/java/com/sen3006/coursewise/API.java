package com.sen3006.coursewise;
import com.google.gson.*;
import com.sen3006.coursewise.enums.Campus;
import com.sen3006.coursewise.json.PearowsGson;
import com.sen3006.coursewise.json.UserPassword;
import com.sen3006.coursewise.models.*;

import java.io.*;
import java.lang.reflect.Type;
import java.nio.charset.StandardCharsets;
import java.time.LocalTime;
import java.util.ArrayList;
import java.util.Hashtable;
import java.util.Observable;
import java.util.Observer;

// API class for handling all the data
// TODO: Add reviews
public class API implements Observer {
    private static API instance;
    private final Gson gson;

    private Hashtable<String, Classroom> classrooms;
    private Hashtable<String, Course> courses;
    private Hashtable<Integer, UserPassword> users;
    private Hashtable<Integer, Department> departments;
    private Hashtable<Integer, Professor> professors;
    private Hashtable<Integer, Section> sections;
    private Hashtable<Integer, Review> reviews;
    private Hashtable<Integer, Rating> ratings;

    // Singleton pattern to ensure only one instance of API exists
    private API() {
        gson = PearowsGson.getGson();
        instance = this;
        loadAll();
    }

    public static API getInstance() {
        if (instance == null) {
            new API();
        }
        return instance;
    }

    @Deprecated
    private void fetchPseudoTables() {
        classrooms = new Hashtable<>();
        courses = new Hashtable<>();
        users = new Hashtable<>();
        departments = new Hashtable<>();
        professors = new Hashtable<>();
        sections = new Hashtable<>();
        int sectionC = 0, asyncC = 0, syncC = 0;
        try {
            for (CourseSection cSection : CourseSection.getAllCourseSections()) {
                // Create and add objects to the respective arrays
                // Use umis data to populate these objects
                sectionC++;

                if (!classrooms.containsKey(cSection.classroom_name)) {
                    classrooms.put(cSection.classroom_name, new Classroom(Campus.fromString(cSection.campus_name).getIntCampus(), cSection.classroom_name)); // Campus won't be found
                }

                if (!courses.containsKey(cSection.course_code)) {
                    int type = 0;
                    if (cSection.classroom_name.contentEquals("ITSLEARNING1")) {
                        type = 1;
                    }
                    courses.put(cSection.course_code, new Course(cSection.course_code, cSection.course_name, null, type));
                }

                if (!professors.containsKey(cSection.instructor_id)) {
                    professors.put(cSection.instructor_id, new Professor(cSection.instructor_id, cSection.instructor_name, cSection.instructor_surname, ""));
                }

                if (!sections.containsKey(cSection.section_id)
                ) {
                    int type = 0;
                    if(cSection.classroom_name.contentEquals("MS TEAMS")){
                        type = 1;
                    } else if(cSection.classroom_name.contentEquals("ITSLEARNING1")){
                        type = 2;
                    }
                    sections.put(getSectionPsuedoId(cSection.section, cSection.course_code), new Section(cSection.section, LocalTime.parse(cSection.start_time), LocalTime.parse(cSection.end_time), cSection.day - 1, getClassroom(cSection.classroom_name), getCourse(cSection.course_code), getProfessor(cSection.instructor_id), type));
                    syncC++;
                } else if (sections.containsKey(cSection.section_id)) {
                    //TODO: Find differences between duplicates
                    System.out.println("Section already exists: " + cSection.section + " " + cSection.course_code + " " + cSection.classroom_name);
                    Section dp = sections.get(cSection.section_id);
                    System.out.println("Is duplicate of: " + dp.getSection_id() + " " + dp.getCourse().getCourse_id() + " " + dp.getClassroom().getClass_id());
                    asyncC++;
                } else {
                    asyncC++;
                }

                //Extract department abbreviation
                String dep = cSection.course_code.replaceAll("[0-9]", "");
                if (!departments.containsKey(dep.hashCode())) {
                    departments.put(dep.hashCode(), new Department(dep.hashCode(), dep, null));
                }
            }
            // Get user data
            UserPassword[] userPasswords = gson.fromJson(new BufferedReader(new InputStreamReader(new FileInputStream("src/main/resources/users.json"), StandardCharsets.UTF_8)).readLine(), UserPassword[].class);
            for (UserPassword user : userPasswords) {
                if (!users.containsKey(user.getId())) {
                    users.put(user.getId(), user);
                }else {
                    System.out.println("User already exists: " + user.getId() + " " + user.getName() + " " + user.getSurname());
                }
            }

            reviews = new Hashtable<>();
            ratings = new Hashtable<>();
        } catch (IOException e) {
            //TODO: Add a proper error message
            e.printStackTrace();
        }
    }

    private int getSectionPsuedoId(int section_id, String course_id) {
        return (section_id + course_id.hashCode()) * (course_id.hashCode() - section_id);
    }

    public int getRatingPsuedoId(int user_id, int professor_id) {
        return (user_id + professor_id) * (professor_id - user_id);
    }
    @Deprecated
    private void saveAll() {
        Hashtable[] tables = {classrooms, courses, departments, professors, sections};
        String[] names = {"classrooms", "courses", "departments", "professors", "sections"};

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

    private void syncAll(){
        syncClassrooms();
        syncCourses();
        syncUsers();
        syncDepartments();
        syncProfessors();
        syncSections();
        syncReviews();
        syncRatings();
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

    public void syncReviews(){
        try {
            BufferedWriter writer = new BufferedWriter(new FileWriter("src/main/resources/reviews.json"));
            Review[] table = new Review[reviews.size()];

            reviews.values().toArray(table);

            String json = gson.toJson(table, Review[].class);
            writer.write(json);
            writer.flush();
            writer.close();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public void syncRatings(){
        try {
            BufferedWriter writer = new BufferedWriter(new FileWriter("src/main/resources/ratings.json"));
            Rating[] table = new Rating[ratings.size()];

            ratings.values().toArray(table);

            String json = gson.toJson(table, Rating[].class);
            writer.write(json);
            writer.flush();
            writer.close();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    //TODO: Add a base model class and collapse all the sync methods into one

    private void loadAll() {
        try {
            Classroom[] classrooms;
            Course[] courses;
            UserPassword[] users;
            Department[] departments;
            Professor[] professors;
            Section[] sections;
            Review[] reviews;
            Rating[] ratings;

            classrooms = gson.fromJson(new BufferedReader(new InputStreamReader(new FileInputStream("src/main/resources/classrooms.json"), StandardCharsets.UTF_8)).readLine(), Classroom[].class);
            this.classrooms = new Hashtable<>();
            for (Classroom classroom : classrooms) {
                this.classrooms.put(classroom.getClass_id(), classroom);
            }

            courses = gson.fromJson(new BufferedReader(new InputStreamReader(new FileInputStream("src/main/resources/courses.json"), StandardCharsets.UTF_8)).readLine(), Course[].class);
            this.courses = new Hashtable<>();
            for (Course course : courses) {
                this.courses.put(course.getCourse_id(), course);
            }

            users = gson.fromJson(new BufferedReader(new InputStreamReader(new FileInputStream("src/main/resources/users.json"), StandardCharsets.UTF_8)).readLine(), UserPassword[].class);
            this.users = new Hashtable<>();
            for (UserPassword user : users) {
                this.users.put(user.getId(), user);
            }

            departments = gson.fromJson(new BufferedReader(new InputStreamReader(new FileInputStream("src/main/resources/departments.json"), StandardCharsets.UTF_8)).readLine(), Department[].class);
            this.departments = new Hashtable<>();
            for (Department department : departments) {
                this.departments.put(department.getDepartment_id(), department);
            }

            professors = gson.fromJson(new BufferedReader(new InputStreamReader(new FileInputStream("src/main/resources/professors.json"), StandardCharsets.UTF_8)).readLine(), Professor[].class);
            this.professors = new Hashtable<>();
            for (Professor professor : professors) {
                this.professors.put(professor.getId(), professor);
            }

            sections = gson.fromJson(new BufferedReader(new InputStreamReader(new FileInputStream("src/main/resources/sections.json"), StandardCharsets.UTF_8)).readLine(), Section[].class);
            this.sections = new Hashtable<>();
            for (Section section : sections) {
                this.sections.put(getSectionPsuedoId(section.getSection_id(), section.getCourse().getCourse_id()), section);
            }

            reviews = gson.fromJson(new BufferedReader(new InputStreamReader(new FileInputStream("src/main/resources/reviews.json"), StandardCharsets.UTF_8)).readLine(), Review[].class);
            this.reviews = new Hashtable<>();
            for (Review review : reviews) {
                this.reviews.put(getSectionPsuedoId(review.getUser().getId(), review.getCourse().getCourse_id()), review);
            }

            ratings = gson.fromJson(new BufferedReader(new InputStreamReader(new FileInputStream("src/main/resources/ratings.json"), StandardCharsets.UTF_8)).readLine(), Rating[].class);
            this.ratings = new Hashtable<>();
            for (Rating rating : ratings) {
                this.ratings.put(getRatingPsuedoId(rating.getUser().getId(), rating.getProfessor().getId()), rating);
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

    public Professor getProfessor(int id) {
        return professors.get(id);
    }

    public Section getSection(int id, String courseId) {
        return sections.get(getSectionPsuedoId(id, courseId));
    }

    public Review getReview(int user_id, String courseId) {
        return reviews.get(user_id + courseId.hashCode());
    }

    public Rating getRating(int user_id, String courseId) {
        return ratings.get(user_id + courseId.hashCode());
    }

    public boolean addReview(User user, Course course, String comment, int rating) {
        int id = getSectionPsuedoId(user.getId(), course.getCourse_id());
        Review review;
        if (reviews.containsKey(id)) {
            review = reviews.get(id);
            review.setComment(comment);
            review.setRating(rating);
        }else {
            review = new Review(course, comment, user, rating);
        }
        reviews.put(id, review);
        syncReviews();
        return true;
    }
    public boolean addReview(User user, Course course, int rating) {
        return addReview(user, course, "", rating);
    }

    public boolean addRating(User user, Professor professor, int rating) {
        int id = getRatingPsuedoId(user.getId(), professor.getId());
        Rating ratingObj;
        if (ratings.containsKey(id)) {
            ratingObj = ratings.get(id);
            ratingObj.setRating(rating);
        }else {
            ratingObj = new Rating(professor, user, rating);
        }
        ratings.put(id, ratingObj);
        syncRatings();
        return true;
    }


    //TODO: Add pagination support
    public Classroom[] getClassrooms() {
        Classroom[] result = new Classroom[classrooms.size()];
        classrooms.values().toArray(result);
        return result;
    }

    public Course[] getCourses() {
        Course[] result = new Course[courses.size()];
        courses.values().toArray(result);
        return result;
    }

    public User[] getUsers() {
        User[] result = new User[users.size()];
        users.values().toArray(result);
        return result;
    }

    public Department[] getDepartments() {
        Department[] result = new Department[departments.size()];
        departments.values().toArray(result);
        return result;
    }

    public Professor[] getProfessors() {
        Professor[] result = new Professor[professors.size()];
        professors.values().toArray(result);
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

    public Review[] getReviews(String courseId) {
        ArrayList<Review> results = new ArrayList<>();
        for (Review review : reviews.values()) {
            if (review.getCourse().getCourse_id().contentEquals(courseId)) {
                results.add(review);
            }
        }
        Review[] result = new Review[results.size()];
        results.toArray(result);
        return result;
    }

    public Review[] getReviews(int user_id) {
        ArrayList<Review> results = new ArrayList<>();
        for (Review review : reviews.values()) {
            if (review.getUser().getId() == user_id) {
                results.add(review);
            }
        }
        Review[] result = new Review[results.size()];
        results.toArray(result);
        return result;
    }

    public Rating[] getRatings(int professorId) {
        ArrayList<Rating> results = new ArrayList<>();
        for (Rating rating : ratings.values()) {
            if (rating.getProfessor().getId() == professorId) {
                results.add(rating);
            }
        }
        Rating[] result = new Rating[results.size()];
        results.toArray(result);
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
        //TODO: Fix professor triggering if
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
        if (o instanceof Review) {
            Review review = (Review) o;
            if (!reviews.containsValue(review)) {
                System.out.println("Review not found in API");
            }
            syncReviews();
        }
    }

    public static void main(String[] args) throws IOException {
        API api = API.getInstance();
        api.fetchPseudoTables();
        api.syncAll();

        System.out.println("OK");
//        User user1 = api.getUser(2200900);
//        User user2 = api.getUser(2200870);
//        Course course = api.getCourse("PHY1001");
//        Professor professor1 = api.getProfessors()[25];
//        Professor professor2 = api.getProfessors()[31];


//        for (Course c : api.getCourses()) {
//            if (c.getRatingCount() > 0) {
//                System.out.println(c.getCourse_id() + " " + c.getAvgRating() + " there are " + c.getRatingCount() + " reviews");
//            }
//        }
//
//        for (Professor professor: api.getProfessors()) {
//            if (professor.getRatingCount() > 0) {
//                System.out.println(professor.getName() + " " + professor.getAvgRating() + " there are " + professor.getRatingCount() + " ratings");
//            }
//        }
    }
}

// Object for handling umis data
@Deprecated
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