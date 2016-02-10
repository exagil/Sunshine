package com.example.android.sunshine.app.data;

import android.content.ContentValues;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.test.AndroidTestCase;

import net.chiragaggarwal.android.sunshine.data.DatabaseHelper;

import java.util.HashSet;

import static com.example.android.sunshine.app.data.TestUtilities.createWeatherValues;
import static net.chiragaggarwal.android.sunshine.data.ForecastContract.ForecastEntry;
import static net.chiragaggarwal.android.sunshine.data.ForecastContract.LocationEntry;

public class TestDb extends AndroidTestCase {
    void deleteTheDatabase() {
        mContext.deleteDatabase(DatabaseHelper.DATABASE_NAME);
    }

    public void setUp() {
        deleteTheDatabase();
    }

    public void testCreateDb() throws Throwable {
        final HashSet<String> tableNameHashSet = new HashSet<String>();
        tableNameHashSet.add(LocationEntry.TABLE_NAME);
        tableNameHashSet.add(ForecastEntry.TABLE_NAME);

        mContext.deleteDatabase(DatabaseHelper.DATABASE_NAME);
        SQLiteDatabase db = new DatabaseHelper(
                this.mContext).getWritableDatabase();
        assertEquals(true, db.isOpen());

        Cursor tablesCursor = db.rawQuery("SELECT name FROM sqlite_master WHERE type='table'", null);

        assertTrue("Error: This means that the database has not been created correctly",
                tablesCursor.moveToFirst());

        do {
            tableNameHashSet.remove(tablesCursor.getString(0));
        } while (tablesCursor.moveToNext());

        assertTrue("Error: Your database was created without both the location entry and weather entry tables",
                tableNameHashSet.isEmpty());

        tablesCursor = db.rawQuery("PRAGMA table_info(" + LocationEntry.TABLE_NAME + ")",
                null);

        assertTrue("Error: This means that we were unable to query the database for table information.",
                tablesCursor.moveToFirst());

        final HashSet<String> locationColumnHashSet = new HashSet<String>();
        locationColumnHashSet.add(LocationEntry._ID);
        locationColumnHashSet.add(LocationEntry.COLUMN_CITY_NAME);
        locationColumnHashSet.add(LocationEntry.COLUMN_LATITUDE);
        locationColumnHashSet.add(LocationEntry.COLUMN_LONGITUDE);
        locationColumnHashSet.add(LocationEntry.COLUMN_LOCATION_SETTING);

        int columnNameIndex = tablesCursor.getColumnIndex("name");
        do {
            String columnName = tablesCursor.getString(columnNameIndex);
            locationColumnHashSet.remove(columnName);
        } while (tablesCursor.moveToNext());

        assertTrue("Error: The database doesn't contain all of the required location entry columns",
                locationColumnHashSet.isEmpty());
        db.close();
    }

    public void testLocationTable() {
        DatabaseHelper databaseHelper = new DatabaseHelper(getContext());
        SQLiteDatabase writableDatabase = databaseHelper.getWritableDatabase();
        ContentValues locationValues = createLocationValues();
        long locationId = insertLocation(locationValues, writableDatabase);
        assertNotSame(-1, locationId);
        Cursor locationResultCursor = findLocationById(writableDatabase, locationId);
        locationResultCursor.moveToFirst();
        TestUtilities.validateCurrentRecord(null, locationResultCursor, locationValues);
        locationResultCursor.close();
        writableDatabase.close();
    }

    public void testWeatherTable() {
        DatabaseHelper databaseHelper = new DatabaseHelper(getContext());
        SQLiteDatabase writableDatabase = databaseHelper.getWritableDatabase();
        long locationId = insertLocation(createLocationValues(), writableDatabase);
        assertNotSame(-1, locationId);
        ContentValues forecastContentValues = createWeatherValues(locationId);
        long forecastId = insertForecast(forecastContentValues, writableDatabase);
        assertNotSame(-1, forecastId);
        Cursor forecastResultCursor = findForecastById(writableDatabase, forecastId);
        forecastResultCursor.moveToFirst();
        TestUtilities.validateCurrentRecord(null, forecastResultCursor, forecastContentValues);
        forecastResultCursor.close();
        writableDatabase.close();
    }

    private Cursor findForecastById(SQLiteDatabase writableDatabase, long forecastId) {
        return writableDatabase.rawQuery("SELECT * FROM " +
                        ForecastEntry.TABLE_NAME + " WHERE " + ForecastEntry._ID + "=?",
                new String[]{String.valueOf(forecastId)});
    }

    private Cursor findLocationById(SQLiteDatabase writableDatabase, long locationId) {
        return writableDatabase.rawQuery("SELECT * FROM " +
                        LocationEntry.TABLE_NAME + " WHERE " + LocationEntry._ID + "=?",
                new String[]{String.valueOf(locationId)});
    }

    private long insertLocation(ContentValues locationContentValues, SQLiteDatabase writableDatabase) {
        return writableDatabase.insert(LocationEntry.TABLE_NAME, null, locationContentValues);
    }

    private long insertForecast(ContentValues forecastContentValues, SQLiteDatabase writableDatabase) {
        return writableDatabase.insert(ForecastEntry.TABLE_NAME, null, forecastContentValues);
    }

    private ContentValues createLocationValues() {
        String testLocationSetting = "99705";
        String testCityName = "North Pole";
        double testLatitude = 64.7488;
        double testLongitude = -147.353;

        ContentValues locationContentValues = new ContentValues();
        locationContentValues.put(LocationEntry.COLUMN_LOCATION_SETTING, testLocationSetting);
        locationContentValues.put(LocationEntry.COLUMN_CITY_NAME, testCityName);
        locationContentValues.put(LocationEntry.COLUMN_LATITUDE, testLatitude);
        locationContentValues.put(LocationEntry.COLUMN_LONGITUDE, testLongitude);
        return locationContentValues;
    }
}
