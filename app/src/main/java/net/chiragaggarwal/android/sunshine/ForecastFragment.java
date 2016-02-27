package net.chiragaggarwal.android.sunshine;

import android.app.AlertDialog;
import android.content.ContentValues;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.database.Cursor;
import android.net.Uri;
import android.os.Bundle;
import android.preference.PreferenceManager;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v4.app.LoaderManager;
import android.support.v4.content.CursorLoader;
import android.support.v4.content.Loader;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ListView;
import android.widget.TextView;

import net.chiragaggarwal.android.sunshine.data.DatabaseHelper;
import net.chiragaggarwal.android.sunshine.models.Callback;
import net.chiragaggarwal.android.sunshine.models.Forecast;
import net.chiragaggarwal.android.sunshine.models.Forecasts;
import net.chiragaggarwal.android.sunshine.models.ForecastsForLocation;
import net.chiragaggarwal.android.sunshine.models.Location;
import net.chiragaggarwal.android.sunshine.models.LocationPreferences;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;

import static net.chiragaggarwal.android.sunshine.data.ForecastContract.ForecastEntry;
import static net.chiragaggarwal.android.sunshine.data.ForecastContract.LocationEntry;

public class ForecastFragment extends Fragment implements LoaderManager.LoaderCallbacks<Cursor> {
    private static final String YYYY_MM_DD = "yyyyMMdd";
    private ListView forecastList;
    private TextView invalidPreferencesTextView;
    private WeatherForecastAdapter weatherForecastAdapter;
    private OnForecastSelectedListener onForecastSelectedListener;

    public void onLocationChanged() {
        loadWeeklyForecastsStartingFromToday();
        LocationPreferences.getInstance(getSharedPreferences()).setLocationAsNotChanged();
    }

    public interface OnForecastSelectedListener {
        void onForecastSelected(Forecast forecast);
    }

