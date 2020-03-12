package common;

import liquibase.change.custom.CustomTaskChange;
import liquibase.database.Database;
import liquibase.database.jvm.JdbcConnection;
import liquibase.exception.CustomChangeException;
import liquibase.exception.SetupException;
import liquibase.exception.ValidationErrors;
import liquibase.resource.ResourceAccessor;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.time.Duration;
import java.time.Instant;

public class V2_2__Split_Primary_Name implements CustomTaskChange {

    Statement statementQuery;
    Statement statementUpdate;

    int totalLines, totalLinesProcessed;
    Instant startTime, lastReportTime;
    final int SECONDS_IN_REPORTING_INTERVAL = 60;

    //to hold the parameter value
    private String file;


    private ResourceAccessor resourceAccessor;


    public String getFile() {
        return file;
    }

    public void setFile(String file) {
        this.file = file;
    }


    @Override
    public void execute(Database database) throws CustomChangeException {
        JdbcConnection connection = (JdbcConnection) database.getConnection();
        try {
            statementQuery = connection.getUnderlyingConnection().createStatement();
            statementUpdate = connection.getUnderlyingConnection().createStatement();

            totalLines = getTotalLines();
            totalLinesProcessed = 0;

            System.out.println("splitting PRIMARY_NAME - " + totalLines + " total lines (" +
                    SECONDS_IN_REPORTING_INTERVAL + " SECOND REPORTING INTERVAL) ");
            startTime = Instant.now();
            lastReportTime = startTime;

            splitPrimaryNameColumn();
        } catch (Exception e) {
            System.err.println(e.getMessage());
            e.printStackTrace();
        }
    }

    private int getTotalLines() throws SQLException {
        String query = "SELECT COUNT(*) FROM NAME;";
        ResultSet rs = statementQuery.executeQuery(query);
        rs.next();
        int count = rs.getInt(1);
        return count;
    }

    private void splitPrimaryNameColumn() throws SQLException {
        String query = "SELECT NCONST, PRIMARY_NAME FROM NAME;";
        ResultSet rs = statementQuery.executeQuery(query);
        while(rs.next()) {
            String nconst = rs.getString(1);
            String primaryName = rs.getString(2);
            int numberOfSplits = primaryName.split(" ").length;
            String firstName = primaryName.split(" ")[0];
            String lastName = "";
            if (numberOfSplits > 1) {
                lastName = primaryName.split(" ")[1];
            }
            query = "UPDATE NAME SET FIRST_NAME=\""+firstName+"\", LAST_NAME=\""+lastName+"\" WHERE NCONST=\""+nconst+"\";";
            statementUpdate.executeUpdate(query);
            ++totalLinesProcessed;
            periodicReport();
        }
    }

    private void periodicReport() {
        Instant nowTime = Instant.now();
        long milliSecondsSinceLastReportTime = Duration.between(lastReportTime, nowTime).toMillis();
        if (milliSecondsSinceLastReportTime >= (SECONDS_IN_REPORTING_INTERVAL*1000)) {
            lastReportTime = nowTime;
            int percentCompleteCurrent = totalLinesProcessed * 100 / totalLines;
            long milliSecondsElapsedTime = Duration.between(startTime, nowTime).toMillis();
            long milliSecondsProjectedCompletionTime = Math.round(Double.valueOf(milliSecondsElapsedTime) / (Double.valueOf(totalLinesProcessed) / Double.valueOf(totalLines)));
            long secondsRemainingCompletionTime = (milliSecondsProjectedCompletionTime - milliSecondsElapsedTime) / 1000;
            int minutesElapsed = (int) ((milliSecondsElapsedTime/1000) / 60);
            int secondsElapsed = (int) ((milliSecondsElapsedTime/1000) % 60);
            int minutesRemaining = (int) (secondsRemainingCompletionTime / 60);
            int secondsRemaining = (int) (secondsRemainingCompletionTime % 60);
            System.out.println("splitting PRIMARY_NAME " +
                    " - " + percentCompleteCurrent + "% complete" +
                    " - elapsed time = " + String.format("%d:%02d", minutesElapsed, secondsElapsed) +
                    " - remaining time = " + String.format("%d:%02d", minutesRemaining, secondsRemaining));
        }
    }

    @Override
    public String getConfirmationMessage() {
        return null;
    }

    @Override
    public void setUp() throws SetupException {

    }

    @Override
    public void setFileOpener(ResourceAccessor resourceAccessor) {
        this.resourceAccessor = resourceAccessor;
    }

    @Override
    public ValidationErrors validate(Database database) {
        return null;
    }

}