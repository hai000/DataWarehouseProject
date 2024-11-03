package db;

import org.jdbi.v3.core.Jdbi;

public class StagingDB {
    private static Jdbi stagingJDBI = Connections.getStagingJDBI();

    public static int deleteData(String tableName) {
        // Xây dựng câu lệnh SQL
        String sql = String.format("DELETE FROM %s", tableName);

        return stagingJDBI.withHandle(handle -> {
            // Thực hiện câu lệnh và trả về số dòng đã xóa
            return handle.createUpdate(sql).execute();
        });
    }

    public static int loadFromFileToTable(String path, String tableName) {
        String sql = String.format("LOAD DATA INFILE '%s' " +
                "INTO TABLE %s " +
                "FIELDS TERMINATED BY ',' " +
                "ENCLOSED BY '\"' " +
                "LINES TERMINATED BY '\\n' " +
                "IGNORE 1 ROWS;", path, tableName);
        return stagingJDBI.withHandle(handel -> {
            return handel.createUpdate(sql).execute();
        });
    }
}
