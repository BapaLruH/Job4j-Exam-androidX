package ru.job4j.tourist.db.contentprovider;

import android.net.Uri;

public class AppContentProviderData {
    public static final String CONTENT_PROVIDER_AUTHORITY_NAME = "ru.job4j.tourist";
    public static final String SCHEME =  "content";

    public static final String PARAM_GROUP_BY = "GroupBy";
    public static final Uri CONTENT_URI = Uri.parse(SCHEME + "://" + CONTENT_PROVIDER_AUTHORITY_NAME);

    public static final int ID_LOCATION_TABLE = 1;
}
