package run;


import crawlData.ACrawler;
import crawlData.DMXCrawler;
import crawlData.DMXProduct;
import crawlData.NKCrawler;
import db.Connections;
import db.DataSource;
import db.StagingDB;
import jdbiInterface.DataSourceInterface;
import jdbiInterface.FileLogInterface;
import jdbiInterface.StagingDBInterface;

import java.io.*;
import java.sql.Date;
import java.text.SimpleDateFormat;

public class Main {
    public static void main(String[] args) throws IOException {

        CrawlProcess.crawlDMX();
        CrawlProcess.crawlNK();
        LoadFileToDB.loadData(null);
        LoadStagingToDW.loadDataFromStgToDW();


    }
}
