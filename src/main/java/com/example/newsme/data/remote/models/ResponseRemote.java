package com.example.newsme.data.remote.models;

import java.util.List;

public class ResponseRemote {
    private String status;
    private Integer totalResults;
    private List<ArticleRemote> articles;

    public ResponseRemote(String status, Integer totalResults, List<ArticleRemote> articles) {
        this.status = status;
        this.totalResults = totalResults;
        this.articles = articles;
    }

    public String getStatus() {
        return status;
    }

    public Integer getTotalResults() {
        return totalResults;
    }

    public List<ArticleRemote> getArticles() {
        return articles;
    }
}
