package run;

import db.Connections;
import org.jdbi.v3.core.Jdbi;

public class LoadStagingToDW {
    private static Jdbi dwJDBI = Connections.getdwJDBI();

    public static void loadDMXData() {
        String sql = "CALL loadDMXData()";
        dwJDBI.withHandle(handle -> {

            return handle.createCall(sql).invoke() ;
        });
    }
    public static void loadNKData() {
        String sql = "CALL loadNKData()";
        dwJDBI.withHandle(handle -> {

            return handle.createCall(sql).invoke() ;
        });
    }

    public static void main(String[] args) {
        loadNKData();
    }
}
