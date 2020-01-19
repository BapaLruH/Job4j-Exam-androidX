package ru.job4j.tourist.dagger;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;

import dagger.Component;
import ru.job4j.tourist.activity.LocationListActivity;
import ru.job4j.tourist.activity.MapsActivity;
import ru.job4j.tourist.db.contentprovider.AppContentProvider;
import ru.job4j.tourist.db.dao.LocationDao;
import ru.job4j.tourist.db.mapper.LocationMapper;

@AppScope
@Component(modules = {DBManagerModule.class, ContextModule.class})
public interface AppComponent {
    LocationMapper getLocationMapper();
    Context getContext();
    LocationDao getLocationDao();
    SQLiteDatabase getSqLiteDatabase();
    void inject(LocationListActivity locationListActivity);
    void inject(MapsActivity mapsActivity);
    void inject(AppContentProvider appContentProvider);
}
