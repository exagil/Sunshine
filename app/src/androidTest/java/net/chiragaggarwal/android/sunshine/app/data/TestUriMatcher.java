package net.chiragaggarwal.android.sunshine.app.data;

import android.content.UriMatcher;
import android.net.Uri;
import android.test.AndroidTestCase;

import net.chiragaggarwal.android.sunshine.data.ForecastContract;
import net.chiragaggarwal.android.sunshine.data.ForecastsProvider;
import net.chiragaggarwal.android.sunshine.data.LocationsProvider;

/*
    Uncomment this class when you are ready to test your UriMatcher.  Note that this class utilizes
    constants that are declared with package protection inside of the UriMatcher, which is why
    the test must be in the same data package as the Android app code.  Doing the test this way is
    a nice compromise between data hiding and testability.
 */
public class TestUriMatcher extends AndroidTestCase {
    private static final String LOCATION_QUERY = "London, UK";
    private static final long TEST_DATE = 1419033600L;  // December 20th, 2014
    private static final long TEST_LOCATION_ID = 10L;

    // content://com.example.android.sunshine.app/weather"
    private static final Uri TEST_WEATHER_DIR = ForecastContract.ForecastEntry.CONTENT_URI;
    private static final Uri TEST_WEATHER_WITH_LOCATION_DIR = ForecastContract.ForecastEntry.buildWeatherLocation(LOCATION_QUERY);
    private static final Uri TEST_WEATHER_WITH_LOCATION_AND_DATE_DIR = ForecastContract.ForecastEntry.
            buildWeatherLocationWithDate(LOCATION_QUERY, TEST_DATE);
    // content://com.example.android.sunshine.app/location"
    private static final Uri TEST_LOCATION_DIR = ForecastContract.LocationEntry.CONTENT_URI;
    /*
        Students: This function tests that your UriMatcher returns the correct integer value
        for each of the Uri types that our ContentProvider can handle.  Uncomment this when you are
        ready to test your UriMatcher.
     */
    public void testUriMatcher() {
        UriMatcher testForecastsMatcher = ForecastsProvider.buildUriMatcher();
        UriMatcher testLocationsMatcher = LocationsProvider.buildUriMatcher();

        assertEquals("Error: The FORECAST URI was matched incorrectly.",
                testForecastsMatcher.match(TEST_WEATHER_DIR), ForecastsProvider.FORECASTS_ENDPOINT);
        assertEquals("Error: The public WITH LOCATION URI was matched incorrectly.",
                testForecastsMatcher.match(TEST_WEATHER_WITH_LOCATION_DIR), ForecastsProvider.FORECASTS_FOR_LOCATION_ENDPOINT);
        assertEquals("Error: The WEATHER WITH LOCATION AND DATE URI was matched incorrectly.",
                testForecastsMatcher.match(TEST_WEATHER_WITH_LOCATION_AND_DATE_DIR), ForecastsProvider.FORECAST_FOR_LOCATION_AND_DATE_ENDPOINT);
        assertEquals("Error: The LOCATION URI was matched incorrectly.",
                testLocationsMatcher.match(TEST_LOCATION_DIR), LocationsProvider.LOCATIONS_ENDPOINT_CODE);
    }
}
