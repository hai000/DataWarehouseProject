package db;

import org.jdbi.v3.core.Jdbi;
import org.jdbi.v3.sqlobject.SqlObjectPlugin;
import org.json.JSONObject;

import java.io.*;

public class Connections {
    private static JSONObject fileConfig;
    private static Jdbi controlJDBI, stagingJDBI, dwJDBI;
    public static void loadFileConfig(){
        try{
            if(fileConfig ==null){
                BufferedReader br = new BufferedReader(new FileReader(new File("Staging/config.json")));
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

}
