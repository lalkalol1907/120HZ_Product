package Functions;

import Keyboards.PupilKeyboard;
import VK.VKManager;
import com.vk.api.sdk.objects.messages.Message;

import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.ArrayList;

import static DataBase.JDBC.getConnection;

public class MessageSender extends Thread {

    public MessageSender(String name){
        super(name);
    }

    private static ArrayList<Integer> GetVks(String act) throws SQLException {
        String Sql = "";
        if (act.equals("Всем")){
            Sql = "SELECT VkId FROM MainTable";
        }
        else {
            Sql = "SELECT VkId FROM " + act.replace(' ', '_');
        }
        ArrayList<Integer> res = new ArrayList<>();

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
            int a = rs.getInt("VkId");
            if (a != 0) {
                res.add(a);
            }
        }
        return res;

    }

    public void run(){
        try {
            Send();
        } catch (SQLException | InterruptedException e) {
            e.printStackTrace();
        }
    }

    public static void Send() throws SQLException, InterruptedException {
        String SqlQ = "SELECT * FROM SendList";

        Connection conn = null;

        try {
            conn = getConnection();
        } catch (Exception e) {
            e.printStackTrace();
        }

        assert conn != null;
        Statement statement = conn.createStatement();
        ResultSet rs = statement.executeQuery(SqlQ);

        while (rs.next()) {
            int Done = rs.getInt("DoneVk");
            if (Done == 0) {
                int id = rs.getInt("id");
                String act = rs.getString("act");
                String text = rs.getString("text");
                ArrayList<Integer> Vks = GetVks(act);
                String sqld = "UPDATE SendList SET DoneVk = '1' WHERE id = '" + id + "'";
                statement.executeUpdate(sqld);
                for (int i : Vks) {
                    try {
                        if (i != 0) {
                            new VKManager().sendMessage(text + "\n\nДля активности: «" + act + "»", i, PupilKeyboard.PupilKbd());
                        }
                    } catch (Exception e) {
                        e.printStackTrace();
                        conn.close();
                    }
                }
            }
        }
        try {
            conn.close();
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }
}
