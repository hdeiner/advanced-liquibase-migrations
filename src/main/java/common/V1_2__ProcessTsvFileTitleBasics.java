package common;

import liquibase.database.jvm.JdbcConnection;

import java.sql.SQLException;

public class V1_2__ProcessTsvFileTitleBasics extends ProcessTsvFile {
    public V1_2__ProcessTsvFileTitleBasics(JdbcConnection context, String tsvFileName) {
        super(context, tsvFileName);
    }

    @Override
    public void processRow(String[] row) {
        try {
            String sql;
            if (tsvProcessedCount != 0) {
                sql = "INSERT INTO TITLE (TCONST, TITLE_TYPE, PRIMARY_TITLE, ORIGINAL_TITLE, IS_ADULT, START_YEAR, END_YEAR, RUNTIME_MINUTES) VALUES (";
                sql += "'" + row[0] + "' "; // tconst
                sql += ", '" + processEscapedQuotes(row[1]) + "' "; // titleType
                sql += ", '" + processEscapedQuotes(row[2]) + "' "; // primaryTitle
                sql += ", '" + processEscapedQuotes(row[3]) + "' "; // originalTitle
                sql += "," + row[4] + " "; // isAdult
                sql += ", '" + processYear(row[5]) + "' "; // startYear
                sql += ", '" + processYear(row[6]) + "' "; // endYear
                sql += ", '" + processInteger(row[7]) + "'"; // runTimeMinutes
                sql += ")";
                statement.execute(sql);

                if (row[8] != null) {
                    String[] genres = row[8].split(",[ ]*");
                    for (String genre : genres) {
                        sql = "INSERT INTO TITLE_GENRE (TCONST, GENRE) VALUES (";
                        sql += "'" + row[0] + "', "; // tconst
                        sql += "'" + genre + "'"; // genre
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
