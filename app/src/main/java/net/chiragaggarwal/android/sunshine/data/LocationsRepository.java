package net.chiragaggarwal.android.sunshine.data;

import android.content.ContentValues;
import android.database.Cursor;

import static net.chiragaggarwal.android.sunshine.data.ForecastContract.LocationEntry;

public class LocationsRepository {

    private static DatabaseHelper databaseHelper;
    private static LocationsRepository locationsRepository;

    public static LocationsRepository getInstance(DatabaseHelper databaseHelper) {
        if (locationsRepository == null)
            locationsRepository = new LocationsRepository(databaseHelper);
        return locationsRepository;
    }

    public Long insert(ContentValues values) {
        return this.databaseHelper.getWritableDatabase().insert(LocationEntry.TABLE_NAME, null, values);
    }

    public int updateLocation(ContentValues values, String selection, String[] selectionArgs) {
        return this.databaseHelper.getWritableDatabase().update(LocationEntry.TABLE_NAME,
                values, selection, selectionArgs);
    }

    public Cursor query(String[] projection, String selection, String[] selectionArgs, String sortOrder) {
        return this.databaseHelper.getReadableDatabase().
                query(LocationEntry.TABLE_NAME, projection, selection,
                        selectionArgs, null, null, sortOrder);
    }

    private LocationsRepository(DatabaseHelper databaseHelper) {
        this.databaseHelper = databaseHelper;
    }
}
