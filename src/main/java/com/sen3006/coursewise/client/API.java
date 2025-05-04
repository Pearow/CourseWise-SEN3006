package com.sen3006.coursewise.client;

import com.google.gson.Gson;
import com.google.gson.JsonElement;
import com.google.gson.JsonObject;
import com.sen3006.coursewise.client.enums.Campus;
import com.sen3006.coursewise.client.enums.Semester;
import com.sen3006.coursewise.client.json.PearowsGson;
import com.sen3006.coursewise.client.json.UserPassword;
import com.sen3006.coursewise.client.models.*;

import java.io.*;
import java.net.*;
import java.net.http.HttpClient;
import java.net.http.HttpRequest;
import java.net.http.HttpResponse;
import java.nio.charset.StandardCharsets;
import java.time.LocalTime;
import java.util.*;
import java.util.stream.Collectors;

// API class for handling all the data
public class API implements Observer {
    private static API instance;
    private final Gson gson;
    private HttpClient httpClient;
    private String host = "http://localhost:3006/api";

    //TODO: Add cache
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
        httpClient = HttpClient.newHttpClient();
        instance = this;
//        loadAll();
    }

    public static API getInstance() {
        if (instance == null) {
            new API();
        }
        return instance;
    }

    private String sendGetRequest(String urlString) {
        try{
            URI uri = new URI(urlString);
            // Create request
            HttpRequest request = HttpRequest.newBuilder()
                    .uri(uri)
                    .header("Accept", "application/json")
                    .header("Content-Type", "application/json")
                    .GET()
                    .build();
            // Send request
            HttpResponse<String> response = httpClient.send(request, HttpResponse.BodyHandlers.ofString());

            if(response.statusCode() == 200) {
                return response.body();
            }else {
                System.out.println("Error: " + response.statusCode() + " " + response.body());
                return null;
            }
        }catch (ConnectException e){
            System.out.println("No connection to server");
            System.out.println(e.toString());
            return null;
        }
        catch (IOException | URISyntaxException | InterruptedException e) {
            e.printStackTrace();
            return null;
        }
    }

    private String sendPostRequest(String urlString, JsonObject jsonObject) {
        try {
            HttpRequest request = HttpRequest.newBuilder()
                    .uri(URI.create(urlString))
                    .header("Content-Type", "application/json")
                    .POST(HttpRequest.BodyPublishers.ofString(jsonObject.toString()))
                    .build();
            HttpResponse<String> response = httpClient.send(request, HttpResponse.BodyHandlers.ofString());
            return response.body();
        } catch (IOException | InterruptedException e) {
            throw new RuntimeException(e);
        }
    }

    private String sendPutRequest(String urlString, JsonObject jsonObject) {
        try {
            HttpRequest request = HttpRequest.newBuilder()
                    .uri(URI.create(urlString))
                    .header("Content-Type", "application/json")
                    .PUT(HttpRequest.BodyPublishers.ofString(jsonObject.toString()))
                    .build();
            HttpResponse<String> response = httpClient.send(request, HttpResponse.BodyHandlers.ofString());
            return response.body();
        } catch (IOException | InterruptedException e) {
            throw new RuntimeException(e);
        }
    }

    private String sendGetRequest(String urlString, Map<String, String> parameters){
        try{
            // Build query string by URL-encoding the parameters
            String queryString = parameters.entrySet().stream()
                    .map(entry -> URLEncoder.encode(entry.getKey(), StandardCharsets.UTF_8) + "=" +
                            URLEncoder.encode(entry.getValue(), StandardCharsets.UTF_8))
                    .collect(Collectors.joining("&"));

            // Create request
            HttpRequest request = HttpRequest.newBuilder()
                    .uri(URI.create(host + urlString + "?" + queryString))
                    .header("Accept", "application/json")
                    .header("Content-Type", "application/json")
                    .GET()
                    .build();
            // Send request
            HttpResponse<String> response = httpClient.send(request, HttpResponse.BodyHandlers.ofString());

            if(response.statusCode() == 200) {
                return response.body();
            }else {
                System.out.println("Error: " + response.statusCode() + " " + response.body());
                return null;
            }
        }catch (ConnectException e){
            System.out.println("No connection to server");
            System.out.println(e.toString());
            return null;
        }
        catch (IOException | InterruptedException e) {
            e.printStackTrace();
            return null;
        }
    }

    private String removeIllegal(String input) {
        return input.replaceAll("[^a-zA-Z0-9]", "-");
    }

    @Deprecated
    private void fetchPseudoTables() {
        classrooms = new Hashtable<>();
        courses = new Hashtable<>();
        users = new Hashtable<>();
        departments = new Hashtable<>();
        professors = new Hashtable<>();
        sections = new Hashtable<>();
        try {
            int i = 0;
            int a = 66;
            for (CourseSection cSection : CourseSection.getAllCourseSections()) {
                System.out.println(i++ + "/"+ CourseSection.getAllCourseSections().length);
                // Create and add objects to the respective arrays
                // Use umis data to populate these objects
                if (cSection.semester_id == 102){
                    System.out.println("Skipping " + cSection.course_code + " " + cSection.section);
                    continue;
                }

                if (!classrooms.containsKey(cSection.classroom_name)) {
                    classrooms.put(cSection.classroom_name, new Classroom(Campus.fromString(cSection.campus_name).getIntCampus(), removeIllegal(cSection.classroom_name))); // Campus won't be found
                    sendPostRequest(host + "/classroom", gson.toJsonTree(new Classroom(Campus.fromString(cSection.campus_name).getIntCampus(), removeIllegal(cSection.classroom_name))).getAsJsonObject());
                }

                //Extract department abbreviation
                String dep = cSection.course_code.replaceAll("[0-9]", "");
                if (!departments.containsKey(dep.hashCode())) {
                    departments.put(dep.hashCode(), new Department(a, dep, null));
                    sendPostRequest(host + "/department", gson.toJsonTree(new Department(a++, dep, null)).getAsJsonObject());
                }
                if (!courses.containsKey(cSection.course_code)) {
                    int type = 0;
                    if (cSection.classroom_name.contentEquals("ITSLEARNING1")) {
                        type = 1;
                    }
                    courses.put(cSection.course_code, new Course(cSection.course_code, cSection.course_name, null, type));
                    sendPostRequest(host + "/course", gson.toJsonTree(new Course(cSection.course_code, cSection.course_name, null, type)).getAsJsonObject());
                }

                if (!professors.containsKey(cSection.instructor_id)) {
                    professors.put(cSection.instructor_id, new Professor(cSection.instructor_id, cSection.instructor_name, cSection.instructor_surname, ""));
                    sendPostRequest(host + "/professor", gson.toJsonTree(new Professor(cSection.instructor_id, cSection.instructor_name, cSection.instructor_surname, "")).getAsJsonObject());
                }

                if (!sections.containsKey(cSection.section_id)
                ) {
                    int type = 0;
                    if (cSection.classroom_name.contentEquals("MS TEAMS")) {
                        type = 1;
                    } else if (cSection.classroom_name.contentEquals("ITSLEARNING1")) {
                        type = 2;
                    }
                    sections.put(getSectionPsuedoId(cSection.section, cSection.course_code), new Section(cSection.id, cSection.section, LocalTime.parse(cSection.start_time), LocalTime.parse(cSection.end_time), cSection.day - 1, classrooms.get(cSection.classroom_name), courses.get(cSection.course_code), professors.get(cSection.instructor_id), type, cSection.semester_id == 103?0:1));
                    sendPostRequest(host + "/section", gson.toJsonTree(new Section(cSection.id, cSection.section, LocalTime.parse(cSection.start_time), LocalTime.parse(cSection.end_time), cSection.day - 1, classrooms.get(cSection.classroom_name), courses.get(cSection.course_code), professors.get(cSection.instructor_id), type, cSection.semester_id == 103?0:1)).getAsJsonObject());
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

    public void syncClassroom(Classroom classroom) {
        JsonObject json = gson.fromJson("{\"data\": " + gson.toJson(classroom, Classroom.class) + "}", JsonObject.class);
        JsonObject response = gson.fromJson(sendPutRequest(host + "/classroom/" + classroom.getClass_id(), json), JsonObject.class);

        if (response.get("status").getAsString().contentEquals("success")) {
            System.out.println("Classroom updated successfully");
        } else {
            System.out.println("Error: " + response.get("status").toString() + " " + response.get("message").getAsString());
        }
    }

    public void syncCourse(Course course) {
        JsonObject json = gson.fromJson("{\"data\": " + gson.toJson(course, Course.class) + "}", JsonObject.class);
        JsonObject response = gson.fromJson(sendPutRequest(host + "/course/" + course.getCourse_id(), json), JsonObject.class);

        if (response.get("status").getAsString().contentEquals("success")) {
            System.out.println("User updated successfully");
        } else {
            System.out.println("Error: " + response.get("status").toString() + " " + response.get("message").getAsString());
        }
    }

    public void syncUser(UserPassword user) {
        JsonObject json = gson.fromJson("{\"data\": " + gson.toJson(user, UserPassword.class) + "}", JsonObject.class);
        JsonObject response = gson.fromJson(sendPutRequest(host + "/user/" + user.getId(), json), JsonObject.class);

        if (response.get("status").getAsString().contentEquals("success")) {
            System.out.println("User updated successfully");
        } else {
            System.out.println("Error: " + response.get("status").toString() + " " + response.get("message").getAsString());
        }
    }

    public void syncDepartment(Department department) {
        JsonObject json = gson.fromJson("{\"data\": " + gson.toJson(department, Department.class) + "}", JsonObject.class);
        JsonObject response = gson.fromJson(sendPutRequest(host + "/department/" + department.getDepartment_id(), json), JsonObject.class);

        if (response.get("status").getAsString().contentEquals("success")) {
            System.out.println("User updated successfully");
        } else {
            System.out.println("Error: " + response.get("status").toString() + " " + response.get("message").getAsString());
        }
    }

    public void syncProfessor(Professor professor) {
        JsonObject json = gson.fromJson("{\"data\": " + gson.toJson(professor, Professor.class) + "}", JsonObject.class);
        JsonObject response = gson.fromJson(sendPutRequest(host + "/professor/" + professor.getId(), json), JsonObject.class);

        if (response.get("status").getAsString().contentEquals("success")) {
            System.out.println("User updated successfully");
        } else {
            System.out.println("Error: " + response.get("status").toString() + " " + response.get("message").getAsString());
        }
    }
    public void syncSection(Section section) {
        JsonObject json = gson.fromJson("{\"data\": " + gson.toJson(section, Section.class) + "}", JsonObject.class);
        JsonObject response = gson.fromJson(sendPutRequest(host + "/section/" + section.getId(), json), JsonObject.class);


        if (response.get("status").getAsString().contentEquals("success")) {
            System.out.println("User updated successfully");
        } else {
            System.out.println("Error: " + response.get("status").toString() + " " + response.get("message").getAsString());
        }
    }

    public void syncReview(Review review) {
        JsonObject json = gson.fromJson("{\"data\": " + gson.toJson(review, Review.class) + "}", JsonObject.class);
        JsonObject response = gson.fromJson(sendPutRequest(host + "/review/" + review.getCourse().getCourse_id() + "/" + review.getUser().getId(), json), JsonObject.class);

        if (response.get("status").getAsString().contentEquals("success")) {
            System.out.println("User updated successfully");
        } else {
            System.out.println("Error: " + response.get("status").toString() + " " + response.get("message").getAsString());
        }
    }

    public void syncRating(Rating rating) {
        JsonObject json = gson.fromJson("{\"data\": " + gson.toJson(rating, Rating.class) + "}", JsonObject.class);
        JsonObject response = gson.fromJson(sendPutRequest(host + "/rating/" + rating.getProfessor().getId() + "/" + rating.getUser().getId(), json), JsonObject.class);

        if (response.get("status").getAsString().contentEquals("success")) {
            System.out.println("User updated successfully");
        } else {
            System.out.println("Error: " + response.get("status").toString() + " " + response.get("message").getAsString());
        }
    }

    //TODO: Add a base model class and collapse all the sync methods into one

    @Deprecated
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
        return gson.fromJson(gson.fromJson(sendGetRequest(host + "/classroom/" + id), JsonElement.class)
                .getAsJsonObject().get("data"), Classroom.class);
    }

    public Course getCourse(String id) {
        return gson.fromJson(gson.fromJson(sendGetRequest(host + "/course/" + id), JsonElement.class)
                .getAsJsonObject().get("data"), Course.class);
    }

    public User getUser(int id) {
        return gson.fromJson(gson.fromJson(sendGetRequest(host + "/user/" + id), JsonElement.class)
                .getAsJsonObject().get("data"), UserPassword.class);
    }

    public Department getDepartment(int id) {
        return gson.fromJson(gson.fromJson(sendGetRequest(host + "/department/" + id), JsonElement.class)
                .getAsJsonObject().get("data"), Department.class);
    }

    public Professor getProfessor(int id) {
        return gson.fromJson(gson.fromJson(sendGetRequest(host + "/professor/" + id), JsonElement.class)
                .getAsJsonObject().get("data"), Professor.class);
    }

    //TODO: Add getSection
//    public Section getSection(int id, String courseId) {
//        return gson.fromJson(gson.fromJson(sendGetRequest(host + "/section/" + courseId + "/" + id), JsonElement.class)
//                .getAsJsonObject().get("data"), Section.class);
//    }

    public Review getReview(int user_id, String courseId) {
        return gson.fromJson(gson.fromJson(sendGetRequest(host + "/review/" + courseId + "/" + user_id), JsonElement.class)
                .getAsJsonObject().get("data"), Review.class);
    }

    public Rating getRating(int user_id, int professor_id) {
        return gson.fromJson(gson.fromJson(sendGetRequest(host + "/rating/" + professor_id + "/" + user_id), JsonObject.class).get("data"), Rating.class);
    }

    public boolean addReview(User user, Course course, String comment, int rating) {
        Review review;
        if ((review = getReview(user.getId(), course.getCourse_id())) != null) {
            review.setComment(comment);
            review.setRating(rating);
//            syncReview(review);//TODO: Return response or boolean
            return true;
        }else {
            review = new Review(course, comment, user, rating);
            String response = sendPostRequest(host + "/review", gson.toJsonTree(review).getAsJsonObject());
            JsonObject jsonResponse = gson.fromJson(response, JsonObject.class);
            if (jsonResponse.get("status").getAsString().contentEquals("success")) {
                System.out.println("Review added successfully");
            } else {
                System.out.println("Error: " + jsonResponse.get("status").toString() + " " + jsonResponse.get("message").getAsString());
                return false;
            }
            return true;
        }
    }
    public boolean addReview(User user, Course course, int rating) {
        return addReview(user, course, "", rating);
    }

    public boolean addRating(User user, Professor professor, int rating) {
        Rating ratingObj;
        String response;
        if ((ratingObj = getRating(user.getId(), professor.getId())) != null) {
            ratingObj.setRating(rating);
//            syncRating(ratingObj);//TODO: Return response or boolean
            return true;
        }else {
            ratingObj = new Rating(professor, user, rating);
            response = sendPostRequest(host + "/rating", gson.toJsonTree(ratingObj).getAsJsonObject());
        }
        JsonObject jsonResponse = gson.fromJson(response, JsonObject.class);
        if (jsonResponse.get("status").getAsString().contentEquals("success")) {
            System.out.println("Rating added successfully");
        } else {
            System.out.println("Error: " + jsonResponse.get("status").toString() + " " + jsonResponse.get("message").getAsString());
            return false;
        }
        return true;
    }


    //TODO: Add pagination support
    public Classroom[] getClassrooms() {
        return gson.fromJson(gson.fromJson(sendGetRequest(host + "/classroom"), JsonObject.class)
                .get("data"), Classroom[].class);
    }

    public Course[] getCourses(Semester semester) {
        return gson.fromJson(gson.fromJson(sendGetRequest(host + "/courses/" + semester.getIntSemester()), JsonObject.class)
                .get("data"), Course[].class);
    }

    public User[] getUsers() {
        return gson.fromJson(gson.fromJson(sendGetRequest(host + "/user"), JsonObject.class)
                .get("data"), UserPassword[].class);
    }

    public Department[] getDepartments() {
        return gson.fromJson(gson.fromJson(sendGetRequest(host + "/department"), JsonObject.class)
                .get("data"), Department[].class);
    }

    public Professor[] getProfessors() {
        return gson.fromJson(gson.fromJson(sendGetRequest(host + "/professor"), JsonObject.class)
                .get("data"), Professor[].class);
    }

    public Section[] getSections(String courseId, Semester semester) {
        return gson.fromJson(gson.fromJson(sendGetRequest(host + "/section/" + courseId + "/" + semester.getIntSemester()), JsonObject.class)
                .get("data"), Section[].class);
    }

    public Review[] getReviews(String courseId) {
        return gson.fromJson(gson.fromJson(sendGetRequest(host + "/review/" + courseId), JsonObject.class)
                .get("data"), Review[].class);
    }

    public Review[] getReviews(int user_id) {
        //TODO: Add getting review from user_id
        return new Review[0];
    }

    public Rating[] getRatings(int professorId) {
        return gson.fromJson(gson.fromJson(sendGetRequest(host + "/rating/" + professorId), JsonObject.class)
                .get("data"), Rating[].class);
    }

    public String[] getCredentials(String email) {
        String[] result = {"", ""}; // id, password TODO: Use a class instead
        JsonObject json = gson.fromJson(sendPostRequest(host + "/login", gson.fromJson("{\"email\": \"" + email + "\"}", JsonObject.class)), JsonObject.class);
        if(json.get("status").getAsString().contentEquals("success")) {
            JsonObject user = gson.fromJson(json.get("data"), JsonObject.class);
            result[1] = user.get("password").getAsString();
            result[0] = user.get("id").getAsString();
        }
        return result;
    }

    public void update(Observable o, Object arg) {
        if (o instanceof Classroom) {
            Classroom classroom = (Classroom) o;
            syncClassroom(classroom);
        }
        if (o instanceof Course) {
            Course course = (Course) o;
            syncCourse(course);
        }
        //TODO: Fix professor triggering if
        if (o instanceof UserPassword user) {
            syncUser(user);
        }
        if (o instanceof Department department) {
            syncDepartment(department);
        }
        if (o instanceof Section section) {
            syncSection(section);
        }
        if (o instanceof Professor professor) {
            syncProfessor(professor);
        }
        if (o instanceof Review review) {
            syncReview(review);
        }
        if(o instanceof Rating rating) {
            syncRating(rating);
        }
    }

    public static void main(String[] args) throws IOException {
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