package net.chiragaggarwal.android.sunshine.models;

import org.junit.Before;
import org.junit.Test;

import java.util.Date;

import static org.junit.Assert.assertEquals;

public class ForecastTest {
    private long millisecondsFromJanOne1970At00HrUntilFeb212016At1600Hr;
    private long millisecondsFromJanOne1970At00HrUntilFeb222016At1600Hr;
    private long millisecondsFromJanOne1970At00HrUntilFeb202016At1600Hr;
    private long millisecondsFromJanOne1970At00HrUntilFeb232016At1600Hr;
    private Date todaysDate;
    private Date tomorrowsDate;
    private Date yesterdaysDate;
    private Date dayAfterTomorrowsDate;


    @Before
    public void beforeEach() {
        this.millisecondsFromJanOne1970At00HrUntilFeb212016At1600Hr = Long.parseLong("1456050025000");
        this.millisecondsFromJanOne1970At00HrUntilFeb222016At1600Hr = Long.parseLong("1456156800000");
        this.millisecondsFromJanOne1970At00HrUntilFeb202016At1600Hr = Long.parseLong("1455984000000");
        this.millisecondsFromJanOne1970At00HrUntilFeb232016At1600Hr = Long.parseLong("1456243200000");

        this.yesterdaysDate = new Date(millisecondsFromJanOne1970At00HrUntilFeb202016At1600Hr);
        this.todaysDate = new Date(millisecondsFromJanOne1970At00HrUntilFeb212016At1600Hr);
        this.tomorrowsDate = new Date(millisecondsFromJanOne1970At00HrUntilFeb222016At1600Hr);
        this.dayAfterTomorrowsDate = new Date(millisecondsFromJanOne1970At00HrUntilFeb232016At1600Hr);
    }

    @Test
    public void friendlyDayShouldBeTodayIfDateIsTodays() {
        Date forecastDate = new Date(millisecondsFromJanOne1970At00HrUntilFeb212016At1600Hr);
        Forecast forecast = new Forecast(forecastDate, null, null, null, null, null, null, null, null);
        assertEquals("Today", forecast.friendlyDay(todaysDate));
    }

    @Test
    public void friendlyDayShouldBeTomorrowIfDateIsTomorrows() {
        Forecast forecast = new Forecast(tomorrowsDate, null, null, null, null, null, null, null, null);
        assertEquals("Tomorrow", forecast.friendlyDay(todaysDate));
    }

    @Test
    public void friendlyDayShouldBeYesterdayIfDateIsYesterdays() {
        Forecast forecast = new Forecast(yesterdaysDate, null, null, null, null, null, null, null, null);
        assertEquals("Yesterday", forecast.friendlyDay(todaysDate));
    }

    @Test
    public void friendlyDayShouldBeNameOfDayIfDateIsNotTodaysTomorrowsOrYesterdays() {
        Forecast forecast = new Forecast(dayAfterTomorrowsDate, null, null, null, null, null, null, null, null);
        assertEquals("Tuesday", forecast.friendlyDay(todaysDate));
    }
}