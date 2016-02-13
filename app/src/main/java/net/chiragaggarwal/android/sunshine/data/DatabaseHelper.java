package net.chiragaggarwal.android.sunshine.data;

import android.content.ContentValues;
import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.support.annotation.NonNull;

import static net.chiragaggarwal.android.sunshine.data.ForecastContract.ForecastEntry;
import static net.chiragaggarwal.android.sunshine.data.ForecastContract.LocationEntry;

public class DatabaseHelper extends SQLiteOpenHelper {
    private static final String COMMA = ", ";
    private static final String START_BRACKET = "(";
    private static final String END_BRACKET = ")";
    private static final String WEATHER_FORECAST_TABLE_NAME = "weather_forecast";
    private static final String NUMERIC = " NUMERIC";
    private static final String TEXT = " TEXT";
    private static final String INTEGER = " INTEGER";
    private static final String NOT_NULL = " NOT NULL";
    private static final String FOREIGN_KEY = "FOREIGN KEY";
    private static final String REFERENCES = " REFERENCES ";
    private static final String DROP_TABLE = "DROP TABLE ";
    private static final String UNIQUE = "UNIQUE ";
    private static final String CREATE_TABLE = "CREATE TABLE ";
    private static final String PRIMARY_KEY = " PRIMARY KEY";
    private static final String AUTOINCREMENT = " AUTOINCREMENT";
    private static final String CITY_NAME_INDIRANAGAR = "Indiranagar";
    private static final String RANDOM_SETTING = "random setting";

    public static String DATABASE_NAME = WEATHER_FORECAST_TABLE_NAME + ".db";
    public static Integer DATABASE_VERSION = 1;

    private static DatabaseHelper databaseHelper;

    private DatabaseHelper(Context context) {
        super(context, DATABASE_NAME, null, DATABASE_VERSION);
    }

    public static DatabaseHelper getInstance(Context context) {
        if (databaseHelper == null) databaseHelper = new DatabaseHelper(context);
        return databaseHelper;
    }

    @Override
    public void onCreate(SQLiteDatabase db) {
        db.execSQL(createLocationTableSQLQuery());
        db.execSQL(createWeatherForecastTableSQLQuery());
        createSeedData(db);
    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        db.execSQL(DROP_TABLE + LocationEntry.TABLE_NAME);
        db.execSQL(DROP_TABLE + ForecastEntry.TABLE_NAME);
        onCreate(db);
    }

    @NonNull
    public String createWeatherForecastTableSQLQuery() {
        return CREATE_TABLE + ForecastEntry.TABLE_NAME +
                START_BRACKET +
                ForecastEntry._ID + INTEGER + PRIMARY_KEY + AUTOINCREMENT +
                COMMA +
                ForecastEntry.COLUMN_LOC_KEY + INTEGER + NOT_NULL +
                COMMA +
                ForecastEntry.COLUMN_DATE + INTEGER + NOT_NULL +
                COMMA +
                ForecastEntry.COLUMN_SHORT_DESC + TEXT + NOT_NULL +
                COMMA +
                ForecastEntry.COLUMN_MIN_TEMP + NUMERIC + NOT_NULL +
                COMMA +
                ForecastEntry.COLUMN_MAX_TEMP + NUMERIC + NOT_NULL +
                COMMA +
                ForecastEntry.COLUMN_HUMIDITY + NUMERIC + NOT_NULL +
                COMMA +
                ForecastEntry.COLUMN_PRESSURE + NUMERIC + NOT_NULL +
                COMMA +
                ForecastEntry.COLUMN_WIND_SPEED + NUMERIC + NOT_NULL +
                COMMA +
                ForecastEntry.COLUMN_DEGREES + NUMERIC + NOT_NULL +
                COMMA +
                ForecastEntry.COLUMN_WEATHER_ID + NUMERIC + NOT_NULL +
                COMMA +

                FOREIGN_KEY + START_BRACKET + ForecastEntry.COLUMN_LOC_KEY + END_BRACKET +
                REFERENCES + LocationEntry.TABLE_NAME + START_BRACKET + LocationEntry._ID + END_BRACKET +
                COMMA +
                UNIQUE + START_BRACKET + ForecastEntry.COLUMN_LOC_KEY + COMMA +
                ForecastEntry.COLUMN_DATE + END_BRACKET + " ON CONFLICT REPLACE" +

                END_BRACKET;
    }

    public String createLocationTableSQLQuery() {
        return CREATE_TABLE + LocationEntry.TABLE_NAME +
                START_BRACKET +
                LocationEntry._ID + INTEGER + PRIMARY_KEY +
                COMMA +
                LocationEntry.COLUMN_CITY_NAME + TEXT + NOT_NULL +
                COMMA +
                LocationEntry.COLUMN_LATITUDE + NUMERIC + NOT_NULL +
                COMMA +
                LocationEntry.COLUMN_LONGITUDE + NUMERIC + NOT_NULL +
                COMMA +
                LocationEntry.COLUMN_LOCATION_SETTING + TEXT + UNIQUE + NOT_NULL +
                COMMA +

                UNIQUE + START_BRACKET + LocationEntry.COLUMN_LATITUDE + COMMA +
                LocationEntry.COLUMN_LONGITUDE + END_BRACKET +

                END_BRACKET;
    }

    private void createSeedData(SQLiteDatabase db) {
        long id = insertIndiranagarInLocationsTable(db);
        insertIndiranagarSampleWeather(id, db);
    }

    private long insertIndiranagarInLocationsTable(SQLiteDatabase db) {
        ContentValues indiranagarValues = new ContentValues();
        indiranagarValues.put(LocationEntry.COLUMN_CITY_NAME, CITY_NAME_INDIRANAGAR);
        indiranagarValues.put(LocationEntry.COLUMN_LATITUDE, 12);
        indiranagarValues.put(LocationEntry.COLUMN_LONGITUDE, 24);
        indiranagarValues.put(LocationEntry.COLUMN_LOCATION_SETTING, RANDOM_SETTING);
        return db.insert(LocationEntry.TABLE_NAME, null, indiranagarValues);
    }

    private long insertIndiranagarSampleWeather(long id, SQLiteDatabase db) {
        ContentValues indiranagarForecastValues = new ContentValues();
        indiranagarForecastValues.put(ForecastEntry.COLUMN_LOC_KEY, id);
        indiranagarForecastValues.put(ForecastEntry.COLUMN_DATE, 123456);
        indiranagarForecastValues.put(ForecastEntry.COLUMN_SHORT_DESC, "blablabla");
        indiranagarForecastValues.put(ForecastEntry.COLUMN_MIN_TEMP, 12);
        indiranagarForecastValues.put(ForecastEntry.COLUMN_MAX_TEMP, 23);
        indiranagarForecastValues.put(ForecastEntry.COLUMN_HUMIDITY, 122);
        indiranagarForecastValues.put(ForecastEntry.COLUMN_PRESSURE, 1234);
        indiranagarForecastValues.put(ForecastEntry.COLUMN_WEATHER_ID, 9);
        return db.insert(ForecastEntry.TABLE_NAME, null, indiranagarForecastValues);
    }
}
