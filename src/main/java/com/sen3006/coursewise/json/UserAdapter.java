package com.sen3006.coursewise.json;

import com.google.gson.*;

import java.lang.reflect.Type;

public class UserAdapter implements JsonSerializer<UserPassword>, JsonDeserializer<UserPassword> {
    @Override
    public JsonElement serialize(UserPassword user, Type typeOfSrc, JsonSerializationContext context) {
        JsonObject jsonObject = new JsonObject();
        jsonObject.addProperty("id", user.getId());
        jsonObject.addProperty("name", user.getName());
        jsonObject.addProperty("surname", user.getSurname());
        jsonObject.addProperty("email", user.getEmail());
        jsonObject.addProperty("password", user.getPassword());
        return jsonObject;
    }
    @Override
    public UserPassword deserialize(JsonElement json, Type typeOfT, JsonDeserializationContext context) throws JsonParseException {
        JsonObject jsonObject = json.getAsJsonObject();
        return new UserPassword(jsonObject.get("id").getAsInt(), jsonObject.get("name").getAsString(), jsonObject.get("surname").getAsString(), jsonObject.get("email").getAsString(), jsonObject.get("password").getAsString());
    }
}
