import Functions.MessageSender;
import VK.VKCore;
import VK.VKServer;

public class Starter {

    public static void main(String[] args) throws InterruptedException {
        new VKServer("VkServer").start();


        while (true) {
            new MessageSender("MessageSender").start();
            Thread.sleep(5000);
        }

    }
}
