package net.chiragaggarwal.android.sunshine.data;

import android.content.ContentProvider;
import android.content.ContentValues;
import android.content.UriMatcher;
import android.database.Cursor;
import android.net.Uri;
import android.support.annotation.Nullable;

public class LocationsProvider extends ContentProvider {
    private static UriMatcher uriMatcher = new UriMatcher(UriMatcher.NO_MATCH);

    private static final int LOCATIONS_ENDPOINT_CODE = 1;

    static {
        uriMatcher.addURI(ForecastContract.LocationEntry.LOCATIONS_PROVIDER_AUTHORITY,
                ForecastContract.LocationEntry.LOCATIONS_PATH,
                LOCATIONS_ENDPOINT_CODE);
    }

    @Override
    public boolean onCreate() {
        return false;
    }

    @Nullable
    @Override
    public Cursor query(Uri uri, String[] projection, String selection, String[] selectionArgs, String sortOrder) {
        return null;
    }

    @Nullable
    @Override
    public String getType(Uri uri) {
        int matchCode = uriMatcher.match(uri);
        switch (matchCode) {
            case LOCATIONS_ENDPOINT_CODE:
                return ForecastContract.LocationEntry.DATA_TYPE_LOCATIONS_COLLECTION;
        }
        return null;
    }

    @Nullable
    @Override
    public Uri insert(Uri uri, ContentValues values) {
        return null;
    }

    @Override
    public int delete(Uri uri, String selection, String[] selectionArgs) {
        return 0;
    }

    @Override
    public int update(Uri uri, ContentValues values, String selection, String[] selectionArgs) {
        return 0;
    }
}
