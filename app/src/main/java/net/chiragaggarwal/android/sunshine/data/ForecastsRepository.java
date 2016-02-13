package net.chiragaggarwal.android.sunshine.data;

import android.database.Cursor;
import android.support.annotation.NonNull;

import static net.chiragaggarwal.android.sunshine.data.ForecastContract.ForecastEntry;

public class ForecastsRepository {

    private static DatabaseHelper databaseHelper;
    private static ForecastsRepository forecastsRepository;

    public static ForecastsRepository getInstance(DatabaseHelper databaseHelper) {
        if (forecastsRepository == null)
            forecastsRepository = new ForecastsRepository(databaseHelper);
        return forecastsRepository;
    }

    private ForecastsRepository(DatabaseHelper databaseHelper) {
        this.databaseHelper = databaseHelper;
    }

    public Cursor fetchAll() {
        return this.databaseHelper.getWritableDatabase().rawQuery(buildFetchForecastsQuery(), null);
    }

    @NonNull
    private String buildFetchForecastsQuery() {
        return "SELECT * FROM " + ForecastEntry.TABLE_NAME;
    }
}
