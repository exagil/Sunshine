package net.chiragaggarwal.android.sunshine;

import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ListView;
import android.widget.TextView;

import net.chiragaggarwal.android.sunshine.models.Callback;
import net.chiragaggarwal.android.sunshine.models.Forecast;
import net.chiragaggarwal.android.sunshine.models.Forecasts;

public class ForecastFragment extends Fragment {
    private static final String INDIA_COUNTRY_CODE = "in";
    private static final String INDIRANAGAR_ZIP_CODE = "560038";

    private ListView forecastList;
    private TextView noInternetConnectionTextView;
    private WeatherForecastAdapter weatherForecastAdapter;

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setHasOptionsMenu(true);
    }

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_forecast, container, false);
        initializeWidgets(view);
        setOnItemClickListenerForForecastList();
        return view;
    }

    @Override
    public void onCreateOptionsMenu(Menu menu, MenuInflater inflater) {
        super.onCreateOptionsMenu(menu, inflater);
        menu.clear();
        inflater.inflate(R.menu.menu_forecast_fragment, menu);
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case R.id.forecast_action_refresh:
                fetchWeatherForecast();
                break;
            case R.id.forecast_action_settings:
                launchSettings();
                break;
        }
        return false;
    }

    private void launchSettings() {
        Intent intent = new Intent(getContext(), SettingsActivity.class);
        startActivity(intent);
    }

    private void fetchWeatherForecast() {
        new FetchWeatherForecastsTask(INDIRANAGAR_ZIP_CODE, INDIA_COUNTRY_CODE, new Callback<Forecasts>() {
            @Override
            public void onSuccess(Forecasts forecasts) {
                weatherForecastAdapter = new WeatherForecastAdapter(getContext(), forecasts);
                forecastList.setAdapter(weatherForecastAdapter);
            }

            @Override
            public void onFailure() {
                removeForecastList();
                showNoInternetConnection();
            }
        }).execute();
    }

    private void initializeWidgets(View view) {
        this.forecastList = (ListView) view.findViewById(R.id.forecast_list);
        this.noInternetConnectionTextView = (TextView) view.findViewById(R.id.no_internet_connection);
    }

    private void removeForecastList() {
        this.forecastList.setVisibility(ListView.GONE);
    }

    private void showNoInternetConnection() {
        this.noInternetConnectionTextView.setVisibility(TextView.VISIBLE);
    }

    private void setOnItemClickListenerForForecastList() {
        this.forecastList.setOnItemClickListener(onItemClickListenerForForecastList());
    }

    @NonNull
    private AdapterView.OnItemClickListener onItemClickListenerForForecastList() {
        return new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                Forecast forecast = weatherForecastAdapter.getItem(position);
                Intent detailActivityIntent = new Intent(getContext(), DetailActivity.class);
                detailActivityIntent.putExtra(Forecast.TAG, forecast);
                startActivity(detailActivityIntent);
            }
        };
    }
}
