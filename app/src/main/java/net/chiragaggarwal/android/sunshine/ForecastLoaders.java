package net.chiragaggarwal.android.sunshine;

import android.support.v4.app.LoaderManager;

import java.util.ArrayList;

public class ForecastLoaders {
    private static ForecastLoaders instance;
    private ArrayList<Integer> loaderIds;

    public static ForecastLoaders getInstance() {
        if (instance == null) instance = new ForecastLoaders();
        return instance;
    }

    public void stopAllExcept(int loaderIdToNotStop, LoaderManager loaderManager) {
        for (int loaderIdsIndex = 0; loaderIdsIndex < loaderIds.size(); loaderIdsIndex++) {
            Integer loaderId = loaderIds.get(loaderIdsIndex);
            if (loaderId == loaderIdToNotStop) continue;
            loaderManager.getLoader(loaderId).stopLoading();
        }
    }

    public ForecastLoaders() {
        loaderIds = new ArrayList<>();
    }

    public void addLoaderId(int id) {
        if (this.loaderIds.contains(id)) return;
        this.loaderIds.add(id);
    }
}