    @Override
    public void onAttach(Context context) {
        super.onAttach(context);
        try {
            onForecastSelectedListener = ((OnForecastSelectedListener) context);
        } catch (ClassCastException exception) {
            throw new ClassCastException(context.toString()
                    + " must implement OnHeadlineSelectedListener");
        }
    }

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setHasOptionsMenu(true);
        this.weatherForecastAdapter = new WeatherForecastAdapter(getContext(),
                new Forecasts());
    }

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_forecasts, container, false);
        initializeWidgets(view);
        setOnItemClickListenerForForecastList();
        return view;
    }

    @Override
    public void onStart() {
        super.onStart();
        loadWeeklyForecastsStartingFromToday();
    }

    @Override
    public void onPause() {
        super.onPause();
        DatabaseHelper.getInstance(getContext()).close();
    }

    @Override
    public void onCreateOptionsMenu(Menu menu, MenuInflater inflater) {
        super.onCreateOptionsMenu(menu, inflater);
        menu.clear();
        inflater.inflate(R.menu.menu_forecast_fragment, menu);
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        SharedPreferences sharedPreferences = getSharedPreferences();
        switch (item.getItemId()) {
            case R.id.forecast_action_show_location:
                String zipCode = savedZipCode(sharedPreferences);
                showLocationAt(zipCode);
                break;
            case R.id.forecast_action_settings:
                launchSettings();
                break;
        }
        return false;
    }

    private void fetchWeatherForecast(SharedPreferences sharedPreferences) {
        new FetchWeatherForecastsTask(
                savedZipCode(sharedPreferences),
                savedCountryCode(sharedPreferences),
                savedTemperatureUnit(sharedPreferences),
                new Callback<ForecastsForLocation>() {
                    @Override
                    public void onSuccess(ForecastsForLocation forecastsForLocation) {
                        if (isForecastListGone()) {
                            removeInvalidPreferences();
                            showForecastList();
                        }
                        save(forecastsForLocation);
                        reloadWeeklyForecastsStartingFromToday();
                    }

                    @Override
                    public void onFailure() {
                        if (isInvalidPreferencesGone()) {
                            removeForecastList();
                            showInvalidPreferences();
                        }
                    }
                }).execute();
    }

    private void showForecasts(Forecasts forecasts) {
        this.weatherForecastAdapter.replaceForecasts(forecasts);
    }

    private void save(ForecastsForLocation forecastsForLocation) {
        Forecasts forecasts = forecastsForLocation.forecasts;
        Location location = forecastsForLocation.location;

        Long locationRowId = insertLocationIfNotPresent(location);
        insertForecastsForLocationIfNotPresent(forecasts, locationRowId);
    }

    private void launchSettings() {
        Intent intent = new Intent(getContext(), SettingsActivity.class);
        startActivity(intent);
    }

    private void showLocationAt(String zipCode) {
        Uri mapInformation = buildUriToViewLocation(zipCode);
        Intent intent = new Intent(Intent.ACTION_VIEW, mapInformation);
        if (canDisplayMaps(intent)) {
            startActivity(intent);
        } else {
            showMapsAppNotFoundAlert();
        }
    }

    private void showMapsAppNotFoundAlert() {
        String errorTitle = "Error";
        String errorMessage = "No supporting app found.\nPlease install Google Maps";
        new AlertDialog.Builder(getContext()).
                setTitle(errorTitle).
                setMessage(errorMessage).
                show();
    }

    private Uri buildUriToViewLocation(String zipCode) {
        return Uri.parse("geo:0,0?q=" + Uri.encode(zipCode));
    }

    private void initializeWidgets(View view) {
        this.forecastList = (ListView) view.findViewById(R.id.forecast_list);
        this.forecastList.setAdapter(this.weatherForecastAdapter);
        this.invalidPreferencesTextView = (TextView) view.findViewById(R.id.invalid_preferences);
    }

    private void setOnItemClickListenerForForecastList() {
        this.forecastList.setOnItemClickListener(onItemClickListenerForForecastList());
    }

    private void showForecastList() {
        this.forecastList.setVisibility(ListView.VISIBLE);
    }

    private void removeForecastList() {
        this.forecastList.setVisibility(ListView.GONE);
    }

    private void showInvalidPreferences() {
        this.invalidPreferencesTextView.setVisibility(TextView.VISIBLE);
    }

    private String savedCountryCode(SharedPreferences sharedPreferences) {
        String preferenceCountryCodeKey = getString(R.string.preference_country_code_key);
        return sharedPreferences.getString(preferenceCountryCodeKey, "");
    }

    private String savedZipCode(SharedPreferences sharedPreferences) {
        String preferenceZipCodeKey = getString(R.string.preference_zip_code_key);
        return sharedPreferences.getString(preferenceZipCodeKey, "");
    }

    private String savedTemperatureUnit(SharedPreferences sharedPreferences) {
        String preferenceTemperatureUnitKey = getString(R.string.preference_temperature_unit_key);
        return sharedPreferences.getString(preferenceTemperatureUnitKey, "");
    }

    @NonNull
    private AdapterView.OnItemClickListener onItemClickListenerForForecastList() {
        return new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                Forecast forecast = weatherForecastAdapter.getItem(position);
                onForecastSelectedListener.onForecastSelected(forecast);
            }
        };
    }

    private void removeInvalidPreferences() {
        this.invalidPreferencesTextView.setVisibility(TextView.GONE);
    }

    public boolean isForecastListGone() {
        return (forecastList.getVisibility() == ListView.GONE);
    }

    private Long insertLocationIfNotPresent(Location location) {
        Cursor locationCursor = queryLocationFromLocationProvider(location);
        Long locationRowId = null;

        if (isLocationNotPresent(locationCursor)) {
            Uri locationUri = insertLocationInLocationProvider(location);
            locationRowId = Long.decode(locationUri.getLastPathSegment());
        } else {
            locationRowId = getIdOfLocationAlreadyPresent(locationCursor);
        }

        return locationRowId;
    }

    private void insertForecastsForLocationIfNotPresent(Forecasts forecasts, Long locationRowId) {
        Cursor forecastsCursor = queryForecastsForLocationFromForecastsProviderStartingFromToday(locationRowId);
        if (isOneWeeksForecastsNotPresent(forecastsCursor)) {
            ContentValues[] forecastsValues = forecasts.toContentValues(locationRowId);
            insertForecastsInForecastsProvider(forecastsValues);
        }
    }

    private boolean isInvalidPreferencesGone() {
        return (this.invalidPreferencesTextView.getVisibility() == TextView.GONE);
    }

    private boolean canDisplayMaps(Intent intent) {
        return intent.resolveActivity(getActivity().getPackageManager()) != null;
    }

    private Cursor queryLocationFromLocationProvider(Location location) {
        return getContext().getContentResolver().
                query(LocationEntry.CONTENT_URI,
                        null,
                        LocationEntry.TABLE_NAME + "." + LocationEntry.COLUMN_LOCATION_SETTING + "=?",
                        new String[]{location.postalCode},
                        null);
    }

    private boolean isLocationNotPresent(Cursor locationCursor) {
        return locationCursor.getCount() == 0;
    }

    private Uri insertLocationInLocationProvider(Location location) {
        return getContext().getContentResolver().insert(
                LocationEntry.CONTENT_URI,
                location.toContentValues()
        );
    }

    @NonNull
    private Long getIdOfLocationAlreadyPresent(Cursor locationCursor) {
        locationCursor.moveToFirst();
        return locationCursor.getLong(0);
    }

    private Cursor queryForecastsForLocationFromForecastsProviderStartingFromToday(Long locationRowId) {
        String locationRowIdArgument = locationRowId.toString();
        String dateArgument = parsedCurrentDateArgument();

        return getContext().getContentResolver().query(
                ForecastEntry.CONTENT_URI,
                null,
                ForecastEntry.buildForecastsSelectionForLocationIdWithStartDate(),
                new String[]{locationRowIdArgument, dateArgument},
                null
        );
    }

    @NonNull
    private String parsedCurrentDateArgument() {
        SimpleDateFormat currentDateFormat = new SimpleDateFormat(YYYY_MM_DD);
        String formattedCurrentDate = currentDateFormat.format(new Date());
        return formattedCurrentDate;
    }

    private boolean isOneWeeksForecastsNotPresent(Cursor forecastsCursor) {
        return forecastsCursor.getCount() < 6;
    }

    private void insertForecastsInForecastsProvider(ContentValues[] forecastsValues) {
        getContext().getContentResolver().bulkInsert(
                ForecastEntry.CONTENT_URI, forecastsValues
        );
    }

    @Override
    public Loader<Cursor> onCreateLoader(int loaderId, Bundle arguments) {
        Loader<Cursor> forecastsLoader = buildLoaderToFetchForecastsStartingFromToday(arguments);
        return forecastsLoader;
    }

    @Override
    public void onLoadFinished(Loader<Cursor> loader, Cursor dataCursor) {
        if (didNotFindAnyForecasts(dataCursor)) onLoaderReset(loader);
        if (dataCursor == null) return;

        onForecastsLoaded(dataCursor);
    }

    private boolean didNotFindAnyForecasts(Cursor dataCursor) {
        return dataCursor == null || dataCursor.getCount() == 0;
    }

    private void onForecastsLoaded(Cursor forecastsCursor) {
        if (isOneWeeksForecastsNotPresent(forecastsCursor)) {
            fetchWeatherForecast(getSharedPreferences());
        } else {
            try {
                Forecasts forecasts = null;
                forecasts = Forecasts.fromCursor(forecastsCursor);
                showForecasts(forecasts);
            } catch (ParseException e) {
                e.printStackTrace();
                return;
            }
        }
    }

    @Override
    public void onLoaderReset(Loader<Cursor> loader) {
        loader.reset();
    }

    private void loadWeeklyForecastsStartingFromToday() {
        Bundle forecastsBundle = forecastsBundle();
        getLoaderManager().initLoader(buildUniqueLoaderId(), forecastsBundle, this);
    }

    private int buildUniqueLoaderId() {
        String zipCode = this.savedZipCode(getSharedPreferences());
        return Integer.parseInt(zipCode);
    }

    private void reloadWeeklyForecastsStartingFromToday() {
        Bundle forecastsBundle = forecastsBundle();
        getLoaderManager().restartLoader(buildUniqueLoaderId(), forecastsBundle, this);
    }

    @NonNull
    private Bundle forecastsBundle() {
        SharedPreferences sharedPreferences = getSharedPreferences();
        String savedZipCode = savedZipCode(sharedPreferences);
        Bundle forecastsBundle = new Bundle();
        forecastsBundle.putString(LocationEntry.COLUMN_LOCATION_SETTING, savedZipCode);
        return forecastsBundle;
    }

    private SharedPreferences getSharedPreferences() {
        return PreferenceManager.getDefaultSharedPreferences(getContext());
    }

    private Loader<Cursor> buildLoaderToFetchForecastsStartingFromToday(Bundle arguments) {
        String savedZipCode = arguments.getString(LocationEntry.COLUMN_LOCATION_SETTING);

        return new CursorLoader(getContext(),
                ForecastEntry.buildForecastsForLocationEndpoint(savedZipCode),
                null,
                ForecastEntry.buildForecastsSelectionForLocationIdWithStartDate(),
                new String[]{savedZipCode, parsedCurrentDateArgument()},
                null
        );
    }
}
