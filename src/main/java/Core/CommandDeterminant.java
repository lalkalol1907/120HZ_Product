package Core;

import Core.Commands.Unknown;
import com.vk.api.sdk.objects.messages.Message;

import java.util.Collection;
import java.util.HashSet;

public class CommandDeterminant {


    public static Command getCommand(Collection<Command> commands, Message message) {
        String body = message.getBody();

        for (Command command : commands
        ) {
            if (command.name.equals(body)) {
                return command;
            }
        }

        return new Unknown("unknown");
    }

}