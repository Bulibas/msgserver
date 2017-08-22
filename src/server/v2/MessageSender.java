package server.v2;

import server.beans.Message;

import java.io.IOException;
import java.util.Queue;
import java.util.concurrent.LinkedBlockingQueue;

import static javafx.scene.input.KeyCode.Q;

/**
 * Created by aatanasov on 8/15/2017.
 */
public class MessageSender implements Runnable{

    private static LinkedBlockingQueue<Message> messageQueue = new LinkedBlockingQueue<>();

    private static MessageSender instance = new MessageSender();

    private MessageSender() {
        //singleton
    }

    public static MessageSender getInstance() {
        return instance;
    }

    @Override
    public void run() {
        while(true) {
            Message msg = null;
            try {
                msg = messageQueue.take();
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
            try {
                msg.send();
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
    }

    public void sendMessage(Message msg) {
        messageQueue.add(msg);
    }
}
