package com.example.newsme.data.remote;

import com.example.newsme.data.remote.models.ResponseRemote;

import org.jetbrains.annotations.NotNull;

import java.io.IOException;
import java.io.UnsupportedEncodingException;
import java.net.URLEncoder;

import io.reactivex.Single;
import okhttp3.Interceptor;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.Response;
import retrofit2.Retrofit;
import retrofit2.adapter.rxjava2.RxJava2CallAdapterFactory;
import retrofit2.converter.gson.GsonConverterFactory;
import timber.log.Timber;

public class NewsMeWebService implements NewsMeApi {
    private static Retrofit retrofit;
    private static final String API_KEY = "2f084824960f4bf486c7c66b60a765d0";
    private static final String BASE_URL = "https://newsapi.org/v2/";
    private final NewsMeApi newsMeApi;

    private static Retrofit getRetrofit() {
        if (retrofit == null){
            OkHttpClient okHttpClient = new OkHttpClient.Builder()
                    .addInterceptor(new Interceptor() {
                        @NotNull
                        @Override
                        public Response intercept(@NotNull Chain chain) throws IOException {
                            Request request = chain.request().newBuilder()
                                    .addHeader("pageSize", "60")
                                    .addHeader("X-Api-Key", API_KEY).build();
                            return chain.proceed(request);
                        }
                    }).build();
            retrofit = new Retrofit.Builder()
                    .baseUrl(BASE_URL)
                    .addConverterFactory(GsonConverterFactory.create())
                    .addCallAdapterFactory(RxJava2CallAdapterFactory.create())
                    .client(okHttpClient)
                    .build();
        }
        return retrofit;
    }

    public NewsMeWebService(){
        newsMeApi = getRetrofit().create(NewsMeApi.class);
    }

    public Single<ResponseRemote> fetchEverything(String query, String language) {
        return newsMeApi.fetchEverything(query, language);
    }

    public Single<ResponseRemote> fetchTopheadlines(String query, String country){
        return newsMeApi.fetchTopheadlines(query, country);
    }

}
