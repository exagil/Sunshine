package net.chiragaggarwal.android.sunshine;

import android.net.Uri;
import android.os.AsyncTask;

import net.chiragaggarwal.android.sunshine.models.Callback;
import net.chiragaggarwal.android.sunshine.models.Forecasts;

import org.json.JSONException;
import org.json.JSONObject;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;

public class FetchWeatherForecastsTask extends AsyncTask<Void, Void, Forecasts> {
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
    private static final String METRIC = "metric";
    private static final String COUNT = "cnt";
    private static final String SEVEN = "7";
    private static final String APPID = "APPID";

    private final Callback callback;
    private final String postalCode;
    private final String countryCode;

    public FetchWeatherForecastsTask(String postalCode, String countryCode, Callback<Forecasts> callback) {
        this.callback = callback;
        this.postalCode = postalCode;
        this.countryCode = countryCode;
    }

    @Override
    protected Forecasts doInBackground(Void... params) {
        Forecasts forecasts = null;
        try {
            URL url = buildWeatherForecastsURL();
            HttpURLConnection httpURLConnection = (HttpURLConnection) url.openConnection();

            if (isResponseAcceptable(httpURLConnection))
                forecasts = getForecasts(forecasts, httpURLConnection);

        } catch (IOException e) {
            e.printStackTrace();
        } catch (JSONException e) {
            e.printStackTrace();
        }
        return forecasts;
    }

    @Override
    protected void onPostExecute(Forecasts forecasts) {
        if (forecasts != null) {
            this.callback.onSuccess(forecasts);
        } else {
            this.callback.onFailure();
        }
    }

    private URL buildWeatherForecastsURL() throws MalformedURLException {
        Uri fetchWeatherForecastsUri = new Uri.Builder()
                .scheme(HTTP)
                .authority(AUTHORITY)
                .path("data")
                .appendPath(PATH_2_5)
                .appendPath(PATH_FORECAST)
                .appendPath(PATH_DAILY)
                .appendQueryParameter(PARAMETER_ZIP, this.postalCode + COMMA + this.countryCode)
                .appendQueryParameter(MODE, JSON)
                .appendQueryParameter(UNITS, METRIC)
                .appendQueryParameter(COUNT, SEVEN)
                .appendQueryParameter(APPID, "8e3db2dc590adf10c1a5e1f8ee25899e")
                .build();
        return new URL(fetchWeatherForecastsUri.toString());
    }

    private boolean isResponseAcceptable(HttpURLConnection httpURLConnection) throws IOException {
        return String.valueOf(httpURLConnection.getResponseCode()).matches(RESPONSE_ACCEPTABLE_RANGE);
    }

    private Forecasts getForecasts(Forecasts forecasts, HttpURLConnection httpURLConnection) throws IOException, JSONException {
        InputStream inputStream = httpURLConnection.getInputStream();
        BufferedReader bufferedReader = new BufferedReader(new InputStreamReader(inputStream));
        String forecastsResponseString = buildResponseString(bufferedReader);
        if (forecastsResponseString != null)
            forecasts = buildForecastFromResponseString(forecastsResponseString);
        return forecasts;
    }

    private String buildResponseString(BufferedReader bufferedReader) throws IOException {
        StringBuilder responseString = new StringBuilder();
        String responseLine;
        while ((responseLine = bufferedReader.readLine()) != null) {
            responseString.append(responseLine);
        }
        return responseString.toString();
    }

    private Forecasts buildForecastFromResponseString(String forecastsResponseString) throws JSONException {
        JSONObject forecastsJSONResponse = new JSONObject(forecastsResponseString);
        return Forecasts.fromJSON(forecastsJSONResponse);
    }
}
