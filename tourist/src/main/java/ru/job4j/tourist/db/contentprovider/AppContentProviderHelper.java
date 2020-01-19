package ru.job4j.tourist.db.contentprovider;

import android.net.Uri;

import ru.job4j.tourist.db.model.ModelLocation;

public class AppContentProviderHelper {
    public static class TableNames {
        public static final String LOCATION_TABLE_NAME = ModelLocation.class.getSimpleName();
    }

    public static class Uris {
        public static final Uri URI_LOCATION_TABLE = Uri.withAppendedPath(AppContentProviderData.CONTENT_URI, TableNames.LOCATION_TABLE_NAME);
    }

    public static class UrisBuilder {
        public static final Uri.Builder LOCATION_URI_BUILDER = Uris.URI_LOCATION_TABLE.buildUpon();
    }
}
