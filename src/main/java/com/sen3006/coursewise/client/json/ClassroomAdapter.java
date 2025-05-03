package com.sen3006.coursewise.client.json;

import com.google.gson.*;
import com.sen3006.coursewise.client.models.Classroom;

import java.lang.reflect.Type;

public class ClassroomAdapter implements JsonSerializer<Classroom>, JsonDeserializer<Classroom> {
    @Override
    public JsonElement serialize(Classroom classroom, Type typeOfSrc, JsonSerializationContext context) {
        JsonObject jsonObject = new JsonObject();
        jsonObject.addProperty("campus", classroom.getCampus().getIntCampus());
        jsonObject.addProperty("id", classroom.getClass_id());
        return jsonObject;
    }

    @Override
    public Classroom deserialize(JsonElement json, Type typeOfT, JsonDeserializationContext context) throws JsonParseException {
        JsonObject jsonObject = json.getAsJsonObject();
        return new Classroom(jsonObject.get("campus").getAsInt(), jsonObject.get("id").getAsString());
    }
}
