package net.chiragaggarwal.android.sunshine.data;

import android.database.Cursor;
import android.support.annotation.NonNull;

import static net.chiragaggarwal.android.sunshine.data.ForecastContract.LocationEntry;

public class LocationsRepository {

    private static DatabaseHelper databaseHelper;
    private static LocationsRepository locationsRepository;

    public static LocationsRepository getInstance(DatabaseHelper databaseHelper) {
        if (locationsRepository == null)
            locationsRepository = new LocationsRepository(databaseHelper);
        return locationsRepository;
    }

    private LocationsRepository(DatabaseHelper databaseHelper) {
        this.databaseHelper = databaseHelper;
    }

    public Cursor fetchAll() {
        return this.databaseHelper.getWritableDatabase().rawQuery(buildFetchLocationsQuery(), null);
    }

    @NonNull
    private String buildFetchLocationsQuery() {
        return "SELECT * FROM " + LocationEntry.TABLE_NAME;
    }
}
