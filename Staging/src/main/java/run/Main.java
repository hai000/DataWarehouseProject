package run;


import crawlData.ACrawler;
import crawlData.DMXCrawler;
import crawlData.DMXProduct;
import crawlData.NKCrawler;
import db.Connections;
import db.DataSource;
import db.StagingDB;
import jdbiInterface.StagingDBInterface;

import java.io.*;
import java.text.SimpleDateFormat;
import java.util.Date;

public class Main {
    public static void main(String[] args) throws IOException {

//        CrawlProcess.crawlDMX();
//        CrawlProcess.crawlNK();
        LoadFileToDB.loadData(null);
//        LoadStagingToDW.loadDMXData();
//        LoadStagingToDW.loadNKData();

    }
}
