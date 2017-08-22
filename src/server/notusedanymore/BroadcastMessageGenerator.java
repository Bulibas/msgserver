package server.notusedanymore;

import server.beans.User;
import server.v2.UsersManager;

import java.util.Hashtable;
import java.util.Iterator;

import java.util.concurrent.LinkedBlockingQueue;

/**
 * Created by aatanasov on 8/15/2017.
 */
//TODO move to MessageGenerator
public class BroadcastMessageGenerator implements Runnable{

    private LinkedBlockingQueue<byte[]> messages = new LinkedBlockingQueue<>();

    private Hashtable<Integer, User> users;

    @Override
    public void run() {
       // users = UsersManager.getUsers();
        while (true) {
            byte[] msg;
            try {
                msg = messages.take();
                Iterator i = users.values().iterator();
                while (i.hasNext()) {
                    User user = (User) i.next();
                    //MessageGenerator.generatePersonalMessage(user, msg);
                    /*try {
                        user.getWriter().write(msg);
                    } catch (IOException e) {
                        e.printStackTrace();
                    }*/
                }
            } catch (InterruptedException e) {
                e.printStackTrace();
            }

        }
    }

    public void generateBroadcast(byte[] message) {
        messages.add(message);
    }
}
