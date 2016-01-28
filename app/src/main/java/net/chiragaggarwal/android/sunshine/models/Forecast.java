package net.chiragaggarwal.android.sunshine.models;

import android.os.Parcel;
import android.os.Parcelable;

public class Forecast implements Parcelable {
    public String entry;

    public Forecast(String entry) {
        this.entry = entry;
    }

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeString(this.entry);
    }

    protected Forecast(Parcel in) {
        this.entry = in.readString();
    }

    public static final Creator<Forecast> CREATOR = new Creator<Forecast>() {
        public Forecast createFromParcel(Parcel source) {
            return new Forecast(source);
        }

        public Forecast[] newArray(int size) {
            return new Forecast[size];
        }
    };
}
