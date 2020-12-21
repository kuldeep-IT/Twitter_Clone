package com.example.twitterclone;

import android.app.Application;

import com.parse.Parse;

public class App extends Application {

    @Override
    public void onCreate() {
        super.onCreate();

        Parse.initialize(new Parse.Configuration.Builder(this)
                .applicationId("I9b9HR3Bl7h2ywNepa15Klz5QhMoKT9F0vHGORWs")
                // if defined
                .clientKey("HemQylu8DToIukzYM1pNgywdFaI5s39cZLfhsaeR")
                .server("https://parseapi.back4app.com/")
                .build()
        );

    }
}
