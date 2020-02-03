package Core.Commands.UserCommands;

import Core.Command;
import Keyboards.DesubKbd;
import Keyboards.Nonamekbd;
import Keyboards.PupilKeyboard;
import VK.VKManager;
import com.vk.api.sdk.objects.messages.Message;

import java.io.IOException;
import java.sql.SQLException;

import static DataBase.MainDataBase.Check;


public class Desub extends Command {

    public Desub(String name) {
        super(name);
    }

    @Override
    public void exec(Message message) throws SQLException {

        if (Check(message.getUserId())) {
            new VKManager().sendMessage("Выбери активность, от которой хочешь отписаться", message.getUserId(), DesubKbd.Desubkbd(message.getUserId())); //
        }
        else {
            new VKManager().sendMessage("Неизвестная команда", message.getUserId(), Nonamekbd.NonameKbd());
        }
    }
}