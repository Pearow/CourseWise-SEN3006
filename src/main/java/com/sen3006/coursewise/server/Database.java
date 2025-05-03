package com.sen3006.coursewise.server;

import com.google.gson.Gson;
import com.google.gson.JsonElement;
import com.google.gson.JsonObject;

import java.sql.*;
import java.util.ArrayList;

public class Database {
    private Connection conn;
    private final Gson gson = new Gson();
    private static Database instance;

    private Database() {
        connect();
        instance = this;
    }

    public static Database getInstance() {
        if (instance == null) {
            new Database();
        }
        return instance;
    }


    public boolean connect() {
        String url = "jdbc:postgresql://172.30.106.119:5432/coursewise?currentSchema=wise";
        String user = "coursewise";
        String password = "coursewise";

        conn = null;

        try {
            // Establishing a connection
            conn = DriverManager.getConnection(url, user, password);
            System.out.println("Connection to the database established successfully.");
            return true;
        } catch (SQLException e) {
            System.out.println("Connection to the database failed.");
            e.printStackTrace();
            return false;
        }
    }

    // Fetching data from the database
    public JsonElement fetchClassrooms(){
        String query = "SELECT * FROM wise.classroom";
        try (Statement statement = conn.createStatement()) {
            ResultSet resultSet = statement.executeQuery(query);
            ArrayList<JsonObject> classrooms = new ArrayList<>();

            while (resultSet.next()) {
                JsonObject jsonObject = new JsonObject();
                jsonObject.addProperty("id", resultSet.getString("id"));
                jsonObject.addProperty("name", resultSet.getString("name"));
                int campus = resultSet.getInt("campus");
                if (!resultSet.wasNull())
                    jsonObject.addProperty("campus", campus);

                classrooms.add(jsonObject);
            }
            return gson.toJsonTree(classrooms);
        } catch (SQLException e) {
            System.out.println("Error fetching classrooms.");
            e.printStackTrace();
            return null;
        }
    }
    public JsonElement fetchCourses(){
        String query = "SELECT * FROM wise.course order by rating_count desc, total_rating desc, id";
        try (Statement statement = conn.createStatement()) {
            ResultSet resultSet = statement.executeQuery(query);
            ArrayList<JsonObject> courses = new ArrayList<>();

            while (resultSet.next()) {
                JsonObject jsonObject = new JsonObject();
                jsonObject.addProperty("id", resultSet.getString("id"));
                jsonObject.addProperty("name", resultSet.getString("name"));
                int dep = resultSet.getInt("department_id");
                if (!resultSet.wasNull())
                    jsonObject.addProperty("department_id", dep);
                int type = resultSet.getInt("type");
                if (!resultSet.wasNull())
                    jsonObject.addProperty("type", type);
                int totalRating = resultSet.getInt("total_rating");
                if (!resultSet.wasNull())
                    jsonObject.addProperty("total_rating", totalRating);
                int ratingCount = resultSet.getInt("rating_count");
                if (!resultSet.wasNull())
                    jsonObject.addProperty("rating_count", ratingCount);
                String lecturersNote = resultSet.getString("lecturers_note");
                if (!resultSet.wasNull())
                    jsonObject.addProperty("lecturers_note", lecturersNote);

                courses.add(jsonObject);
            }

            return gson.toJsonTree(courses);

        }catch (SQLException e) {
                System.out.println("Error fetching courses.");
                e.printStackTrace();}
        return null;
    }
    public JsonElement fetchDepartments() {
        String query = "SELECT * FROM wise.department";
        try (Statement statement = conn.createStatement()) {
            ResultSet resultSet = statement.executeQuery(query);
            ArrayList<JsonObject> departments = new ArrayList<>();

            while (resultSet.next()) {
                JsonObject jsonObject = new JsonObject();
                jsonObject.addProperty("id", resultSet.getInt("id"));
                jsonObject.addProperty("name", resultSet.getString("name"));
                int facultyName = resultSet.getInt("faculty_name");
                    jsonObject.addProperty("faculty_name", facultyName);

                departments.add(jsonObject);
            }
            return gson.toJsonTree(departments);
        } catch (SQLException e) {
            System.out.println("Error fetching departments.");
            e.printStackTrace();
            return null;
        }
    }
    public JsonElement fetchProfessors() {
        String query = "SELECT * FROM wise.professor";
        try (Statement statement = conn.createStatement()) {
            ResultSet resultSet = statement.executeQuery(query);
            ArrayList<JsonObject> professors = new ArrayList<>();

            while (resultSet.next()) {
                JsonObject jsonObject = new JsonObject();
                jsonObject.addProperty("id", resultSet.getInt("id"));
                jsonObject.addProperty("name", resultSet.getString("name"));
                String surname = resultSet.getString("surname");
                if (!resultSet.wasNull())
                    jsonObject.addProperty("surname", surname);
                String email = resultSet.getString("email");
                if (!resultSet.wasNull())
                    jsonObject.addProperty("email", email);
                int totalRating = resultSet.getInt("total_rating");
                if (!resultSet.wasNull())
                    jsonObject.addProperty("total_rating", totalRating);
                int ratingCount = resultSet.getInt("rating_count");
                if (!resultSet.wasNull())
                    jsonObject.addProperty("rating_count", ratingCount);

                professors.add(jsonObject);
            }
            return gson.toJsonTree(professors);
        } catch (SQLException e) {
            System.out.println("Error fetching professors.");
            e.printStackTrace();
            return null;
        }
    }
    public JsonElement fetchRatings(int professorId) {
        String query = "SELECT * FROM wise.rating WHERE professor_id = ?";
        try (PreparedStatement statement = conn.prepareStatement(query)){
            statement.setInt(1, professorId);

            ResultSet resultSet = statement.executeQuery();
            ArrayList<JsonObject> ratings = new ArrayList<>();
            while (resultSet.next()) {
                JsonObject jsonObject = new JsonObject();
                jsonObject.addProperty("user_id", resultSet.getInt("user_id"));
                jsonObject.addProperty("professor_id", resultSet.getInt("professor_id"));
                jsonObject.addProperty("rating", resultSet.getInt("rating"));

                ratings.add(jsonObject);
            }
            return gson.toJsonTree(ratings);
        }catch (SQLException e) {
            System.out.println("Error fetching ratings.");
            e.printStackTrace();
            return null;
        }
    }
    public JsonElement fetchReviews(String courseId) {
        String query = "SELECT * FROM wise.review WHERE course_id = ?";
        try (PreparedStatement statement = conn.prepareStatement(query)){
            statement.setString(1, courseId);

            ResultSet resultSet = statement.executeQuery();
            ArrayList<JsonObject> reviews = new ArrayList<>();
            while (resultSet.next()) {
                JsonObject jsonObject = new JsonObject();
                jsonObject.addProperty("user_id", resultSet.getInt("user_id"));
                jsonObject.addProperty("course_id", resultSet.getString("course_id"));
                jsonObject.addProperty("rating", resultSet.getInt("rating"));
                String comment = resultSet.getString("comment");
                if (!resultSet.wasNull())
                    jsonObject.addProperty("comment", comment);
                reviews.add(jsonObject);
            }
            return gson.toJsonTree(reviews);
        }catch (SQLException e) {
            System.out.println("Error fetching reviews.");
            e.printStackTrace();
            return null;
        }
    }
    public JsonElement fetchSections(String courseId, int semester) {
        String query = "SELECT * FROM wise.section WHERE course_id = ? AND semester = ?";
        try (PreparedStatement statement = conn.prepareStatement(query)){
            statement.setString(1, courseId);
            statement.setInt(2, semester);

            ResultSet resultSet = statement.executeQuery();
            ArrayList<JsonObject> sections = new ArrayList<>();
            while (resultSet.next()) {
                JsonObject jsonObject = new JsonObject();
                jsonObject.addProperty("id", resultSet.getInt("id"));
                jsonObject.addProperty("section_id", resultSet.getInt("section_id"));
                jsonObject.addProperty("course_id", resultSet.getString("course_id"));
                jsonObject.addProperty("professor_id", resultSet.getInt("professor_id"));
                String startTime = resultSet.getString("start_time");
                if (!resultSet.wasNull())
                    jsonObject.addProperty("start_time", startTime);
                String endTime = resultSet.getString("end_time");
                if (!resultSet.wasNull())
                    jsonObject.addProperty("end_time", endTime);
                int day = resultSet.getInt("day");
                if (!resultSet.wasNull())
                    jsonObject.addProperty("day", day);
                String classroomName = resultSet.getString("classroom_name");
                if (!resultSet.wasNull()) {
                    jsonObject.addProperty("classroom_name", classroomName);
                }
                jsonObject.addProperty("type", resultSet.getInt("type"));
                jsonObject.addProperty("semester", resultSet.getInt("semester"));

                sections.add(jsonObject);
            }
            return gson.toJsonTree(sections);
        }catch (SQLException e) {
            System.out.println("Error fetching sections.");
            e.printStackTrace();
            return null;
        }
    }
    public JsonElement fetchUsers() {
        String query = "SELECT * FROM wise.user";
        try (Statement statement = conn.createStatement()) {
            ResultSet resultSet = statement.executeQuery(query);
            ArrayList<JsonObject> users = new ArrayList<>();

            while (resultSet.next()) {
                JsonObject jsonObject = new JsonObject();
                jsonObject.addProperty("id", resultSet.getInt("id"));
                jsonObject.addProperty("name", resultSet.getString("name"));
                String surname = resultSet.getString("surname");
                if (!resultSet.wasNull())
                    jsonObject.addProperty("surname", surname);
                jsonObject.addProperty("email", resultSet.getString("email"));
                jsonObject.addProperty("password", resultSet.getString("password"));

                users.add(jsonObject);
            }
            return gson.toJsonTree(users);
        } catch (SQLException e) {
            System.out.println("Error fetching users.");
            e.printStackTrace();
            return null;
        }
    }

