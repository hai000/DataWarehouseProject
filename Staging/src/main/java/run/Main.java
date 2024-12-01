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
        CrawlProcess.crawlDMX();
        CrawlProcess.crawlNK();
        LoadFileToDB.loadData();
        LoadStagingToDW.loadDMXData();
        LoadStagingToDW.loadNKData();

    }
}
