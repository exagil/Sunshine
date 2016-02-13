package net.chiragaggarwal.android.sunshine.app.data;

import android.content.ContentValues;
import android.content.Context;
import android.database.ContentObserver;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.net.Uri;
import android.os.Handler;
import android.os.HandlerThread;
import android.test.AndroidTestCase;

import net.chiragaggarwal.android.sunshine.app.utils.PollingCheck;
import net.chiragaggarwal.android.sunshine.data.DatabaseHelper;

import java.util.Map;
import java.util.Set;

import static net.chiragaggarwal.android.sunshine.data.ForecastContract.ForecastEntry;
import static net.chiragaggarwal.android.sunshine.data.ForecastContract.LocationEntry;

public class TestUtilities extends AndroidTestCase {
    static final String TEST_LOCATION = "99705";
    static final long TEST_DATE = 1419033600L;  // December 20th, 2014

    static void validateCursor(String error, Cursor valueCursor, ContentValues expectedValues) {
        assertTrue("Empty cursor returned. " + error, valueCursor.moveToFirst());
        validateCurrentRecord(error, valueCursor, expectedValues);
        valueCursor.close();
    }

    static void validateCurrentRecord(String error, Cursor valueCursor, ContentValues expectedValues) {
        Set<Map.Entry<String, Object>> valueSet = expectedValues.valueSet();
        for (Map.Entry<String, Object> entry : valueSet) {
            String columnName = entry.getKey();
            int idx = valueCursor.getColumnIndex(columnName);
            assertFalse("Column '" + columnName + "' not found. " + error, idx == -1);
            String expectedValue = entry.getValue().toString();
            assertEquals("Value '" + entry.getValue().toString() +
                    "' did not match the expected value '" +
                    expectedValue + "'. " + error, expectedValue, valueCursor.getString(idx));
        }
    }

    static ContentValues createWeatherValues(long locationRowId) {
        ContentValues forecastValues = new ContentValues();
        forecastValues.put(ForecastEntry.COLUMN_LOC_KEY, locationRowId);
        forecastValues.put(ForecastEntry.COLUMN_DATE, TEST_DATE);
        forecastValues.put(ForecastEntry.COLUMN_DEGREES, 1.1);
        forecastValues.put(ForecastEntry.COLUMN_HUMIDITY, 1.2);
        forecastValues.put(ForecastEntry.COLUMN_PRESSURE, 1.3);
        forecastValues.put(ForecastEntry.COLUMN_MAX_TEMP, 75);
        forecastValues.put(ForecastEntry.COLUMN_MIN_TEMP, 65);
        forecastValues.put(ForecastEntry.COLUMN_SHORT_DESC, "Asteroids");
        forecastValues.put(ForecastEntry.COLUMN_WIND_SPEED, 5.5);
        forecastValues.put(ForecastEntry.COLUMN_WEATHER_ID, 321);

        return forecastValues;
    }

    static ContentValues createNorthPoleLocationValues() {
        ContentValues testValues = new ContentValues();
        testValues.put(LocationEntry.COLUMN_LOCATION_SETTING, TEST_LOCATION);
        testValues.put(LocationEntry.COLUMN_CITY_NAME, "North Pole");
        testValues.put(LocationEntry.COLUMN_LATITUDE, 64.7488);
        testValues.put(LocationEntry.COLUMN_LONGITUDE, -147.353);

        return testValues;
    }

    static long insertNorthPoleLocationValues(Context context) {
        DatabaseHelper dbHelper = DatabaseHelper.getInstance(context);
        SQLiteDatabase db = dbHelper.getWritableDatabase();
        ContentValues testValues = TestUtilities.createNorthPoleLocationValues();

        long locationRowId;
        locationRowId = db.insert(LocationEntry.TABLE_NAME, null, testValues);

        assertTrue("Error: Failure to insert North Pole Location Values", locationRowId != -1);

        return locationRowId;
    }

    static class TestContentObserver extends ContentObserver {
        final HandlerThread mHT;
        boolean mContentChanged;

        static TestContentObserver getTestContentObserver() {
            HandlerThread ht = new HandlerThread("ContentObserverThread");
            ht.start();
            return new TestContentObserver(ht);
        }

        private TestContentObserver(HandlerThread ht) {
            super(new Handler(ht.getLooper()));
            mHT = ht;
        }

        @Override
        public void onChange(boolean selfChange) {
            onChange(selfChange, null);
        }

        @Override
        public void onChange(boolean selfChange, Uri uri) {
            mContentChanged = true;
        }

        public void waitForNotificationOrFail() {
            // Note: The PollingCheck class is taken from the Android CTS (Compatibility Test Suite).
            // It's useful to look at the Android CTS source for ideas on how to test your Android
            // applications.  The reason that PollingCheck works is that, by default, the JUnit
            // testing framework is not running on the main Android application thread.
            new PollingCheck(5000) {
                @Override
                protected boolean check() {
                    return mContentChanged;
                }
            }.run();
            mHT.quit();
        }
    }

    static TestContentObserver getTestContentObserver() {
        return TestContentObserver.getTestContentObserver();
    }
}
