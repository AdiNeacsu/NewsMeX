package com.example.newsme.data.local;

import androidx.room.TypeConverter;

import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;

import java.lang.reflect.Type;

public class ArticleEntityConverters {

    @TypeConverter
    public static SourceLocal storedStringToSourceLocal(String data) {
        Gson gson = new Gson();
        if (data == null) {
            return new SourceLocal(null,null);
        }
        Type objectType = new TypeToken<SourceLocal>() {}.getType();
        return gson.fromJson(data, objectType);
    }

    @TypeConverter
    public static String sourceLocalToStoredString(SourceLocal sourceLocal) {
        Gson gson = new Gson();
        return gson.toJson(sourceLocal);
    }

}
