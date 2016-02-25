package net.chiragaggarwal.android.sunshine.models;

import android.content.SharedPreferences;

import org.junit.Test;

import static org.junit.Assert.assertEquals;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

public class LocationTest {
    @Test
    public void shouldNotDescribeLocationAsChangedIfItIsNotChanged() {
        SharedPreferences sharedPreferences = mock(SharedPreferences.class);
        when(sharedPreferences.getBoolean(Location.HAS_CHANGED, false)).thenReturn(false);
        assertEquals(false, Location.hasChanged(sharedPreferences));
    }

    @Test
    public void shouldDescribeLocationAsChangedIfItIsChanged() {
        SharedPreferences sharedPreferences = mock(SharedPreferences.class);
        when(sharedPreferences.getBoolean(Location.HAS_CHANGED, true)).thenReturn(true);
        assertEquals(true, Location.hasChanged(sharedPreferences));
    }
}