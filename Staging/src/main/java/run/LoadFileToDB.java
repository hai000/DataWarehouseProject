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
import java.sql.Timestamp;
import java.util.List;

public class LoadFileToDB {
    private static Jdbi control = Connections.getControlJDBI();
    private static Jdbi staging = Connections.getStagingJDBI();
    private static DataSourceInterface dataSourceInterface = control.onDemand(DataSourceInterface.class);
    private static FileLogInterface fileLogInterface = control.onDemand(FileLogInterface.class);

    public static void loadData(String date) {
//      2.1: Load cấu hình từ file config
        Connections.loadFileConfig();

//      Lấy danh sách các nguồn dữ liệu để load từng nguồn
        List<DataSource> sources = dataSourceInterface.getAllDataSource();
        for (DataSource source : sources) {
            boolean reload = false;
//      2.2: Kiểm tra xem có process load dữ liệu từ file vào stagingdb đang chạy hay không
            FileLog fileLog = fileLogInterface.getBySourceAndStatus(source.getId(), "RLS");
            if (fileLog != null) {
//      Có process đang chạy -> kết thúc process
                continue;
            } else {
//      2.3: Kiểm tra xem đã có process load dữ liệu từ file vào stagingdb thành công mà chưa load vào dw hay không
//      nếu có thì kết thúc process để tránh bị mất dữ liệu trước đó
                fileLog = fileLogInterface.getBySourceAndStatus(source.getId(), "LSS");
                if (fileLog != null) continue;
//      2.4: Kiểm tra ngày tryền vào có null hay không
                if (date == null) {
//      2.4.1: Nếu ngày load là null thì load ngày hiện tại
                    date = new Date(System.currentTimeMillis()).toString();
//      2.4.2: Kiểm tra ngày hiện tại đã crawl dữ liệu về thành công hay chưa, nếu chưa thì kết thúc process
                    fileLog = fileLogInterface.getBySourceAndStatusAndDate(source.getId(), "ES", date);
                    if (fileLog == null) continue;

                } else {
//      2.4.3: Ngày load khác null -> reload dữ liệu của ngày nhập vào
                    fileLog = fileLogInterface.getBySourceAndDate(source.getId(), date);
//      2.4.4: Kiểm tra ngày load đã có dữ liệu crawl về thành công hay chưa
                    if (fileLog == null || fileLog.getStatus().equals("ER")) {
//                  chưa có -> không load được, kết thúc process
                        System.out.println("null");
                        continue;
                    }
//                  Kiểm tra xem log này đã được load vào stagingdb chưa -> nếu rồi thì cập nhật ngày reload
                    if(!fileLog.getStatus().equals("ES"))
                        fileLog.setReloadedAt(new Timestamp(System.currentTimeMillis()));
                }
//      2.5: Cập nhập lại trạng thái log RLS: bắt đầu load dữ liệu vào staging
                fileLogInterface.updateStatusByID("RLS", fileLog.getId());
//                try{
//      2.6: Kiểm tra file dữ liệu có tồn tại hay không
                    File file = new File(source.getFile_location() + "//" + fileLog.getFile_data());
                    String fileLocationConvert = file.getAbsolutePath().replaceAll("\\\\", "/");
                    if (!file.exists()) {
//      2.7: File dữ liệu không tồn lại -> cập nhật trạng thái log LSE: load dữ liệu vào staging lỗi và kết thúc process
                        fileLogInterface.updateStatusByID("LSE", fileLog.getId());
                        continue;
                    }
//      2.8: File dữ liệu có tồn tại thì xóa hết dữ liệu trong stagingdb trước đó đi
                    StagingDB.deleteData(source.getStaging_table());
//      2.9: Tiến hành load dữ liệu từ file vào stagingdb
                    int status = StagingDB.loadFromFileToTable(fileLocationConvert, source.getStaging_table());
//      2.10: Kiểm tra kết quả load dữ liệu vào file
                    if (status > 0) {
//      2.10.1: load thành công -> cập nhật trạng thái log LSS: load dữ liệu vào staging thành công
                        fileLog.setStatus("LSS");
                        fileLogInterface.updateLogById(fileLog);
                    } else {
//      2.10.2: load thất bại -> cập nhật trạng thái log LSE: load dữ liệu vào staging lỗi
                        fileLog.setStatus("LSE");
                        fileLogInterface.updateLogById(fileLog);
                    }
//      Kết thúc process
//                } catch (Exception e) {
//                    System.out.println(e.toString());
//                    fileLogInterface.updateStatusByID("LSE", fileLog.getId());
////
//                }

            }
        }
    }
}
