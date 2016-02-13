package net.chiragaggarwal.android.sunshine.data;

import android.content.ContentProvider;
import android.content.ContentValues;
import android.content.UriMatcher;
import android.database.Cursor;
import android.net.Uri;
import android.support.annotation.Nullable;

import static net.chiragaggarwal.android.sunshine.data.ForecastContract.ForecastEntry;

public class ForecastsProvider extends ContentProvider {
    private static UriMatcher uriMatcher = new UriMatcher(UriMatcher.NO_MATCH);

    private static final int FORECASTS_ENDPOINT = 1;
    private static final int FORECASTS_FOR_LOCATION_ENDPOINT = 2;

    private static final int FORECAST_FOR_LOCATION_AND_DATE_ENDPOINT = 3;

    static {
        uriMatcher.addURI(ForecastEntry.FORECASTS_PROVIDER_AUTHORITY,
                ForecastEntry.FORECASTS_PATH,
                FORECASTS_ENDPOINT);

        uriMatcher.addURI(ForecastEntry.FORECASTS_PROVIDER_AUTHORITY,
                ForecastEntry.FORECASTS_FOR_LOCATION_PATH,
                FORECASTS_FOR_LOCATION_ENDPOINT);

        uriMatcher.addURI(ForecastEntry.FORECASTS_PROVIDER_AUTHORITY,
                ForecastEntry.FORECAST_FOR_LOCATION_AND_DATE_PATH,
                FORECAST_FOR_LOCATION_AND_DATE_ENDPOINT);
    }

    @Override
    public boolean onCreate() {
        return true;
    }

    @Nullable
    @Override
    public Cursor query(Uri uri, String[] projection, String selection, String[] selectionArgs, String sortOrder) {
        int matchCode = uriMatcher.match(uri);

        DatabaseHelper databaseHelper = DatabaseHelper.getInstance(getContext());
        ForecastsRepository forecastsRepository = ForecastsRepository.getInstance(databaseHelper);

        switch (matchCode) {
            case FORECASTS_ENDPOINT:
                Cursor forecastsCursor = forecastsRepository.fetchAll();
                return forecastsCursor;
        }
        return null;
    }

    @Nullable
    @Override
    public String getType(Uri uri) {
        int matchedCode = uriMatcher.match(uri);
        switch (matchedCode) {
            case FORECASTS_ENDPOINT:
            case FORECASTS_FOR_LOCATION_ENDPOINT:
                return ForecastEntry.DATA_TYPE_FORECASTS_COLLECTION;
            case FORECAST_FOR_LOCATION_AND_DATE_ENDPOINT:
                return ForecastEntry.DATA_TYPE_FORECAST_ITEM;
        }
        return null;
    }

    @Nullable
    @Override
    public Uri insert(Uri uri, ContentValues values) {
        return null;
    }

    @Override
    public int delete(Uri uri, String selection, String[] selectionArgs) {
        return 0;
    }

    @Override
    public int update(Uri uri, ContentValues values, String selection, String[] selectionArgs) {
        return 0;
    }
}
