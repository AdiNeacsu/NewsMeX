package com.example.newsme.data.remote.models;

import com.example.newsme.data.local.SourceLocal;

public class SourceRemote {
    private String id;
    private String name;

    public SourceRemote(String id, String name) {
        this.id = id;
        this.name = name;
    }

    public String getId() {
        return id;
    }

    public String getName() {
        return name;
    }

    //transformarea din SourceRemote in SourceLocal
    public SourceLocal toLocal(){
        return new SourceLocal(id,name);
    }
}
