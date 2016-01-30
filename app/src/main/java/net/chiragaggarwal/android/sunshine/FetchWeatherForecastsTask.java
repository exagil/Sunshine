package net.chiragaggarwal.android.sunshine;

import android.os.AsyncTask;

import net.chiragaggarwal.android.sunshine.models.Callback;
import net.chiragaggarwal.android.sunshine.models.Forecasts;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;

public class FetchWeatherForecastsTask extends AsyncTask<Void, Void, Forecasts> {
    private final Callback callback;

    public FetchWeatherForecastsTask(Callback<Forecasts> callback) {
        this.callback = callback;
    }

    @Override
    protected Forecasts doInBackground(Void... params) {
        try {
            URL url = new URL("http://api.openweathermap.org/data/2.5/forecast/daily?" + "zip=94040" +
                    "&mode=json" + "&units=metric" + "&cnt=7" +
                    "&APPID=8e3db2dc590adf10c1a5e1f8ee25899e");
            HttpURLConnection httpURLConnection = (HttpURLConnection) url.openConnection();
            InputStream inputStream = httpURLConnection.getInputStream();
            BufferedReader bufferedReader = new BufferedReader(new InputStreamReader(inputStream));
            System.out.println();
            System.out.println();
        } catch (IOException e) {
            e.printStackTrace();
        }
        return new Forecasts();
    }

    @Override
    protected void onPostExecute(Forecasts forecasts) {
        if (forecasts != null) {
            this.callback.onSuccess(forecasts);
        } else {
            this.callback.onFailure();
        }
    }
}
