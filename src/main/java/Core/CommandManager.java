package Core;

import Core.Commands.*;
import Core.Commands.UserCommands.Desub;
import Core.Commands.UserCommands.Showacts;
import Core.Commands.UserCommands.Subscribe;
import Functions.CodeDostavatel;

import java.io.IOException;
import java.sql.SQLException;
import java.util.HashSet;

public class CommandManager {
    private static HashSet<Command> commands = new HashSet<>();

    static {
        commands.add(new Unknown("unknown"));
        commands.add(new Showacts("Покажи мои активности"));
        commands.add(new Subscribe("Подписаться на активность"));
        commands.add(new Desub("Отписаться от активности"));
        commands.add(new Register("Регистрация"));
        commands.add(new Start("start"));
        commands.add(new Back("Назад"));
        try {
            commands.add(new Code(Integer.toString(CodeDostavatel.coded())));
        } catch (IOException | SQLException e) {
            e.printStackTrace();
        }
    }

    public static HashSet<Command> getCommands(){
        return commands;
    }
    public static void addCommand(Command command) { commands.add(command);}
}