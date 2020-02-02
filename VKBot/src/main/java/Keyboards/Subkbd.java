package Keyboards;

import DataBase.ActsDataBase;
import DataBase.MainDataBase;

import java.sql.SQLException;
import java.util.ArrayList;

public class Subkbd {

    public static String Subkbd() throws SQLException {
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


        for(int i = 0; i < Allacts.size();i++){
            kbd += "      [{\n" +
                    "        \"action\": {\n" +
                    "          \"type\": \"text\",\n" +
                    "          \"payload\": \"{\\\"button\\\": \\\"1\\\"}\",\n" +
                    "          \"label\": \"!" + Allacts.get(i) + "\"\n" +
                    "        },\n" +
                    "        \"color\": \"positive\"\n";
            if (i == Allacts.size()-1) {
                kbd += "      }]\n";
            }
            else{
                kbd += "      }],\n";
            }
        }

        kbd+="  ]\n" +
                "}";
        return kbd;
    }
}
