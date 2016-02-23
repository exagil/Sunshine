/*
 * Copyright (C) 2014 The Android Open Source Project
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *      http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */
package net.chiragaggarwal.android.sunshine.data;

import android.net.Uri;
import android.provider.BaseColumns;
import android.text.format.Time;

/**
 * Defines table and column names for the forecast database.
 */
public class ForecastContract {
    private static final String SLASH = "/";
    private static final String CONTENT_URI_SCHEME = "content://";
    private static final String CHARACTERS_OF_ANY_LENGTH_PATH = "*";
    private static final String NUMBERS_OF_ANY_LENGTH = "#";
    private static final String DOT = ".";
    private static final String COMMA = ", ";

    public static long normalizeDate(long startDate) {
        Time time = new Time();
        time.set(startDate);
        int julianDay = Time.getJulianDay(startDate, time.gmtoff);
        return time.setJulianDay(julianDay);
    }

    public static final class LocationEntry implements BaseColumns {
        public static final String TABLE_NAME = "locations";
        public static final String COLUMN_LOCATION_SETTING = "location_setting";
        public static final String COLUMN_CITY_NAME = "city_name";
        public static final String COLUMN_LATITUDE = "latitude";
        public static final String COLUMN_LONGITUDE = "longitude";

        public static final String LOCATIONS_PROVIDER_AUTHORITY = "net.chiragaggarwal.android.sunshine.data.locations_provider";
        public static final String LOCATIONS_PATH = "locations";

        public static final String DATA_TYPE_LOCATIONS_COLLECTION = "vnd.android.cursor.dir/vnd." + LOCATIONS_PROVIDER_AUTHORITY + SLASH + LOCATIONS_PATH;

        public static final Uri CONTENT_URI = Uri.parse(CONTENT_URI_SCHEME + LOCATIONS_PROVIDER_AUTHORITY + SLASH + LOCATIONS_PATH);
    }

    public static final class ForecastEntry implements BaseColumns {

        public static final String TABLE_NAME = "forecasts";

        public static final String COLUMN_LOC_KEY = "location_id";
        public static final String COLUMN_DATE = "date";
        // Weather id as returned by API, to identify the icon to be used
        public static final String COLUMN_WEATHER_ID = "weather_id";

        // Short description and long description of the weather, as provided by API.
        // e.g "clear" vs "sky is clear".
        public static final String COLUMN_SHORT_DESC = "short_desc";

        // Min and max temperatures for the day (stored as floats)
        public static final String COLUMN_MIN_TEMP = "min";
        public static final String COLUMN_MAX_TEMP = "max";

        // Humidity is stored as a float representing percentage
        public static final String COLUMN_HUMIDITY = "humidity";

        // Humidity is stored as a float representing percentage
        public static final String COLUMN_PRESSURE = "pressure";

        // Windspeed is stored as a float representing windspeed  mph
        public static final String COLUMN_WIND_SPEED = "wind";

        // Degrees are meteorological degrees (e.g, 0 is north, 180 is south).  Stored as floats.
        public static final String COLUMN_DEGREES = "degrees";
        public static final String COLUMN_ICON = "icon";

        public static final String FORECASTS_PROVIDER_AUTHORITY = "net.chiragaggarwal.android.sunshine.data.forecasts_provider";

        public static final String FORECASTS_PATH = TABLE_NAME;
        public static final String FORECASTS_FOR_LOCATION_PATH = FORECASTS_PATH + SLASH + CHARACTERS_OF_ANY_LENGTH_PATH;
        public static final String FORECAST_FOR_LOCATION_AND_DATE_PATH = FORECASTS_PATH + SLASH + CHARACTERS_OF_ANY_LENGTH_PATH + SLASH + NUMBERS_OF_ANY_LENGTH;

        public static final Uri CONTENT_URI = Uri.parse(CONTENT_URI_SCHEME + FORECASTS_PROVIDER_AUTHORITY + SLASH + FORECASTS_PATH);

        public static final String DATA_TYPE_FORECASTS_COLLECTION = "vnd.android.cursor.dir/vnd." + FORECASTS_PROVIDER_AUTHORITY + DOT + FORECASTS_PATH;
        public static final String DATA_TYPE_FORECAST_ITEM = "vnd.android.cursor.item/vnd." + FORECASTS_PROVIDER_AUTHORITY + DOT + FORECASTS_PATH;

        public static Uri buildForecastsForLocationEndpoint(String locationSetting) {
            return CONTENT_URI.buildUpon().appendPath(locationSetting).build();
        }

        public static Uri buildForecastsForLocationWithDateEndpoint(String locationZipCode, long date) {
            return CONTENT_URI.buildUpon().
                    appendPath(locationZipCode).
                    appendPath(String.valueOf(date)).
                    build();
        }

        public static Uri buildWeatherLocation(String location) {
            return CONTENT_URI.buildUpon().
                    appendPath(location).
                    build();
        }

        public static Uri buildWeatherLocationWithDate(String query, long date) {
            return buildForecastsForLocationWithDateEndpoint(query, date);
        }

        public static String buildForecastsSelectionForLocationIdWithStartDate() {
            return ForecastEntry.COLUMN_LOC_KEY + "=? AND " + ForecastEntry.COLUMN_DATE + ">=?";
        }
    }
}
