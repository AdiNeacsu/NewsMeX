package com.example.newsme.data.remote.models;

import java.util.List;

public class ResponseSourcesRemote {
    private String status;
    private List<SourcesRemote> sources;

    public ResponseSourcesRemote(String status, List<SourcesRemote> sources) {
        this.status = status;
        this.sources = sources;
    }

    public String getStatus() {
        return status;
    }

    public List<SourcesRemote> getSources() {
        return sources;
    }
}
