package com.sen3006.coursewise.json;

import com.google.gson.*;
import com.sen3006.coursewise.models.Department;

import java.lang.reflect.Type;

public class DepartmentAdapter implements JsonSerializer<Department>, JsonDeserializer<Department> {
    @Override
    public JsonElement serialize(Department department, Type typeOfSrc, JsonSerializationContext context) {
        JsonObject jsonObject = new JsonObject();
        jsonObject.addProperty("id", department.getDepartment_id());
        jsonObject.addProperty("name", department.getDepartment_name());
        if (department.getFaculty_name() != null)
            jsonObject.addProperty("faculty_name", department.getFaculty_name());

        return jsonObject;
    }

    @Override
    public Department deserialize(JsonElement json, Type typeOfT, JsonDeserializationContext context) throws JsonParseException {
        JsonObject jsonObject = json.getAsJsonObject();
        String faculty = null;
        if (jsonObject.get("faculty_name") != null)
            faculty = jsonObject.get("faculty_name").getAsString();
        return new Department(jsonObject.get("id").getAsInt(), jsonObject.get("name").getAsString(), faculty);
    }
}
