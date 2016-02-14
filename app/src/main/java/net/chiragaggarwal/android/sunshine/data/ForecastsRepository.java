package net.chiragaggarwal.android.sunshine.data;

import android.content.ContentValues;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
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

    public Cursor query(String[] projection, String selection, String[] selectionArgs,
                        String sortOrder) {
        return this.databaseHelper.getReadableDatabase().
                query(ForecastEntry.TABLE_NAME, projection, selection, selectionArgs,
                        null, null, sortOrder);
    }

    public Cursor findForecastsByLocationSelectionAndStartDateIfAny(String locationSelection,
                                                                    String dateSelectionArgs) {
        SQLiteDatabase db = this.databaseHelper.getReadableDatabase();
        if (dateSelectionArgs == null) {
            return db.rawQuery(forecastsByLocationSettingSqlStatement(),
                    new String[]{locationSelection});
        } else {
            return db.rawQuery(forecastsByLocationSettingAndStartDateSqlStatement(),
                    new String[]{locationSelection, dateSelectionArgs});
        }
    }

    public Cursor findForecastByLocationSelectionAndDate(String selection, String date) {
        return this.databaseHelper.getReadableDatabase().
                rawQuery(forecastsByLocationSettingAndDateSQLStatement(),
                        new String[]{selection, date});
    }

    public Long insert(ContentValues values) {
        return this.databaseHelper.getWritableDatabase().
                insert(ForecastEntry.TABLE_NAME, null, values);
    }

    public int deleteAll() {
        return this.databaseHelper.getWritableDatabase().delete(ForecastEntry.TABLE_NAME, null, null);
    }

    public int bulkInsert(ContentValues[] values) {
        SQLiteDatabase db = this.databaseHelper.getWritableDatabase();
        int numberOfForecastsInserted = bulkInsertForecastsAsTransaction(values, db);
        return numberOfForecastsInserted;
    }

    private String forecastsByLocationSettingSqlStatement() {
        return forecastsByLocationJoinSQLStatement() + locationSettingSQLStatement();
    }

    @NonNull
    private String forecastsByLocationSettingAndStartDateSqlStatement() {
        return forecastsByLocationJoinSQLStatement() + locationSettingAndStartDateQuerySQLStatement();
    }

    @NonNull
    private String forecastsByLocationSettingAndDateSQLStatement() {
        return forecastsByLocationJoinSQLStatement() + locationSettingAndDateQuerySQLStatement();
    }

    @NonNull
    private String forecastsByLocationJoinSQLStatement() {
        return "SELECT *" + " FROM " + ForecastEntry.TABLE_NAME +
                " INNER JOIN " + LocationEntry.TABLE_NAME + " ON " +
                ForecastEntry.TABLE_NAME + "." + ForecastEntry.COLUMN_LOC_KEY +
                "=" +
                LocationEntry.TABLE_NAME + "." + LocationEntry._ID;
    }

    @NonNull
    private String locationSettingAndStartDateQuerySQLStatement() {
        return locationSettingSQLStatement() + " AND " + startDateSQLStatement();
    }

    @NonNull
    private String locationSettingSQLStatement() {
        return " WHERE " + LocationEntry.COLUMN_LOCATION_SETTING + " =?";
    }

    @NonNull
    private String startDateSQLStatement() {
        return ForecastEntry.COLUMN_DATE + ">=?";
    }

    private String locationSettingAndDateQuerySQLStatement() {
        return locationSettingSQLStatement() + " AND " + ForecastEntry.COLUMN_DATE + "=?";
    }

    private int bulkInsertForecastsAsTransaction(ContentValues[] values, SQLiteDatabase db) {
        int numberOfForecastsInserted = 0;

        db.beginTransaction();
        for (ContentValues value : values) {
            ContentValues forecastContentValueWithNormalizedDate =
                    buildForecastContentValueWithNormalizedDate(value);
            long rowId = db.insert(ForecastEntry.TABLE_NAME, null,
                    forecastContentValueWithNormalizedDate);
            if (isForecastSuccessfullyInserted(rowId)) numberOfForecastsInserted++;
        }
        db.setTransactionSuccessful();
        db.endTransaction();
        return numberOfForecastsInserted;
    }

    private ContentValues buildForecastContentValueWithNormalizedDate(ContentValues value) {
        if (value.containsKey(ForecastEntry.COLUMN_DATE)) {
            Long date = value.getAsLong(ForecastEntry.COLUMN_DATE);
            value.put(ForecastEntry.COLUMN_DATE, ForecastContract.normalizeDate(date));
        }
        return value;
    }

    private boolean isForecastSuccessfullyInserted(long rowId) {
        return rowId != -1;
    }
}
