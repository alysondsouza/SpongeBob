package Util;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;

public class DB_Connect {
    private Connection connection;
    private static DB_Connect instance;
    private DB_Connect() {
        try {
            String baseurl = "jdbc:mysql://ali.cwic1cb4gyam.eu-central-1.rds.amazonaws.com";
            String db = "sponge_bob";
            String timeZ = "serverTimezone=UTC&allowPublicKeyRetrieval=true";
            String totalUrl = baseurl+db+"?"+timeZ;
            String user = "guest";
            String password = "guest";
            connection = DriverManager.getConnection(totalUrl,user,password);

        } catch (SQLException id) {
            System.out.println("Error: " + id.getMessage());
        }
    }
    public static DB_Connect getInstance() {
        if (instance == null ) {
            instance = new DB_Connect();
        }
        return instance;
    }

}