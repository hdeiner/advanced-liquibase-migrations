package common;

import liquibase.database.jvm.JdbcConnection;

import java.sql.SQLException;

public class V1_2__ProcessTsvFileTitleCrew extends ProcessTsvFile {
    public V1_2__ProcessTsvFileTitleCrew(JdbcConnection connection, String tsvFileName) {
        super(connection, tsvFileName);
    }

    @Override
    public void processRow(String[] row) {
        try {
            String sql;
            if (tsvProcessedCount != 0) {

                if (row[1].equals("\\N")) row[1] = null;
                if (row[1] != null) {
                    String[] directors = row[1].split(",[ ]*");
                    for (String director : directors) {
                        sql = "INSERT INTO TITLE_DIRECTOR (TCONST, NCONST) VALUES (";
                        sql += "'" + row[0] + "', "; // tconst
                        sql += "'" + director + "'"; // nconst
                        sql += ")";
                        statement.execute(sql);
                    }
                }

                if (row[2].equals("\\N")) row[2] = null;
                if (row[2] != null) {
                    String[] writers = row[2].split(",[ ]*");
                    for (String writer : writers) {
                        sql = "INSERT INTO TITLE_WRITER (TCONST, NCONST) VALUES (";
                        sql += "'" + row[0] + "', "; // tconst
                        sql += "'" + writer + "'"; // nconst
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
