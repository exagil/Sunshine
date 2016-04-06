package net.chiragaggarwal.android.sunshine.models;

import android.os.Parcel;
import android.os.Parcelable;

import org.json.JSONException;
import org.json.JSONObject;

public class ForecastsForLocation implements Parcelable {
    public static final String ACTION_BROADCAST = "forecsasts_for_location_broadcast";
    public static final String TAG = "net.chiragaggarwal.android.sunshine.models.ForecastsForLocation";
    public Location location;
    public Forecasts forecasts;

    public ForecastsForLocation(Location location, Forecasts forecasts) {
        this.location = location;
        this.forecasts = forecasts;
    }

    public static ForecastsForLocation fromJSON(JSONObject response, String postalCode) throws JSONException {
        Location location = Location.fromJSON(response, postalCode);
        Forecasts forecasts = Forecasts.fromJSON(response);
        return new ForecastsForLocation(location, forecasts);
    }

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeParcelable(this.location, flags);
        dest.writeParcelable(this.forecasts, 0);
    }

    protected ForecastsForLocation(Parcel in) {
        this.location = in.readParcelable(Location.class.getClassLoader());
        this.forecasts = in.readParcelable(Forecasts.class.getClassLoader());
    }

    public static final Creator<ForecastsForLocation> CREATOR = new Creator<ForecastsForLocation>() {
        public ForecastsForLocation createFromParcel(Parcel source) {
            return new ForecastsForLocation(source);
        }

        public ForecastsForLocation[] newArray(int size) {
            return new ForecastsForLocation[size];
        }
    };
}
