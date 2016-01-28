package net.chiragaggarwal.android.sunshine;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ListView;

import net.chiragaggarwal.android.sunshine.models.Forecasts;

public class PlaceHolderFragment extends Fragment {
    ListView forecastList;

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_placeholder, container, false);

        Forecasts forecasts = getForecastsFromFragmentArguments();
        initializeWidgets(view);
        showWeatherForecast(forecasts);
        return view;
    }

    private Forecasts getForecastsFromFragmentArguments() {
        return getArguments().getParcelable(Forecasts.TAG);
    }

    private void showWeatherForecast(Forecasts forecasts) {
        WeatherForecastAdapter weatherForecastAdapter = new WeatherForecastAdapter(getContext(), forecasts);
        this.forecastList.setAdapter(weatherForecastAdapter);
    }

    private void initializeWidgets(View view) {
        this.forecastList = (ListView) view.findViewById(R.id.forecast_list);
    }
}
