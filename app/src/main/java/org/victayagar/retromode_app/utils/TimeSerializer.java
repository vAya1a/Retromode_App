package org.victayagar.retromode_app.utils;

import com.google.gson.*;

import java.lang.reflect.Type;
import java.sql.Time;

public class TimeSerializer implements JsonDeserializer<Time>, JsonSerializer<Time> {
    @Override
    public Time deserialize(JsonElement json, Type typeOfT, JsonDeserializationContext context) throws JsonParseException {

        return Time.valueOf(json.getAsString());
    }

    @Override
    public JsonElement serialize(Time src, Type typeOfSrc, JsonSerializationContext context) {
        return new JsonPrimitive(src.getHours() + ":" + src.getMinutes() + ":" + src.getSeconds());
    }
}
