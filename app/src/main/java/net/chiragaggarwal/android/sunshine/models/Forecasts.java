package net.chiragaggarwal.android.sunshine.models;

import android.content.ContentValues;
import android.database.Cursor;
import android.os.Parcel;
import android.os.Parcelable;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;

public class Forecasts implements Parcelable {
    private static final String LIST = "list";
    private ArrayList<Forecast> forecasts = new ArrayList<>();

    public Forecasts(Forecast... forecasts) {
        initialize(forecasts);
    }

    public static Forecasts fromJSON(JSONObject forecastsJSONResponse) throws JSONException {
        JSONArray forecastsList = forecastsJSONResponse.getJSONArray(LIST);
        Forecasts forecasts = new Forecasts();
        for (Integer dayIndex = 0; dayIndex < forecastsList.length(); dayIndex++) {
            JSONObject dayForecastJSON = forecastsList.getJSONObject(dayIndex);
            Forecast forecast = Forecast.fromJSON(dayForecastJSON);
            forecasts.add(forecast);
        }
        return forecasts;
    }

    public static Forecasts fromCursor(Cursor forecastsCursor) {
        Forecasts forecasts = new Forecasts();

        while (forecastsCursor.moveToNext()) {
            Forecast forecast = Forecast.fromCursor(forecastsCursor);
            forecasts.add(forecast);
        }

        return forecasts;
    }

    public int getCount() {
        return this.count();
    }

    public Forecast getItem(int position) {
        return this.forecasts.get(position);
    }

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeTypedList(forecasts);
    }

    public static final Creator<Forecasts> CREATOR = new Creator<Forecasts>() {
        public Forecasts createFromParcel(Parcel source) {
            return new Forecasts(source);
        }

        public Forecasts[] newArray(int size) {
            return new Forecasts[size];
        }
    };

    public ContentValues[] toContentValues(long locationRowId) {
        int numberOfForecasts = this.count();
        ContentValues[] contentValues = new ContentValues[numberOfForecasts];

        for (int forecastIndex = 0; forecastIndex < numberOfForecasts; forecastIndex++) {
            Forecast forecast = this.forecasts.get(forecastIndex);
            contentValues[forecastIndex] = forecast.toContentValues(locationRowId);
        }
        return contentValues;
    }

    protected Forecasts(Parcel in) {
        this.forecasts = in.createTypedArrayList(Forecast.CREATOR);
    }

    private boolean add(Forecast forecast) {
        return this.forecasts.add(forecast);
    }

    private void initialize(Forecast[] forecasts) {
        for (int forecastPosition = 0; forecastPosition < forecasts.length; forecastPosition++) {
            this.forecasts.add(forecasts[forecastPosition]);
        }
    }

    private int count() {
        return this.forecasts.size();
    }
}
