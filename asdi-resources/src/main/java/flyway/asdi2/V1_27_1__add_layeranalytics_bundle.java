package flyway.asdi2;

import fi.nls.oskari.domain.map.view.Bundle;
import org.flywaydb.core.api.migration.BaseJavaMigration;
import org.flywaydb.core.api.migration.Context;
import org.oskari.helpers.AppSetupHelper;

import java.sql.Connection;
import java.util.List;

public class V1_27_1__add_layeranalytics_bundle extends BaseJavaMigration {
    @Override
    public void migrate(Context context) throws Exception {
        Connection connection = context.getConnection();
        String bundleName = "layeranalytics";
        Bundle layerAnalyticsBundle = new Bundle(bundleName);
        List<Long> appsetupIds = AppSetupHelper.getSetupsForType(connection, null);
        AppSetupHelper.addOrUpdateBundleInApps(connection, layerAnalyticsBundle, appsetupIds);
    }
}
