package jdbiInterface;

import org.jdbi.v3.sqlobject.customizer.Bind;
import org.jdbi.v3.sqlobject.statement.SqlUpdate;

public interface StagingDBInterface {
    @SqlUpdate("delete from :table")
    void deleteData(@Bind("table") String table);
    @SqlUpdate("LOAD DATA INFILE :fileLocation " +
            "INTO TABLE :tableName "+
            "FIELDS TERMINATED BY ',' " +
            "ENCLOSED BY '\"' " +
            "LINES TERMINATED BY '\\n' " +
            "IGNORE 1 ROWS;")
    int loadFileToTable(@Bind("fileLocation") String fileLocation, @Bind("tableName") String tableName);

}
