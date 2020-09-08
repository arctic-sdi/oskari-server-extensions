package flyway.asdistats;

import org.flywaydb.core.api.migration.BaseJavaMigration;
import org.flywaydb.core.api.migration.Context;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.SQLException;

/**
 * Add permissions for regionset: protected areas
 */
public class V1_6__add_unsd_datasources extends BaseJavaMigration {

    private static final String layer = "ne_110m_countries-3575";
    private static final String prefix = "UN Agenda 2030 SD Goal";

    public void migrate(Context context) throws Exception {
        Connection connection = context.getConnection();
        addDS(connection, "No poverty", "1");
        addDS(connection, "Zero hunger", "2");
        addDS(connection, "Good health and well-being", "3");
        addDS(connection, "Quality education", "4");
        addDS(connection, "Gender equality", "5");
        addDS(connection, "Clean water and sanitation", "6");
        addDS(connection, "Affordable and clean energy", "7");
        addDS(connection, "Decent work and economic growth", "8");
        addDS(connection, "Industry, innovation and infrastructure", "9");
        addDS(connection, "Reduced inequalities", "10");
        addDS(connection, "Sustainable cities and communities", "11");
        addDS(connection, "Responsible consumption and production", "12");
        addDS(connection, "Climate action", "13");
        addDS(connection, "Life below water", "14");
        addDS(connection, "Life on land", "15");
        addDS(connection, "Peace, justice and strong institutions", "16");
        addDS(connection, "Partnerships for the goals", "17");
    }

    private void addDS(Connection conn, String name, String goal) throws SQLException {
        name = prefix + " " + goal + ": " + name;
        try (PreparedStatement statement = conn.prepareStatement(getDSInsert(name, goal))){
            statement.execute();
        }

        try (PreparedStatement statement = conn.prepareStatement(getRegionsetLinkInsert(name))){
            statement.execute();
        }
    }


    private String getDSInsert(String name, String goal) {

        return "INSERT INTO oskari_statistical_datasource (locale, config, plugin) " +
            "VALUES ('{\"en\":{\"name\":\"" + name + "\"}}', '{ \"goal\": \"" + goal + "\" }', 'UNSD');";
    }

    private String getRegionsetLinkInsert(String name) {
        return "INSERT INTO oskari_statistical_layer(datasource_id, layer_id, config) " +
            "VALUES(" +
                "(SELECT id FROM oskari_statistical_datasource " +
                    "WHERE locale like '%" + name + "%'), " +
                "(SELECT id FROM oskari_maplayer WHERE type='statslayer' AND name = '" + layer + "'), " +
                "'{}');";
    }
}
