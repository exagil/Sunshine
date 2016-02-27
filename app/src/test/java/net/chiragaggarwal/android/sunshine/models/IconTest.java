package net.chiragaggarwal.android.sunshine.models;

import net.chiragaggarwal.android.sunshine.R;

import org.junit.Test;

import static junit.framework.TestCase.assertEquals;

public class IconTest {
    @Test
    public void clearDaySkyCodeForAForecastShouldRepresentClearSkyIcon() {
        assertEquals(Icon.CLEAR_SKY, Icon.parse("01d"));
    }

    @Test
    public void clearDaySkyIconShouldHave01dCode() {
        assertEquals("01d", Icon.CLEAR_SKY.code());
    }

    @Test
    public void clearSkyIconShouldHaveClearSkyIconResource() {
        assertEquals(R.drawable.ic_clear, Icon.CLEAR_SKY.resource());
    }

    @Test
    public void clearNightSkyCodeForAForecastShouldRepresentClearSkyIcon() {
        assertEquals(Icon.CLEAR_SKY_NIGHT, Icon.parse("01n"));
    }

    @Test
    public void clearSkyNightIconShouldHaveClearSkyIconResource() {
        assertEquals(R.drawable.ic_clear, Icon.CLEAR_SKY_NIGHT.resource());
    }

    @Test
    public void clearSkyNightIconShouldHave01nCode() {
        assertEquals("01n", Icon.CLEAR_SKY_NIGHT.code());
    }

    @Test
    public void fewCloudsDayIconCodeShouldRepresentLightCloudsIcon() {
        assertEquals(Icon.LIGHT_CLOUDS, Icon.parse("02d"));
    }

    @Test
    public void lightCloudsIconShouldHave02dCode() {
        assertEquals("02d", Icon.LIGHT_CLOUDS.code());
    }

    @Test
    public void lightCloudsIconShouldHaveLightCloudsIconResource() {
        assertEquals(R.drawable.ic_light_clouds, Icon.LIGHT_CLOUDS.resource());
    }

    @Test
    public void fewCloudsNightIconCodeShouldRepresentLightCloudsIcon() {
        assertEquals(Icon.LIGHT_CLOUDS_NIGHT, Icon.parse("02n"));
    }

    @Test
    public void lightCloudsNightIconShouldHave02nCode() {
        assertEquals("02n", Icon.LIGHT_CLOUDS_NIGHT.code());
    }

    @Test
    public void lightCloudsNightIconShouldHaveLightCloudsIconResource() {
        assertEquals(R.drawable.ic_light_clouds, Icon.LIGHT_CLOUDS_NIGHT.resource());
    }

    @Test
    public void scatteredCloudsDayIconCodeShouldRepresentScatteredCloudsIcon() {
        assertEquals(Icon.SCATTERED_CLOUDS, Icon.parse("03d"));
    }

    @Test
    public void scatteredCloudsDayIconShouldHave03dCode() {
        assertEquals("03d", Icon.SCATTERED_CLOUDS.code());
    }

    @Test
    public void scatteredCloudsIconShouldHaveCloudyIconResource() {
        assertEquals(R.drawable.ic_cloudy, Icon.SCATTERED_CLOUDS.resource());
    }

    @Test
    public void scatteredCloudsNightIconCodeShouldRepresentScatteredCloudsNightIcon() {
        assertEquals(Icon.SCATTERED_CLOUDS_NIGHT, Icon.parse("03n"));
    }

    @Test
    public void scatteredCloudsNightIconShouldHave03nCode() {
        assertEquals("03n", Icon.SCATTERED_CLOUDS_NIGHT.code());
    }

    @Test
    public void scatteredCloudsNightIconShouldHaveCloudyIconResource() {
        assertEquals(R.drawable.ic_cloudy, Icon.SCATTERED_CLOUDS_NIGHT.resource());
    }

    @Test
    public void brokenCloudsDayIconCodeShouldRepresentBrokenCloudsIcon() {
        assertEquals(Icon.BROKEN_CLOUDS, Icon.parse("04d"));
    }

    @Test
    public void brokenCloudsIconShouldHave04dCode() {
        assertEquals("04d", Icon.BROKEN_CLOUDS.code());
    }

