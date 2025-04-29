package com.sen3006.coursewise.json;

import com.google.gson.*;
import com.sen3006.coursewise.API;
import com.sen3006.coursewise.models.*;

import java.lang.reflect.Type;

public class RatingAdapter implements JsonDeserializer<Rating>, JsonSerializer<Rating> {
    @Override
    public Rating deserialize(JsonElement json, Type typeOfT, JsonDeserializationContext context) throws JsonParseException {
        JsonObject jsonObject = json.getAsJsonObject();
        API api = API.getInstance();
        // Extract the fields from the JSON object
        Professor professor = api.getProfessor(jsonObject.get("professor_id").getAsInt());
        User user = api.getUser(jsonObject.get("user_id").getAsInt());

        // Create a new Review object using the extracted fields
        return new Rating(professor,user, jsonObject.get("rating").getAsInt(), false);
    }

    @Override
    public JsonElement serialize(Rating src, Type typeOfSrc, JsonSerializationContext context) {
        JsonObject jsonObject = new JsonObject();
        // Add the fields to the JSON object
        jsonObject.addProperty("professor_id", src.getProfessor().getId());
        jsonObject.addProperty("user_id", src.getUser().getId());
        jsonObject.addProperty("rating", src.getRating());

        return jsonObject;
    }
}
