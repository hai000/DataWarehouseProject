package run;

import db.Connections;
import db.DataSource;
import db.FileLog;
import db.StagingDB;
import jdbiInterface.DataSourceInterface;
import jdbiInterface.FileLogInterface;
import org.jdbi.v3.core.Jdbi;

import java.io.File;
import java.sql.Date;
import java.util.List;

public class LoadFileToDB {
    public static void loadData(Date date) {
        Jdbi control = Connections.getControlJDBI();
        Jdbi staging = Connections.getStagingJDBI();
        DataSourceInterface dataSourceInterface = control.onDemand(DataSourceInterface.class);
        FileLogInterface fileLogInterface = control.onDemand(FileLogInterface.class);

        List<DataSource> sources = dataSourceInterface.getAllDataSource();
        for (DataSource source : sources) {
            FileLog fileLog = fileLogInterface.getBySourceAndStatus(source.getId(), "RLS");
            if (fileLog != null) {
                continue;
            } else {
                if(date == null){
                    date = new Date(System.currentTimeMillis());
                    fileLog = fileLogInterface.getBySourceAndStatusAndDate(source.getId(),"ES", date);
                    if(fileLog == null) continue;

                }else{
                    fileLog = fileLogInterface.getBySourceAndDate(source.getId(), date);
                    if(fileLog == null) continue;
                }
                fileLogInterface.updateStatusByID("RLS", fileLog.getId());
                File file = new File(fileLog.getFile_data());
                if(!file.exists()){
                    fileLogInterface.updateStatusByID("LSE", fileLog.getId());
                    continue;
                }
//                fileLog = fileLogInterface.getBySourceAndStatus(source.getId(), "ES");
//                System.out.println(fileLog);
                StagingDB.deleteData(source.getStaging_table());
                int status = StagingDB.loadFromFileToTable(fileLog.getFile_data(), source.getStaging_table());
                if (status > 0) {
                    fileLogInterface.updateStatusByID("LSS", fileLog.getId());
                } else {
                    fileLogInterface.updateStatusByID("LSE", fileLog.getId());
                }
            }
        }
    }
}
