package flyway.asdi2;

import org.flywaydb.core.api.migration.BaseJavaMigration;
import org.flywaydb.core.api.migration.Context;
import org.oskari.helpers.AppSetupHelper;

/**
 * Add layeranalytics bundle to views that don't have it.
 */
public class V1_23_3__add_layeranalytics_to_geoportal extends BaseJavaMigration {

    public void migrate(Context context) throws Exception {
        AppSetupHelper.addBundleToApps(context.getConnection(), "layeranalytics");
    }
}