    public JsonElement fetchCourses(int semester){
        String query = "SELECT * FROM wise.course where (select count(*) from wise.section where course_id = course.id and semester = %s) > 0 order by rating_count desc, total_rating desc, id;";
        query = query.formatted(semester);
        try (Statement statement = conn.createStatement()) {
            ResultSet resultSet = statement.executeQuery(query);
            ArrayList<JsonObject> courses = new ArrayList<>();

            while (resultSet.next()) {
                JsonObject jsonObject = new JsonObject();
                jsonObject.addProperty("id", resultSet.getString("id"));
                jsonObject.addProperty("name", resultSet.getString("name"));
                int dep = resultSet.getInt("department_id");
                if (!resultSet.wasNull())
                    jsonObject.addProperty("department_id", dep);
                int type = resultSet.getInt("type");
                if (!resultSet.wasNull())
                    jsonObject.addProperty("type", type);
                int totalRating = resultSet.getInt("total_rating");
                if (!resultSet.wasNull())
                    jsonObject.addProperty("total_rating", totalRating);
                int ratingCount = resultSet.getInt("rating_count");
                if (!resultSet.wasNull())
                    jsonObject.addProperty("rating_count", ratingCount);
                String lecturersNote = resultSet.getString("lecturers_note");
                if (!resultSet.wasNull())
                    jsonObject.addProperty("lecturers_note", lecturersNote);

                courses.add(jsonObject);
            }

            return gson.toJsonTree(courses);

        }catch (SQLException e) {
            System.out.println("Error fetching courses.");
            e.printStackTrace();}
        return null;
    }

