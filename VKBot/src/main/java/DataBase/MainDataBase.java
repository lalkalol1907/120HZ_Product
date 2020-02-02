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

        String selectTableSQL = "SELECT VkId, Man from MainTableV1";
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

        String selectTableSQL = "SELECT VkId, acts from MainTableV1";
        assert conn != null;
        Statement statement = conn.createStatement();
        ResultSet rs = statement.executeQuery(selectTableSQL);

        ArrayList<String> acts = new ArrayList<>();

        while (rs.next()) {

            int VkId = rs.getInt("VkId");
            if (VkId == ID) {
                String act = rs.getString("acts");
                if (!act.equals("") && !act.equals(" ") && !act.equals("0")) {
                    acts.add(act);
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

        String InsertTableSQL = "INSERT INTO MainTableV1 VALUES"
                + "('0', 'pupil','', " + ID + ")";

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
        String selectTableSQL = "SELECT VkId, TgId from MainTableV1";
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

    private static String typeidenty(int ID) throws SQLException {
        Connection conn = null;
        String Man = "pupil";

        try{
            conn = getConnection();
        } catch (Exception e) {
            e.printStackTrace();
        }

        String selectTableSQL = "SELECT VkId, Man from MainTableV1";
        assert conn != null;
        Statement statement = conn.createStatement();
        ResultSet rs = statement.executeQuery(selectTableSQL);

        while (rs.next()) {
            int VkId = rs.getInt("VkId");
            if (VkId == ID){
                Man = rs.getString("Man");
            }
        }
        conn.close();
        return (Man);
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
        String InsertTableSQL = "INSERT INTO MainTableV1 VALUES" +
                "('" + GetTGID(ID) + "', '"+ typeidenty(ID)+"', '" + act + "', '" + ID + "')";

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

        String DeleteSql = "UPDATE MainTableV1 SET acts = '' WHERE VkId = '" + ID + "' AND acts = '" + act + "'";

        statement.executeUpdate(DeleteSql);
        conn.close();

    }

}
