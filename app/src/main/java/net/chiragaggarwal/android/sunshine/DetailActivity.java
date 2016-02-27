package net.chiragaggarwal.android.sunshine;

import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.v4.view.MenuItemCompat;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.Menu;
import android.view.MenuItem;

import net.chiragaggarwal.android.sunshine.models.Forecast;

public class DetailActivity extends AppCompatActivity {
    private Forecast forecast;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_detail);
        initializeAppToolbar();

        this.forecast = getIntent().getParcelableExtra(Forecast.TAG);

        showForecastFragment(this.forecast);
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
        int whiteColor = getResources().getColor(R.color.white);
        toolbar.setTitleTextColor(whiteColor);
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

    private void showForecastFragment(Forecast forecast) {
        Bundle forecastDetailsBundle = prepareForecastDetailsBundle(forecast);
        DetailsFragment forecastDetailsFragment = buildForecastDetailsFragment(forecastDetailsBundle);
        replaceForecastPlaceholderWithDetailsFragment(forecastDetailsFragment);
    }

    @NonNull
    private Bundle prepareForecastDetailsBundle(Forecast forecast) {
        Bundle forecastDetailsBundle = new Bundle();
        forecastDetailsBundle.putParcelable(Forecast.TAG, forecast);
        return forecastDetailsBundle;
    }

    @NonNull
    private DetailsFragment buildForecastDetailsFragment(Bundle forecastDetailsBundle) {
        DetailsFragment forecastDetailsFragment = new DetailsFragment();
        forecastDetailsFragment.setArguments(forecastDetailsBundle);
        return forecastDetailsFragment;
    }

    private void replaceForecastPlaceholderWithDetailsFragment(DetailsFragment forecastDetailsFragment) {
        getSupportFragmentManager().beginTransaction().
                replace(R.id.placeholder_forecast, forecastDetailsFragment).
                commit();
    }
}
