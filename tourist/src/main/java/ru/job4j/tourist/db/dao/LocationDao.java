package ru.job4j.tourist.db.dao;

import android.content.ContentValues;
import android.database.Cursor;
import android.net.Uri;

import java.util.ArrayList;
import java.util.List;

import io.reactivex.Single;
import ru.job4j.tourist.db.AppContentResolverWrapper;
import ru.job4j.tourist.db.contentprovider.AppContentProviderHelper;
import ru.job4j.tourist.db.mapper.LocationMapper;
import ru.job4j.tourist.db.model.ModelLocation;

public class LocationDao {
    private final AppContentResolverWrapper appContentResolverWrapper;
    private final LocationMapper mapper;

    public LocationDao(AppContentResolverWrapper appContentResolverWrapper, LocationMapper mapper) {
        this.appContentResolverWrapper = appContentResolverWrapper;
        this.mapper = mapper;
    }

    private List<ModelLocation> getLocations() {
        Cursor cursor = appContentResolverWrapper._query(AppContentProviderHelper.Uris.URI_LOCATION_TABLE, null, null, null, null);
        List<ModelLocation> modelLocations = new ArrayList<>();
        if (cursor != null) {
            modelLocations.addAll(mapper.getListFromCursor(cursor));
            cursor.close();
        }
        return modelLocations;
    }

    private ModelLocation getLocation(String id) {
        Cursor cursor = appContentResolverWrapper._query(AppContentProviderHelper.Uris.URI_LOCATION_TABLE, null, "_id = ?", new String[]{id}, null);
        ModelLocation modelLocation = null;
        if (cursor != null) {
            modelLocation = mapper.getFromCursor(cursor);
            cursor.close();
        }
        return modelLocation;
    }

    private int create(ModelLocation modelLocation) {
        Uri newUri = AppContentProviderHelper.Uris.URI_LOCATION_TABLE;
        ContentValues cv = mapper.getContentValues(modelLocation);
        Uri uri = appContentResolverWrapper._insert(newUri, cv);
        int localIndexId = 0;
        if (uri != null) {
            localIndexId = Integer.parseInt(uri.getLastPathSegment());
        }
        return localIndexId;
    }

    private int update(ModelLocation modelLocation) {
        Uri newUri = AppContentProviderHelper.Uris.URI_LOCATION_TABLE;
        ContentValues cv = mapper.getContentValues(modelLocation);
        return appContentResolverWrapper._update(newUri, cv, null, null);
    }

    private int delete(String id) {
        return appContentResolverWrapper._delete(AppContentProviderHelper.Uris.URI_LOCATION_TABLE, "_id = '" + id + "'", null);
    }

    private int bulkInsert(List<ModelLocation> comments) {
        return appContentResolverWrapper._bulkInsert(AppContentProviderHelper.Uris.URI_LOCATION_TABLE, mapper.getContentValuesList(comments));
    }


    public Single<ModelLocation> getLocationById(String id) {
        return Single.create(emitter -> emitter.onSuccess(getLocation(id)));
    }

    public Single<List<ModelLocation>> getAllLocation() {
        return Single.create(emitter -> emitter.onSuccess(getLocations()));
    }

    public Single<Integer> createLocation(ModelLocation modelLocation) {
        return Single.create(emitter -> emitter.onSuccess(create(modelLocation)));
    }

    public Single<Integer> updateLocation(ModelLocation modelLocation) {
        return Single.create(emitter -> emitter.onSuccess(update(modelLocation)));
    }

    public Single<Integer> deleteLocation(String id) {
        return Single.create(emitter -> emitter.onSuccess(delete(id)));
    }

    public Single<Integer> insertList(List<ModelLocation> modelLocations) {
        return Single.create(emitter -> emitter.onSuccess(bulkInsert(modelLocations)));
    }

}
