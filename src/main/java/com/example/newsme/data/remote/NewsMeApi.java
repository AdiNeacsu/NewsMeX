package com.example.newsme.data.remote;

import com.example.newsme.data.remote.models.ResponseRemote;

import io.reactivex.Single;
import retrofit2.http.GET;
import retrofit2.http.Query;

public interface NewsMeApi {
    //cere response din endpointul "everything" dupa expresia de cautare pusa in query
    @GET("everything")
    Single<ResponseRemote> fetchEverything(@Query("q") String query, @Query("language") String language);

    //intoarcerea stirilor pe categorii predefinite e doar in "top-headlines"
    @GET("top-headlines")
    Single<ResponseRemote> fetchTopheadlines(@Query("category") String query, @Query("country") String country);
}
