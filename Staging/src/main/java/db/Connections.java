package db;

import org.jdbi.v3.core.Jdbi;
import org.jdbi.v3.sqlobject.SqlObjectPlugin;
import org.json.JSONObject;

import java.io.*;

public class Connections {
    private static JSONObject fileConfig;
    private static Jdbi controlJDBI, stagingJDBI, dwJDBI, dmJDBI;
    // Đọc dữ liệu từ file config
    public static void loadFileConfig(){
        try{
            if(fileConfig ==null){
                BufferedReader br = new BufferedReader(new FileReader(new File("./config.json")));
                String fileContent="";
                String in;
                while((in = br.readLine())!= null){
                    fileContent += in;
                }
                fileConfig = new JSONObject(fileContent);
            }
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }
    public static Jdbi getControlJDBI(){
        try {
            loadFileConfig();
            if(controlJDBI == null ){
                JSONObject jsonControl = fileConfig.getJSONObject("control");
//        System.out.println(jsonControl.getString("ip"));
                String dbConnect = String.format("jdbc:mysql://%s:%s/%s", jsonControl.getString("ip"),
                        jsonControl.getInt("port"), jsonControl.getString("dbname"));
                controlJDBI = Jdbi.create(dbConnect, jsonControl.getString("username"),
                        jsonControl.getString("password"));
                controlJDBI.installPlugin(new SqlObjectPlugin());
            }
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    return controlJDBI;
    }

    public static Jdbi getDMJDBI(){
        try {
            loadFileConfig();
            if(dmJDBI == null ){
                JSONObject jsonControl = fileConfig.getJSONObject("data_mart");
//        System.out.println(jsonControl.getString("ip"));
                String dbConnect = String.format("jdbc:mysql://%s:%s/%s", jsonControl.getString("ip"),
                        jsonControl.getInt("port"), jsonControl.getString("dbname"));
                dmJDBI = Jdbi.create(dbConnect, jsonControl.getString("username"),
                        jsonControl.getString("password"));
                dmJDBI.installPlugin(new SqlObjectPlugin());
            }
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
        return dmJDBI;
    }
    public static Jdbi getStagingJDBI(){

        try {
            loadFileConfig();
            if(stagingJDBI == null ){
                JSONObject jsonControl = fileConfig.getJSONObject("staging");
//        System.out.println(jsonControl.getString("ip"));
                String dbConnect = String.format("jdbc:mysql://%s:%s/%s", jsonControl.getString("ip"),
                        jsonControl.getInt("port"), jsonControl.getString("dbname"));
                stagingJDBI = Jdbi.create(dbConnect, jsonControl.getString("username"),
                        jsonControl.getString("password"));
                stagingJDBI.installPlugin(new SqlObjectPlugin());
            }
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
        return stagingJDBI;
    }
    public static Jdbi getdwJDBI(){

        try {
            loadFileConfig();
            if(dwJDBI == null ){
                JSONObject jsonControl = fileConfig.getJSONObject("data_warehouse");
//        System.out.println(jsonControl.getString("ip"));
                String dbConnect = String.format("jdbc:mysql://%s:%s/%s", jsonControl.getString("ip"),
                        jsonControl.getInt("port"), jsonControl.getString("dbname"));
                dwJDBI = Jdbi.create(dbConnect, jsonControl.getString("username"),
                        jsonControl.getString("password"));
                dwJDBI.installPlugin(new SqlObjectPlugin());
            }
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
        return dwJDBI;
    }

    public static void main(String[] args) {
        loadFileConfig();
        System.out.println(fileConfig.getJSONObject("data_warehouse"));
    }
}
