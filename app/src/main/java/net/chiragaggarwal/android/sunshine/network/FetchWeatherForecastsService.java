package net.chiragaggarwal.android.sunshine.network;

import android.app.IntentService;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.support.v4.content.LocalBroadcastManager;

import net.chiragaggarwal.android.sunshine.models.Forecast;
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

public class FetchWeatherForecastsService extends IntentService {
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
    private String postalCode;
    private String countryCode;
    private String temperatureUnit;

    public FetchWeatherForecastsService() {
        super("net.chiragaggarwal.android.sunshine.network.FetchWeatherForecastsService");
    }

    @Override
    protected void onHandleIntent(Intent intent) {
        Bundle extras = intent.getExtras();
        this.postalCode = extras.getString(Location.POSTAL_CODE);
        this.countryCode = extras.getString(Location.COUNTRY_CODE);
        this.temperatureUnit = extras.getString(Forecast.TEMPERATURE_UNIT);

        ForecastsForLocation forecastsForLocation = null;
        try {
            URL url = buildWeatherForecastsURL();
            HttpURLConnection httpURLConnection = (HttpURLConnection) url.openConnection();

            if (isResponseAcceptable(httpURLConnection)) {
                forecastsForLocation = getForecastsForLocation(httpURLConnection, postalCode);
            }

        } catch (IOException e) {
            e.printStackTrace();
        } catch (JSONException e) {
            e.printStackTrace();
        }

        Intent forecastsBroadcast = new Intent(ForecastsForLocation.ACTION_BROADCAST);
        forecastsBroadcast.putExtra(ForecastsForLocation.TAG, forecastsForLocation);
        LocalBroadcastManager.getInstance(this).sendBroadcast(forecastsBroadcast);
    }
    private URL buildWeatherForecastsURL() throws MalformedURLException {
        Uri fetchWeatherForecastsUri = new Uri.Builder()
                .scheme(HTTP)
                .authority(AUTHORITY)
                .path(DATA)
                .appendPath(PATH_2_5)
                .appendPath(PATH_FORECAST)
                .appendPath(PATH_DAILY)
                .appendQueryParameter(PARAMETER_ZIP, this.postalCode + COMMA + this.countryCode)
                .appendQueryParameter(MODE, JSON)
                .appendQueryParameter(UNITS, this.temperatureUnit)
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
}
