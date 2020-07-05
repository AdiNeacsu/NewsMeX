package com.example.newsme;

import android.app.Application;

import androidx.room.Room;

import com.example.newsme.data.NewsMeRepository;
import com.example.newsme.data.local.NewsMeDatabase;

import timber.log.Timber;

public class InitApp extends Application {

    private static InitApp INSTANCE;
    private NewsMeRepository newsMeRepository;
    private NewsMeDatabase newsMeDatabase;

    @Override
    public void onCreate() {
        super.onCreate();

        INSTANCE = this;

        if (BuildConfig.DEBUG){
            Timber.plant(new Timber.DebugTree());
        }

        newsMeDatabase = Room.databaseBuilder(this, NewsMeDatabase.class, "newsMe")
                                .fallbackToDestructiveMigration()       // !!!!! temporar ; o scot la final cand am toata baza gata
                                .build();
        newsMeRepository = new NewsMeRepository();
    }

    public static InitApp getInitApp() {
        return INSTANCE;
    }

    public NewsMeDatabase getNewsMeDatabase() {
        return newsMeDatabase;
    }

    public NewsMeRepository getNewsMeRepository() {
        return newsMeRepository;
    }

}
