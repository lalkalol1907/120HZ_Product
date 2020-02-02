package Core.Commands;

import Core.Command;
import DataBase.MainDataBase;
import Keyboards.Nonamekbd;
import Keyboards.PupilKeyboard;
import VK.VKManager;
import com.vk.api.sdk.objects.messages.Message;

import java.sql.SQLException;

public class Back extends Command {

    public Back(String name){
        super(name);
    }

    @Override
    public void exec(Message message) throws SQLException {
        if (MainDataBase.Check(message.getUserId())){
            new VKManager().sendMessage("Вы вернулись назад", message.getUserId(), PupilKeyboard.PupilKbd());
        }
        else {
            new VKManager().sendMessage("Вы вернулись назад", message.getUserId(), Nonamekbd.NonameKbd()); //
        }
    }

}
