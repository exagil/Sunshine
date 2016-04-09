package net.chiragaggarwal.android.sunshine.network.sync;

// WeatherForecastSyncService makes use of WeatherForecastsSyncAdapter
// to service fetched data to app components

import android.app.Service;
import android.content.Intent;
import android.os.IBinder;
import android.support.annotation.Nullable;

public class WeatherForecastSyncService extends Service {
    private final Object weatherForecastSyncAdapterLock = new Object();
    private WeatherForecastsSyncAdapter weatherForecastsSyncAdapter;

    @Override
    public void onCreate() {
        super.onCreate();
        synchronized (weatherForecastSyncAdapterLock) {
            this.weatherForecastsSyncAdapter = new WeatherForecastsSyncAdapter(this, true);
        }
    }

    @Nullable
    @Override
    public IBinder onBind(Intent intent) {
        return weatherForecastsSyncAdapter.getSyncAdapterBinder();
    }
}
