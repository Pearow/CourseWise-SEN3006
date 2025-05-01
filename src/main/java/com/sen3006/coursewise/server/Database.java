package com.sen3006.coursewise.server;

import com.google.gson.Gson;
import com.google.gson.JsonElement;
import com.google.gson.JsonObject;
import com.sen3006.coursewise.client.json.UserPassword;

import java.sql.*;
import java.util.ArrayList;

public class Database {
    private Connection conn;
    private Gson gson = new Gson();

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
    public JsonElement fetchclassrooms(){
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
                jsonObject.addProperty("department_id", resultSet.getInt("department_id"));
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
                jsonObject.addProperty("password", resultSet.getString("password"));
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
        String query = "INSERT INTO wise.department (id, name, faculty_name) VALUES (?, ?, ?)";
        try (PreparedStatement statement = conn.prepareStatement(query)) {
            statement.setInt(1, department.get("id").getAsInt());
            statement.setString(2, department.get("name").getAsString());
            if (department.has("faculty_name"))
                statement.setString(3, department.get("faculty_name").getAsString());
            else statement.setNull(3, Types.VARCHAR);

            int rowsInserted = statement.executeUpdate();
            return rowsInserted > 0;
        } catch (SQLException e) {
            System.out.println("Error inserting department.");
            e.printStackTrace();
            return false;
        }
    }
    public boolean insertProfessor(JsonObject professor) {
        String query = "INSERT INTO wise.professor (id, name, surname, email, password, total_rating, rating_count) VALUES (?, ?, ?, ?, ?, ?, ?)";
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
        String query = "INSERT INTO wise.section (id, course_id, classroom_name, professor_id, day, start_time, end_time, type) VALUES (?, ?, ?, ?, ?, ?, ?, ?)";
        try (PreparedStatement statement = conn.prepareStatement(query)) {
            statement.setInt(1, section.get("id").getAsInt());
            statement.setString(2, section.get("course_id").getAsString());
            if (section.has("classroom_name"))
                statement.setString(3, section.get("classroom_name").getAsString());
            else statement.setNull(3, Types.VARCHAR);
            statement.setInt(4, section.get("professor_id").getAsInt());
            if (section.has("day"))
                statement.setInt(5, section.get("day").getAsInt());
            else statement.setNull(5, Types.VARCHAR);
            if (section.has("start_time"))
            statement.setString(6, section.get("start_time").getAsString());
            else statement.setNull(6, Types.VARCHAR);
            if (section.has("end_time"))
                statement.setString(7, section.get("end_time").getAsString());
            else statement.setNull(7, Types.VARCHAR);
            if (section.has("type"))
                statement.setInt(8, section.get("type").getAsInt());
            else statement.setNull(8, Types.SMALLINT);

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
