package org.asdi.search;

import fi.nls.oskari.util.IOHelper;
import fi.nls.oskari.util.PropertyUtil;
import org.junit.After;
import org.junit.Test;

import java.util.Locale;
import java.util.Map;
import java.util.Set;

import static org.junit.Assert.assertEquals;

/**
 * Created by SMAKINEN on 7.9.2017.
 */
public class GeoLocatorCountriesTest {

    @After
    public void tearDown() {
        PropertyUtil.clearProperties();
    }

    @Test
    public void testCountrySearch() throws Exception {
        PropertyUtil.addProperty("search.channel.GEOLOCATOR_CHANNEL.service.url", "http://dummy.url");
        GeoLocatorCountries countries = GeoLocatorCountries.getInstance();
        String response = IOHelper.readString(getClass().getResourceAsStream("geolocator-country-filter-response.xml"));
        Map<String, String> map = countries.parseCountryMap(response);

        assertEquals("Should get 9 values", 9 , map.size());
        countries.setCountryMap(map);

        Set<String> countrySet = countries.getCountries();
        assertEquals("Should get 8 values", 8 , countrySet.size());

        String countryName = countries.getAdminCountry(new Locale("en"), "Norway polar - GN");
        assertEquals("Countryname should match expected", "Norway", countryName);

    }
}