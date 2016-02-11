package net.chiragaggarwal.android.sunshine.data;

import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.support.annotation.NonNull;

import static net.chiragaggarwal.android.sunshine.data.ForecastContract.ForecastEntry;

public class ForecastsRepository extends SQLiteOpenHelper {

    private static DatabaseHelper databaseHelper;
    private static ForecastsRepository forecastsRepository;

    public static ForecastsRepository getInstance(Context context) {
        databaseHelper = DatabaseHelper.getInstance(context);
        if (forecastsRepository == null)
            forecastsRepository = new ForecastsRepository(context,
                    databaseHelper.DATABASE_NAME,
                    null,
                    databaseHelper.DATABASE_VERSION);
        return forecastsRepository;
    }

    @Override
    public void onCreate(SQLiteDatabase db) {
        this.databaseHelper.onCreate(db);
    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        this.databaseHelper.onUpgrade(db, oldVersion, newVersion);
    }

    public Cursor fetchAll() {
        return getWritableDatabase().
                rawQuery(buildFetchForecastsQuery(),
                        new String[]{ForecastEntry.TABLE_NAME});
    }

    private ForecastsRepository(Context context, String name, SQLiteDatabase.CursorFactory factory, int version) {
        super(context, name, factory, version);
    }

    @NonNull
    private String buildFetchForecastsQuery() {
        return "SELECT * FROM ?";
    }
}
