package ru.job4j.tourist;

import android.app.Application;

import ru.job4j.tourist.dagger.AppComponent;
import ru.job4j.tourist.dagger.ContextModule;
import ru.job4j.tourist.dagger.DBManagerModule;
import ru.job4j.tourist.dagger.DaggerAppComponent;

public class App extends Application {
    private static AppComponent component;

    public App() {
        component = DaggerAppComponent.builder()
                .contextModule(new ContextModule(this))
                .dBManagerModule(new DBManagerModule())
                .build();
    }

    @Override
    public void onCreate() {
        super.onCreate();
    }

    public static AppComponent getComponent() {
        return component;
    }
}