    // Fetching single instance
    public JsonElement fetchClassroom(String classroomName) {
        String query = "SELECT * FROM wise.classroom WHERE id = ?";
        try (PreparedStatement statement = conn.prepareStatement(query)) {
            statement.setString(1, classroomName);
            ResultSet resultSet = statement.executeQuery();
            if (resultSet.next()) {
                JsonObject jsonObject = new JsonObject();
                jsonObject.addProperty("id", resultSet.getString("id"));
                int campus = resultSet.getInt("campus");
                if (!resultSet.wasNull())
                    jsonObject.addProperty("campus", campus);
                return jsonObject;
            }else {
                System.out.println("Classroom not found.");
                return null;
            }
        } catch (SQLException e) {
            System.out.println("Error fetching classroom.");
            e.printStackTrace();
            return null;
        }
    }
    public JsonElement fetchCourse(String courseId) { //TODO: Search by name
        String query = "SELECT * FROM wise.course WHERE id = ?";
        try (PreparedStatement statement = conn.prepareStatement(query)) {
            statement.setString(1, courseId);
            ResultSet resultSet = statement.executeQuery();
            if (resultSet.next()) {
                JsonObject jsonObject = new JsonObject();
                jsonObject.addProperty("id", resultSet.getString("id"));
                jsonObject.addProperty("name", resultSet.getString("name"));
                int departmentId = resultSet.getInt("department_id");
                if (!resultSet.wasNull())
                    jsonObject.addProperty("department_id", departmentId);
                int type = resultSet.getInt("type");
                if (!resultSet.wasNull())
                    jsonObject.addProperty("type", type);
                int totalRating = resultSet.getInt("total_rating");
                if (!resultSet.wasNull())
                    jsonObject.addProperty("total_rating", totalRating);
                int ratingCount = resultSet.getInt("rating_count");
                if (!resultSet.wasNull())
                    jsonObject.addProperty("rating_count", ratingCount);
                String lecturersNote = resultSet.getString("lecturers_note");
                if (!resultSet.wasNull())
                    jsonObject.addProperty("lecturers_note", lecturersNote);
                return jsonObject;
            }else {
                System.out.println("Course not found.");
                return null;
            }
        } catch (SQLException e) {
            System.out.println("Error fetching course.");
            e.printStackTrace();
            return null;
        }
    }
    public JsonElement fetchDepartment(int departmentId) { //TODO: Search by name
        String query = "SELECT * FROM wise.department WHERE id = ?";
        try (PreparedStatement statement = conn.prepareStatement(query)) {
            statement.setInt(1, departmentId);
            ResultSet resultSet = statement.executeQuery();
            if (resultSet.next()) {
                JsonObject jsonObject = new JsonObject();
                jsonObject.addProperty("id", resultSet.getInt("id"));
                jsonObject.addProperty("name", resultSet.getString("name"));
                String facultyName = resultSet.getString("faculty_name");
                if (!resultSet.wasNull())
                    jsonObject.addProperty("faculty_name", facultyName);
                return jsonObject;
            }else {
                System.out.println("Department not found.");
                return null;
            }
        } catch (SQLException e) {
            System.out.println("Error fetching department.");
            e.printStackTrace();
            return null;
        }
    }
    public JsonElement fetchProfessor(int professorId) {
        String query = "SELECT * FROM wise.professor WHERE id = ?";
        try (PreparedStatement statement = conn.prepareStatement(query)) {
            statement.setInt(1, professorId);
            ResultSet resultSet = statement.executeQuery();
            if (resultSet.next()) {
                JsonObject jsonObject = new JsonObject();
                jsonObject.addProperty("id", resultSet.getInt("id"));
                jsonObject.addProperty("name", resultSet.getString("name"));
                String surname = resultSet.getString("surname");
                if (!resultSet.wasNull())
                    jsonObject.addProperty("surname", surname);
                String email = resultSet.getString("email");
                if (!resultSet.wasNull())
                    jsonObject.addProperty("email", email);
                int totalRating = resultSet.getInt("total_rating");
                if (!resultSet.wasNull())
                    jsonObject.addProperty("total_rating", totalRating);
                int ratingCount = resultSet.getInt("rating_count");
                if (!resultSet.wasNull())
                    jsonObject.addProperty("rating_count", ratingCount);
                return jsonObject;
            }else {
                System.out.println("Professor not found.");
                return null;
            }
        } catch (SQLException e) {
            System.out.println("Error fetching professor.");
            e.printStackTrace();
            return null;
        }
    }
    public JsonElement fetchRating(int userId, int professorId) {
        String query = "SELECT * FROM wise.rating WHERE user_id = ? AND professor_id = ?";
        try (PreparedStatement statement = conn.prepareStatement(query)) {
            statement.setInt(1, userId);
            statement.setInt(2, professorId);
            ResultSet resultSet = statement.executeQuery();
            if (resultSet.next()) {
                JsonObject jsonObject = new JsonObject();
                jsonObject.addProperty("user_id", resultSet.getInt("user_id"));
                jsonObject.addProperty("professor_id", resultSet.getInt("professor_id"));
                jsonObject.addProperty("rating", resultSet.getInt("rating"));
                return jsonObject;
            }else {
                System.out.println("Rating not found.");
                return null;
            }
        } catch (SQLException e) {
            System.out.println("Error fetching rating.");
            e.printStackTrace();
            return null;
        }
    }
    public JsonElement fetchReview(int userId, String courseId) {
        String query = "SELECT * FROM wise.review WHERE user_id = ? AND course_id = ?";
        try (PreparedStatement statement = conn.prepareStatement(query)) {
            statement.setInt(1, userId);
            statement.setString(2, courseId);
            ResultSet resultSet = statement.executeQuery();
            if (resultSet.next()) {
                JsonObject jsonObject = new JsonObject();
                jsonObject.addProperty("user_id", resultSet.getInt("user_id"));
                jsonObject.addProperty("course_id", resultSet.getString("course_id"));
                jsonObject.addProperty("rating", resultSet.getInt("rating"));
                String comment = resultSet.getString("comment");
                if (!resultSet.wasNull())
                    jsonObject.addProperty("comment", comment);
                return jsonObject;
            }else {
                System.out.println("Review not found.");
                return null;
            }
        } catch (SQLException e) {
            System.out.println("Error fetching review.");
            e.printStackTrace();
            return null;
        }
    }
    public JsonElement fetchSection(int id) {
        String query = "SELECT * FROM wise.section WHERE id = ?";
        try (PreparedStatement statement = conn.prepareStatement(query)) {
            statement.setInt(1, id);
            ResultSet resultSet = statement.executeQuery();
            if (resultSet.next()) {
                JsonObject jsonObject = new JsonObject();
                jsonObject.addProperty("id", resultSet.getInt("id"));
                jsonObject.addProperty("section_id", resultSet.getInt("section_id"));
                jsonObject.addProperty("course_id", resultSet.getString("course_id"));
                jsonObject.addProperty("professor_id", resultSet.getInt("professor_id"));
                String startTime = resultSet.getString("start_time");
                if (!resultSet.wasNull())
                    jsonObject.addProperty("start_time", startTime);
                String endTime = resultSet.getString("end_time");
                if (!resultSet.wasNull())
                    jsonObject.addProperty("end_time", endTime);
                int day = resultSet.getInt("day");
                if (!resultSet.wasNull())
                    jsonObject.addProperty("day", day);
                String classroomName = resultSet.getString("classroom_name");
                if (!resultSet.wasNull())
                    jsonObject.addProperty("classroom_name", classroomName);
                jsonObject.addProperty("type", resultSet.getInt("type"));
                jsonObject.addProperty("semester", resultSet.getInt("semester"));
                return jsonObject;
            }else {
                System.out.println("Section not found.");
                return null;
            }
        } catch (SQLException e) {
            System.out.println("Error fetching section.");
            e.printStackTrace();
            return null;
        }
    }
    public JsonElement fetchUser(int userId) {
        String query = "SELECT * FROM wise.user WHERE id = ?";
        try (PreparedStatement statement = conn.prepareStatement(query)) {
            statement.setInt(1, userId);
            ResultSet resultSet = statement.executeQuery();
            if (resultSet.next()) {
                JsonObject jsonObject = new JsonObject();
                jsonObject.addProperty("id", resultSet.getInt("id"));
                jsonObject.addProperty("name", resultSet.getString("name"));
                String surname = resultSet.getString("surname");
                if (!resultSet.wasNull())
                    jsonObject.addProperty("surname", surname);
                jsonObject.addProperty("email", resultSet.getString("email"));
                jsonObject.addProperty("password", resultSet.getString("password"));
                return jsonObject;
            }else {
                System.out.println("User not found.");
                return null;
            }
        } catch (SQLException e) {
            System.out.println("Error fetching user.");
            e.printStackTrace();
            return null;
        }
    }

