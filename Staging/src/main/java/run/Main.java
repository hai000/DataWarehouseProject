package run;


import crawlData.ACrawler;
import crawlData.DMXCrawler;
import crawlData.NKCrawler;
import db.Connections;
import jdbiInterface.StagingDBInterface;

import java.io.*;

public class Main {
    public static void main(String[] args) throws IOException {
//    LoadFileToDB loadFileToDB = new LoadFileToDB();
//    loadFileToDB.loadData();
//        new DMXCrawler().crawlData();
        CrawlProcess.crawlDMX();
        CrawlProcess.crawlNK();
        LoadFileToDB.loadData();
        LoadStagingToDW.loadDMXData();
        LoadStagingToDW.loadNKData();

    }
}
