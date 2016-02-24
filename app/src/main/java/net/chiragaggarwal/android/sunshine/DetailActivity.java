package net.chiragaggarwal.android.sunshine;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.v4.view.MenuItemCompat;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.ImageView;
import android.widget.TextView;

import net.chiragaggarwal.android.sunshine.models.Forecast;

public class DetailActivity extends AppCompatActivity {
    private Forecast forecast;
    private TextView textForecastDescription;
    private ImageView imageForecast;
    private TextView textForecastPressure;
    private TextView textForecastWind;
    private TextView textForecastDate;
    private TextView textForecastHumidity;
    private TextView textForecastMaxTemperature;
    private TextView textForecastMinTemperature;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_detail);
        initializeAppToolbar();
        initializeViews();

        this.forecast = getIntent().getParcelableExtra(Forecast.TAG);
        populateViews(forecast, getApplicationContext());
    }

    private void populateViews(Forecast forecast, Context context) {
        String mainDescription = forecast.mainDescription;
        String pressureInHectopascal = forecast.pressureInHectopascal();
        String northWindSpeedInKmph = forecast.northWindSpeedInKmph();
        String humidityInPercentage = forecast.humidityInPercentage();
        String formattedMinimumTemperature = forecast.formattedMinimumTemperature(context);
        String formattedMaximumTemperature = forecast.formattedMaximumTemperature(context);
        String formattedDate = forecast.formattedDate();

        this.textForecastDescription.setText(mainDescription);
        this.imageForecast.setImageResource(forecast.iconArt());
        this.textForecastPressure.setText(pressureInHectopascal);
        this.textForecastWind.setText(northWindSpeedInKmph);
        this.textForecastHumidity.setText(humidityInPercentage);
        this.textForecastMinTemperature.setText(formattedMinimumTemperature);
        this.textForecastMaxTemperature.setText(formattedMaximumTemperature);
        this.textForecastDate.setText(formattedDate);
    }

    private void initializeViews() {
        this.textForecastDescription = (TextView) findViewById(R.id.text_forecast_description);
        this.imageForecast = (ImageView) findViewById(R.id.image_forecast);
        this.textForecastPressure = (TextView) findViewById(R.id.text_forecast_pressure);
        this.textForecastWind = (TextView) findViewById(R.id.text_forecast_wind);
        this.textForecastHumidity = (TextView) findViewById(R.id.text_forecast_humidity);
        this.textForecastMinTemperature = (TextView) findViewById(R.id.text_forecast_min_temperature);
        this.textForecastMaxTemperature = (TextView) findViewById(R.id.text_forecast_max_temperature);
        this.textForecastDate = (TextView) findViewById(R.id.text_forecast_date);
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        menu.clear();
        getMenuInflater().inflate(R.menu.menu_detail, menu);
        initializeShareActionProvider(menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case android.R.id.home:
                finish();
                break;
            case R.id.detail_action_settings:
                launchSettings();
                break;
        }
        return super.onOptionsItemSelected(item);
    }

    private void initializeAppToolbar() {
        Toolbar toolbar = (Toolbar) findViewById(R.id.app_toolbar);
        setSupportActionBar(toolbar);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
    }

    private void initializeShareActionProvider(Menu menu) {
        MenuItem shareMenuItem = menu.findItem(R.id.menu_item_share);
        android.support.v7.widget.ShareActionProvider shareActionProvider =
                (android.support.v7.widget.ShareActionProvider) MenuItemCompat.
                        getActionProvider(shareMenuItem);
        if (shareActionProvider != null) shareActionProvider.setShareIntent(createShareIntent());
    }

    private Intent createShareIntent() {
        Intent shareIntent = new Intent(Intent.ACTION_SEND);
        shareIntent.setType(getString(R.string.text_plain));
        shareIntent.setFlags(Intent.FLAG_ACTIVITY_NEW_DOCUMENT);
        shareIntent.putExtra(Intent.EXTRA_TEXT, this.forecast.summaryWithHashtag(this));
        return shareIntent;
    }

    private void launchSettings() {
        Intent intent = new Intent(this, SettingsActivity.class);
        startActivity(intent);
    }
}
