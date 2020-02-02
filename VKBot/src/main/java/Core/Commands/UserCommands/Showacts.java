package Core.Commands.UserCommands;

import Keyboards.PupilKeyboard;
import VK.VKManager;
import Core.Command;
import com.vk.api.sdk.objects.messages.Message;
import org.apache.commons.collections4.CollectionUtils;

import java.io.IOException;
import java.sql.SQLException;
import java.util.ArrayList;

import static DataBase.ActsDataBase.GetAllActs;
import static DataBase.MainDataBase.Check;
import static DataBase.MainDataBase.GetUserActs;

public class Showacts extends Command{

    public Showacts(String name) {
        super(name);
    }

    @Override
    public void exec(Message message) throws SQLException {
        if (Check(message.getUserId())) {
            String res = "";
            ArrayList<String> list = GetUserActs(message.getUserId());
            if (!(CollectionUtils.isNotEmpty(list))) {
                new VKManager().sendMessage("У тебя нет активностей", message.getUserId(),PupilKeyboard.PupilKbd());
            }
            else {

                for (int i = 0; i < list.size(); i++) {
                    String value = list.get(i);
                    res += value + "\n";
                }

                new VKManager().sendMessage(res, message.getUserId(),PupilKeyboard.PupilKbd());
            }//
        }
        else {
            new VKManager().sendMessage("Неизвестная команда", message.getUserId(), PupilKeyboard.PupilKbd());
        }
    }
}
