package net.chiragaggarwal.android.sunshine.models;

import android.content.ContentValues;
import android.content.SharedPreferences;

import org.json.JSONException;
import org.json.JSONObject;

import static net.chiragaggarwal.android.sunshine.data.ForecastContract.LocationEntry;

public class Location {

    private static final String CITY = "city";
    private static final String COORDINATES = "coord";
    private static final String NAME = "name";
    private static final String LONGITUDE = "lon";
    private static final String LATITUDE = "lat";
    public static final String HAS_CHANGED = "net.chiragaggarwal.android.sunshine.models.Location.HAS_CHANGED";

    public String name;
    public String postalCode;
    private Double latitude;
    private Double longitude;

    public Location(String name, String postalCode, Double latitude, Double longitude) {
        this.name = name;
        this.postalCode = postalCode;
        this.latitude = latitude;
        this.longitude = longitude;
    }

    public static Location fromJSON(JSONObject response, String postalCode) throws JSONException {
        JSONObject cityJSONObject = response.getJSONObject(CITY);
        JSONObject coordinatesJSONObject = cityJSONObject.getJSONObject(COORDINATES);

        String cityName = cityJSONObject.getString(NAME);
        Double longitude = coordinatesJSONObject.getDouble(LONGITUDE);
        Double latitude = coordinatesJSONObject.getDouble(LATITUDE);

        return new Location(cityName, postalCode, latitude, longitude);
    }

    public static boolean hasChanged(SharedPreferences sharedPreferences) {
        return sharedPreferences.getBoolean(Location.HAS_CHANGED, false);
    }

    public ContentValues toContentValues() {
        ContentValues locationContentValues = new ContentValues();
        locationContentValues.put(LocationEntry.COLUMN_CITY_NAME, this.name);
        locationContentValues.put(LocationEntry.COLUMN_LOCATION_SETTING, this.postalCode);
        locationContentValues.put(LocationEntry.COLUMN_LATITUDE, this.latitude);
        locationContentValues.put(LocationEntry.COLUMN_LONGITUDE, this.longitude);
        return locationContentValues;
    }
}
