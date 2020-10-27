package flyway.asdi2;

import fi.nls.oskari.util.PropertyUtil;
import fi.nls.oskari.domain.map.view.Bundle;
import fi.nls.oskari.log.LogFactory;
import fi.nls.oskari.log.Logger;
import org.flywaydb.core.api.migration.BaseJavaMigration;
import org.flywaydb.core.api.migration.Context;
import org.oskari.helpers.AppSetupHelper;
import org.oskari.helpers.BundleHelper;

import java.sql.Connection;
import java.sql.SQLException;
import java.util.Arrays;
import java.util.List;

public class V1_23_1__migrate_layer_list extends BaseJavaMigration {
    private static final Logger LOG = LogFactory.getLogger(V1_23_1__migrate_layer_list.class);
    private static final String LAYERSELECTOR_BUNDLE_NAME = "layerselector2";
    private static final String LAYERSELECTION_BUNDLE_NAME = "layerselection2";
    private static final String LAYERLIST_BUNDLE_NAME = "layerlist";
    private static final String LAYER_EDITOR_BUNDLE_NAME = "admin-layereditor";

    public void migrate(Context context) throws Exception {
        if(PropertyUtil.getOptional("flyway.asdi.1_23_1.skip", true)) {
            LOG.warn("You are skipping layer list migration.");
            return;
        }
        Connection connection = context.getConnection();
        createBundles(connection);
        updateDefaultAndUserViews(connection);
    }

    private void createBundles(Connection connection) throws Exception {
        for (String bundleName : Arrays.asList(LAYER_EDITOR_BUNDLE_NAME, LAYERLIST_BUNDLE_NAME)) {
            Bundle bundle = new Bundle();
            bundle.setName(bundleName);
            BundleHelper.registerBundle(connection, bundle);
            LOG.info("Created " + bundleName + " bundle");
        }
    }

    private void updateDefaultAndUserViews(Connection connection) throws SQLException {
        Bundle newBundle = BundleHelper.getRegisteredBundle(connection, LAYERLIST_BUNDLE_NAME);
        List<Long> viewIds = AppSetupHelper.getSetupsForUserAndDefaultType(connection);
        for(Long viewId : viewIds) {
            Bundle bundle = AppSetupHelper.getAppBundle(connection, viewId, LAYERSELECTOR_BUNDLE_NAME);
            if (bundle == null) {

                Bundle migratedBetaBundle = AppSetupHelper.getAppBundle(connection, viewId, LAYERLIST_BUNDLE_NAME);
                if (migratedBetaBundle != null) {
                    // view already migrated at beta stage- > just remove the old "selected layers" bundle
                    AppSetupHelper.removeBundleFromApp(connection, viewId, LAYERSELECTION_BUNDLE_NAME);
                }
                continue;
            }
            newBundle.setSeqNo(bundle.getSeqNo());
            newBundle.setConfig(bundle.getConfig());
            AppSetupHelper.removeBundleFromApp(connection, viewId, LAYERSELECTOR_BUNDLE_NAME);
            AppSetupHelper.removeBundleFromApp(connection, viewId, LAYERSELECTION_BUNDLE_NAME);
            AppSetupHelper.addBundleToApp(connection, viewId, LAYERLIST_BUNDLE_NAME);
            AppSetupHelper.updateAppBundle(connection, viewId, newBundle);
        }
    }
}
