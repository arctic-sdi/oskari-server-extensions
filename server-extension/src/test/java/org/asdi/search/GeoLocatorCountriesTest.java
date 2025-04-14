package org.asdi.search;

import fi.nls.oskari.util.IOHelper;
import fi.nls.oskari.util.PropertyUtil;
import org.junit.jupiter.api.AfterAll;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.Test;

import java.util.Locale;
import java.util.Map;
import java.util.Set;

import static org.junit.jupiter.api.Assertions.assertEquals;


/**
 * Created by SMAKINEN on 7.9.2017.
 */
public class GeoLocatorCountriesTest {

    @AfterEach
    public void tearDown() {
        PropertyUtil.clearProperties();
    }

    @Test
    public void testCountrySearch() throws Exception {
        PropertyUtil.addProperty("search.channel.GEOLOCATOR_CHANNEL.service.url", "http://dummy.url");
        GeoLocatorCountries countries = GeoLocatorCountries.getInstance();
        String response = IOHelper.readString(getClass().getResourceAsStream("geolocator-country-filter-response.xml"));
        Map<String, String> map = countries.parseCountryMap(response);

        assertEquals(9 , map.size(), "Should get 9 values");
        countries.setCountryMap(map);

        Set<String> countrySet = countries.getCountries();
        assertEquals(8 , countrySet.size(), "Should get 8 values");

        String countryName = countries.getAdminCountry(new Locale("en"), "Norway polar - GN");
        assertEquals("Norway", countryName, "Countryname should match expected");

    }
}