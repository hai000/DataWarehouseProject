package run;

import crawlData.DMXCrawler;
import crawlData.NKCrawler;
import db.Connections;
import db.DataSource;
import db.FileLog;
import jdbiInterface.DataSourceInterface;
import jdbiInterface.FileLogInterface;
import org.jdbi.v3.core.Jdbi;
// Process 1: Crawl dữ liệu từ
public class CrawlProcess {
    private static Jdbi controlJDBi = Connections.getControlJDBI();
    private static FileLogInterface fileLogInterface = controlJDBi.onDemand(FileLogInterface.class);
    public static void crawlAll(){
        crawlDMX();
        crawlNK();
    }
    public static void crawlDMX() {
        crawlData("dmx_tivi");
    }

    public static void crawlNK() {
        crawlData("nk_tivi");
    }

    public static void crawlData(String source) {
//        1.1: Load cấu hình thì file config
        Connections.loadFileConfig();
//        1.2: Kiểm tra hôm nay đã có log chưa (hôm nay đã chạy process crawl lần nào chưa)
        FileLog fileLog = fileLogInterface.getTodayLogBySource(source);
        DataSource dataSource = controlJDBi.onDemand(DataSourceInterface.class).getDataSource(source);
        long config_id = dataSource.getId();

//            1.2.1: Ngày hiệnn tại chưa có log -> Tạo log mới cho ngày hiện tại với status = "RE" (đang crawl data)
        if (fileLog == null) {
            fileLog = new FileLog(config_id, "RE");
            fileLogInterface.addLog(fileLog);
        } else {
//              1.2.2: Ngày hiện tại đã có log thì kiểm tra xem log đó đã chạy thành công hay bị lỗi
            if(fileLog.getStatus().equals("ER")){
//              1.2.3: Nếu lần chạy trước đó bị lỗi thì cập nhật lại trạng thái là "RE" để chạy lại
                fileLog.setStatus("RE");
                fileLogInterface.updateStatusBySource("RE", config_id);
            }else{
//               Nếu đã có log và trạng thái khác "ER" tức là precess đang chạy hoặc đã chạy thành công -> dừng process
                return;
            }
        }
//        1.3: Bắt đầu crawl dữ liệu về
        String fileLocation;
        switch (source) {
            case "dmx_tivi":
                fileLog = new DMXCrawler().crawlData(fileLog);
                break;
            case "nk_tivi":
                fileLog = new NKCrawler().crawlData(fileLog);
                break;
            default:
                fileLog = null;
        }
//        1.4: Kiểm tra kết quả crawl dữ liệu
        if (fileLog == null || fileLog.getCount() == 0) {
//        1.4.1: Crawl dữ liệu thất bại -> Cập nhật trạng thái log ER
            fileLogInterface.updateStatusBySource("ER", config_id);
        } else {
//        1.4.2: Crawl dữ liệu thành công -> Cập nhật trạng thái log ER
            fileLog.setStatus("ES");
            fileLogInterface.updateTodayLog(fileLog, config_id);
        }
//         Kết thúc process 1
    }
}
