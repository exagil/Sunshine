package net.chiragaggarwal.android.sunshine.models;

import org.json.JSONException;
import org.json.JSONObject;

public class ForecastsForLocation {
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
}
