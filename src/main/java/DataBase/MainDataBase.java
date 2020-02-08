package DataBase;


import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.ArrayList;
import java.util.Iterator;

import static DataBase.JDBC.getConnection;


public class MainDataBase {

    public static boolean Check(int ID) throws SQLException {
        Connection conn = null;

        try{
            conn = getConnection();
        } catch (Exception e) {
            e.printStackTrace();
        }

        String selectTableSQL = "SELECT VkId from MainTable";
        assert conn != null;
        Statement statement = conn.createStatement();
        ResultSet rs = statement.executeQuery(selectTableSQL);

        while (rs.next()) {
            int VkId = rs.getInt("VkId");
            if (VkId == ID){
                conn.close();
                return true;
            }
        }
        rs = statement.executeQuery("SELECT VkId FROM AdminTable");
        while (rs.next()) {
            int VkId = rs.getInt("VkId");
            if (VkId == ID){
                conn.close();
                return true;
            }
        }

        conn.close();
        return false;
    }

    public static ArrayList<String> GetUserActs(int ID) throws SQLException {

        Connection conn = null;

        try {
            conn = getConnection();
        } catch (Exception e) {
            e.printStackTrace();
        }

        String selectTableSQL = "SELECT name from Acts";
        assert conn != null;
        Statement statement = conn.createStatement();
        ResultSet rs = statement.executeQuery(selectTableSQL);

        ArrayList<String> acts = new ArrayList<>();

        while (rs.next()) {
            String act = rs.getString("name");
            acts.add(act);
        }

        for( String i : acts ){
            selectTableSQL = "SELECT VkId FROM " + i;
            rs = statement.executeQuery(selectTableSQL);

            while (rs.next()){
                ArrayList<Integer> temparr = new ArrayList<>();
                int VkId = rs.getInt("VkId");

                if (VkId == ID){
                    acts.add(i);
                }
            }
        }

        conn.close();
        return acts;
    }

    public static void Registration(int ID) throws SQLException {
        Connection conn = null;

        try {
            conn = getConnection();
        } catch (Exception e) {
            e.printStackTrace();
        }

        assert conn != null;
        Statement statement = conn.createStatement();

        String InsertTableSQL = "INSERT INTO MainTable VALUES"
                + "('0', " + ID + ")";

        statement.executeUpdate(InsertTableSQL);
        conn.close();
    }

    private static int GetTGID(int ID) throws SQLException {
        Connection conn = null;
        int TGID = 0;
        try {
            conn = getConnection();
        } catch (Exception e) {
            e.printStackTrace();
        }

        assert conn != null;
        Statement statement = conn.createStatement();
        String selectTableSQL = "SELECT VkId, TgId from MainTable";
        ResultSet rs = statement.executeQuery(selectTableSQL);

        while (rs.next()) {

            int VkId = rs.getInt("VkId");
            if (VkId == ID) {
                try {
                    TGID = rs.getInt("TgId");
                }catch(Exception e){
                    System.out.print(e);
                }
                if (TGID != 0) {
                    conn.close();
                    return TGID;
                }
            }
        }
        conn.close();
        return 0;
    }


    public static void subscribe(int ID, String act) throws SQLException{
        Connection conn = null;

        try {
            conn = getConnection();
        } catch (Exception e) {
            e.printStackTrace();
        }

        assert conn != null;
        Statement statement = conn.createStatement();
        String InsertTableSQL = "INSERT INTO " + act.replace(' ', '_') + " VALUES" +
                "('" + GetTGID(ID) + "', '" + ID + "')";

        statement.executeUpdate(InsertTableSQL);
        conn.close();
    }

    public static void desub(int ID, String act) throws SQLException{
        Connection conn = null;

        try {
            conn = getConnection();
        } catch (Exception e) {
            e.printStackTrace();
        }

        assert conn != null;

        Statement statement = conn.createStatement();

        String DeleteSql = "DELETE FROM" + act.replace(' ', '_') + " WHERE VkId = '" + ID + "'";

        statement.executeUpdate(DeleteSql);
        conn.close();

    }

}
