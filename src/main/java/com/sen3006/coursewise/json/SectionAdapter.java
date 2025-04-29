package com.sen3006.coursewise.json;

import com.google.gson.*;
import com.sen3006.coursewise.API;
import com.sen3006.coursewise.models.Classroom;
import com.sen3006.coursewise.models.Course;
import com.sen3006.coursewise.models.Professor;
import com.sen3006.coursewise.models.Section;

import java.lang.reflect.Type;
import java.time.LocalTime;

public class SectionAdapter implements JsonSerializer<Section>, JsonDeserializer<Section> {
    @Override
    public JsonElement serialize(Section section, Type typeOfSrc, JsonSerializationContext context) {
        JsonObject jsonObject = new JsonObject();
        jsonObject.addProperty("id", section.getSection_id());
        jsonObject.addProperty("course_id", section.getCourse().getCourse_id());
        jsonObject.addProperty("professor_id", section.getProfessor().getId());
        jsonObject.addProperty("start_time", section.getStart_time().toString());
        jsonObject.addProperty("end_time", section.getEnd_time().toString());
        jsonObject.addProperty("day", section.getSection_day().getIntWeekday());
        jsonObject.addProperty("classroom_name", section.getClassroom().getClass_id());
        jsonObject.addProperty("type", section.getType().getIntType());
        return jsonObject;
    }

    @Override
    public Section deserialize(JsonElement json, Type typeOfT, JsonDeserializationContext context) throws JsonParseException {
        JsonObject jsonObject = json.getAsJsonObject();

        Classroom classroom = API.getInstance().getClassroom(jsonObject.get("classroom_name").getAsString());
        Course course = API.getInstance().getCourse(jsonObject.get("course_id").getAsString());
        Professor professor = API.getInstance().getProfessor(jsonObject.get("professor_id").getAsInt());
        return new Section(jsonObject.get("id").getAsInt(), LocalTime.parse(jsonObject.get("start_time").getAsString()), LocalTime.parse(jsonObject.get("end_time").getAsString()), jsonObject.get("day").getAsInt(), classroom, course, professor, jsonObject.get("type").getAsInt());
    }
}
