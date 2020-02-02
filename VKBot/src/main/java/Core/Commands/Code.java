package Core.Commands;

import Core.Command;
import DataBase.MainDataBase;
import Keyboards.PupilKeyboard;
import VK.VKManager;
import com.vk.api.sdk.objects.messages.Message;

import java.sql.SQLException;

public class Code extends Command {

    public Code(String name) {
        super(name);
    }

    @Override
    public void exec(Message message) throws SQLException {

        if (MainDataBase.Check(message.getUserId())){
            new VKManager().sendMessage("Ты уже зарегистрирован", message.getUserId(), PupilKeyboard.PupilKbd());
        }
        else {
            MainDataBase.Registration(message.getUserId());
            new VKManager().sendMessage("Успешная регистрация", message.getUserId(), PupilKeyboard.PupilKbd()); //
        }
    }

}
