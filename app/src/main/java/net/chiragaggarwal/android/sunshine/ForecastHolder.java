package net.chiragaggarwal.android.sunshine;

import android.content.Context;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import net.chiragaggarwal.android.sunshine.models.Forecast;

import java.util.Date;

class ForecastHolder {
    private ImageView imageWeatherIcon;
    private TextView textForecastDay;
    private TextView textForecastDescription;
    private TextView textForecastMaxTemperature;
    private TextView textForecastMinTemperature;

    public ForecastHolder(View view) {
        this.imageWeatherIcon = (ImageView) view.findViewById(R.id.img_weather_icon);
        this.textForecastDay = (TextView) view.findViewById(R.id.text_forecast_day);
        this.textForecastDescription = (TextView) view.findViewById(R.id.text_forecast_description);
        this.textForecastMaxTemperature = (TextView) view.findViewById(R.id.text_max_temperature);
        this.textForecastMinTemperature = (TextView) view.findViewById(R.id.text_min_temperature);
    }

    public void bindView(Forecast forecast, Context context) {
        this.imageWeatherIcon.setImageResource(forecast.iconResource());
        this.textForecastDay.setText(forecast.friendlyDay(new Date()));
        this.textForecastDescription.setText(forecast.mainDescription);
        this.textForecastMaxTemperature.setText(forecast.formattedMaximumTemperature(context));
        this.textForecastMinTemperature.setText(forecast.formattedMinimumTemperature(context));
    }
}
