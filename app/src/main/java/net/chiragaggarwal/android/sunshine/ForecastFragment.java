package net.chiragaggarwal.android.sunshine;

import android.accounts.Account;
import android.accounts.AccountManager;
import android.app.AlertDialog;
import android.content.ContentResolver;
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
import net.chiragaggarwal.android.sunshine.models.Forecast;
import net.chiragaggarwal.android.sunshine.models.Forecasts;
import net.chiragaggarwal.android.sunshine.models.LocationPreferences;

import java.text.ParseException;

import static android.widget.AdapterView.OnItemClickListener;
import static net.chiragaggarwal.android.sunshine.Utils.Utility.parsedCurrentDateArgument;
import static net.chiragaggarwal.android.sunshine.data.ForecastContract.ForecastEntry;
import static net.chiragaggarwal.android.sunshine.data.ForecastContract.LocationEntry;

public class ForecastFragment extends Fragment implements LoaderManager.LoaderCallbacks<Cursor> {
    private static final int FIRST_POSITION_INDEX = 0;
    private static final String SELECTED_FORECAST_POSITION = "net.chiragaggarwal.android.sunshine.ForecastFragment.SELECTED_FORECAST_POSITION";
    private static final String LOG_TAG = "chi6rag";
    private static final int REQUEST_CODE = 1;
    private static final String ACCOUNT_NAME = "chiragaggarwal";

    private ListView forecastList;
    private TextView invalidPreferencesTextView;
    private WeatherForecastAdapter weatherForecastAdapter;
    private OnForecastSelectedListener onForecastSelectedListener;
    private Integer selectedPosition = FIRST_POSITION_INDEX;

    @Override
    public void onSaveInstanceState(Bundle outState) {
        outState.putInt(SELECTED_FORECAST_POSITION, selectedPosition);
        outState.putParcelable(Forecasts.TAG, this.weatherForecastAdapter.getForecasts());
        super.onSaveInstanceState(outState);
    }

    public void onLocationChanged() {
        int loaderIdToNotStop = buildUniqueLoaderId();
        ForecastLoaders.getInstance().stopAllExcept(loaderIdToNotStop, getLoaderManager());
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
        boolean isTablet = getArguments().getBoolean(MainActivity.IS_TABLET, false);
        this.weatherForecastAdapter = new WeatherForecastAdapter(getContext(),
                new Forecasts(), !isTablet);
    }

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_forecasts, container, false);
        initializeWidgets(view, savedInstanceState);
        setOnItemClickListenerForForecastList();
        return view;
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
        String savedZipCode = savedZipCode(sharedPreferences);
        String savedCountryCode = savedCountryCode(sharedPreferences);
        String savedTemperatureUnit = savedTemperatureUnit(sharedPreferences);

        AccountManager accountManager = (AccountManager) getContext().getSystemService(Context.ACCOUNT_SERVICE);
        Account account = new Account(ACCOUNT_NAME, getContext().getString(R.string.account_type));
        if (accountManager.addAccountExplicitly(account, null, null)) {
        }

        Bundle extras = new Bundle();
        extras.putString(getContext().getString(R.string.preference_zip_code_key), savedZipCode);
        extras.putString(getContext().getString(R.string.preference_country_code_key), savedCountryCode);
        extras.putString(getContext().getString(R.string.preference_temperature_unit_key), savedTemperatureUnit);
        extras.putBoolean(ContentResolver.SYNC_EXTRAS_MANUAL, true);

        ContentResolver.requestSync(account, ForecastEntry.FORECASTS_PROVIDER_AUTHORITY, extras);
        reloadWeeklyForecastsStartingFromToday();
    }

    private void showForecasts(Forecasts forecasts) {
        this.weatherForecastAdapter.replaceForecasts(forecasts);
        this.forecastList.smoothScrollToPosition(selectedPosition);
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

    private void initializeWidgets(View view, Bundle savedInstanceState) {
        this.forecastList = (ListView) view.findViewById(R.id.forecast_list);
        this.forecastList.setAdapter(this.weatherForecastAdapter);
        restoreForecastsOrRequeryIfNone(savedInstanceState);
        restoreUserForecastSelectionIfAny(savedInstanceState);
        this.invalidPreferencesTextView = (TextView) view.findViewById(R.id.invalid_preferences);
    }

    private void restoreForecastsOrRequeryIfNone(Bundle savedInstanceState) {
        if (savedInstanceState == null) {
            loadWeeklyForecastsStartingFromToday();
            return;
        }
        Forecasts forecasts = savedInstanceState.getParcelable(Forecasts.TAG);
        if (forecasts == null) {
            loadWeeklyForecastsStartingFromToday();
            return;
        }
        showForecasts(forecasts);
    }

    private void restoreUserForecastSelectionIfAny(Bundle savedInstanceState) {
        if (savedInstanceState == null) return;
        this.selectedPosition = savedInstanceState.getInt(SELECTED_FORECAST_POSITION);
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
    private OnItemClickListener onItemClickListenerForForecastList() {
        return new OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                selectedPosition = position;
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

    private boolean isInvalidPreferencesGone() {
        return (this.invalidPreferencesTextView.getVisibility() == TextView.GONE);
    }

    private boolean canDisplayMaps(Intent intent) {
        return intent.resolveActivity(getActivity().getPackageManager()) != null;
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
        if (forecastsCursor.getCount() < 6) {
            fetchWeatherForecast(getSharedPreferences());
        } else {
            try {
                if (isForecastListGone()) {
                    removeInvalidPreferences();
                    showForecastList();
                }
                Forecasts forecasts = Forecasts.fromCursor(forecastsCursor);
                showForecasts(forecasts);
            } catch (ParseException e) {
                if (isInvalidPreferencesGone()) {
                    removeForecastList();
                    showInvalidPreferences();
                }
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
        int newLoaderId = Integer.parseInt(zipCode);
        ForecastLoaders.getInstance().addLoaderId(newLoaderId);
        return newLoaderId;
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