    @Test
    public void brokenCloudsIconShouldHaveCloudyIconResource() {
        assertEquals(R.drawable.ic_cloudy, Icon.BROKEN_CLOUDS.resource());
    }

    @Test
    public void brokenCloudsNightIconCodeShouldRepresentBrokenCloudsIcon() {
        assertEquals(Icon.BROKEN_CLOUDS_NIGHT, Icon.parse("04n"));
    }

    @Test
    public void brokenCloudsNightIconShouldHave04nCode() {
        assertEquals("04n", Icon.BROKEN_CLOUDS_NIGHT.code());
    }

    @Test
    public void brokenCloudsNightIconShouldHaveCloudyIconResource() {
        assertEquals(R.drawable.ic_cloudy, Icon.BROKEN_CLOUDS_NIGHT.resource());
    }

    @Test
    public void showerRainDayIconCodeShouldRepresentRainIcon() {
        assertEquals(Icon.RAIN, Icon.parse("09d"));
    }

    @Test
    public void showerRainDayIconShouldHave09dCode() {
        assertEquals("09d", Icon.RAIN.code());
    }

    @Test
    public void rainIconShouldHaveRainIconResource() {
        assertEquals(R.drawable.ic_rain, Icon.RAIN.resource());
    }

    @Test
    public void showerRainNightIconCodeShouldRepresentRainIcon() {
        assertEquals(Icon.RAIN_NIGHT, Icon.parse("09n"));
    }

    @Test
    public void showerRainNightIconShouldHave09nCode() {
        assertEquals("09n", Icon.RAIN_NIGHT.code());
    }

    @Test
    public void rainNightIconShouldHaveRainIconResource() {
        assertEquals(R.drawable.ic_rain, Icon.RAIN_NIGHT.resource());
    }

    @Test
    public void rainDayIconCodeShouldRepresentLightRainIcon() {
        assertEquals(Icon.LIGHT_RAIN, Icon.parse("10d"));
    }

    @Test
    public void lightRainIconShouldHave10dCode() {
        assertEquals("10d", Icon.LIGHT_RAIN.code());
    }

    @Test
    public void rainNightIconCodeShouldRepresentLightRainIcon() {
        assertEquals(Icon.LIGHT_RAIN_NIGHT, Icon.parse("10n"));
    }

    @Test
    public void lightRainNightIconShouldHave10nCode() {
        assertEquals("10n", Icon.LIGHT_RAIN_NIGHT.code());
    }

    @Test
    public void thunderstormDayIconCodeShouldRepresentStormIcon() {
        assertEquals(Icon.STORM, Icon.parse("11d"));
    }

    @Test
    public void stormIconShouldHave11dCode() {
        assertEquals("11d", Icon.STORM.code());
    }

    @Test
    public void thunderstormNightIconCodeShouldRepresentStormIcon() {
        assertEquals(Icon.STORM_NIGHT, Icon.parse("11n"));
    }

    @Test
    public void stormNightIconShouldHave11nCode() {
        assertEquals("11n", Icon.STORM_NIGHT.code());
    }

    @Test
    public void snowDayIconCodeShouldRepresentSnowIcon() {
        assertEquals(Icon.SNOW, Icon.parse("13d"));
    }

    @Test
    public void snowIconShouldHave13dCode() {
        assertEquals("13d", Icon.SNOW.code());
    }

    @Test
    public void snowNightIconCodeShouldRepresentSnowIcon() {
        assertEquals(Icon.SNOW_NIGHT, Icon.parse("13n"));
    }

    @Test
    public void snowNightIconShouldHave13dCode() {
        assertEquals("13n", Icon.SNOW_NIGHT.code());
    }

    @Test
    public void mistDayIconCodeShouldRepresentFogIcon() {
        assertEquals(Icon.FOG, Icon.parse("50d"));
    }

    @Test
    public void fogIconShouldHave50dCode() {
        assertEquals("50d", Icon.FOG.code());
    }

    @Test
    public void mistNightIconCodeShouldRepresentFogIcon() {
        assertEquals(Icon.FOG_NIGHT, Icon.parse("50n"));
    }

    @Test
    public void fogNightIconShouldHave50nCode() {
        assertEquals("50n", Icon.FOG_NIGHT.code());
    }
}
