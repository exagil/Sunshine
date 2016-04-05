package net.chiragaggarwal.android.sunshine.network;

import android.net.Uri;
import android.os.AsyncTask;

import net.chiragaggarwal.android.sunshine.models.Callback;
import net.chiragaggarwal.android.sunshine.models.ForecastsForLocation;

import org.json.JSONException;
import org.json.JSONObject;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;

public class FetchWeatherForecastsTask extends AsyncTask<Void, Void, ForecastsForLocation> {
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

    private final Callback callback;
    private final String postalCode;
    private final String countryCode;
    private final String temperatureUnit;

    public FetchWeatherForecastsTask(String postalCode,
                                     String countryCode,
                                     String temperatureUnit,
                                     Callback<ForecastsForLocation> callback) {

        this.postalCode = postalCode;
        this.countryCode = countryCode;
        this.temperatureUnit = temperatureUnit;
        this.callback = callback;
    }

    @Override
    protected ForecastsForLocation doInBackground(Void... params) {
        ForecastsForLocation forecastsForLocation = null;
        try {
            URL url = buildWeatherForecastsURL();
            HttpURLConnection httpURLConnection = (HttpURLConnection) url.openConnection();

            if (isResponseAcceptable(httpURLConnection)) {
                forecastsForLocation = getForecastsForLocation(httpURLConnection, this.postalCode);
            }

        } catch (IOException e) {
            e.printStackTrace();
        } catch (JSONException e) {
            e.printStackTrace();
        }
        return forecastsForLocation;
    }

    @Override
    protected void onPostExecute(ForecastsForLocation forecastsForLocation) {
        if (forecastsForLocation != null) {
            this.callback.onSuccess(forecastsForLocation);
        } else {
            this.callback.onFailure();
        }
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
