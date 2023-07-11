package com.example.pawsome.utils;

import android.app.Application;

public class App extends Application {

    @Override
    public void onCreate() {
        super.onCreate();
        SignalUtils.init(this);
    }
}
