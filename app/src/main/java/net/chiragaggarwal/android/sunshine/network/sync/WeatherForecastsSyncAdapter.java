package net.chiragaggarwal.android.sunshine.network.sync;

import android.accounts.Account;
import android.annotation.TargetApi;
import android.content.AbstractThreadedSyncAdapter;
import android.content.ContentProviderClient;
import android.content.ContentValues;
import android.content.Context;
import android.content.SyncResult;
import android.database.Cursor;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.support.annotation.NonNull;

import net.chiragaggarwal.android.sunshine.R;
import net.chiragaggarwal.android.sunshine.Utils.Utility;
import net.chiragaggarwal.android.sunshine.data.ForecastContract;
import net.chiragaggarwal.android.sunshine.models.Forecasts;
import net.chiragaggarwal.android.sunshine.models.ForecastsForLocation;
import net.chiragaggarwal.android.sunshine.models.Location;

import org.json.JSONException;
import org.json.JSONObject;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;

// WeatherForecastsSyncAdapter fetches the weather forecasts
// as required by the app components

public class WeatherForecastsSyncAdapter extends AbstractThreadedSyncAdapter {
    private static final String RESPONSE_ACCEPTABLE_RANGE = "^2.*";
    private static final String HTTP = "http";
    private static final String AUTHORITY = "api.openweathermap.org";
    private static final String PATH_2_5 = "2.5";
    private static final String PATH_FORECAST = "forecast";
    private static final String PATH_DAILY = "daily";
    private static final String PARAMETER_ZIP = "zip";
    private static final String COMMA = ",";
    private static final String MODE = "mode";
    private static final String JSON = "json";
    private static final String UNITS = "units";
    private static final String COUNT = "cnt";
    private static final String SEVEN = "7";
    private static final String APPID = "APPID";
    private static final String DATA = "data";

    public WeatherForecastsSyncAdapter(Context context, boolean autoInitialize) {
        super(context, autoInitialize);
    }

    @TargetApi(Build.VERSION_CODES.HONEYCOMB)
    public WeatherForecastsSyncAdapter(Context context, boolean autoInitialize, boolean allowParallelSyncs) {
        super(context, autoInitialize, allowParallelSyncs);
    }

    @Override
    public void onPerformSync(Account account, Bundle extras, String authority, ContentProviderClient provider, SyncResult syncResult) {
        String countryCode = extras.getString(this.getContext().getString(R.string.preference_country_code_key));
        String temperatureUnit = extras.getString(this.getContext().getString(R.string.preference_temperature_unit_key));
        String postalCode = extras.getString(this.getContext().getString(R.string.preference_zip_code_key));

        ForecastsForLocation forecastsForLocation = null;
        try {
            URL url = buildWeatherForecastsURL(countryCode, postalCode, temperatureUnit);
            HttpURLConnection httpURLConnection = (HttpURLConnection) url.openConnection();

            if (isResponseAcceptable(httpURLConnection)) {
                forecastsForLocation = getForecastsForLocation(httpURLConnection, postalCode);
            }
        } catch (IOException e) {
            e.printStackTrace();
        } catch (JSONException e) {
            e.printStackTrace();
        }

        save(forecastsForLocation);
    }

    private URL buildWeatherForecastsURL(String countryCode, String postalCode, String temperatureUnit)
            throws MalformedURLException {

        Uri fetchWeatherForecastsUri = new Uri.Builder()
                .scheme(HTTP)
                .authority(AUTHORITY)
                .path(DATA)
                .appendPath(PATH_2_5)
                .appendPath(PATH_FORECAST)
                .appendPath(PATH_DAILY)
                .appendQueryParameter(PARAMETER_ZIP, postalCode + COMMA + countryCode)
                .appendQueryParameter(MODE, JSON)
                .appendQueryParameter(UNITS, temperatureUnit)
                .appendQueryParameter(COUNT, SEVEN)
                .appendQueryParameter(APPID, "8e3db2dc590adf10c1a5e1f8ee25899e")
                .build();
        return new URL(fetchWeatherForecastsUri.toString());
    }

    private boolean isResponseAcceptable(HttpURLConnection httpURLConnection) throws IOException {
        return String.valueOf(httpURLConnection.getResponseCode()).matches(RESPONSE_ACCEPTABLE_RANGE);
    }

