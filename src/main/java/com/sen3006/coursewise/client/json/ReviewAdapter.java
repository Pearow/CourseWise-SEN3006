package com.sen3006.coursewise.client.json;

import com.google.gson.*;
import com.sen3006.coursewise.client.API;
import com.sen3006.coursewise.client.models.Course;
import com.sen3006.coursewise.client.models.Review;
import com.sen3006.coursewise.client.models.User;

import java.lang.reflect.Type;

public class ReviewAdapter implements JsonDeserializer<Review>, JsonSerializer<Review> {
    @Override
    public Review deserialize(JsonElement json, Type typeOfT, JsonDeserializationContext context) throws JsonParseException {
        JsonObject jsonObject = json.getAsJsonObject();
        API api = API.getInstance();
        // Extract the fields from the JSON object
        Course course = api.getCourse(jsonObject.get("course_id").getAsString());
        User user = api.getUser(jsonObject.get("user_id").getAsInt());

        // Create a new Review object using the extracted fields
        return new Review(course, jsonObject.get("comment").getAsString(), user, jsonObject.get("rating").getAsInt(), false);
    }

    @Override
    public JsonElement serialize(Review src, Type typeOfSrc, JsonSerializationContext context) {
        JsonObject jsonObject = new JsonObject();
        // Add the fields to the JSON object
        jsonObject.addProperty("course_id", src.getCourse().getCourse_id());
        jsonObject.addProperty("user_id", src.getUser().getId());
        jsonObject.addProperty("comment", src.getComment());
        jsonObject.addProperty("rating", src.getRating());

        return jsonObject;
    }
}
