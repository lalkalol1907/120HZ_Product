package VK;

import Core.Commander;
import com.vk.api.sdk.objects.messages.Message;

import java.io.IOException;
import java.sql.SQLException;

public class Messenger implements Runnable{

    private Message message;

    public Messenger(Message message){
        this.message = message;
    }

    @Override
    public void run() {
        try {
            Commander.execute(message);
        } catch (IOException | SQLException e) {
            e.printStackTrace();
        }
    }
}
