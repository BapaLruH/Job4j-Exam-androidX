package ru.job4j.tourist.dagger;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;

import dagger.Module;
import dagger.Provides;
import ru.job4j.tourist.db.AppContentResolverWrapper;
import ru.job4j.tourist.db.contentprovider.DBHelper;
import ru.job4j.tourist.db.dao.LocationDao;
import ru.job4j.tourist.db.mapper.LocationMapper;

@Module
public class DBManagerModule {

    @AppScope
    @Provides
    LocationMapper provideLocationMapper() {
        return new LocationMapper();
    }

    @AppScope
    @Provides
    AppContentResolverWrapper provideAppResolver(Context context){
        return new AppContentResolverWrapper(context);
    }

    @AppScope
    @Provides
    LocationDao provideLocationDao(AppContentResolverWrapper appContentResolverWrapper, LocationMapper mapper) {
        return new LocationDao(appContentResolverWrapper, mapper);
    }

    @AppScope
    @Provides
    SQLiteDatabase provideDatabase(Context context) {
        return new DBHelper(context, "db_locations").getWritableDatabase();
    }
}
