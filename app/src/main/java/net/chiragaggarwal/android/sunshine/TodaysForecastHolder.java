package net.chiragaggarwal.android.sunshine;

import android.content.Context;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import net.chiragaggarwal.android.sunshine.models.Forecast;

import java.util.Date;

class TodaysForecastHolder {
    private final TextView textMaxTemperature;
    private final TextView textMinTemperature;
    private final ImageView imageWeatherIcon;
    private final TextView textForecastDescription;
    private TextView textFormattedDate;

    public TodaysForecastHolder(View view) {
        this.textFormattedDate = (TextView) view.findViewById(R.id.text_formatted_date);
        this.textMaxTemperature = (TextView) view.findViewById(R.id.text_max_temperature);
        this.textMinTemperature = (TextView) view.findViewById(R.id.text_min_temperature);
        this.textForecastDescription = (TextView) view.findViewById(R.id.text_forecast_description);
        this.imageWeatherIcon = (ImageView) view.findViewById(R.id.img_weather_icon);
    }

    public void bindView(Forecast forecast, Context context) {
        this.textFormattedDate.setText(forecast.friendlyDay(new Date()));
        this.textMaxTemperature.setText(forecast.formattedMaximumTemperature(context));
        this.textMinTemperature.setText(forecast.formattedMinimumTemperature(context));
        this.textForecastDescription.setText(forecast.mainDescription);
        this.imageWeatherIcon.setImageResource(forecast.iconArt());
    }
}