    private ForecastsForLocation getForecastsForLocation(
            HttpURLConnection connection,
            String postalCode) throws IOException, JSONException {

        ForecastsForLocation forecastsForLocation = null;
        InputStream forecastsForLocationStream = connection.getInputStream();
        BufferedReader forecastsForLocationResponseReader =
                new BufferedReader(new InputStreamReader(forecastsForLocationStream));
        String forecastsForLocationResponseString = buildResponseString(forecastsForLocationResponseReader);

        if (forecastsForLocationResponseString != null) {
            forecastsForLocation = buildForecastsForLocationFromResponseString(
                    forecastsForLocationResponseString, postalCode);
        }
        return forecastsForLocation;
    }

    private String buildResponseString(BufferedReader bufferedReader) throws IOException {
        StringBuilder responseString = new StringBuilder();
        String responseLine;
        while ((responseLine = bufferedReader.readLine()) != null) {
            responseString.append(responseLine);
        }
        return responseString.toString();
    }

    private ForecastsForLocation buildForecastsForLocationFromResponseString(
            String forecastsForLocationResponseString, String postalCode) throws JSONException {

        JSONObject forecastsFromLocationJSONResponse = new JSONObject(forecastsForLocationResponseString);
        return ForecastsForLocation.fromJSON(forecastsFromLocationJSONResponse, postalCode);
    }

    private void save(ForecastsForLocation forecastsForLocation) {
        Forecasts forecasts = forecastsForLocation.forecasts;
        Location location = forecastsForLocation.location;

        Long locationRowId = insertLocationIfNotPresent(location);
        insertForecastsForLocationIfNotPresent(forecasts, locationRowId);
    }

    private Long insertLocationIfNotPresent(Location location) {
        Cursor locationCursor = queryLocationFromLocationProvider(location);
        Long locationRowId = null;

        if (isLocationNotPresent(locationCursor)) {
            Uri locationUri = insertLocationInLocationProvider(location);
            locationRowId = Long.decode(locationUri.getLastPathSegment());
        } else {
            locationRowId = getIdOfLocationAlreadyPresent(locationCursor);
        }

        return locationRowId;
    }

    private Uri insertLocationInLocationProvider(Location location) {
        return getContext().getContentResolver().insert(
                ForecastContract.LocationEntry.CONTENT_URI,
                location.toContentValues()
        );
    }

    @NonNull
    private Long getIdOfLocationAlreadyPresent(Cursor locationCursor) {
        locationCursor.moveToFirst();
        return locationCursor.getLong(0);
    }

    private Cursor queryLocationFromLocationProvider(Location location) {
        return getContext().getContentResolver().
                query(ForecastContract.LocationEntry.CONTENT_URI,
                        null,
                        ForecastContract.LocationEntry.TABLE_NAME + "." + ForecastContract.LocationEntry.COLUMN_LOCATION_SETTING + "=?",
                        new String[]{location.postalCode},
                        null);
    }

    private boolean isLocationNotPresent(Cursor locationCursor) {
        return locationCursor.getCount() == 0;
    }

    private void insertForecastsForLocationIfNotPresent(Forecasts forecasts, Long locationRowId) {
        Cursor forecastsCursor = queryForecastsForLocationFromForecastsProviderStartingFromToday(locationRowId);
        if (isOneWeeksForecastsNotPresent(forecastsCursor)) {
            ContentValues[] forecastsValues = forecasts.toContentValues(locationRowId);
            insertForecastsInForecastsProvider(forecastsValues);
        }
    }

    private Cursor queryForecastsForLocationFromForecastsProviderStartingFromToday(Long locationRowId) {
        String locationRowIdArgument = locationRowId.toString();
        String dateArgument = Utility.parsedCurrentDateArgument();

        return getContext().getContentResolver().query(
                ForecastContract.ForecastEntry.CONTENT_URI,
                null,
                ForecastContract.ForecastEntry.buildForecastsSelectionForLocationIdWithStartDate(),
                new String[]{locationRowIdArgument, dateArgument},
                null
        );
    }

    private boolean isOneWeeksForecastsNotPresent(Cursor forecastsCursor) {
        return forecastsCursor.getCount() < 6;
    }

    private void insertForecastsInForecastsProvider(ContentValues[] forecastsValues) {
        getContext().getContentResolver().bulkInsert(
                ForecastContract.ForecastEntry.CONTENT_URI, forecastsValues
        );
    }
}
