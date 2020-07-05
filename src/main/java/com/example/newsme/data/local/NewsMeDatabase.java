package com.example.newsme.data.local;

import androidx.room.Database;
import androidx.room.RoomDatabase;
import androidx.room.TypeConverters;

@Database(entities = {ArticleEntity.class, LastOpenedEntity.class}, version = 2)
@TypeConverters(ArticleEntityConverters.class)
public abstract class NewsMeDatabase extends RoomDatabase {
    public abstract ArticleDao articleDao();
}
