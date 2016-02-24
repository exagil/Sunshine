package net.chiragaggarwal.android.sunshine;

import android.content.Context;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import net.chiragaggarwal.android.sunshine.models.Forecast;

public class DetailsFragment extends Fragment {
    private TextView textForecastDescription;
    private ImageView imageForecast;
    private TextView textForecastPressure;
    private TextView textForecastWind;
    private TextView textForecastHumidity;
    private TextView textForecastMinTemperature;
    private TextView textForecastMaxTemperature;
    private TextView textForecastDate;

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_forecast, container, false);
        setHasOptionsMenu(true);

        Forecast forecast = getArguments().getParcelable(Forecast.TAG);
        initializeViews(view);
        populateViews(forecast, getContext());

        return view;
    }

    private void initializeViews(View view) {
        this.textForecastDescription = (TextView) view.findViewById(R.id.text_forecast_description);
        this.imageForecast = (ImageView) view.findViewById(R.id.image_forecast);
        this.textForecastPressure = (TextView) view.findViewById(R.id.text_forecast_pressure);
        this.textForecastWind = (TextView) view.findViewById(R.id.text_forecast_wind);
        this.textForecastHumidity = (TextView) view.findViewById(R.id.text_forecast_humidity);
        this.textForecastMinTemperature = (TextView) view.findViewById(R.id.text_forecast_min_temperature);
        this.textForecastMaxTemperature = (TextView) view.findViewById(R.id.text_forecast_max_temperature);
        this.textForecastDate = (TextView) view.findViewById(R.id.text_forecast_date);
    }

    private void populateViews(Forecast forecast, Context context) {
        String mainDescription = forecast.mainDescription;
        String pressureInHectopascal = forecast.pressureInHectopascal();
        String northWindSpeedInKmph = forecast.northWindSpeedInKmph();
        String humidityInPercentage = forecast.humidityInPercentage();
        String formattedMinimumTemperature = forecast.formattedMinimumTemperature(context);
        String formattedMaximumTemperature = forecast.formattedMaximumTemperature(context);
        String formattedDate = forecast.formattedDate();

        this.textForecastDescription.setText(mainDescription);
        this.imageForecast.setImageResource(forecast.iconArt());
        this.textForecastPressure.setText(pressureInHectopascal);
        this.textForecastWind.setText(northWindSpeedInKmph);
        this.textForecastHumidity.setText(humidityInPercentage);
        this.textForecastMinTemperature.setText(formattedMinimumTemperature);
        this.textForecastMaxTemperature.setText(formattedMaximumTemperature);
        this.textForecastDate.setText(formattedDate);
    }
}
