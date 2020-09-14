package flyway.asdistats;

import fi.nls.oskari.domain.map.view.Bundle;
import org.flywaydb.core.api.migration.BaseJavaMigration;
import org.flywaydb.core.api.migration.Context;
import org.oskari.helpers.AppSetupHelper;

/**
 * Add statsgrid bundle to views that don't have it.
 */
public class V1_0__add_statsgrid_to_geoportal extends BaseJavaMigration {

    public void migrate(Context context) throws Exception {
        AppSetupHelper.addBundleToDefaultAndUserApps(
                context.getConnection(), new Bundle("statsgrid"));
    }
}
