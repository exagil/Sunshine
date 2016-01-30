package net.chiragaggarwal.android.sunshine.models;

import android.os.Parcel;
import android.os.Parcelable;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.Date;

public class Forecast implements Parcelable {
    private static final String TEMPERATURE = "temp";
    private static final String WEATHER = "weather";
    private static final String DATE = "dt";
    private static final String MINIMUM_TEMPERATURE = "min";
    private static final String MAXIMUM_TEMPERATURE = "max";
    private static final String MAIN_DESCRIPTION = "main";

    private final Date date;
    private final Double minimumTemperature;
    public final String mainDescription;
    private final Double maximumTemperature;

    public Forecast(Date date, Double minimumTemperature, Double maximumTemperature, String mainDescription) {
        this.date = date;
        this.minimumTemperature = minimumTemperature;
        this.maximumTemperature = maximumTemperature;
        this.mainDescription = mainDescription;
    }

    public static Forecast fromJSON(JSONObject dayForecast) throws JSONException {
        JSONObject temperature = dayForecast.getJSONObject(TEMPERATURE);
        JSONObject weather = dayForecast.getJSONArray(WEATHER).getJSONObject(0);

        Date date = new Date(Long.parseLong(dayForecast.getString(DATE)));
        Double minimumTemperature = temperature.getDouble(MINIMUM_TEMPERATURE);
        Double maximumTemperature = temperature.getDouble(MAXIMUM_TEMPERATURE);
        String mainDescription = weather.getString(MAIN_DESCRIPTION);
        return new Forecast(date, minimumTemperature, maximumTemperature, mainDescription);
    }

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeLong(date != null ? date.getTime() : -1);
        dest.writeValue(this.minimumTemperature);
        dest.writeString(this.mainDescription);
        dest.writeValue(this.maximumTemperature);
    }

    public static final Creator<Forecast> CREATOR = new Creator<Forecast>() {
        public Forecast createFromParcel(Parcel source) {
            return new Forecast(source);
        }

        public Forecast[] newArray(int size) {
            return new Forecast[size];
        }
    };

    protected Forecast(Parcel in) {
        long tmpDate = in.readLong();
        this.date = tmpDate == -1 ? null : new Date(tmpDate);
        this.minimumTemperature = (Double) in.readValue(Double.class.getClassLoader());
        this.mainDescription = in.readString();
        this.maximumTemperature = (Double) in.readValue(Double.class.getClassLoader());
    }
}
