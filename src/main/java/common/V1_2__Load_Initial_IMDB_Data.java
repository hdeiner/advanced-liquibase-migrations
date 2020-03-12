package common;

import liquibase.change.custom.CustomTaskChange;
import liquibase.database.Database;
import liquibase.database.jvm.JdbcConnection;
import liquibase.exception.CustomChangeException;
import liquibase.exception.SetupException;
import liquibase.exception.ValidationErrors;
import liquibase.resource.ResourceAccessor;

public class V1_2__Load_Initial_IMDB_Data implements CustomTaskChange {

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
        new V1_2__ProcessTsvFileNameBasics(connection, "src/main/java/common/data/name.basics.tsv.smaller").execute();
        new V1_2__ProcessTsvFileTitleAkas(connection, "src/main/java/common/data/title.akas.tsv.smaller").execute();
        new V1_2__ProcessTsvFileTitleBasics(connection, "src/main/java/common/data/title.basics.tsv.smaller").execute();
        new V1_2__ProcessTsvFileTitleCrew(connection, "src/main/java/common/data/title.crew.tsv.smaller").execute();
        new V1_2__ProcessTsvFileTitleEpisode(connection, "src/main/java/common/data/title.episode.tsv.smaller").execute();
        new V1_2__ProcessTsvFileTitlePrincipals(connection, "src/main/java/common/data/title.principals.tsv.smaller").execute();
        new V1_2__ProcessTsvFileTitleRatings(connection, "src/main/java/common/data/title.ratings.tsv.smaller").execute();
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