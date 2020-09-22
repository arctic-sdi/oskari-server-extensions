package flyway.asdi2;

import org.flywaydb.core.api.migration.BaseJavaMigration;
import org.flywaydb.core.api.migration.Context;
import org.oskari.helpers.AppSetupHelper;

/**
 * Add printout bundle to views that don't have it.
 */
public class V1_22_1__add_printout_to_geoportal extends BaseJavaMigration {

    public void migrate(Context context) throws Exception {
        AppSetupHelper.addBundleToApps(context.getConnection(), "printout");
    }
}
