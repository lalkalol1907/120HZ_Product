package Keyboards;

import DataBase.ActsDataBase;
import DataBase.MainDataBase;

import java.sql.SQLException;
import java.util.ArrayList;

public class DesubKbd {

    public static String Desubkbd(int ID) throws SQLException {
        String kbd = "{\n" +
                "  \"one_time\": false,\n" +
                "  \"buttons\": [\n" +
                "      [{\n" +
                "        \"action\": {\n" +
                "          \"type\": \"text\",\n" +
                "          \"payload\": \"{\\\"button\\\": \\\"1\\\"}\",\n" +
                "          \"label\": \"Назад\"\n" +
                "        },\n" +
                "        \"color\": \"negative\"\n" +
                "      }],\n";
        ArrayList<String> Allacts = ActsDataBase.GetAllActs();

        ArrayList<String> Useracts = MainDataBase.GetUserActs(ID);

        for(int i = 0; i < Useracts.size();i++){

            if (Useracts.get(i) != null) {
                kbd += "      [{\n" +
                        "        \"action\": {\n" +
                        "          \"type\": \"text\",\n" +
                        "          \"payload\": \"{\\\"button\\\": \\\"1\\\"}\",\n" +
                        "          \"label\": \"|" + Useracts.get(i) + "\"\n" +
                        "        },\n" +
                        "        \"color\": \"positive\"\n";
                if (i == Useracts.size() - 1) {
                    kbd += "      }]\n";
                } else {
                    kbd += "      }],\n";
                }
            }
        }

        kbd+="  ]\n" +
                "}";
        return kbd;
    }
}
