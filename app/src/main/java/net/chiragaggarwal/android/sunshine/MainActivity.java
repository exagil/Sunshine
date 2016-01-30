package net.chiragaggarwal.android.sunshine;

import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;

import net.chiragaggarwal.android.sunshine.models.Callback;
import net.chiragaggarwal.android.sunshine.models.Forecast;
import net.chiragaggarwal.android.sunshine.models.Forecasts;

public class MainActivity extends AppCompatActivity {

    public Forecasts forecasts = new Forecasts(
            new Forecast("Today - Sunny - 88/63"),
            new Forecast("Tomorrow - Foggy - 70/46"),
            new Forecast("Wednesday - Cloudy - 72/63"),
            new Forecast("Thursday - Rainy - 64/51"),
            new Forecast("Friday - Foggy - 70/46"),
            new Forecast("Saturday - Sunny - 76/68")
    );

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        new FetchWeatherForecastsTask(
                new Callback<Forecasts>() {
                    @Override
                    public void onSuccess(Forecasts forecasts) {
                        showWeatherForecast(forecasts);
                    }

                    @Override
                    public void onFailure() {
                        showInternetNotConnectedError();
                    }
                }
        ).execute();
    }

    private void showInternetNotConnectedError() {

    }

    private void showWeatherForecast(Forecasts forecasts) {
        Bundle placeHolderFragmentArguments = new Bundle();
        placeHolderFragmentArguments.putParcelable(Forecasts.TAG, forecasts);

        ForecastFragment forecastFragment = new ForecastFragment();
        forecastFragment.setArguments(placeHolderFragmentArguments);

        getSupportFragmentManager().beginTransaction()
                .replace(R.id.activity_main_placeholder, forecastFragment)
                .commit();
    }
}
