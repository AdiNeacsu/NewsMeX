package com.example.newsme.data.remote.models;

import com.example.newsme.data.local.ArticleEntity;

public class ArticleRemote {
    private SourceRemote source;
    private String author;
    private String title;
    private String description;
    private String url;
    private String urlToImage;
    private String publishedAt;
    private String content;

    public ArticleRemote(SourceRemote source, String author, String title, String description, String url, String urlToImage, String publishedAt, String content) {
        this.source = source;
        this.author = author;
        this.title = title;
        this.description = description;
        this.url = url;
        this.urlToImage = urlToImage;
        this.publishedAt = publishedAt;
        this.content = content;
    }

    public SourceRemote getSource() {
        return source;
    }

    public String getAuthor() {
        return author;
    }

    public String getTitle() {
        return title;
    }

    public String getDescription() {
        return description;
    }

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

    //adaug metoda de transformat un articol din ArticleRemote in ArticleEntity
    public ArticleEntity toEntity(){
        //cere in prealabil si transformarea din SourceRemote in SourceLocal
        return new ArticleEntity(source.toLocal(),author,title,description,url,urlToImage,publishedAt,content);
    }
}
