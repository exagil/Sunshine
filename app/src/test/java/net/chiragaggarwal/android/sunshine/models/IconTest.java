package net.chiragaggarwal.android.sunshine.models;

import org.junit.Test;

import static junit.framework.TestCase.assertEquals;

public class IconTest {
    @Test
    public void clearDaySkyCodeForAForecastShouldRepresentClearSkyIcon() {
        assertEquals(Icon.CLEAR_SKY, Icon.parse("01d"));
    }

    @Test
    public void clearNightSkyCodeForAForecastShouldRepresentClearSkyIcon() {
        assertEquals(Icon.CLEAR_SKY, Icon.parse("01n"));
    }

    @Test
    public void fewCloudsDayIconCodeShouldRepresentLightCloudsIcon() {
        assertEquals(Icon.LIGHT_CLOUDS, Icon.parse("02d"));
    }

    @Test
    public void fewCloudsNightIconCodeShouldRepresentLightCloudsIcon() {
        assertEquals(Icon.LIGHT_CLOUDS, Icon.parse("02n"));
    }
}