    public JsonElement fetchLogin(String email){
        String query = "SELECT id, email, password FROM wise.user WHERE email = ?";
        try(PreparedStatement statement = conn.prepareStatement(query)) {
            statement.setString(1, email);
            ResultSet resultSet = statement.executeQuery();
            if (resultSet.next()) {
                JsonObject jsonObject = new JsonObject();
                jsonObject.addProperty("id", resultSet.getInt("id"));
                jsonObject.addProperty("email", resultSet.getString("email"));
                jsonObject.addProperty("password", resultSet.getString("password"));
                return jsonObject;
            }else {
                System.out.println("User not found.");
                return null;
            }
        } catch (SQLException e) {
            System.out.println("Error fetching user.");
            e.printStackTrace();
            return null;
        }
    }



    // Inserting data into the database
    public boolean insertClassroom(JsonObject clasroom) {
        String query = "INSERT INTO wise.classroom (id, campus) VALUES (?, ?)";
        try (PreparedStatement statement = conn.prepareStatement(query)) {
            statement.setString(1, clasroom.get("id").getAsString());
            if (clasroom.has("campus"))
                statement.setInt(2, clasroom.get("campus").getAsInt());
            else statement.setNull(2, Types.SMALLINT);

            int rowsInserted = statement.executeUpdate();
            return rowsInserted > 0;
        } catch (SQLException e) {
            System.out.println("Error inserting classroom.");
            e.printStackTrace();
            return false;
        }
    }
    public boolean insertCourse(JsonObject course) {
        String query = "INSERT INTO wise.course (id, name, department_id, type, total_rating, rating_count, lecturers_note) VALUES (?, ?, ?, ?, ?, ?, ?)";
        try (PreparedStatement statement = conn.prepareStatement(query)) {
            statement.setString(1, course.get("id").getAsString());
            statement.setString(2, course.get("name").getAsString());
            if (course.has("department_id"))
                statement.setInt(3, course.get("department_id").getAsInt());
            else statement.setNull(3, Types.INTEGER);
            if (course.has("type"))
                statement.setInt(4, course.get("type").getAsInt());
            else statement.setNull(4, Types.SMALLINT);
            if (course.has("total_rating"))
                statement.setInt(5, course.get("total_rating").getAsInt());
            else statement.setNull(5, Types.INTEGER);
            if (course.has("rating_count"))
                statement.setInt(6, course.get("rating_count").getAsInt());
            else statement.setNull(6, Types.INTEGER);
            if (course.has("lecturers_note"))
                statement.setString(7, course.get("lecturers_note").getAsString());
            else statement.setNull(7, Types.VARCHAR);

            int rowsInserted = statement.executeUpdate();
            return rowsInserted > 0;
        } catch (SQLException e) {
            System.out.println("Error inserting course.");
            e.printStackTrace();
            return false;
        }
    }
    public boolean insertDepartment(JsonObject department) {
        String query = "INSERT INTO wise.department (name, faculty_name) VALUES (?, ?)";
        try (PreparedStatement statement = conn.prepareStatement(query)) {
            statement.setString(1, department.get("name").getAsString());
            if (department.has("faculty_name"))
                statement.setString(2, department.get("faculty_name").getAsString());
            else statement.setNull(2, Types.VARCHAR);

            int rowsInserted = statement.executeUpdate();
            return rowsInserted > 0;
        } catch (SQLException e) {
            System.out.println("Error inserting department.");
            e.printStackTrace();
            return false;
        }
    }
    public boolean insertProfessor(JsonObject professor) {
        String query = "INSERT INTO wise.professor (id, name, surname, email, total_rating, rating_count) VALUES (?, ?, ?, ?, ?, ?)";
        try (PreparedStatement statement = conn.prepareStatement(query)) {
            statement.setInt(1, professor.get("id").getAsInt());
            statement.setString(2, professor.get("name").getAsString());
            if (professor.has("surname"))
                statement.setString(3, professor.get("surname").getAsString());
            else statement.setNull(3, Types.VARCHAR);
            if(professor.has("email"))
                statement.setString(4, professor.get("email").getAsString());
            else statement.setNull(4, Types.VARCHAR);
            if (professor.has("total_rating"))
                statement.setInt(5, professor.get("total_rating").getAsInt());
            else statement.setNull(5, Types.INTEGER);
            if (professor.has("rating_count"))
                statement.setInt(6, professor.get("rating_count").getAsInt());
            else statement.setNull(6, Types.INTEGER);

            int rowsInserted = statement.executeUpdate();
            return rowsInserted > 0;
        } catch (SQLException e) {
            System.out.println("Error inserting professor.");
            e.printStackTrace();
            return false;
        }
    }
    public boolean insertRating(JsonObject rating) {
        String query = "INSERT INTO wise.rating (user_id, professor_id, rating) VALUES (?, ?, ?)";
        try (PreparedStatement statement = conn.prepareStatement(query)) {
            statement.setInt(1, rating.get("user_id").getAsInt());
            statement.setInt(2, rating.get("professor_id").getAsInt());
            statement.setInt(3, rating.get("rating").getAsInt());

            int rowsInserted = statement.executeUpdate();
            return rowsInserted > 0;
        } catch (SQLException e) {
            System.out.println("Error inserting rating.");
            e.printStackTrace();
            return false;
        }
    }
    public boolean insertReview(JsonObject review) {
        String query = "INSERT INTO wise.review (user_id, course_id, rating, comment) VALUES (?, ?, ?, ?)";
        try (PreparedStatement statement = conn.prepareStatement(query)) {
            statement.setInt(1, review.get("user_id").getAsInt());
            statement.setString(2, review.get("course_id").getAsString());
            statement.setInt(3, review.get("rating").getAsInt());
            if (review.has("comment"))
                statement.setString(4, review.get("comment").getAsString());
            else statement.setNull(4, Types.VARCHAR);

            int rowsInserted = statement.executeUpdate();
            return rowsInserted > 0;
        } catch (SQLException e) {
            System.out.println("Error inserting review.");
            e.printStackTrace();
            return false;
        }
    }
    public boolean insertSection(JsonObject section) {
        String query = "INSERT INTO wise.section (id, section_id, course_id, classroom_name, professor_id, day, start_time, end_time, type, semester) VALUES (?, ?, ?, ?, ?, ?, ?, ?, ?, ?)";
        try (PreparedStatement statement = conn.prepareStatement(query)) {
            statement.setInt(1, section.get("id").getAsInt());
            statement.setInt(2, section.get("section_id").getAsInt());
            statement.setString(3, section.get("course_id").getAsString());
            if (section.has("classroom_name"))
                statement.setString(4, section.get("classroom_name").getAsString());
            else statement.setNull(4, Types.VARCHAR);
            statement.setInt(5, section.get("professor_id").getAsInt());
            if (section.has("day"))
                statement.setInt(6, section.get("day").getAsInt());
            else statement.setNull(6, Types.VARCHAR);
            if (section.has("start_time"))
            statement.setString(7, section.get("start_time").getAsString());
            else statement.setNull(7, Types.VARCHAR);
            if (section.has("end_time"))
                statement.setString(8, section.get("end_time").getAsString());
            else statement.setNull(8, Types.VARCHAR);
            if (section.has("type"))
                statement.setInt(9, section.get("type").getAsInt());
            else statement.setNull(9, Types.SMALLINT);
            if (section.has("semester"))
                statement.setInt(10, section.get("semester").getAsInt());
            else statement.setNull(10, Types.SMALLINT);

            int rowsInserted = statement.executeUpdate();
            return rowsInserted > 0;
        } catch (SQLException e) {
            System.out.println("Error inserting section.");
            e.printStackTrace();
            return false;
        }
    }
    public boolean insertUser(JsonObject user) {
        String query = "INSERT INTO wise.user (id, name, surname, email, password) VALUES (?, ?, ?, ?, ?)";
        try (PreparedStatement statement = conn.prepareStatement(query)) {
            statement.setInt(1, user.get("id").getAsInt());
            statement.setString(2, user.get("name").getAsString());
            if (user.has("surname"))
                statement.setString(3, user.get("surname").getAsString());
            else statement.setNull(3, Types.VARCHAR);
            statement.setString(4, user.get("email").getAsString());
            statement.setString(5, user.get("password").getAsString());

            int rowsInserted = statement.executeUpdate();
            return rowsInserted > 0;
        } catch (SQLException e) {
            System.out.println("Error inserting user.");
            e.printStackTrace();
            return false;
        }
    }

