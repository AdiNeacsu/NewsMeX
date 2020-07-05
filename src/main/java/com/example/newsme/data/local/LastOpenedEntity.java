package com.example.newsme.data.local;

import androidx.annotation.NonNull;
import androidx.room.Entity;
import androidx.room.ForeignKey;
import androidx.room.PrimaryKey;

@Entity(tableName = "last_opened", foreignKeys = @ForeignKey(entity = ArticleEntity.class, parentColumns = "url", childColumns = "urlLastOpened",
                                                                onDelete = ForeignKey.RESTRICT, onUpdate = ForeignKey.RESTRICT))
public class LastOpenedEntity {
    @PrimaryKey
    @NonNull
    private String urlLastOpened;

    public LastOpenedEntity(@NonNull String urlLastOpened) {
        this.urlLastOpened = urlLastOpened;
    }

    @NonNull
    public String getUrlLastOpened() {
        return urlLastOpened;
    }

    public void setUrlLastOpened(@NonNull String urlLastOpened) {
        this.urlLastOpened = urlLastOpened;
    }
}
