package net.chiragaggarwal.android.sunshine.network.sync;

import android.accounts.Account;
import android.accounts.AccountManager;
import android.annotation.TargetApi;
import android.app.PendingIntent;
import android.app.TaskStackBuilder;
import android.content.AbstractThreadedSyncAdapter;
import android.content.ContentProviderClient;
import android.content.ContentValues;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.SyncResult;
import android.database.Cursor;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.preference.PreferenceManager;
import android.support.annotation.NonNull;
import android.support.v4.app.NotificationCompat;
import android.support.v4.app.NotificationManagerCompat;

import net.chiragaggarwal.android.sunshine.MainActivity;
import net.chiragaggarwal.android.sunshine.R;
import net.chiragaggarwal.android.sunshine.Utils.Utility;
import net.chiragaggarwal.android.sunshine.data.ForecastContract;
import net.chiragaggarwal.android.sunshine.models.Forecast;
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
import java.text.ParseException;
import java.util.Date;

// WeatherForecastsSyncAdapter fetches the weather forecasts
// as required by the app components

public class WeatherForecastsSyncAdapter extends AbstractThreadedSyncAdapter {
    public static final String ACCOUNT_NAME = "chiragaggarwal";
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
    private static final int NOTIFICATION_ID = 2002;

    public WeatherForecastsSyncAdapter(Context context, boolean autoInitialize) {
        super(context, autoInitialize);
    }

    @TargetApi(Build.VERSION_CODES.HONEYCOMB)
    public WeatherForecastsSyncAdapter(Context context, boolean autoInitialize, boolean allowParallelSyncs) {
        super(context, autoInitialize, allowParallelSyncs);
    }

    public static Account init(Context context) {
        AccountManager accountManager = (AccountManager) context.getSystemService(Context.ACCOUNT_SERVICE);
        Account account = new Account(ACCOUNT_NAME, context.getString(R.string.account_type));
        if (accountManager.addAccountExplicitly(account, null, null)) {
        }
        return account;
    }

    @Override
    public void onPerformSync(Account account, Bundle extras, String authority, ContentProviderClient provider, SyncResult syncResult) {
        Context context = getContext();
        SharedPreferences sharedPreferences = PreferenceManager.getDefaultSharedPreferences(context);
        String countryCode = Utility.savedCountryCode(context, sharedPreferences);
        String temperatureUnit = Utility.savedTemperatureUnit(context, sharedPreferences);
        String postalCode = Utility.savedZipCode(context, sharedPreferences);

        try {
            URL url = buildWeatherForecastsURL(countryCode, postalCode, temperatureUnit);
            HttpURLConnection httpURLConnection = (HttpURLConnection) url.openConnection();

            if (isResponseAcceptable(httpURLConnection)) {
                ForecastsForLocation forecastsForLocation = getForecastsForLocation(httpURLConnection, postalCode);
                save(forecastsForLocation);
            }
        } catch (IOException e) {
            e.printStackTrace();
        } catch (JSONException e) {
            e.printStackTrace();
        }

        try {
            if (isNotificationEnabled()) notifyWeather(postalCode);
        } catch (ParseException e) {
            e.printStackTrace();
        }
    }

    private boolean isNotificationEnabled() {
        String notificationPreferenceKey = getContext().getString(R.string.preference_show_notifications_key);
        return PreferenceManager.getDefaultSharedPreferences(getContext()).getBoolean(notificationPreferenceKey, false);
    }

    private void notifyWeather(String postalCode) throws ParseException {
        Forecast forecast = queryTodaysForecast(postalCode);
        NotificationCompat.Builder notificationBuilder = getNotificationBuilder(forecast);
        addContentIntentIfPossible(notificationBuilder);
        NotificationManagerCompat.from(getContext()).notify(NOTIFICATION_ID, notificationBuilder.build());
    }

    private void addContentIntentIfPossible(NotificationCompat.Builder notificationBuilder) {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.JELLY_BEAN) {
            PendingIntent launchMainActivityIntent = TaskStackBuilder.create(getContext())
                    .addNextIntentWithParentStack(new Intent(getContext(), MainActivity.class))
                    .getPendingIntent(0, PendingIntent.FLAG_UPDATE_CURRENT);
            notificationBuilder.setContentIntent(launchMainActivityIntent);
        }
    }

    private NotificationCompat.Builder getNotificationBuilder(Forecast forecast) {
        return new NotificationCompat.Builder(getContext())
                .setContentText(forecast.summary())
                .setContentTitle("Forecast")
                .setSmallIcon(R.drawable.ic_action_bar_icon);
    }

    private Forecast queryTodaysForecast(String postalCode) throws ParseException {
        long todaysPersistableDate = Forecast.persistableDate(new Date());
        Cursor forecastCursor = getContext().getContentResolver().query(
                ForecastContract.ForecastEntry.buildWeatherLocationWithDate(postalCode, todaysPersistableDate), null,
                null, null, null);
        forecastCursor.moveToFirst();
        Forecast queriedForecast = Forecast.fromCursor(forecastCursor);
        forecastCursor.close();
        return queriedForecast;
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
