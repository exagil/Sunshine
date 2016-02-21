package net.chiragaggarwal.android.sunshine.models;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.os.Parcel;
import android.os.Parcelable;

import net.chiragaggarwal.android.sunshine.R;
import net.chiragaggarwal.android.sunshine.data.ForecastContract;

import org.json.JSONException;
import org.json.JSONObject;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Locale;

public class Forecast implements Parcelable {
    private static final String TEMPERATURE = "temp";
    private static final String WEATHER = "weather";
    private static final String DATE = "dt";
    private static final String MINIMUM_TEMPERATURE = "min";
    private static final String MAXIMUM_TEMPERATURE = "max";
    private static final String MAIN_DESCRIPTION = "main";
    private static final String SEPERATOR = " - ";
    private static final String DAY_KEYWORD = "E";
    private static final String MONTH_NAME_KEYWORD = "MMMM";
    private static final String COMMA = ", ";
    private static final String DATE_KEYWORD = " d";
    public static final String TAG = "net.chiragaggarwal.android.sunshine.models.Forecast";
    private static final String SPACE = " ";
    private static final String DEGREES = "deg";
    private static final String HUMIDITY = "humidity";
    private static final String PRESSURE = "pressure";
    private static final String WIND_SPEED = "speed";
    private static final String WEATHER_ID = "id";
    private static final long ONE_THOUSAND_MILLISECONDS = 1000;
    private static final String DD_MM_YYYY = "ddMMyyyy";

    private final Date date;
    private final Double minimumTemperature;
    private final String mainDescription;
    private Double degrees;
    private Double humidity;
    private Double pressure;
    private Double windSpeed;
    private Long weatherId;
    private final Double maximumTemperature;

    public Forecast(Date date, Double minimumTemperature, Double maximumTemperature,
                    String mainDescription, Double degrees, Double humidity,
                    Double pressure, Double windSpeed, Long weatherId) {

        this.date = date;
        this.minimumTemperature = minimumTemperature;
        this.maximumTemperature = maximumTemperature;
        this.mainDescription = mainDescription;
        this.degrees = degrees;
        this.humidity = humidity;
        this.pressure = pressure;
        this.windSpeed = windSpeed;
        this.weatherId = weatherId;
    }

    public static Forecast fromJSON(JSONObject dayForecast) throws JSONException {
        JSONObject temperature = dayForecast.getJSONObject(TEMPERATURE);
        JSONObject weather = dayForecast.getJSONArray(WEATHER).getJSONObject(0);
        long extractedDateInMillisecondsFrom1970 = extractedUnixTimestampInSeconds(dayForecast) * ONE_THOUSAND_MILLISECONDS;

        Date date = new Date(extractedDateInMillisecondsFrom1970);
        Double minimumTemperature = temperature.getDouble(MINIMUM_TEMPERATURE);
        Double maximumTemperature = temperature.getDouble(MAXIMUM_TEMPERATURE);
        String mainDescription = weather.getString(MAIN_DESCRIPTION);
        Double degrees = dayForecast.getDouble(DEGREES);
        Double humidity = dayForecast.getDouble(HUMIDITY);
        Double pressure = dayForecast.getDouble(PRESSURE);
        Double windSpeed = dayForecast.getDouble(WIND_SPEED);
        Long weatherId = weather.getLong(WEATHER_ID);

        return new Forecast(date, minimumTemperature, maximumTemperature, mainDescription, degrees,
                humidity, pressure, windSpeed, weatherId);
    }

    public static Forecast fromCursor(Cursor forecastsCursor) throws ParseException {
        int dateIndex = forecastsCursor.getColumnIndex(ForecastContract.ForecastEntry.COLUMN_DATE);
        int minimumTemperatureIndex = forecastsCursor.getColumnIndex(ForecastContract.ForecastEntry.COLUMN_MIN_TEMP);
        int maximumTemperatureIndex = forecastsCursor.getColumnIndex(ForecastContract.ForecastEntry.COLUMN_MAX_TEMP);
        int degreesIndex = forecastsCursor.getColumnIndex(ForecastContract.ForecastEntry.COLUMN_DEGREES);
        int humidityIndex = forecastsCursor.getColumnIndex(ForecastContract.ForecastEntry.COLUMN_HUMIDITY);
        int pressureIndex = forecastsCursor.getColumnIndex(ForecastContract.ForecastEntry.COLUMN_PRESSURE);
        int mainDescriptionIndex = forecastsCursor.getColumnIndex(ForecastContract.ForecastEntry.COLUMN_SHORT_DESC);
        int weatherIdIndex = forecastsCursor.getColumnIndex(ForecastContract.ForecastEntry.COLUMN_WEATHER_ID);
        int windSpeedIndex = forecastsCursor.getColumnIndex(ForecastContract.ForecastEntry.COLUMN_WIND_SPEED);

        String dateString = forecastsCursor.getString(dateIndex);
        Date date = new SimpleDateFormat(DD_MM_YYYY).parse(dateString);
        Double minimumTemperature = forecastsCursor.getDouble(minimumTemperatureIndex);
        Double maximumTemperature = forecastsCursor.getDouble(maximumTemperatureIndex);
        String mainDescription = forecastsCursor.getString(mainDescriptionIndex);
        Double degrees = forecastsCursor.getDouble(degreesIndex);
        Double humidity = forecastsCursor.getDouble(humidityIndex);
        Double pressure = forecastsCursor.getDouble(pressureIndex);
        Double windSpeed = forecastsCursor.getDouble(windSpeedIndex);
        Long weatherId = forecastsCursor.getLong(weatherIdIndex);

        return new Forecast(date, minimumTemperature, maximumTemperature, mainDescription, degrees,
                humidity, pressure, windSpeed, weatherId);
    }

