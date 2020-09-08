package flyway.asdistats;

import org.flywaydb.core.api.migration.BaseJavaMigration;
import org.flywaydb.core.api.migration.Context;
import org.oskari.helpers.AppSetupHelper;

import java.sql.Connection;
import java.util.List;

/**
 * Add statsgrid bundle to views that don't have it.
 */
public class V1_0__add_statsgrid_to_geoportal extends BaseJavaMigration {
    private static final String BUNDLE_ID = "statsgrid";

    public void migrate(Context context) throws Exception {
        Connection connection = context.getConnection();
        final List<Long> views = AppSetupHelper.getSetupsForUserAndDefaultType(connection);
        for (Long viewId : views) {
            if (AppSetupHelper.appContainsBundle(connection, viewId, BUNDLE_ID)) {
                continue;
            }
            AppSetupHelper.addBundleToApp(connection, viewId, BUNDLE_ID);
        }
    }
}
