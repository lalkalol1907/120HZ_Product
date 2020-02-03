package Core.Commands.UserCommands;

import Core.Command;
import Keyboards.Nonamekbd;
import Keyboards.Subkbd;
import VK.VKManager;
import com.vk.api.sdk.objects.messages.Message;

import java.io.IOException;
import java.sql.SQLException;

import static DataBase.MainDataBase.Check;

public class Subscribe extends Command {

    public Subscribe(String name) {
        super(name);
    }

    @Override
    public void exec(Message message) throws SQLException {
        if (Check(message.getUserId())) {
            new VKManager().sendMessage("Нажми на активность, на которую хочешь подписаться:", message.getUserId(),Subkbd.Subkbd()); //
        }
        else {
            new VKManager().sendMessage("Неизвестная команда", message.getUserId(), Nonamekbd.NonameKbd());
        }
    }
}