    // Updating data in the database
    public boolean updateClassroom(String classroomName, JsonObject classroom) {
        String query = "UPDATE wise.classroom SET campus = ? WHERE name = ?";

        try (PreparedStatement statement = conn.prepareStatement(query)) {
            statement.setInt(1, classroom.get("campus").getAsInt());
            statement.setString(2, classroomName);

            int rowsUpdated = statement.executeUpdate();
            return rowsUpdated > 0;
        } catch (SQLException e) {
            System.out.println("Error updating classroom.");
            e.printStackTrace();
            return false;
        }
    }
    public boolean updateCourse(String courseId, JsonObject course) {
        int i = 1;
        String query = "UPDATE wise.course SET " + (course.has("name")?"name = ?, ":"") +
                (course.has("department_id")?"department_id = ?, ":"") +
                (course.has("type")?"type = ?, ":"") +
                (course.has("total_rating")?"total_rating = ?, ":"") +
                (course.has("rating_count")?"rating_count = ?, ":"") +
                (course.has("lecturers_note")?"lecturers_note = ?, ":"") +
                "WHERE id = ?";
        query = query.substring(0,query.lastIndexOf(',')) + query.substring(query.lastIndexOf(',') +1);

        try (PreparedStatement statement = conn.prepareStatement(query)) {
            if (course.has("name"))
                statement.setString(i++, course.get("name").getAsString());
            if (course.has("department_id"))
                statement.setInt(i++, course.get("department_id").getAsInt());
            if (course.has("type"))
                statement.setInt(i++, course.get("type").getAsInt());
            if (course.has("total_rating"))
                statement.setInt(i++, course.get("total_rating").getAsInt());
            if (course.has("rating_count"))
                statement.setInt(i++, course.get("rating_count").getAsInt());
            if (course.has("lecturers_note"))
                statement.setString(i++, course.get("lecturers_note").getAsString());
            if (course.has("semester"))
                statement.setString(i++, course.get("semester").getAsString());
            statement.setString(i, courseId);

            int rowsUpdated = statement.executeUpdate();
            return rowsUpdated > 0;
        } catch (SQLException e) {
            System.out.println("Error updating course.");
            e.printStackTrace();
            return false;
        }
    }
    public boolean updateDepartment(int departmentId, JsonObject department) {
        int i = 1;
        String query = "UPDATE wise.department SET " + (department.has("name")?"name = ?, ":"") +
                (department.has("faculty_name")?"faculty_name = ?, ":"") + "WHERE id = ?";

        query = query.substring(0,query.lastIndexOf(',')) + query.substring(query.lastIndexOf(',') +1);

        try (PreparedStatement statement = conn.prepareStatement(query)) {
            if (department.has("name"))
                statement.setString(i++, department.get("name").getAsString());
            if (department.has("faculty_name"))
                statement.setString(i++, department.get("faculty_name").getAsString());
            statement.setInt(i, departmentId);

            int rowsUpdated = statement.executeUpdate();
            return rowsUpdated > 0;
        } catch (SQLException e) {
            System.out.println("Error updating department.");
            e.printStackTrace();
            return false;
        }
    }
    public boolean updateProfessor(int professorId, JsonObject professor) {
        int i = 1;
        String query = "UPDATE wise.professor SET " + (professor.has("name")?"name = ?, ":"") +
                (professor.has("surname")?"surname = ?, ":"") +
                (professor.has("email")?"email = ?, ":"") +
                (professor.has("total_rating")?"total_rating = ?, ":"") +
                (professor.has("rating_count")?"rating_count = ?, ":"") + "WHERE id = ?";
        query = query.substring(0,query.lastIndexOf(',')) + query.substring(query.lastIndexOf(',') +1);

        try (PreparedStatement statement = conn.prepareStatement(query)) {
            if (professor.has("name"))
                statement.setString(i++, professor.get("name").getAsString());
            if (professor.has("surname"))
                statement.setString(i++, professor.get("surname").getAsString());
            if (professor.has("email"))
                statement.setString(i++, professor.get("email").getAsString());
            if (professor.has("total_rating"))
                statement.setInt(i++, professor.get("total_rating").getAsInt());
            if (professor.has("rating_count"))
                statement.setInt(i++, professor.get("rating_count").getAsInt());
            statement.setInt(i, professorId);

            int rowsUpdated = statement.executeUpdate();
            return rowsUpdated > 0;
        } catch (SQLException e) {
            System.out.println("Error updating professor.");
            e.printStackTrace();
            return false;
        }
    }
    public boolean updateRating(int userId, int professorId, JsonObject rating) {
        String query = "UPDATE wise.rating SET rating = ? WHERE user_id = ? AND professor_id = ?";
        try (PreparedStatement statement = conn.prepareStatement(query)) {
            statement.setInt(1, rating.get("rating").getAsInt());
            statement.setInt(2, userId);
            statement.setInt(3, professorId);

            int rowsUpdated = statement.executeUpdate();
            return rowsUpdated > 0;
        } catch (SQLException e) {
            System.out.println("Error updating rating.");
            e.printStackTrace();
            return false;
        }
    }
    public boolean updateReview(int userId, String courseId, JsonObject review) {
        int i = 1;
        String query = "UPDATE wise.review SET " + (review.has("rating")?"rating = ?, ":"") +
                (review.has("comment")?"comment = ?, ":"") + "WHERE user_id = ? AND course_id = ?";

        query = query.substring(0,query.lastIndexOf(',')) + query.substring(query.lastIndexOf(',') +1);

        try (PreparedStatement statement = conn.prepareStatement(query)) {
            if (review.has("rating"))
                statement.setInt(i++, review.get("rating").getAsInt());
            if (review.has("comment"))
                statement.setString(i++, review.get("comment").getAsString());
            statement.setInt(i++, userId);
            statement.setString(i, courseId);

            int rowsUpdated = statement.executeUpdate();
            return rowsUpdated > 0;
        } catch (SQLException e) {
            System.out.println("Error updating review.");
            e.printStackTrace();
            return false;
        }
    }
    public boolean updateSection(int SectionId, JsonObject section) {
        int i = 1;
        String query = "UPDATE wise.section SET " + (section.has("classroom_name")?"classroom_name = ?, ":"") +
                (section.has("professor_id")?"professor_id = ?, ":"") +
                (section.has("day")?"day = ?, ":"") +
                (section.has("start_time")?"start_time = ?, ":"") +
                (section.has("end_time")?"end_time = ?, ":"") +
                (section.has("type")?"type = ?, ":"") +
                (section.has("section_id")?"section_id = ?, ":"") +
                (section.has("course_id")?"course_id = ?, ":"") +
                (section.has("semester")?"semester = ?, ":"") +
                "WHERE id = ?";

        query = query.substring(0,query.lastIndexOf(',')) + query.substring(query.lastIndexOf(',') +1);

        try (PreparedStatement statement = conn.prepareStatement(query)) {
            if (section.has("classroom_name"))
                statement.setString(i++, section.get("classroom_name").getAsString());
            if (section.has("professor_id"))
                statement.setInt(i++, section.get("professor_id").getAsInt());
            if (section.has("day"))
                statement.setInt(i++, section.get("day").getAsInt());
            if (section.has("start_time"))
                statement.setString(i++, section.get("start_time").getAsString());
            if (section.has("end_time"))
                statement.setString(i++, section.get("end_time").getAsString());
            if (section.has("type"))
                statement.setInt(i++, section.get("type").getAsInt());
            if(section.has("section_id"))
                statement.setInt(i++, section.get("section_id").getAsInt());
            if(section.has("course_id"))
                statement.setString(i++, section.get("course_id").getAsString());
            if(section.has("semester"))
                statement.setInt(i++, section.get("semester").getAsInt());
            statement.setInt(i, SectionId);

            int rowsUpdated = statement.executeUpdate();
            return rowsUpdated > 0;
        } catch (SQLException e) {
            System.out.println("Error updating section.");
            e.printStackTrace();
            return false;
        }
    }
    public boolean updateUser(int id, JsonObject user) {
        int i = 1;
        String query = "UPDATE wise.user SET " + (user.has("name")?"name = ?, ":"") +
                (user.has("surname")?"surname = ?, ":"") +
                (user.has("email")?"email = ?, ":"") +
                (user.has("password")?"password = ?, ":"") + "WHERE id = ?";
        query = query.substring(0,query.lastIndexOf(',')) + query.substring(query.lastIndexOf(',') +1);

        try (PreparedStatement statement = conn.prepareStatement(query)) {
            if (user.has("name"))
                statement.setString(i++, user.get("name").getAsString());
            if (user.has("surname"))
                statement.setString(i++, user.get("surname").getAsString());
            if (user.has("email"))
                statement.setString(i++, user.get("email").getAsString());
            if (user.has("password"))
                statement.setString(i++, user.get("password").getAsString());
            statement.setInt(i, id);

            int rowsUpdated = statement.executeUpdate();
            return rowsUpdated > 0;
        } catch (SQLException e) {
            System.out.println("Error updating user.");
            e.printStackTrace();
            return false;
        }
    }

