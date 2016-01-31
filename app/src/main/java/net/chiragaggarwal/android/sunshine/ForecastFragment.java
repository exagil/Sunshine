package net.chiragaggarwal.android.sunshine;

import android.app.AlertDialog;
import android.content.Intent;
import android.content.SharedPreferences;
import android.net.Uri;
import android.os.Bundle;
import android.preference.PreferenceManager;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ListView;
import android.widget.TextView;

import net.chiragaggarwal.android.sunshine.models.Callback;
import net.chiragaggarwal.android.sunshine.models.Forecast;
import net.chiragaggarwal.android.sunshine.models.Forecasts;

public class ForecastFragment extends Fragment {
    private ListView forecastList;
    private TextView invalidPreferencesTextView;
    private WeatherForecastAdapter weatherForecastAdapter;

    @Override
    public void onStart() {
        super.onStart();
        SharedPreferences sharedPreferences = PreferenceManager.getDefaultSharedPreferences(getActivity().getApplicationContext());
        fetchWeatherForecast(sharedPreferences);
    }

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setHasOptionsMenu(true);
    }

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_forecast, container, false);
        initializeWidgets(view);
        setOnItemClickListenerForForecastList();
        return view;
    }

    @Override
    public void onCreateOptionsMenu(Menu menu, MenuInflater inflater) {
        super.onCreateOptionsMenu(menu, inflater);
        menu.clear();
        inflater.inflate(R.menu.menu_forecast_fragment, menu);
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        SharedPreferences sharedPreferences = PreferenceManager.getDefaultSharedPreferences(getContext());
        switch (item.getItemId()) {
            case R.id.forecast_action_show_location:
                String zipCode = savedZipCode(sharedPreferences);
                showLocationAt(zipCode);
                break;
            case R.id.forecast_action_refresh:
                fetchWeatherForecast(sharedPreferences);
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
                new Callback<Forecasts>() {
                    @Override
                    public void onSuccess(Forecasts forecasts) {
                        if (isForecastListGone()) {
                            removeInvalidPreferences();
                            showForecaseList();
                        }
                        weatherForecastAdapter = new WeatherForecastAdapter(getContext(), forecasts);
                        forecastList.setAdapter(weatherForecastAdapter);
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
        this.invalidPreferencesTextView = (TextView) view.findViewById(R.id.invalid_preferences);
    }

    private void setOnItemClickListenerForForecastList() {
        this.forecastList.setOnItemClickListener(onItemClickListenerForForecastList());
    }

    private void showForecaseList() {
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
                Intent detailActivityIntent = new Intent(getContext(), DetailActivity.class);
                detailActivityIntent.putExtra(Forecast.TAG, forecast);
                startActivity(detailActivityIntent);
            }
        };
    }

    private void removeInvalidPreferences() {
        this.invalidPreferencesTextView.setVisibility(TextView.GONE);
    }

    public boolean isForecastListGone() {
        return (forecastList.getVisibility() == ListView.GONE);
    }

    public boolean isInvalidPreferencesGone() {
        return (this.invalidPreferencesTextView.getVisibility() == TextView.GONE);
    }

    private boolean canDisplayMaps(Intent intent) {
        return intent.resolveActivity(getActivity().getPackageManager()) != null;
    }
}
