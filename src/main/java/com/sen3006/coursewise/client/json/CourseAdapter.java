package com.sen3006.coursewise.client.json;

import com.google.gson.*;
import com.sen3006.coursewise.client.API;
import com.sen3006.coursewise.client.models.Course;
import com.sen3006.coursewise.client.models.Department;

import java.lang.reflect.Type;

public class CourseAdapter implements JsonSerializer<Course>, JsonDeserializer<Course> {
    @Override
    public JsonElement serialize(Course course, Type typeOfSrc, JsonSerializationContext context) {
        JsonObject jsonObject = new JsonObject();
        jsonObject.addProperty("id", course.getCourse_id());
        jsonObject.addProperty("name", course.getCourse_name());
        if (course.getDepartment() != null)
            jsonObject.addProperty("department_id", course.getDepartment().getDepartment_id());
//        else {
//            System.out.println("WARNING: Department is null in course object: " + course.getCourse_id() + " " + course.getCourse_name());
//        }
        jsonObject.addProperty("type", course.getType().getIntType());
        jsonObject.addProperty("total_rating", course.getTotalRating());
        jsonObject.addProperty("rating_count", course.getRatingCount());
        if(!course.getLecturersNote().isBlank())
            jsonObject.addProperty("lecturers_note", course.getLecturersNote());


        return jsonObject;
    }

    @Override
    public Course deserialize(JsonElement json, Type typeOfT, JsonDeserializationContext context) throws JsonParseException {
        JsonObject jsonObject = json.getAsJsonObject();
        Department department = null;
        String lecturersNote = "";
        if (jsonObject.get("department_id") != null)
            department = API.getInstance().getDepartment(jsonObject.get("department_id").getAsInt());
        if (jsonObject.has("lecturers_note") && !jsonObject.get("lecturers_note").isJsonNull()) {
            lecturersNote = jsonObject.get("lecturers_note").getAsString();
        }
        return new Course(jsonObject.get("id").getAsString(), jsonObject.get("name").getAsString(), department, jsonObject.get("type").getAsInt(), jsonObject.get("total_rating").getAsInt(), jsonObject.get("rating_count").getAsInt(), lecturersNote);
    }
}
