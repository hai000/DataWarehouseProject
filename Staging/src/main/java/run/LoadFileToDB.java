package run;

import db.Connections;
import db.DataSource;
import db.FileLog;
import db.StagingDB;
import jdbiInterface.DataSourceInterface;
import jdbiInterface.FileLogInterface;
import org.jdbi.v3.core.Jdbi;

import java.util.List;

public class LoadFileToDB {
    public static void loadData() {
        Jdbi control = Connections.getControlJDBI();
        Jdbi staging = Connections.getStagingJDBI();
        DataSourceInterface dataSourceInterface = control.onDemand(DataSourceInterface.class);
        FileLogInterface fileLogInterface = control.onDemand(FileLogInterface.class);

        List<DataSource> sources = dataSourceInterface.getAllDataSource();
        for (DataSource source : sources) {
            FileLog fileLog = fileLogInterface.getBySourceAndStatus(source.getId(), "LS");
            if (fileLog != null) {
                continue;
            } else {
                fileLog = fileLogInterface.getBySourceAndStatus(source.getId(), "ES");
                StagingDB.deleteData(source.getStaging_table());
                int status = StagingDB.loadFromFileToTable(fileLog.getFile_data(), source.getStaging_table());
                if (status > 0) {
                    fileLogInterface.updateStatusByID("LS", fileLog.getId());
                } else {
                    fileLogInterface.updateStatusByID("LE", fileLog.getId());
                }
            }
        }
    }
}
