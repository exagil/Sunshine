package net.chiragaggarwal.android.sunshine.models;

public interface Callback<T> {
    void onSuccess(T t);

    void onFailure();
}
