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
                jsonObject.addProperty("campus", resultSet.getInt("campus"));

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
        String query = "SELECT * FROM wise.course";
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
                jsonObject.addProperty("type", resultSet.getInt("type"));
                jsonObject.addProperty("total_rating", resultSet.getInt("total_rating"));
                jsonObject.addProperty("rating_count", resultSet.getInt("rating_count"));
                jsonObject.addProperty("lecturers_note", resultSet.getString("lecturers_note"));

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
                jsonObject.addProperty("faculty_name", resultSet.getString("faculty_name"));

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
                jsonObject.addProperty("surname", resultSet.getString("surname"));
                jsonObject.addProperty("email", resultSet.getString("email"));
                jsonObject.addProperty("total_rating", resultSet.getInt("total_rating"));
                jsonObject.addProperty("rating_count", resultSet.getInt("rating_count"));

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
    public JsonElement fetchReviews(int courseId) {
        String query = "SELECT * FROM wise.review WHERE course_id = ?";
        try (PreparedStatement statement = conn.prepareStatement(query)){
            statement.setInt(1, courseId);

            ResultSet resultSet = statement.executeQuery();
            ArrayList<JsonObject> reviews = new ArrayList<>();
            while (resultSet.next()) {
                JsonObject jsonObject = new JsonObject();
                jsonObject.addProperty("user_id", resultSet.getInt("user_id"));
                jsonObject.addProperty("course_id", resultSet.getString("course_id"));
                jsonObject.addProperty("rating", resultSet.getInt("rating"));
                jsonObject.addProperty("comment", resultSet.getString("comment"));

                reviews.add(jsonObject);
            }
            return gson.toJsonTree(reviews);
        }catch (SQLException e) {
            System.out.println("Error fetching reviews.");
            e.printStackTrace();
            return null;
        }
    }
    public JsonElement fetchSections(int courseId) {
        String query = "SELECT * FROM wise.section WHERE course_id = ?";
        try (PreparedStatement statement = conn.prepareStatement(query)){
            statement.setInt(1, courseId);

            ResultSet resultSet = statement.executeQuery();
            ArrayList<JsonObject> sections = new ArrayList<>();
            while (resultSet.next()) {
                JsonObject jsonObject = new JsonObject();
                jsonObject.addProperty("id", resultSet.getInt("id"));
                jsonObject.addProperty("course_id", resultSet.getString("course_id"));
                jsonObject.addProperty("classroom_name", resultSet.getString("classroom_name"));
                jsonObject.addProperty("professor_id", resultSet.getInt("professor_id"));
                jsonObject.addProperty("day", resultSet.getString("day"));
                jsonObject.addProperty("start_time", resultSet.getString("start_time"));
                jsonObject.addProperty("end_time", resultSet.getString("end_time"));
                jsonObject.addProperty("type", resultSet.getInt("type"));

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
                jsonObject.addProperty("surname", resultSet.getString("surname"));
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

    // Fetching single instance
    public JsonElement fetchClassroom(String classroomName) {
        String query = "SELECT * FROM wise.classroom WHERE id = ?";
        try (PreparedStatement statement = conn.prepareStatement(query)) {
            statement.setString(1, classroomName);
            ResultSet resultSet = statement.executeQuery();
            if (resultSet.next()) {
                JsonObject jsonObject = new JsonObject();
                jsonObject.addProperty("id", resultSet.getString("id"));
                jsonObject.addProperty("campus", resultSet.getInt("campus"));
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
                jsonObject.addProperty("department_id", resultSet.getInt("department_id"));
                jsonObject.addProperty("type", resultSet.getInt("type"));
                jsonObject.addProperty("total_rating", resultSet.getInt("total_rating"));
                jsonObject.addProperty("rating_count", resultSet.getInt("rating_count"));
                jsonObject.addProperty("lecturers_note", resultSet.getString("lecturers_note"));
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
                jsonObject.addProperty("faculty_name", resultSet.getString("faculty_name"));
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
                jsonObject.addProperty("surname", resultSet.getString("surname"));
                jsonObject.addProperty("email", resultSet.getString("email"));
                jsonObject.addProperty("total_rating", resultSet.getInt("total_rating"));
                jsonObject.addProperty("rating_count", resultSet.getInt("rating_count"));
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
                jsonObject.addProperty("comment", resultSet.getString("comment"));
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
                jsonObject.addProperty("classroom_name", resultSet.getString("classroom_name"));
                jsonObject.addProperty("professor_id", resultSet.getInt("professor_id"));
                jsonObject.addProperty("day", resultSet.getString("day"));
                jsonObject.addProperty("start_time", resultSet.getString("start_time"));
                jsonObject.addProperty("end_time", resultSet.getString("end_time"));
                jsonObject.addProperty("type", resultSet.getInt("type"));
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
                jsonObject.addProperty("surname", resultSet.getString("surname"));
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
        String query = "UPDATE wise.classroom SET " + (classroom.has("campus")?"campus = ? ":"") +
                "WHERE name = ?";
        try (PreparedStatement statement = conn.prepareStatement(query)) {
            if (classroom.has("campus"))
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
    //TODO: Fix false index while data adding arbitrary columns
    public boolean updateCourse(String courseId, JsonObject course) {
        String query = "UPDATE wise.course SET " + (course.has("name")?"name = ?, ":"") +
                (course.has("department_id")?"department_id = ?, ":"") +
                (course.has("type")?"type = ?, ":"") +
                (course.has("total_rating")?"total_rating = ?, ":"") +
                (course.has("rating_count")?"rating_count = ?, ":"") +
                (course.has("lecturers_note")?"lecturers_note = ? ":"") +
                (course.has("semester")?"semester = ?":"") + "WHERE id = ?";

        try (PreparedStatement statement = conn.prepareStatement(query)) {
            if (course.has("name"))
                statement.setString(1, course.get("name").getAsString());
            if (course.has("department_id"))
                statement.setInt(2, course.get("department_id").getAsInt());
            if (course.has("type"))
                statement.setInt(3, course.get("type").getAsInt());
            if (course.has("total_rating"))
                statement.setInt(4, course.get("total_rating").getAsInt());
            if (course.has("rating_count"))
                statement.setInt(5, course.get("rating_count").getAsInt());
            if (course.has("lecturers_note"))
                statement.setString(6, course.get("lecturers_note").getAsString());
            if (course.has("semester"))
                statement.setString(7, course.get("semester").getAsString());
            statement.setString(8, courseId);

            int rowsUpdated = statement.executeUpdate();
            return rowsUpdated > 0;
        } catch (SQLException e) {
            System.out.println("Error updating course.");
            e.printStackTrace();
            return false;
        }
    }
    public boolean updateDepartment(int departmentId, JsonObject department) {
        String query = "UPDATE wise.department SET " + (department.has("name")?"name = ?, ":"") +
                (department.has("faculty_name")?"faculty_name = ? ":"") + "WHERE id = ?";
        try (PreparedStatement statement = conn.prepareStatement(query)) {
            if (department.has("name"))
                statement.setString(1, department.get("name").getAsString());
            if (department.has("faculty_name"))
                statement.setString(2, department.get("faculty_name").getAsString());
            statement.setInt(3, departmentId);

            int rowsUpdated = statement.executeUpdate();
            return rowsUpdated > 0;
        } catch (SQLException e) {
            System.out.println("Error updating department.");
            e.printStackTrace();
            return false;
        }
    }
    public boolean updateProfessor(int professorId, JsonObject professor) {
        String query = "UPDATE wise.professor SET " + (professor.has("name")?"name = ?, ":"") +
                (professor.has("surname")?"surname = ?, ":"") +
                (professor.has("email")?"email = ?, ":"") +
                (professor.has("total_rating")?"total_rating = ?, ":"") +
                (professor.has("rating_count")?"rating_count = ? ":"") + "WHERE id = ?";
        try (PreparedStatement statement = conn.prepareStatement(query)) {
            if (professor.has("name"))
                statement.setString(1, professor.get("name").getAsString());
            if (professor.has("surname"))
                statement.setString(2, professor.get("surname").getAsString());
            if (professor.has("email"))
                statement.setString(3, professor.get("email").getAsString());
            if (professor.has("total_rating"))
                statement.setInt(4, professor.get("total_rating").getAsInt());
            if (professor.has("rating_count"))
                statement.setInt(5, professor.get("rating_count").getAsInt());
            statement.setInt(6, professorId);

            int rowsUpdated = statement.executeUpdate();
            return rowsUpdated > 0;
        } catch (SQLException e) {
            System.out.println("Error updating professor.");
            e.printStackTrace();
            return false;
        }
    }
    public boolean updateRating(int userId, int professorId, JsonObject rating) {
        String query = "UPDATE wise.rating SET " + (rating.has("rating")?"rating = ? ":"") +
                "WHERE user_id = ? AND professor_id = ?";
        try (PreparedStatement statement = conn.prepareStatement(query)) {
            if (rating.has("rating"))
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
        String query = "UPDATE wise.review SET " + (review.has("rating")?"rating = ?, ":"") +
                (review.has("comment")?"comment = ? ":"") + "WHERE user_id = ? AND course_id = ?";
        try (PreparedStatement statement = conn.prepareStatement(query)) {
            if (review.has("rating"))
                statement.setInt(1, review.get("rating").getAsInt());
            if (review.has("comment"))
                statement.setString(2, review.get("comment").getAsString());
            statement.setInt(3, userId);
            statement.setString(4, courseId);

            int rowsUpdated = statement.executeUpdate();
            return rowsUpdated > 0;
        } catch (SQLException e) {
            System.out.println("Error updating review.");
            e.printStackTrace();
            return false;
        }
    }
    public boolean updateSection(int SectionId, JsonObject section) {
        String query = "UPDATE wise.section SET " + (section.has("classroom_name")?"classroom_name = ?, ":"") +
                (section.has("professor_id")?"professor_id = ?, ":"") +
                (section.has("day")?"day = ?, ":"") +
                (section.has("start_time")?"start_time = ?, ":"") +
                (section.has("end_time")?"end_time = ?, ":"") +
                (section.has("type")?"type = ? ":"") +
                (section.has("section_id")?"type = ? ":"") +
                (section.has("course_id")?"type = ? ":"") +
                "WHERE id = ?";
        try (PreparedStatement statement = conn.prepareStatement(query)) {
            if (section.has("classroom_name"))
                statement.setString(1, section.get("classroom_name").getAsString());
            if (section.has("professor_id"))
                statement.setInt(2, section.get("professor_id").getAsInt());
            if (section.has("day"))
                statement.setString(3, section.get("day").getAsString());
            if (section.has("start_time"))
                statement.setString(4, section.get("start_time").getAsString());
            if (section.has("end_time"))
                statement.setString(5, section.get("end_time").getAsString());
            if (section.has("type"))
                statement.setInt(6, section.get("type").getAsInt());
            if(section.has("section_id"))
                statement.setInt(7, section.get("section_id").getAsInt());
            if(section.has("course_id"))
                statement.setString(8, section.get("course_id").getAsString());
            statement.setInt(9, SectionId);

            int rowsUpdated = statement.executeUpdate();
            return rowsUpdated > 0;
        } catch (SQLException e) {
            System.out.println("Error updating section.");
            e.printStackTrace();
            return false;
        }
    }
    public boolean updateUser(int id, JsonObject user) {
        String query = "UPDATE wise.user SET " + (user.has("name")?"name = ?, ":"") +
                (user.has("surname")?"surname = ?, ":"") +
                (user.has("email")?"email = ?, ":"") +
                (user.has("password")?"password = ? ":"") + "WHERE id = ?";
        try (PreparedStatement statement = conn.prepareStatement(query)) {
            if (user.has("name"))
                statement.setString(1, user.get("name").getAsString());
            if (user.has("surname"))
                statement.setString(2, user.get("surname").getAsString());
            if (user.has("email"))
                statement.setString(3, user.get("email").getAsString());
            if (user.has("password"))
                statement.setString(4, user.get("password").getAsString());
            statement.setInt(5, id);

            int rowsUpdated = statement.executeUpdate();
            return rowsUpdated > 0;
        } catch (SQLException e) {
            System.out.println("Error updating user.");
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

    // TODO: Test all the datatypes by adding and fetching data
    public static void main(String[] args) throws SQLException {
        Database db = new Database();
        db.connect();

        System.out.println("OK");
        db.close();
    }
}
