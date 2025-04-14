package org.asdi.search;

/**
 * Created with IntelliJ IDEA.
 * User: Oskari team
 * Date: 7.5.2014
 * Time: 10:09
 */

import fi.nls.oskari.service.ServiceRuntimeException;
import org.geotools.api.filter.Filter;
import org.geotools.api.filter.FilterFactory;
import org.geotools.api.filter.PropertyIsEqualTo;
import org.geotools.factory.CommonFactoryFinder;
import org.geotools.xsd.Configuration;
import org.geotools.xsd.Encoder;
import org.xml.sax.helpers.NamespaceSupport;

import java.util.ArrayList;
import java.util.List;

public class GeoLocatorQueryHelper {
    private GeoLocatorCountries countriesHelper = GeoLocatorCountries.getInstance();
    private NamespaceSupport namespaceSupport = new NamespaceSupport();

    public GeoLocatorQueryHelper() {
        namespaceSupport.declarePrefix("iso19112", "http://www.isotc211.org/19112" );
    }

    /**
     * Creates a filter based on user input and possible country filter
     * @param userinput
     * @param country
     * @return
     */
    public String getFilter(String userinput, String country) {
        FilterFactory ff = CommonFactoryFinder.getFilterFactory();
        Filter userInputFilter = getUserInputFilter(userinput, ff);
        Filter adminFilter = getAdminFilter(country, ff);

        try {
            Configuration cfg = new org.geotools.filter.v2_0.FESConfiguration();
            Encoder encoder = new Encoder(cfg);
            encoder.setOmitXMLDeclaration(true);

            String result = encoder.encodeAsString(
                    createOptionalAndFilter(ff, adminFilter, userInputFilter),
                    org.geotools.filter.v2_0.FES.Filter);
            // iso19112and gmdsf1 are referenced in filter and geolocator returns empty result if it's not mentioned as namespace...
            // also matchAction causes problems on the server...
            // TODO: more elegant solution...
            String toReplace = "xmlns:fes=\"http://www.opengis.net/fes/2.0\"";
            String replaceWith = "xmlns:fes=\"http://www.opengis.net/fes/2.0\" xmlns:iso19112=\"http://www.isotc211.org/19112\" xmlns:gmdsf1=\"http://www.isotc211.org/2005/gmdsf1\" ";
            return result
                    .replace(toReplace, replaceWith)
                    .replace("matchAction=\"ANY\"", "");
        } catch (Exception e) {
            throw new ServiceRuntimeException("Error encoding filter for country " + country, e);
        }
    }

    private Filter getUserInputFilter(String input, FilterFactory ff) {
        if(input == null || input.isEmpty()) {
            return null;
        }
        PropertyIsEqualTo moi = ff.equal(
                ff.property("iso19112:alternativeGeographicIdentifiers/iso19112:alternativeGeographicIdentifier/iso19112:name", namespaceSupport),
                ff.literal(input),
                false
        );
        return moi;
    }

    private Filter getAdminFilter(String country, FilterFactory ff) {
        if(country == null || country.isEmpty()) {
            return null;
        }
        List<String> adminNameList = countriesHelper.getAdminName(country);
        if (adminNameList.isEmpty()) {
            throw new ServiceRuntimeException("Couldn't find admin(s) for country " + country);
        }

        List<Filter> filterList = new ArrayList<>();
        for (String admin : adminNameList) {
            filterList.add(ff.equals(ff.property("iso19112:administrator/gmdsf1:CI_ResponsibleParty/gmdsf1:organizationName", namespaceSupport), ff.literal(admin)));
        }

        if (filterList.size() > 1) {
            return ff.or(filterList);
        }
        return filterList.get(0);
    }

    private Filter createOptionalAndFilter(FilterFactory ff, Filter first, Filter second) {
        if(ff == null || (first == null && second == null)) {
            return null;
        }
        if (first != null && second != null) {
            return ff.and(first, second);
        }
        if(first != null) {
            return first;
        }
        return second;
    }
}