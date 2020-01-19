package ru.job4j.tourist.db.mapper;

import android.content.ContentValues;
import android.database.Cursor;

import ru.job4j.tourist.db.dbhelper.DBClass;
import ru.job4j.tourist.db.model.ModelLocation;

public class LocationMapper extends BaseMapper<ModelLocation> {
    @Override
    public ContentValues getContentValues(DBClass object) {
        ContentValues cv = new ContentValues();
        if (object instanceof ModelLocation) {
            ModelLocation modelLocation = (ModelLocation) object;
            if (modelLocation._id != null) {
                cv.put("_id", modelLocation._id);
            }
            if (modelLocation.title != null) {
                cv.put("title", modelLocation.title);
            }
            if (modelLocation.latitude != null) {
                cv.put("latitude", modelLocation.latitude);
            }
            if (modelLocation.longitude != null) {
                cv.put("longitude", modelLocation.longitude);
            }
        }
        return cv;
    }

    @Override
    public ModelLocation getFromCursor(Cursor cursor) {
        ModelLocation modelLocation = new ModelLocation();
        modelLocation._id =(Integer) getDataFromCursor(cursor, "_id", Integer.class);
        modelLocation.title = (String) getDataFromCursor(cursor, "title", String.class);
        modelLocation.latitude = Double.parseDouble((String) getDataFromCursor(cursor, "latitude", String.class));
        modelLocation.longitude = Double.parseDouble((String) getDataFromCursor(cursor, "longitude", String.class));
        return modelLocation;
    }
}
