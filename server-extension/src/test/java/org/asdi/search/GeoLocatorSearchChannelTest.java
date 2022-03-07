package org.asdi.search;

import fi.mml.portti.service.search.SearchCriteria;
import fi.nls.oskari.util.IOHelper;
import fi.nls.oskari.util.JSONHelper;
import fi.nls.oskari.util.PropertyUtil;
import org.custommonkey.xmlunit.Diff;
import org.custommonkey.xmlunit.XMLUnit;
import org.json.JSONObject;
import org.junit.After;
import org.junit.BeforeClass;
import org.junit.Test;

import java.io.IOException;
import java.util.List;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertTrue;

public class GeoLocatorSearchChannelTest {

    private String wildcardQueryXML;

    public GeoLocatorSearchChannelTest() throws IOException {
        this.wildcardQueryXML = IOHelper.readString(getClass().getResourceAsStream("GeoLocatorWildcardQuery.xml"));
    }

    @BeforeClass
    public static void setUp() {
        // use relaxed comparison settings
        XMLUnit.setIgnoreComments(true);
        XMLUnit.setIgnoreWhitespace(true);
        XMLUnit.setIgnoreDiffBetweenTextAndCDATA(true);
        XMLUnit.setIgnoreAttributeOrder(true);
    }

    @After
    public void tearDown() {
        PropertyUtil.clearProperties();
    }

    @Test
    public void testWildcardQueryCreation() throws Exception {
        PropertyUtil.addProperty("search.channel.GEOLOCATOR_CHANNEL.service.url", "http://dummy.url");

        GeoLocatorSearchChannel channel = new GeoLocatorSearchChannel();
        channel.init();

        SearchCriteria searchCriteria = new SearchCriteria();
        searchCriteria.setSearchString("Hel*ki");

        assertTrue("Channel should detect wildcard in query", channel.hasWildcard(searchCriteria.getSearchString()));

        String xml = channel.getWildcardQuery(searchCriteria);
        Diff xmlDiff = new Diff(wildcardQueryXML, xml);
        assertTrue("Should get expected query " + xmlDiff, xmlDiff.similar());
    }

    @Test
    public void getElasticQueryLegacy() {
        GeoLocatorSearchChannel channel = new GeoLocatorSearchChannel();
        final JSONObject expected = JSONHelper.createJSONObject("{\"normal_search\":{\"text\":\"\\\"}, {\\\"break\\\": []\",\"completion\":{\"field\":\"name_suggest\",\"size\":20}},\"fuzzy_search\":{\"text\":\"\\\"}, {\\\"break\\\": []\",\"completion\":{\"field\":\"name_suggest\",\"size\":20,\"fuzzy\":{\"fuzziness\":5}}}}");
        final JSONObject actual = JSONHelper.createJSONObject(channel.getElasticQuery("\"}, {\"break\": []", true));
        assertTrue("JSON should not break", JSONHelper.isEqual(expected, actual));
    }

    @Test
    public void getElasticQueryCurrent() {
        GeoLocatorSearchChannel channel = new GeoLocatorSearchChannel();
        final JSONObject expected = JSONHelper.createJSONObject("{\"suggest\":{\"placenamesuggest\":{\"completion\":{\"field\":\"name_suggest\",\"skip_duplicates\":true},\"prefix\":\"\\\"}, {\\\"break\\\": []\"}}}");
        final JSONObject actual = JSONHelper.createJSONObject(channel.getElasticQuery("\"}, {\"break\": []", false));
        assertTrue("JSON should not break", JSONHelper.isEqual(expected, actual));
    }

    @Test
    public void parseElasticResponseCurrent() throws IOException {
        String response = IOHelper.readString(getClass().getResourceAsStream("geolocator-autocomplete-expected.json"));

        GeoLocatorSearchChannel channel = new GeoLocatorSearchChannel();
        final JSONObject actual = JSONHelper.createJSONObject(channel.getElasticQuery("\"}, {\"break\": []", false));
        List<String> result = channel.parseAutocompleteResults(response, "hels", false);
        assertEquals("Should get 5 results", 5, result.size());
        assertEquals("Helsberg should be the second last", "Helsberg", result.get(result.size() - 2));
    }

}