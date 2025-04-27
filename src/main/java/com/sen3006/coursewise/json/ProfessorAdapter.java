package com.sen3006.coursewise.json;

import com.google.gson.*;
import com.sen3006.coursewise.models.Professor;

import java.lang.reflect.Type;

public class ProfessorAdapter implements JsonSerializer<Professor>, JsonDeserializer<Professor> {
    @Override
    public JsonElement serialize(Professor professor, Type typeOfSrc, JsonSerializationContext context) {
        JsonObject jsonObject = new JsonObject();
        jsonObject.addProperty("id", professor.getId());
        jsonObject.addProperty("name", professor.getName());
        jsonObject.addProperty("surname", professor.getSurname());
        jsonObject.addProperty("email", professor.getEmail());
        jsonObject.addProperty("total_rating", professor.getTotalRating());
        jsonObject.addProperty("rating_count", professor.getRatingCount());

        return jsonObject;
    }

    @Override
    public Professor deserialize(JsonElement json, Type typeOfT, JsonDeserializationContext context) throws JsonParseException {
        JsonObject jsonObject = json.getAsJsonObject();
        return new Professor(jsonObject.get("id").getAsInt(), jsonObject.get("name").getAsString(), jsonObject.get("surname").getAsString(), jsonObject.get("email").getAsString(), jsonObject.get("total_rating").getAsInt(), jsonObject.get("rating_count").getAsInt());
    }
}
