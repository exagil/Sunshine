package net.chiragaggarwal.android.sunshine.models;

import android.os.Parcel;
import android.os.Parcelable;

import java.util.ArrayList;

public class Forecasts implements Parcelable {
    public static final String TAG = "net/chiragaggarwal/android/sunshine/models/Forecasts";
    private ArrayList<Forecast> forecasts = new ArrayList<>();

    public Forecasts(Forecast... forecasts) {
        initialize(forecasts);
    }

    private void initialize(Forecast[] forecasts) {
        for (int forecastPosition = 0; forecastPosition < forecasts.length; forecastPosition++) {
            this.forecasts.add(forecasts[forecastPosition]);
        }
    }

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeTypedList(forecasts);
    }

    protected Forecasts(Parcel in) {
        this.forecasts = in.createTypedArrayList(Forecast.CREATOR);
    }

    public static final Creator<Forecasts> CREATOR = new Creator<Forecasts>() {
        public Forecasts createFromParcel(Parcel source) {
            return new Forecasts(source);
        }

        public Forecasts[] newArray(int size) {
            return new Forecasts[size];
        }
    };
}
