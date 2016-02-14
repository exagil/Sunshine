package net.chiragaggarwal.android.sunshine.data;

import android.content.ContentProvider;
import android.content.ContentValues;
import android.content.UriMatcher;
import android.database.Cursor;
import android.net.Uri;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;

import static net.chiragaggarwal.android.sunshine.data.ForecastContract.ForecastEntry;

public class ForecastsProvider extends ContentProvider {
    private static UriMatcher uriMatcher = buildUriMatcher();

    public static final int FORECASTS_ENDPOINT = 1;
    public static final int FORECASTS_FOR_LOCATION_ENDPOINT = 2;
    public static final int FORECAST_FOR_LOCATION_AND_DATE_ENDPOINT = 3;

    public static UriMatcher buildUriMatcher() {
        UriMatcher uriMatcher = new UriMatcher(UriMatcher.NO_MATCH);
        addUriForForecasts(uriMatcher);
        addUriForForecastsWithLocation(uriMatcher);
        addUriForForecastsWithLocationAndDate(uriMatcher);
        return uriMatcher;
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

        Cursor forecastsCursor = null;
        String locationSelection;
        String date;
        switch (matchCode) {
            case FORECASTS_ENDPOINT:
                forecastsCursor = forecastsRepository.query(projection, selection, selectionArgs, sortOrder);
                break;
            case FORECASTS_FOR_LOCATION_ENDPOINT:
                locationSelection = extractLastElementFromUri(uri);
                forecastsCursor = forecastsRepository.findForecastsByLocationSelection(locationSelection);
                break;
            case FORECAST_FOR_LOCATION_AND_DATE_ENDPOINT:
                locationSelection = extractSecondLastElementFromUri(uri);
                date = extractLastElementFromUri(uri);
                forecastsCursor = forecastsRepository.findForecastsByLocationSelectionAndDate(locationSelection, date);
                break;
        }
        forecastsCursor.setNotificationUri(getContext().getContentResolver(), uri);
        return forecastsCursor;
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
        DatabaseHelper databaseHelper = DatabaseHelper.getInstance(getContext());
        ForecastsRepository forecastsRepository = ForecastsRepository.getInstance(databaseHelper);

        Uri newForecastUri = null;
        if (hasHitForecastsEndpoint(uri)) {
            Long id = forecastsRepository.insert(values);
            newForecastUri = buildUriForForecastWithId(id);
        }
        notifyDatasetChanged(uri);
        return newForecastUri;
    }

    @Override
    public int delete(Uri uri, String selection, String[] selectionArgs) {
        DatabaseHelper databaseHelper = DatabaseHelper.getInstance(getContext());
        ForecastsRepository forecastsRepository = ForecastsRepository.getInstance(databaseHelper);

        int numberOfForecastsDeleted = 0;
        if (hasHitForecastsEndpoint(uri)) {
            numberOfForecastsDeleted = forecastsRepository.deleteAll();
        }
        notifyDatasetChanged(uri);
        return numberOfForecastsDeleted;
    }

    @Override
    public int update(Uri uri, ContentValues values, String selection, String[] selectionArgs) {
        return 0;
    }

    private String extractLastElementFromUri(Uri uri) {
        String[] uriParts = splitUriIntoParts(uri);
        int lastUriElementIndex = getLastUriElementIndex(uriParts);
        return uriParts[lastUriElementIndex];
    }

    private String extractSecondLastElementFromUri(Uri uri) {
        String[] uriParts = splitUriIntoParts(uri);
        int secondLastUriElementIndex = getSecondLastUriElementIndex(uriParts);
        return uriParts[secondLastUriElementIndex];
    }

    @NonNull
    private String[] splitUriIntoParts(Uri uri) {
        return uri.toString().split("/");
    }

    private int getLastUriElementIndex(String[] uriParts) {
        return uriParts.length - 1;
    }

    private int getSecondLastUriElementIndex(String[] parts) {
        return parts.length - 2;
    }

    private static void addUriForForecastsWithLocationAndDate(UriMatcher uriMatcher) {
        uriMatcher.addURI(ForecastEntry.FORECASTS_PROVIDER_AUTHORITY,
                ForecastEntry.FORECAST_FOR_LOCATION_AND_DATE_PATH,
                FORECAST_FOR_LOCATION_AND_DATE_ENDPOINT);
    }

    private static void addUriForForecastsWithLocation(UriMatcher uriMatcher) {
        uriMatcher.addURI(ForecastEntry.FORECASTS_PROVIDER_AUTHORITY,
                ForecastEntry.FORECASTS_FOR_LOCATION_PATH,
                FORECASTS_FOR_LOCATION_ENDPOINT);
    }

    private static void addUriForForecasts(UriMatcher uriMatcher) {
        uriMatcher.addURI(ForecastEntry.FORECASTS_PROVIDER_AUTHORITY,
                ForecastEntry.FORECASTS_PATH,
                FORECASTS_ENDPOINT);
    }

    private boolean hasHitForecastsEndpoint(Uri uri) {
        return uriMatcher.match(uri) == FORECASTS_ENDPOINT;
    }

    private Uri buildUriForForecastWithId(Long id) {
        String forecastIdPath = id.toString();
        return ForecastEntry.CONTENT_URI.buildUpon().appendPath(forecastIdPath).build();
    }

    private void notifyDatasetChanged(Uri uri) {
        getContext().getContentResolver().notifyChange(uri, null);
    }
}
