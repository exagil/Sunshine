package net.chiragaggarwal.android.sunshine.models;

import net.chiragaggarwal.android.sunshine.R;

import org.junit.Before;
import org.junit.Test;

import java.util.Date;

import static org.junit.Assert.assertEquals;

public class ForecastTest {
    private long millisecondsFromJanOne1970At00HrUntilFeb212016At1600Hr;
    private long millisecondsFromJanOne1970At00HrUntilFeb222016At1600Hr;
    private long millisecondsFromJanOne1970At00HrUntilFeb232016At1600Hr;
    private Date todaysDate;
    private Date tomorrowsDate;
    private Date dayAfterTomorrowsDate;


    @Before
    public void beforeEach() {
        this.millisecondsFromJanOne1970At00HrUntilFeb212016At1600Hr = Long.parseLong("1456050025000");
        this.millisecondsFromJanOne1970At00HrUntilFeb222016At1600Hr = Long.parseLong("1456156800000");
        this.millisecondsFromJanOne1970At00HrUntilFeb232016At1600Hr = Long.parseLong("1456243200000");

        this.todaysDate = new Date(millisecondsFromJanOne1970At00HrUntilFeb212016At1600Hr);
        this.tomorrowsDate = new Date(millisecondsFromJanOne1970At00HrUntilFeb222016At1600Hr);
        this.dayAfterTomorrowsDate = new Date(millisecondsFromJanOne1970At00HrUntilFeb232016At1600Hr);
    }

    @Test
    public void friendlyDayShouldBeTodayIfDateIsTodays() {
        Date forecastDate = new Date(millisecondsFromJanOne1970At00HrUntilFeb212016At1600Hr);
        Forecast forecast = new Forecast(forecastDate, null, null, null, null, null, null, null, null, null);
        assertEquals("Today", forecast.friendlyDay(todaysDate));
    }

    @Test
    public void friendlyDayShouldBeTomorrowIfDateIsTomorrows() {
        Forecast forecast = new Forecast(tomorrowsDate, null, null, null, null, null, null, null, null, null);
        assertEquals("Tomorrow", forecast.friendlyDay(todaysDate));
    }

    @Test
    public void friendlyDayShouldBeNameOfDayIfDateIsNotTodaysTomorrowsOrYesterdays() {
        Forecast forecast = new Forecast(dayAfterTomorrowsDate, null, null, null, null, null, null, null, null, null);
        assertEquals("Tuesday", forecast.friendlyDay(todaysDate));
    }

    @Test
    public void shouldHaveIconResourceAsClearSkyIconIfHasIconIdThatOfClearSky() {
        String clearSkyIcon = "01d";
        Forecast forecast = new Forecast(null, null, null, null, null, null, null, null, null, clearSkyIcon);
        assertEquals("Forecast should have `Clear Sky Icon` but was something else",
                R.drawable.ic_clear, forecast.iconResource());
    }

    @Test
    public void shouldHaveIconResourceAsLightCloudsIconIfHasIconIdThatOfFewClouds() {
        String fewCloudsIcon = "02d";
        Forecast forecast = new Forecast(null, null, null, null, null, null, null, null, null, fewCloudsIcon);
        assertEquals("Forecast should have `Light Clouds Icon` but was something else",
                R.drawable.ic_light_clouds, forecast.iconResource());
    }

    @Test
    public void shouldHaveNoIconResourceIfHasInexistantIconId() {
        String inexistantIcon = "some_inexistant_icon_resource";
        Forecast forecast = new Forecast(null, null, null, null, null, null, null, null, null, inexistantIcon);
        assertEquals("Forecast should have `no Icon` but was something else", 0, forecast.iconResource());
    }

    @Test
    public void shouldHaveIconResourceAsCloudsIconIfHasIconIdThatOfScatteredClouds() {
        String scatteredCloudsIcon = "03d";
        Forecast forecast = new Forecast(null, null, null, null, null, null, null, null, null, scatteredCloudsIcon);
        assertEquals("Forecast should have `Clouds Icon` but was something else",
                R.drawable.ic_cloudy, forecast.iconResource());
    }

    @Test
    public void shouldHaveIconResourceAsCloudsIconIfHasIconIdThatOfBrokenClouds() {
        String brokenCloudsIcon = "04d";
        Forecast forecast = new Forecast(null, null, null, null, null, null, null, null, null, brokenCloudsIcon);
        assertEquals("Forecast should have `Clouds Icon` but was something else",
                R.drawable.ic_cloudy, forecast.iconResource());
    }

    @Test
    public void shouldHaveIconResourceAsRainIconIfHasIconIdThatOfShowerRain() {
        String showerRainIcon = "09d";
        Forecast forecast = new Forecast(null, null, null, null, null, null, null, null, null, showerRainIcon);
        assertEquals("Forecast should have `Rain Icon` but was something else",
                R.drawable.ic_rain, forecast.iconResource());
    }

    @Test
    public void shouldHaveIconResourceAsRainIconIfHasIconIdThatOfRain() {
        String rainIcon = "10d";
        Forecast forecast = new Forecast(null, null, null, null, null, null, null, null, null, rainIcon);
        assertEquals("Forecast should have `Light Rain Icon` but was something else",
                R.drawable.ic_light_rain, forecast.iconResource());
    }

