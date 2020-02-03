package Core.Commands;

import Core.Command;
import DataBase.ActsDataBase;
import DataBase.MainDataBase;
import Functions.ArrayChecker;
import Keyboards.Nonamekbd;
import Keyboards.PupilKeyboard;
import VK.VKManager;
import com.vk.api.sdk.objects.messages.Message;


import java.sql.SQLException;

public class Unknown extends Command {

    public Unknown(String name) {
        super(name);
    }

    @Override
    public void exec(Message message) throws SQLException {
        String act = "";
        String body = message.getBody();
        char[] text = body.toCharArray();
        if ((text[0] == '!') && (MainDataBase.Check(message.getUserId()))){  // Подписаться на активность
            for(int i = 1; i < text.length; i++){
                act += text[i];
            }
            if (ArrayChecker.ArrayCheck(ActsDataBase.GetAllActs(), act)){
                if (!(ArrayChecker.ArrayCheck(MainDataBase.GetUserActs(message.getUserId()), act))){
                   MainDataBase.subscribe(message.getUserId(), act);
                   new VKManager().sendMessage("Ты подписался на эту активность", message.getUserId(), PupilKeyboard.PupilKbd());
                }
                else{
                    new VKManager().sendMessage("Ты уже подписан на эту активность", message.getUserId(), PupilKeyboard.PupilKbd());
                }
            }
            else{
                new VKManager().sendMessage("Данной активности не существует", message.getUserId(), PupilKeyboard.PupilKbd());
            }
        }
        else {
            if ((text[0] == '|') && (MainDataBase.Check(message.getUserId()))){
                for(int i = 1;i<text.length;i++){
                    act+=text[i];
                }

                if (ArrayChecker.ArrayCheck(ActsDataBase.GetAllActs(), act)){
                    if (ArrayChecker.ArrayCheck(MainDataBase.GetUserActs(message.getUserId()), act)){
                        MainDataBase.desub(message.getUserId(), act);
                        new VKManager().sendMessage("Ты отписсался от этой активности", message.getUserId(), PupilKeyboard.PupilKbd());
                    }
                    else{
                        new VKManager().sendMessage("Ты не подписан на эту активность", message.getUserId(), PupilKeyboard.PupilKbd());
                    }
                }
                else{
                    new VKManager().sendMessage("Данной активности не существует", message.getUserId(), PupilKeyboard.PupilKbd());
                }
            }



        else{
                try{
                    int code = Integer.parseInt(body);
                    if (MainDataBase.Check(message.getUserId())){
                        new VKManager().sendMessage("Ты уже зарегистрирован", message.getUserId(), PupilKeyboard.PupilKbd());

                    }
                    else {
                        new VKManager().sendMessage("Неверный код", message.getUserId(), PupilKeyboard.PupilKbd());
                    }

                }
                catch (NumberFormatException e) {
                    if (MainDataBase.Check(message.getUserId())) {
                        new VKManager().sendMessage("Неизвестная команда", message.getUserId(), PupilKeyboard.PupilKbd());
                    }
                    else{
                        new VKManager().sendMessage("Неизвестная команда", message.getUserId(), Nonamekbd.NonameKbd());
                    }
                }
            }
        }
    }
}