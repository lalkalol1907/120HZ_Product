package Core.Commands;

import Core.Command;
import DataBase.MainDataBase;
import Keyboards.Nonamekbd;
import Keyboards.PupilKeyboard;
import VK.VKManager;
import com.vk.api.sdk.objects.messages.Message;

import java.sql.SQLException;

public class Start extends Command {

    public Start(String name) {
        super(name);
    }

    @Override
    public void exec(Message message) throws SQLException {
        if (MainDataBase.Check(message.getUserId())) {
            new VKManager().sendMessage("Вот функции, доступные тебе:", message.getUserId(), PupilKeyboard.PupilKbd()); //
        }
        else{
            new VKManager().sendMessage("Вот функции, доступные тебе:", message.getUserId(), Nonamekbd.NonameKbd()); //

        }
    }
}
