package net.chiragaggarwal.android.sunshine;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;

import net.chiragaggarwal.android.sunshine.models.Forecast;
import net.chiragaggarwal.android.sunshine.models.Forecasts;

public class WeatherForecastAdapter extends BaseAdapter {
    private static final int DEFAULT_FORECAST_VIEW_TYPE = 0;
    private static final int TODAYS_FORECAST_VIEW_TYPE = 1;
    private static final int MAX_FORECAST_LAYOUT_TYPES = 2;
    private final Context context;
    private final boolean isTodaysForecastViewTypeEnabled;
    private Forecasts forecasts;

    public WeatherForecastAdapter(Context context, Forecasts forecasts, boolean isTodaysForecastViewTypeEnabled) {
        this.context = context;
        this.forecasts = forecasts;
        this.isTodaysForecastViewTypeEnabled = isTodaysForecastViewTypeEnabled;
    }

    @Override
    public int getViewTypeCount() {
        return MAX_FORECAST_LAYOUT_TYPES;
    }

    @Override
    public int getItemViewType(int position) {
        int viewType = DEFAULT_FORECAST_VIEW_TYPE;
        if (isTodaysForecastViewTypeEnabled && isFirstForecast(position))
            viewType = TODAYS_FORECAST_VIEW_TYPE;
        return viewType;
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
        int viewType = getItemViewType(position);

        if (viewType == TODAYS_FORECAST_VIEW_TYPE) {
            view = loadTodaysForecastView(position, convertView);
        } else {
            view = loadForecastView(position, convertView);
        }

        return view;
    }

    private View loadForecastView(int position, View convertView) {
        View view;
        ForecastHolder forecastHolder;

        if (convertView == null) {
            view = LayoutInflater.from(this.context).inflate(R.layout.list_item_forecast, null);
            forecastHolder = new ForecastHolder(view);
            view.setTag(forecastHolder);
        } else {
            view = convertView;
            forecastHolder = (ForecastHolder) view.getTag();
        }

        Forecast forecast = getItem(position);
        forecastHolder.bindView(forecast, this.context);
        return view;
    }

    private View loadTodaysForecastView(int position, View convertView) {
        View view;
        TodaysForecastHolder todaysForecastHolder;

        if (convertView == null) {
            view = LayoutInflater.from(this.context).inflate(R.layout.list_item_forecast_today, null);
            todaysForecastHolder = new TodaysForecastHolder(view);
            view.setTag(todaysForecastHolder);
        } else {
            view = convertView;
            todaysForecastHolder = (TodaysForecastHolder) view.getTag();
        }
        Forecast forecast = getItem(position);
        todaysForecastHolder.bindView(forecast, this.context);
        return view;
    }

    public void replaceForecasts(Forecasts forecasts) {
        this.forecasts = forecasts;
        notifyDataSetChanged();
    }

    private boolean isFirstForecast(int position) {
        return position == 0;
    }

    public Forecasts getForecasts() {
        return forecasts;
    }
}
