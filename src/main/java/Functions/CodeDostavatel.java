package Functions;

import java.io.FileInputStream;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.ArrayList;
import java.util.Properties;

import static DataBase.JDBC.getConnection;


public class CodeDostavatel{

    private static int GetLastId(){
        int lastid = 0;



        return lastid;
    }

    public static int coded() throws IOException, SQLException {
    int res = 0;

    String Sql = "SELECT * FROM codetable";


    Connection conn = null;
    try{
        conn = getConnection();
    } catch (Exception e) {
        e.printStackTrace();
    }

    assert conn != null;
    Statement statement = conn.createStatement();
    ResultSet rs = statement.executeQuery(Sql);

    while (rs.next()){



    }


    return res;
    }
}