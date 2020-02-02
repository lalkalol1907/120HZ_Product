package Core.Commands;

import Core.Command;
import VK.VKManager;
import com.vk.api.sdk.objects.messages.Message;

import java.io.IOException;
import java.sql.SQLException;

import static DataBase.MainDataBase.Check;
import static DataBase.MainDataBase.Registration;

public class Register extends Command{

    public Register(String name) {
        super(name);
    }

    @Override
    public void exec(Message message) throws SQLException {
        if (!Check(message.getUserId())) {
            new VKManager().sendMessage("Введи код приглашения:", message.getUserId(),"");
        }

        else {
            new VKManager().sendMessage("Ты уже зарегистрирован!", message.getUserId(),"");
        }
    }

}