    public boolean recalculateRatings(){
        String query1 = "UPDATE wise.course \"c\" SET total_rating = (select SUM(rating) from wise.review \"r\" where r.course_id = c.id);";
        String query2 = "UPDATE wise.course \"c\" SET rating_count = (select COUNT(*) from wise.review \"r\" where r.course_id = c.id);";
        String query3 = "UPDATE wise.professor \"p\" SET total_rating = (select SUM(rating) from wise.rating \"r\" where r.professor_id = p.id);";
        String query4 = "UPDATE wise.professor \"p\" SET rating_count = (select COUNT(*) from wise.rating \"r\" where r.professor_id = p.id);";

        try (Statement statement = conn.createStatement()){
            statement.executeUpdate(query1);
            statement.executeUpdate(query2);
            statement.executeUpdate(query3);
            statement.executeUpdate(query4);
            return true;
        } catch (SQLException e) {
            System.out.println("Error resetting course ratings.");
            e.printStackTrace();
            return false;
        }
    }

    public void close(){
        try {
            if (conn != null) {
                conn.close();
                conn = null;
            }
        } catch (SQLException e) {
            System.out.println("Error closing the connection.");
            e.printStackTrace();
        }
    }

    public static void main(String[] args) throws SQLException {
        Database db = new Database();
        db.connect();

        System.out.println("OK");
        db.close();
    }
}
