package net.chiragaggarwal.android.sunshine;

import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.preference.PreferenceManager;
import android.support.annotation.NonNull;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.Menu;
import android.view.MenuInflater;

import net.chiragaggarwal.android.sunshine.models.Forecast;
import net.chiragaggarwal.android.sunshine.models.LocationPreferences;

public class MainActivity extends AppCompatActivity implements
        ForecastFragment.OnForecastSelectedListener {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        initializeAppToolbar();
        loadDefaultPreferences();

        showForecastList(savedInstanceState);
    }

    @Override
    protected void onResume() {
        super.onResume();
        if (hasLocationChanged()) {
            ForecastFragment forecastFragment = (ForecastFragment) getSupportFragmentManager().
                    findFragmentById(R.id.placeholder_forecast_list);
            forecastFragment.onLocationChanged();
        }

    }

    @Override
    public void onForecastSelected(Forecast forecast) {
        if (isTablet()) {
            showForecastDetailsOnRightSide(forecast);
        } else {
            launchForecastDetailsActivity(forecast);
        }
    }

    private void showForecastDetailsOnRightSide(Forecast forecast) {
        Bundle forecastBundle = prepareForecastBundle(forecast);
        DetailsFragment detailsFragment = buildDetailsFragment(forecastBundle);
        showForecastDetails(detailsFragment);
    }

    private void launchForecastDetailsActivity(Forecast forecast) {
        Intent forecastDetailIntent = new Intent(this, DetailActivity.class);
        forecastDetailIntent.putExtra(Forecast.TAG, forecast);
        startActivity(forecastDetailIntent);
    }

    @NonNull
    private Bundle prepareForecastBundle(Forecast forecast) {
        Bundle forecastBundle = new Bundle();
        forecastBundle.putParcelable(Forecast.TAG, forecast);
        return forecastBundle;
    }

    private void showForecastDetails(DetailsFragment detailsFragment) {
        getSupportFragmentManager().beginTransaction().
                replace(R.id.placeholder_forecast_detail, detailsFragment).
                commit();
    }

    @NonNull
    private DetailsFragment buildDetailsFragment(Bundle forecastBundle) {
        DetailsFragment detailsFragment = new DetailsFragment();
        detailsFragment.setArguments(forecastBundle);
        return detailsFragment;
    }

    private void showForecastList(Bundle savedInstanceState) {
        if (savedInstanceState == null) {
            showForecastFragment();
        }
    }

    private void showForecastFragment() {
        ForecastFragment forecastFragment = new ForecastFragment();
        getSupportFragmentManager().beginTransaction()
                .replace(R.id.placeholder_forecast_list, forecastFragment)
                .commit();
    }

    private boolean isTablet() {
        return findViewById(R.id.placeholder_forecast_detail) != null;
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        MenuInflater menuInflater = getMenuInflater();
        menuInflater.inflate(R.menu.menu_main, menu);
        return true;
    }

    private void initializeAppToolbar() {
        Toolbar toolbar = (Toolbar) findViewById(R.id.app_toolbar);
        setSupportActionBar(toolbar);
    }

    private void loadDefaultPreferences() {
        PreferenceManager.setDefaultValues(this, R.xml.pref_general, false);
    }

    private boolean hasLocationChanged() {
        SharedPreferences sharedPreferences = PreferenceManager.getDefaultSharedPreferences(this);
        return LocationPreferences.getInstance(sharedPreferences).hasChanged();
    }
}
