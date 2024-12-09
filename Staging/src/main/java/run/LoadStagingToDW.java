package run;

import db.Connections;
import db.DataSource;
import db.FileLog;
import jdbiInterface.DataSourceInterface;
import jdbiInterface.FileLogInterface;
import org.jdbi.v3.core.Jdbi;

import java.util.List;

public class LoadStagingToDW {
    private static Jdbi dwJDBI = Connections.getdwJDBI();
    private static Jdbi control = Connections.getControlJDBI();
    private static Jdbi staging = Connections.getStagingJDBI();
    private static DataSourceInterface dataSourceInterface = control.onDemand(DataSourceInterface.class);
    private static FileLogInterface fileLogInterface = control.onDemand(FileLogInterface.class);

    public static void loadDataFromStgToDW(){
        List<DataSource> sources = dataSourceInterface.getAllDataSource();
        for(DataSource source:sources){
            FileLog fileLog = fileLogInterface.getBySourceAndStatus(source.getId(), "LSS");
            if(fileLog ==null) continue;

            fileLogInterface.updateStatusByID("RLD", fileLog.getId());
            String sql = "select count(*) from "+source.getStaging_table();
            int rowdata = staging.withHandle(handle ->{
                return handle.createQuery(sql).mapTo(Integer.class).one();
            });
            if(rowdata ==0){
                fileLogInterface.updateStatusByID("LDE", fileLog.getId());

            }else{
                loadData(source.getDw_procedure());
                fileLogInterface.updateStatusByID("LDS", fileLog.getId());
            }
        }
    }

    public static void loadData(String dw_procedure) {
        String sql = String.format("CALL %s",dw_procedure);
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
        loadDataFromStgToDW();

    }
}
