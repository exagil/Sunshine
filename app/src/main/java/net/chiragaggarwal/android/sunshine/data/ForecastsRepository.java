package net.chiragaggarwal.android.sunshine.data;

import android.content.ContentValues;
import android.database.Cursor;
import android.support.annotation.NonNull;

import static net.chiragaggarwal.android.sunshine.data.ForecastContract.ForecastEntry;
import static net.chiragaggarwal.android.sunshine.data.ForecastContract.LocationEntry;

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

    public Cursor findForecastsByLocationSelection(String locationSelection) {
        return this.databaseHelper.getReadableDatabase().
                rawQuery(forecastsByLocationSettingSqlStatement(), new String[]{locationSelection});
    }

    public Cursor findForecastsByLocationSelectionAndDate(String selection, String date) {
        return this.databaseHelper.getReadableDatabase().
                rawQuery(forecastsByLocationSettingAndDateSQLStatement(),
                        new String[]{selection, date});
    }

    @NonNull
    private String buildFetchForecastsQuery() {
        return "SELECT * FROM " + ForecastEntry.TABLE_NAME;
    }

    @NonNull
    private String forecastsByLocationSettingSqlStatement() {
        return forecastsByLocationJoinSQLStatement() + locationSettingQuerySQLStatement();
    }

    @NonNull
    private String forecastsByLocationSettingAndDateSQLStatement() {
        return forecastsByLocationJoinSQLStatement() + locationSettingAndDateQuerySQLStatement();
    }

    @NonNull
    private String forecastsByLocationJoinSQLStatement() {
        return "SELECT " + ForecastEntry.FORECAST_COLUMNS + " FROM " + ForecastEntry.TABLE_NAME +
                " INNER JOIN " + LocationEntry.TABLE_NAME + " ON " +
                ForecastEntry.TABLE_NAME + "." + ForecastEntry.COLUMN_LOC_KEY +
                "=" +
                LocationEntry.TABLE_NAME + "." + LocationEntry._ID;
    }

    @NonNull
    private String locationSettingQuerySQLStatement() {
        return " WHERE " + LocationEntry.COLUMN_LOCATION_SETTING + " =?";
    }

    private String locationSettingAndDateQuerySQLStatement() {
        return locationSettingQuerySQLStatement() + " AND " + ForecastEntry.COLUMN_DATE + " =?";
    }

    public Integer insert(ContentValues values) {
        return null;
    }
}
