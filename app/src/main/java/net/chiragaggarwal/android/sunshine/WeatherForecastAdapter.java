package net.chiragaggarwal.android.sunshine;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;

import net.chiragaggarwal.android.sunshine.models.Forecast;
import net.chiragaggarwal.android.sunshine.models.Forecasts;

public class WeatherForecastAdapter extends BaseAdapter {
    private final Context context;
    private Forecasts forecasts;

    public WeatherForecastAdapter(Context context, Forecasts forecasts) {
        this.context = context;
        this.forecasts = forecasts;
    }

    @Override
    public int getCount() {
        return forecasts.getCount();
    }

    @Override
    public Forecast getItem(int position) {
        return this.forecasts.getItem(position);
    }

    @Override
    public long getItemId(int position) {
        return position;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        View view;
        Forecast forecast = getItem(position);
        if (convertView == null) {
            view = LayoutInflater.from(this.context).inflate(R.layout.list_item_forecast, parent, false);
        } else {
            view = convertView;
        }
        TextView weatherForecastSummary = (TextView) view.findViewById(R.id.list_item_forecast_summary);
        weatherForecastSummary.setText(forecast.summary());
        return view;
    }

    public void replaceForecasts(Forecasts forecasts) {
        this.forecasts = forecasts;
        notifyDataSetChanged();
    }
}
