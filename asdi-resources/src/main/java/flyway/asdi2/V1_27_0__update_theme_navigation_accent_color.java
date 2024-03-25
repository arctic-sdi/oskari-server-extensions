package flyway.asdi2;

import fi.nls.oskari.domain.map.view.ViewTypes;
import fi.nls.oskari.util.JSONHelper;
import org.flywaydb.core.api.migration.BaseJavaMigration;
import org.flywaydb.core.api.migration.Context;
import org.json.JSONObject;
import org.oskari.helpers.AppSetupHelper;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.List;

/**
 * Change navigation accent color for geoportal apps on ASDI geoportal
 */
public class V1_27_0__update_theme_navigation_accent_color extends BaseJavaMigration {

    public void migrate(Context context) throws Exception {
        Connection connection = context.getConnection();
        // update theme for geoportal views
        List<Long> ids = AppSetupHelper.getSetupsForType(connection, ViewTypes.DEFAULT, ViewTypes.USER);
        for (Long id: ids) {
            JSONObject metadata = getAppSetupMetadata(connection, id);
            JSONObject theme = metadata.getJSONObject("theme");

            theme.getJSONObject("map").getJSONObject("navigation").getJSONObject("color").put("accent", "#009fe3");
            JSONHelper.putValue(metadata, "theme", theme);
            updateAppMetadata(connection, id, metadata);
        }
    }

    /*
Oskari.app.getTheming().setTheme({
    "color": {
        "icon": "#FFFFFF",
        "accent": "#0c3c62",
        "primary": "#009fe3",
        "header": {
            "bg": "#009fe3",
            "icon": "#FFFFFF",
            "text": "#FFFFFF"
        },
        "text": "#FFFFFF"
    },
    "navigation": {
        "color": {
            "accent": "#0c3c62",
            "primary": "#FFFFFF"
        }
    },
    "map": {
        "color": {
            "header": {
                "bg": "#009fe3",
                "icon": "#FFFFFF",
                "text": "#FFFFFF"
            },
            "accent": "#0c3c62"
        },
        "navigation": {
            "color": {
                "text": "#000000",
                "accent": "#009fe3",
                "primary": "#FFFFFF"
            },
            "roundness": 50,
            "opacity": 0.8
        },
        "font": "arial"
    }
    })
*/

    private JSONObject getAppSetupMetadata(Connection conn, long id) throws SQLException {
        try (PreparedStatement statement = conn
                .prepareStatement("SELECT metadata FROM oskari_appsetup WHERE id=?")) {
            statement.setLong(1, id);
            try (ResultSet rs = statement.executeQuery()) {
                if (!rs.next()) {
                    return null;
                }
                return JSONHelper.createJSONObject(rs.getString("metadata"));
            }
        }
    }

    private void updateAppMetadata(Connection connection, long viewId, JSONObject metadata)
            throws SQLException {
        final String sql = "UPDATE oskari_appsetup SET metadata=? WHERE id=?";

        try (final PreparedStatement statement =
                     connection.prepareStatement(sql)) {
            statement.setString(1, metadata.toString());
            statement.setLong(2, viewId);
            statement.execute();
        }
    }
}
