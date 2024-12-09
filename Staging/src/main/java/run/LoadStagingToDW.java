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
//      3.1: Load cấu hình từ file config
        Connections.loadFileConfig();
        List<DataSource> sources = dataSourceInterface.getAllDataSource();
        for(DataSource source:sources){
//      3.2: Kiểm tra có log nào trạng thái LDS không
            FileLog fileLog = fileLogInterface.getBySourceAndStatus(source.getId(), "LDS");
//            Có -> kết thúc process để tránh mất dữ liệu khi chưa load vào data mart
            if(fileLog != null) continue;
//      3.3: Kiểm tra đang có process nào load lỗi không (LDE), nếu có thì chạy lại log đó
            fileLog = fileLogInterface.getBySourceAndStatus(source.getId(), "LDE");
            if(fileLog ==null){
//      3.4: Nếu không có process lỗi thì kiểm tra xem có dữ liệu mới được load và staging không
                fileLog = fileLogInterface.getBySourceAndStatus(source.getId(), "LSS");
                if(fileLog == null) continue; //Không có -> kết thúc process
            }
//      3.5: Cập nhật trạng thái log RLD -> tiến hành load dữ liệu
            fileLogInterface.updateStatusByID("RLD", fileLog.getId());
//      3.6: Kiểm tra trong staging hiện có dữ liệu không
            String sql = "select count(*) from "+source.getStaging_table();
            int rowdata = staging.withHandle(handle ->{
                return handle.createQuery(sql).mapTo(Integer.class).one();
            });
            if(rowdata ==0){
//      3.7: staging không có dữ liệu -> cập nhật trạng thái log LDE: load dữ liệu vào dw lỗi
                fileLogInterface.updateStatusByID("LDE", fileLog.getId());
                continue; // kết thúc process
            }else{
//      3.9 -> 3.17 trong sql (procedure)
                loadData(source.getDw_procedure());
//      3.18: Cập nhật lại trạng thái log (LDS): load dữ liệu vào dw thành công
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


    public static void main(String[] args) {
        loadDataFromStgToDW();

    }
}
