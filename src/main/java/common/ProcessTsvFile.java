package common;

import com.univocity.parsers.tsv.TsvParser;
import com.univocity.parsers.tsv.TsvParserSettings;
import liquibase.database.jvm.JdbcConnection;
import liquibase.exception.DatabaseException;

import java.io.*;
import java.sql.SQLException;
import java.sql.Statement;
import java.time.Duration;
import java.time.Instant;

public class ProcessTsvFile {

    final int SECONDS_IN_REPORTING_INTERVAL = 15;

    String tsvFileName;

    JdbcConnection connection;
    Statement  statement;

    int tsvTotalLines, tsvProcessedCount;
    Instant startTime, lastReportTime;

    public ProcessTsvFile(JdbcConnection connection, String tsvFileName) {
        this.connection  = connection;
        this.tsvFileName = tsvFileName;
    }

    public void execute() {
        tsvTotalLines = countLines(tsvFileName);
        System.out.println(tsvFileName + " - " + tsvTotalLines + " total lines (" +
                 SECONDS_IN_REPORTING_INTERVAL + " SECOND REPORTING INTERVAL) ");
        startTime = Instant.now();
        lastReportTime = startTime;

        try {
            statement = connection.getUnderlyingConnection().createStatement();

            TsvParserSettings settings = new TsvParserSettings();
            settings.getFormat().setLineSeparator("\n");
            settings.setMaxCharsPerColumn(8192);

            TsvParser parser = new TsvParser(settings);

            String[] row;
            tsvProcessedCount = 0;
            parser.beginParsing(getReader(tsvFileName));
            while (((row = parser.parseNext()) != null) && (tsvProcessedCount < tsvTotalLines)) {
                processRow(row);
                ++tsvProcessedCount;

                periodicReport();
            }

            commitDatabase();

            lastReportTime = Instant.now().minusSeconds(SECONDS_IN_REPORTING_INTERVAL);
            periodicReport();
        }
        catch(SQLException e) {
            System.err.println("Got an SQLException! ");
            System.err.println(e.getMessage());
        }
    }

    public void processRow(String[] row) {
    }

    public void commitDatabase() {
        try {
            connection.commit();
        } catch (DatabaseException e) {
            System.err.println("Got an SQLException! ");
            System.err.println(e.getMessage());
        }
    }

    public void periodicReport() {
        Instant nowTime = Instant.now();
        long milliSecondsSinceLastReportTime = Duration.between(lastReportTime, nowTime).toMillis();
        if (milliSecondsSinceLastReportTime >= (SECONDS_IN_REPORTING_INTERVAL*1000)) {
            lastReportTime = nowTime;
            int percentCompleteCurrent = tsvProcessedCount * 100 / tsvTotalLines;
            long milliSecondsElapsedTime = Duration.between(startTime, nowTime).toMillis();
            long milliSecondsProjectedCompletionTime = Math.round(Double.valueOf(milliSecondsElapsedTime) / (Double.valueOf(tsvProcessedCount) / Double.valueOf(tsvTotalLines)));
            long secondsRemainingCompletionTime = (milliSecondsProjectedCompletionTime - milliSecondsElapsedTime) / 1000;
            int minutesElapsed = (int) ((milliSecondsElapsedTime/1000) / 60);
            int secondsElapsed = (int) ((milliSecondsElapsedTime/1000) % 60);
            int minutesRemaining = (int) (secondsRemainingCompletionTime / 60);
            int secondsRemaining = (int) (secondsRemainingCompletionTime % 60);
            System.out.println(tsvFileName +
                    " - " + percentCompleteCurrent + "% complete" +
                    " - elapsed time = " + String.format("%d:%02d", minutesElapsed, secondsElapsed) +
                    " - remaining time = " + String.format("%d:%02d", minutesRemaining, secondsRemaining));
        }
    }

    public final int countLines(String fileName)  {
        File file = new File(fileName);
        ProcessBuilder builder = new ProcessBuilder("wc", "-l", file.getAbsolutePath());
        Process process = null;
        try {
            process = builder.start();
        } catch (IOException e) {
            e.printStackTrace();
        }
        InputStream in = process.getInputStream();
        LineNumberReader reader = new LineNumberReader(new InputStreamReader(in));
        String line = null;
        try {
            line = reader.readLine();
        } catch (IOException e) {
            e.printStackTrace();
        }
        if (line != null) {
            return Integer.parseInt(line.trim().split(" ")[0]);
        } else {
            return 0;
        }
    }

    public Reader getReader(String relativePath) {
        try {
            return new InputStreamReader(new FileInputStream(relativePath), "UTF-8");
        } catch (UnsupportedEncodingException e) {
            throw new IllegalStateException("Unable to read input", e);
        } catch (FileNotFoundException e) {
            throw new IllegalStateException("Unable to read input", e);
        }
    }

    public String processEscapedQuotes(String field) {
        if (field.equals("\\N")) {
            return "";
        }
        return field.replaceAll("'", "''");
    }

    public String processYear(String year) {
        String result = "";
        if (year.matches("^\\d+$")) {
            result = Integer.toString(Integer.parseInt(year));
        } else {
            result = "-1";
        }
        return result;
    }

    public String processBoolean(String trueOrFalse) {
        String result = "0";
        if (trueOrFalse.matches("^1$")) {
            result = "1";
        }
        return result;
    }

    public String processInteger(String number) {
        String result = "";
        if (number.matches("^\\d+$")) {
            result = number;
        } else {
            result = "-1";
        }
        return result;
    }

}
