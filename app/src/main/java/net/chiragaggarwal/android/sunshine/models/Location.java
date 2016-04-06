package net.chiragaggarwal.android.sunshine.models;

import android.content.ContentValues;
import android.os.Parcel;
import android.os.Parcelable;

import org.json.JSONException;
import org.json.JSONObject;

import static net.chiragaggarwal.android.sunshine.data.ForecastContract.LocationEntry;

public class Location implements Parcelable {
    public static final String COUNTRY_CODE = "country_code";
    public static final String POSTAL_CODE = "postal_code";

    private static final String CITY = "city";
    private static final String COORDINATES = "coord";
    private static final String NAME = "name";
    private static final String LONGITUDE = "lon";
    private static final String LATITUDE = "lat";

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

    public ContentValues toContentValues() {
        ContentValues locationContentValues = new ContentValues();
        locationContentValues.put(LocationEntry.COLUMN_CITY_NAME, this.name);
        locationContentValues.put(LocationEntry.COLUMN_LOCATION_SETTING, this.postalCode);
        locationContentValues.put(LocationEntry.COLUMN_LATITUDE, this.latitude);
        locationContentValues.put(LocationEntry.COLUMN_LONGITUDE, this.longitude);
        return locationContentValues;
    }

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeString(this.name);
        dest.writeString(this.postalCode);
        dest.writeValue(this.latitude);
        dest.writeValue(this.longitude);
    }

    protected Location(Parcel in) {
        this.name = in.readString();
        this.postalCode = in.readString();
        this.latitude = (Double) in.readValue(Double.class.getClassLoader());
        this.longitude = (Double) in.readValue(Double.class.getClassLoader());
    }

    public static final Creator<Location> CREATOR = new Creator<Location>() {
        public Location createFromParcel(Parcel source) {
            return new Location(source);
        }

        public Location[] newArray(int size) {
            return new Location[size];
        }
    };
}
