package common;

import liquibase.database.jvm.JdbcConnection;

import java.sql.SQLException;

public class V1_2__ProcessTsvFileTitleEpisode extends ProcessTsvFile {
    public V1_2__ProcessTsvFileTitleEpisode(JdbcConnection connection, String tsvFileName) {
        super(connection, tsvFileName);
    }

    @Override
    public void processRow(String[] row) {
        try {
            String sql;
            if (tsvProcessedCount != 0) {
                sql = "INSERT INTO TITLE_EPISODE (TCONST, TCONST_PARENT, SEASON_NUMBER, EPISODE_NUMBER) VALUES (";
                sql += "'" + row[0] + "' "; // tconst
                sql += ", '" + row[1] + "' "; // tconstParent
                sql += ", '" + processInteger(row[2]) + "' "; // seasonNumber
                sql += ", '" + processInteger(row[3]) + "'"; // episodeNumber
                sql += ")";
                statement.execute(sql);
            }
        } catch (SQLException e) {
            System.err.println("Got an SQLException! ");
            System.err.println(e.getMessage());
        }
    }
}
