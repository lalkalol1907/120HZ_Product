package VK;

import Functions.MessageSender;
import com.vk.api.sdk.exceptions.ApiException;
import com.vk.api.sdk.exceptions.ClientException;
import com.vk.api.sdk.objects.messages.Message;

import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;


public class VKServer extends Thread {

    public VKServer(String name){
        super(name);
    }

    public static VKCore vkCore;
    static {
        try {
            vkCore = new VKCore();
        } catch (ApiException | ClientException e) {
            e.printStackTrace();
        }
    }

    public void run(){
        try {
            VKstart();
        } catch (ApiException | ClassNotFoundException | InterruptedException e) {
            e.printStackTrace();
        }
    }

    public static void VKstart() throws NullPointerException, ApiException, InterruptedException, ClassNotFoundException {

        System.out.println("Running server...");
        Class.forName("com.mysql.cj.jdbc.Driver");
        while (true) {
            Thread.sleep(300);
            try {
                Message message = vkCore.getMessage();
                if (message != null) {
                    ExecutorService exec = Executors.newCachedThreadPool();
                    exec.execute(new Messenger(message));
                }

            } catch (ClientException e) {
                System.out.println("Возникли проблемы");
                final int RECONNECT_TIME = 10000;
                System.out.println("Повторное соединение через " + RECONNECT_TIME / 1000 + " секунд");
                Thread.sleep(RECONNECT_TIME);

            }

        }
    }
}