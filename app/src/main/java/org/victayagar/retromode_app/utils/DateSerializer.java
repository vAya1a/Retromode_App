package org.victayagar.retromode_app.utils;

import com.google.gson.*;

import java.lang.reflect.Type;
import java.sql.Date;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.TimeZone;

public class DateSerializer implements JsonDeserializer<Date>, JsonSerializer<Date> {
    @Override
    public Date deserialize(JsonElement je, Type type, JsonDeserializationContext jdc) throws JsonParseException {
        String date = je.getAsString();
        SimpleDateFormat formatter = new SimpleDateFormat("dd-MM-yyyy");
        formatter.setTimeZone(TimeZone.getDefault());
        try {
            return new Date(formatter.parse(date).getTime());
        } catch (ParseException e) {
            System.err.println("Failed to parse Date due to:" + e.getMessage());
            return null;
        }
    }

    @Override
    public JsonElement serialize(Date date, Type typeOfSrc, JsonSerializationContext context) {
        SimpleDateFormat formatter = new SimpleDateFormat("dd-MM-yyyy");
        formatter.setTimeZone(TimeZone.getDefault());
        try {
            return new JsonPrimitive(formatter.format(date));
        } catch (Exception e) {
            System.err.println("Failed to parse Date due to:" + e.getMessage());
            return null;
        }
    }
}