    @Test
    public void shouldHaveIconResourceAsRainIconIfHasIconIdThatOfThunderstorm() {
        String thunderstormIcon = "11d";
        Forecast forecast = new Forecast(null, null, null, null, null, null, null, null, null, thunderstormIcon);
        assertEquals("Forecast should have `Thunderstorm Icon` but was something else",
                R.drawable.ic_storm, forecast.iconResource());
    }

    @Test
    public void shouldHaveIconResourceAsSnowIconIfHasIconIdThatOfSnow() {
        String snowIcon = "13d";
        Forecast forecast = new Forecast(null, null, null, null, null, null, null, null, null, snowIcon);
        assertEquals("Forecast should have `Snow Icon` but was something else",
                R.drawable.ic_snow, forecast.iconResource());
    }

    @Test
    public void shouldHaveIconResourceAsFogIconIfHasIconIdThatOfFog() {
        String fogIcon = "50d";
        Forecast forecast = new Forecast(null, null, null, null, null, null, null, null, null, fogIcon);
        assertEquals("Forecast should have `Fog Icon` but was something else",
                R.drawable.ic_fog, forecast.iconResource());
    }

    @Test
    public void shouldHaveIconArtAsClearSkyIfHasIconIdThatOfClearSky() {
        String clearSkyIcon = "01d";
        Forecast forecast = new Forecast(null, null, null, null, null, null, null, null, null, clearSkyIcon);
        assertEquals("Forecast should have `Clear Sky Icon` but was something else",
                R.drawable.art_clear, forecast.iconArt());
    }

    @Test
    public void shouldHaveIconArtAsLightCloudsIfHasIconIdThatOfFewClouds() {
        String fewCloudsIcon = "02d";
        Forecast forecast = new Forecast(null, null, null, null, null, null, null, null, null, fewCloudsIcon);
        assertEquals("Forecast should have `Light Clouds Art` but was something else",
                R.drawable.art_light_clouds, forecast.iconArt());
    }

    @Test
    public void shouldHaveNoArtIfHasInexistantIconId() {
        String inexistantIcon = "some_inexistant_icon_resource";
        Forecast forecast = new Forecast(null, null, null, null, null, null, null, null, null, inexistantIcon);
        assertEquals("Forecast should have `no Art` but was something else", 0, forecast.iconArt());
    }

    @Test
    public void shouldHaveIconArtAsCloudsIfHasIconIdThatOfScatteredClouds() {
        String scatteredCloudsIcon = "03d";
        Forecast forecast = new Forecast(null, null, null, null, null, null, null, null, null, scatteredCloudsIcon);
        assertEquals("Forecast should have `Clouds Art` but was something else",
                R.drawable.art_clouds, forecast.iconArt());
    }

    @Test
    public void shouldHaveIconArtAsCloudsIfHasIconIdThatOfBrokenClouds() {
        String brokenCloudsIcon = "04d";
        Forecast forecast = new Forecast(null, null, null, null, null, null, null, null, null, brokenCloudsIcon);
        assertEquals("Forecast should have `Clouds Art` but was something else",
                R.drawable.art_clouds, forecast.iconArt());
    }

    @Test
    public void shouldHaveIconArtAsRainIfHasIconIdThatOfShowerRain() {
        String showerRainIcon = "09d";
        Forecast forecast = new Forecast(null, null, null, null, null, null, null, null, null, showerRainIcon);
        assertEquals("Forecast should have `Rain Art` but was something else",
                R.drawable.art_rain, forecast.iconArt());
    }

    @Test
    public void shouldHaveIconArtAsRainIfHasIconIdThatOfRain() {
        String rainIcon = "10d";
        Forecast forecast = new Forecast(null, null, null, null, null, null, null, null, null, rainIcon);
        assertEquals("Forecast should have `Light Rain Art` but was something else",
                R.drawable.art_light_rain, forecast.iconArt());
    }

    @Test
    public void shouldHaveIconArtAsRainIfHasIconIdThatOfThunderstorm() {
        String thunderstormIcon = "11d";
        Forecast forecast = new Forecast(null, null, null, null, null, null, null, null, null, thunderstormIcon);
        assertEquals("Forecast should have `Thunderstorm Art` but was something else",
                R.drawable.art_storm, forecast.iconArt());
    }

    @Test
    public void shouldHaveIconArtAsSnowIfHasIconIdThatOfSnow() {
        String snowIcon = "13d";
        Forecast forecast = new Forecast(null, null, null, null, null, null, null, null, null, snowIcon);
        assertEquals("Forecast should have `Snow Art` but was something else",
                R.drawable.art_snow, forecast.iconArt());
    }

    @Test
    public void shouldHaveIconArtAsFogIfHasIconIdThatOfFog() {
        String fogIcon = "50d";
        Forecast forecast = new Forecast(null, null, null, null, null, null, null, null, null, fogIcon);
        assertEquals("Forecast should have `Fog Art` but was something else",
                R.drawable.art_fog, forecast.iconArt());
    }
}