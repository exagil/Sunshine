package net.chiragaggarwal.android.sunshine.data;

import android.content.ContentProvider;
import android.content.ContentValues;
import android.content.UriMatcher;
import android.database.Cursor;
import android.net.Uri;
import android.support.annotation.Nullable;

public class LocationsProvider extends ContentProvider {
    private static UriMatcher uriMatcher = buildUriMatcher();

    public static final int LOCATIONS_ENDPOINT_CODE = 1;

    @Override
    public boolean onCreate() {
        return false;
    }

    @Nullable
    @Override
    public Cursor query(Uri uri, String[] projection, String selection, String[] selectionArgs, String sortOrder) {
        int matchCode = uriMatcher.match(uri);

        DatabaseHelper databaseHelper = DatabaseHelper.getInstance(getContext());
        LocationsRepository locationsRepository = LocationsRepository.getInstance(databaseHelper);

        switch (matchCode) {
            case LOCATIONS_ENDPOINT_CODE:
                Cursor locationsCursor = locationsRepository.fetchAll();
                locationsCursor.setNotificationUri(getContext().getContentResolver(), uri);
                return locationsCursor;
        }
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
        Long locationRowid = null;
        if (uriMatcher.match(uri) == LOCATIONS_ENDPOINT_CODE) {
            DatabaseHelper databaseHelper = DatabaseHelper.getInstance(getContext());
            LocationsRepository locationsRepository = LocationsRepository.getInstance(databaseHelper);
            locationRowid = locationsRepository.insert(values);
        }
        return buildUriForNewLocationHavingId(locationRowid, uri);
    }

    @Override
    public int delete(Uri uri, String selection, String[] selectionArgs) {
        return 0;
    }

    @Override
    public int update(Uri uri, ContentValues values, String selection, String[] selectionArgs) {
        return 0;
    }

    public static UriMatcher buildUriMatcher() {
        UriMatcher uriMatcher = new UriMatcher(UriMatcher.NO_MATCH);
        uriMatcher.addURI(ForecastContract.LocationEntry.LOCATIONS_PROVIDER_AUTHORITY,
                ForecastContract.LocationEntry.LOCATIONS_PATH,
                LOCATIONS_ENDPOINT_CODE);
        return uriMatcher;
    }

    private Uri buildUriForNewLocationHavingId(Long rowId, Uri uri) {
        String rowIdPath = rowId.toString();
        return uri.buildUpon().appendPath(rowIdPath).build();
    }
}
