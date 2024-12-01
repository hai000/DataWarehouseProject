package run;

import crawlData.DMXCrawler;
import crawlData.NKCrawler;
import db.Connections;
import db.DataSource;
import db.FileLog;
import jdbiInterface.DataSourceInterface;
import jdbiInterface.FileLogInterface;
import org.jdbi.v3.core.Jdbi;

public class CrawlProcess {
    public static void crawlDMX(){
        crawlData("dmx_tivi");
    }
    public static void crawlNK(){
        crawlData("nk_tivi");
    }

    public static void crawlData(String source){
        Connections.loadFileConfig();
        Jdbi controlJDBi = Connections.getControlJDBI();
        FileLogInterface fileLogInterface = controlJDBi.onDemand(FileLogInterface.class);
        FileLog fileLog = fileLogInterface.getTodayLogBySource(source);
//        System.out.println(fileLog);
        DataSource dataSource = controlJDBi.onDemand(DataSourceInterface.class).getDataSource(source);
        long config_id = dataSource.getId();

        if(fileLog == null || fileLog.getStatus().equals("ER")){
            if(fileLog == null){
                fileLog = new FileLog(config_id, "RE");
                fileLogInterface.addLog(fileLog);
            }else{
                fileLog.setStatus("RE");
                fileLogInterface.updateStatusBySource("RE", config_id);
            }
            String fileLocation ;
            switch (source){
                case "dmx_tivi":
                    fileLocation = new DMXCrawler().crawlData();
                    break;
                case "nk_tivi":
                    fileLocation = new NKCrawler().crawlData();
                    break;
                default: fileLocation ="";
            }
            if(fileLocation == null){
                fileLogInterface.updateStatusBySource("ER", config_id);
            }else{
                fileLogInterface.updateStatusBySource("ES", config_id);
                fileLogInterface.updateFileLocation(fileLocation, config_id);
            }

        }
    }
}
