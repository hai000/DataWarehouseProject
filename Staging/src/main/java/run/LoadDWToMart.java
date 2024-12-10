package run;

import db.Connections;
import db.DataSource;
import db.FileLog;
import jdbiInterface.DataSourceInterface;
import jdbiInterface.FileLogInterface;
import org.jdbi.v3.core.Jdbi;

public class LoadDWToMart {
    private static Jdbi dmJDBI = Connections.getDMJDBI();
    private static Jdbi controlJDBI = Connections.getControlJDBI();
    private static Jdbi dwJDBI = Connections.getdwJDBI();
    private static FileLogInterface fileLogInterface = controlJDBI.onDemand(FileLogInterface.class);
    private static DataSourceInterface dataSourceInterface = controlJDBI.onDemand(DataSourceInterface.class);

    public static void loadDwToDm() {
//        4.1: Load cấu hình từ file config
        Connections.loadFileConfig();

//        4.2: Kiểm tra có dữ liệu mới được load vào data_tivi của data_warehouse không (status LDS)
        for (DataSource sources : dataSourceInterface.getAllDataSource()) {
            FileLog fileLog = fileLogInterface.getBySourceAndStatus(sources.getId(), "LDS");
            // Khôgn có dữ liệu mới được load vào -> kết thúc process
            if(fileLog == null) continue;
//         4.3.1: Kiểm tra bảng data_tivi trong data_warehouse có dữ liệu không
            String sql = "select count(*) from data_tivi";
            int rowdata = dwJDBI.withHandle(handle ->{
                return handle.createQuery(sql).mapTo(Integer.class).one();
            });
            // 4.3.2: Không có dữ liệu -> cập nhật trạng thái load dữ liệu bị lỗi (LME)
            if(rowdata ==0) {
                fileLogInterface.updateStatusByID("LME", fileLog.getId());
                continue;
            }else{
//          4.4: Load dữ liệu từ data_tivi của data_warehouse vào raw_data_tivi của data_mart
                dmJDBI.withHandle(handle -> {
                    return handle.createCall("Call update_raw_tivi_data()").invoke() ;
                });
//          4.5: Cập nhật các dim
                dmJDBI.withHandle(handle -> {
                    return handle.createCall("Call update_dim()").invoke() ;
                });
//          4.6: Cập nhật fact_product
                dmJDBI.withHandle(handle -> {
                    return handle.createCall("Call update_fact_product()").invoke() ;
                });
//          4.7: Cập nhật các bảng thống kê
                dmJDBI.withHandle(handle -> {
                    return handle.createCall("Call update_statistical()").invoke() ;
                });
//          4.8: Cập nhật trạng thái log (LMS)
                fileLogInterface.updateStatusByID("LMS", fileLog.getId());
            }

        }
    }

    public static void main(String[] args) {
        loadDwToDm();
    }
}
