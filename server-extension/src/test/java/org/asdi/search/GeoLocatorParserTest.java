package org.asdi.search;

import fi.mml.portti.service.search.ChannelSearchResult;
import fi.nls.oskari.util.IOHelper;
import org.junit.Test;

import java.io.IOException;
import java.util.Locale;

import static org.junit.Assert.assertEquals;

/**
 * Created by SMAKINEN on 17.5.2017.
 */
public class GeoLocatorParserTest {

    private String resultXML;

    public GeoLocatorParserTest() throws IOException {
        resultXML = IOHelper.readString(getClass().getResourceAsStream("geolocator-wildcard-result.xml"));

    }

    @Test
    public void testParse() throws Exception {

        GeoLocatorParser parser = new GeoLocatorParser(new GeoLocatorSearchChannel());
        ChannelSearchResult result = parser.parse(resultXML, "EPSG:4258", Locale.ENGLISH, true);
        assertEquals("Should parse 10 result items", 10, result.getNumberOfResults());
    }
}