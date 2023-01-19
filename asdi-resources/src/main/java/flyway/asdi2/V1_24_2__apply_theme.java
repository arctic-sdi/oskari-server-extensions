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
 * Change theme colors for geoportal apps on ASDI geoportal
 */
public class V1_24_2__apply_theme extends BaseJavaMigration {

    public void migrate(Context context) throws Exception {
        Connection connection = context.getConnection();
        // update theme for geoportal views
        List<Long> ids = AppSetupHelper.getSetupsForType(connection, ViewTypes.DEFAULT, ViewTypes.USER);
        JSONObject theme = generateTheme();
        for (Long id: ids) {
            JSONObject metadata = getAppSetupMetadata(connection, id);
            JSONHelper.putValue(metadata, "theme", theme);
            updateAppMetadata(connection, id, metadata);
        }
    }

    /*
    Oskari.app.getTheming().setTheme({
        "color": {
          "header": {
            "bg": "#009fe3",
            "text": "#FFFFFF",
            "icon": "#FFFFFF"
          },
          "icon": "#FFFFFF",
          "accent": "#0c3c62",
          "primary": "#009fe3",
          "text": "#FFFFFF"
        },
        "navigation": {
          "color": {
            "primary": "#FFFFFF",
            "accent": "#0c3c62"
          }
        },
        "map": {
          "font": "arial",
          "color": {
            "header": {
              "bg": "#009fe3",
              "text": "#FFFFFF",
              "icon": "#FFFFFF"
            },
            "accent": "#0c3c62"
          },
          "navigation": {
            "roundness": 50,
            "opacity": 0.8,
            "color": {
              "primary": "#ffffff",
              "accent": "#0c3c62",
              "text": "#000000"
            }
          }
        }
      })

     */
    protected JSONObject generateTheme() {
        JSONObject theme = new JSONObject();

        JSONObject geoportalColor = new JSONObject();
        JSONHelper.putValue(theme, "color", geoportalColor);
        JSONHelper.putValue(geoportalColor, "primary", "#009fe3");
        JSONHelper.putValue(geoportalColor, "accent", "#0c3c62");
        JSONHelper.putValue(geoportalColor, "text", "#FFFFFF");
        JSONHelper.putValue(geoportalColor, "icon", "#FFFFFF");

        JSONObject geoportalColorHeaderColor = new JSONObject();
        JSONHelper.putValue(geoportalColor, "header", geoportalColorHeaderColor);
        JSONHelper.putValue(geoportalColorHeaderColor, "bg", "#009fe3");
        JSONHelper.putValue(geoportalColorHeaderColor, "text", "#FFFFFF");
        JSONHelper.putValue(geoportalColorHeaderColor, "icon", "#FFFFFF");

        JSONObject geoportalNav = new JSONObject();
        JSONHelper.putValue(theme, "navigation", geoportalNav);
        JSONObject geoportalNavColor = new JSONObject();
        JSONHelper.putValue(geoportalNav, "color", geoportalNavColor);
        JSONHelper.putValue(geoportalNavColor, "primary", "#FFFFFF");
        JSONHelper.putValue(geoportalNavColor, "accent", "#0c3c62");

        JSONObject mapTheme = new JSONObject();
        JSONHelper.putValue(theme, "map", mapTheme);
        JSONHelper.putValue(mapTheme, "font", "arial");

        JSONObject mapNav = new JSONObject();
        JSONHelper.putValue(mapTheme, "navigation", mapNav);
        JSONHelper.putValue(mapNav, "roundness", 50);
        JSONHelper.putValue(mapNav, "opacity", 0.8);

        JSONObject navColor = new JSONObject();
        JSONHelper.putValue(mapNav, "color", navColor);
        JSONHelper.putValue(navColor, "primary", "#FFFFFF");
        JSONHelper.putValue(navColor, "accent", "#000000");
        JSONHelper.putValue(navColor, "text", "#000000");

        JSONObject mainColor = new JSONObject();
        JSONHelper.putValue(mapTheme, "color", mainColor);
        JSONHelper.putValue(mainColor, "accent", "#0c3c62");
        JSONObject headerColor = new JSONObject();
        JSONHelper.putValue(mainColor, "header", headerColor);
        JSONHelper.putValue(headerColor, "bg", "#009fe3");
        JSONHelper.putValue(headerColor, "text", "#FFFFFF");
        JSONHelper.putValue(headerColor, "icon", "#FFFFFF");

        return theme;
    }

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
