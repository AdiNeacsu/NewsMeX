package com.example.newsme.data.local;

import android.widget.ImageView;

import androidx.annotation.NonNull;
import androidx.databinding.BaseObservable;
import androidx.databinding.Bindable;
import androidx.databinding.BindingAdapter;
import androidx.room.Entity;
import androidx.room.PrimaryKey;
import androidx.room.TypeConverters;

import com.bumptech.glide.Glide;
import com.bumptech.glide.request.RequestOptions;
import com.example.newsme.BR;
import com.example.newsme.R;

@Entity(tableName = "articles")
public class ArticleEntity extends BaseObservable {
    @TypeConverters(ArticleEntityConverters.class)
    private SourceLocal source;
    private String author;
    private String title;
    private String description;
    @PrimaryKey
    @NonNull
    private String url;//temporar stabilesc asta ca cheie primara, dar ulterior voi vrea sa retin articole si cere prea multa memorie sa retin url in loc de un Integer
    private String urlToImage;
    private String publishedAt;
    private String content;
    
    public ArticleEntity(SourceLocal source, String author, String title, String description, @NonNull String url, String urlToImage, String publishedAt, String content) {
        this.source = source;
        this.author = author;
        this.title = title;
        this.description = description;
        this.url = url;
        this.urlToImage = urlToImage;
        this.publishedAt = publishedAt;
        this.content = content;
    }

    public SourceLocal getSource() {
        return source;
    }

    public String getAuthor() {
        return author;
    }

    //@Bindable
    public String getTitle() {
        return title;
    }

    public String getDescription() {
        return description;
    }

    @NonNull
    public String getUrl() {
        return url;
    }

    public String getUrlToImage() {
        return urlToImage;
    }

    public String getPublishedAt() {
        return publishedAt;
    }

    public String getContent() {
        return content;
    }

    @BindingAdapter({"urlToImage"})
    public static void loadImage(ImageView imageView, String imageURL) {
        Glide.with(imageView.getContext())
                .setDefaultRequestOptions(new RequestOptions().circleCrop())
                .load(imageURL)
                .placeholder(R.drawable.ic_launcher_foreground)
                .into(imageView);
    }

/*    public void setTitle(String title) {
        this.title = title;
        notifyPropertyChanged(com.example.newsme.BR.title);
    }*/
}

