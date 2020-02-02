package DataBase;

import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.ArrayList;

import static DataBase.JDBC.getConnection;

public class ActsDataBase {

    public static ArrayList<String> GetAllActs() throws SQLException {

        Connection conn = null;

        try{
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
        conn.close();
        return acts;
    }

}
