package run;


import crawlData.ACrawler;
import crawlData.DMXCrawler;
import crawlData.NKCrawler;
import db.Connections;
import db.StagingDB;
import jdbiInterface.StagingDBInterface;

import java.io.*;

public class Main {
    public static void main(String[] args) throws IOException {
//    LoadFileToDB loadFileToDB = new LoadFileToDB();
//    loadFileToDB.loadData();
//        new DMXCrawler().crawlData();
//        StagingDB.loadFromFileToTable("D:/Program/DataWarehouseProject/Staging/data/DMX/DMXdata_2024-11-05.csv", "stg_dmx_data");
//        CrawlProcess.crawlDMX();
//        CrawlProcess.crawlNK();
        LoadFileToDB.loadData();
        LoadStagingToDW.loadDMXData();
        LoadStagingToDW.loadNKData();

    }
}