    public ContentValues toContentValues(Long locationRowId) {
        ContentValues contentValues = new ContentValues();
        contentValues.put(ForecastContract.ForecastEntry.COLUMN_MAX_TEMP, maximumTemperature);
        contentValues.put(ForecastContract.ForecastEntry.COLUMN_MIN_TEMP, minimumTemperature);
        contentValues.put(ForecastContract.ForecastEntry.COLUMN_DATE, persistableDate());
        contentValues.put(ForecastContract.ForecastEntry.COLUMN_SHORT_DESC, mainDescription);
        contentValues.put(ForecastContract.ForecastEntry.COLUMN_LOC_KEY, locationRowId);
        contentValues.put(ForecastContract.ForecastEntry.COLUMN_DEGREES, degrees);
        contentValues.put(ForecastContract.ForecastEntry.COLUMN_HUMIDITY, humidity);
        contentValues.put(ForecastContract.ForecastEntry.COLUMN_PRESSURE, pressure);
        contentValues.put(ForecastContract.ForecastEntry.COLUMN_WIND_SPEED, windSpeed);
        contentValues.put(ForecastContract.ForecastEntry.COLUMN_WEATHER_ID, weatherId);
        return contentValues;
    }

    public String summary() {
        return formattedDate() + SEPERATOR + this.mainDescription + SEPERATOR + formattedTemperatures();
    }

    public String summaryWithHashtag(Context context) {
        return summary() + SPACE + context.getString(R.string.hashtag);
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
        dest.writeValue(this.degrees);
        dest.writeValue(this.humidity);
        dest.writeValue(this.pressure);
        dest.writeValue(this.windSpeed);
        dest.writeValue(this.weatherId);
        dest.writeValue(this.maximumTemperature);
    }

    protected Forecast(Parcel in) {
        long tmpDate = in.readLong();
        this.date = tmpDate == -1 ? null : new Date(tmpDate);
        this.minimumTemperature = (Double) in.readValue(Double.class.getClassLoader());
        this.mainDescription = in.readString();
        this.degrees = (Double) in.readValue(Double.class.getClassLoader());
        this.humidity = (Double) in.readValue(Double.class.getClassLoader());
        this.pressure = (Double) in.readValue(Double.class.getClassLoader());
        this.windSpeed = (Double) in.readValue(Double.class.getClassLoader());
        this.weatherId = (Long) in.readValue(Long.class.getClassLoader());
        this.maximumTemperature = (Double) in.readValue(Double.class.getClassLoader());
    }

    public static final Creator<Forecast> CREATOR = new Creator<Forecast>() {
        public Forecast createFromParcel(Parcel source) {
            return new Forecast(source);
        }

        public Forecast[] newArray(int size) {
            return new Forecast[size];
        }
    };

    private static long extractedUnixTimestampInSeconds(JSONObject dayForecast) throws JSONException {
        return dayForecast.getLong(DATE);
    }

    private String formattedTemperatures() {
        return this.maximumTemperature + "/" + this.minimumTemperature;
    }

    private String formattedDate() {
        return new SimpleDateFormat(DAY_KEYWORD + COMMA + MONTH_NAME_KEYWORD + DATE_KEYWORD,
                Locale.US).format(this.date);
    }

    private long persistableDate() {
        SimpleDateFormat persistableDateFormat = new SimpleDateFormat(DD_MM_YYYY);
        String formattedPersistableDate = persistableDateFormat.format(this.date);
        return Long.parseLong(formattedPersistableDate);
    }
}
