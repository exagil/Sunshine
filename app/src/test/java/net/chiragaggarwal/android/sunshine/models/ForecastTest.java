package net.chiragaggarwal.android.sunshine.models;

import org.junit.Test;

import java.util.Date;

import static junit.framework.Assert.assertEquals;

public class ForecastTest {
    @Test
    public void shouldCalculatePersistableDate() {
        Date date = new Date(new Long("1421042400000"));
        Forecast forecast = new Forecast(date, null, null, null, null, null, null, null, null);
        assertEquals(12012015, forecast.persistableDate());
    }
}