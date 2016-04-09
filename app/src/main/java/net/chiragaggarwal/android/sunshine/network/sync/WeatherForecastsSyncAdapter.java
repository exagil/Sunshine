package net.chiragaggarwal.android.sunshine.network.sync;

import android.accounts.Account;
import android.annotation.TargetApi;
import android.content.AbstractThreadedSyncAdapter;
import android.content.ContentProviderClient;
import android.content.Context;
import android.content.SyncResult;
import android.os.Build;
import android.os.Bundle;

// WeatherForecastsSyncAdapter fetches the weather forecasts
// as required by the app components

public class WeatherForecastsSyncAdapter extends AbstractThreadedSyncAdapter {
    public WeatherForecastsSyncAdapter(Context context, boolean autoInitialize) {
        super(context, autoInitialize);
    }

    @TargetApi(Build.VERSION_CODES.HONEYCOMB)
    public WeatherForecastsSyncAdapter(Context context, boolean autoInitialize, boolean allowParallelSyncs) {
        super(context, autoInitialize, allowParallelSyncs);
    }

    @Override
    public void onPerformSync(Account account, Bundle extras, String authority, ContentProviderClient provider, SyncResult syncResult) {

    }
}
