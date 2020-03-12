package common;

import java.sql.SQLException;

import liquibase.database.jvm.JdbcConnection;

public class V1_2__ProcessTsvFileNameBasics extends ProcessTsvFile {
    public V1_2__ProcessTsvFileNameBasics(JdbcConnection connection, String tsvFileName) {
        super(connection, tsvFileName);
    }

    @Override
    public void processRow(String[] row) {
        try {
            String sql;
            if (tsvProcessedCount != 0) {
                sql = "INSERT INTO NAME (NCONST, PRIMARY_NAME, BIRTH_YEAR, DEATH_YEAR) VALUES (";
                sql += "'" + row[0] + "' "; // nconst
                sql += ", '" + processEscapedQuotes(row[1]) + "' "; // primaryName
                sql += ", '" + processYear(row[2]) + "' "; // birthYear
                sql += ", '" + processYear(row[3]) + "'"; // deathYear
                sql += ")";
                statement.execute(sql);

                if (row[4] != null) {
                    String[] professions = row[4].split(",[ ]*");
                    for (String profession : professions) {
                        sql = "INSERT INTO NAME_PROFESSION (NCONST, PROFESSION) VALUES (";
                        sql += "'" + row[0] + "', "; // nconst
                        sql += "'" + profession + "'"; // profession
                        sql += ")";
                        statement.execute(sql);
                    }
                }

                if (row[5] != null) {
                    String[] titles = row[5].split(",[ ]*");
                    for (String title : titles) {
                        sql = "INSERT INTO NAME_TITLE (NCONST, TCONST) VALUES (";
                        sql += "'" + row[0] + "', "; // nconst
                        sql += "'" + title + "'"; // tconst
                        sql += ")";
                        statement.execute(sql);
                    }
                }
            }
        } catch (SQLException e) {
            System.err.println("Got an SQLException! ");
            System.err.println(e.getMessage());
        }
    }
}
