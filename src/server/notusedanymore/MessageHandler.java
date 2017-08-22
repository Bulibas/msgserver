package server.notusedanymore;

import server.beans.User;
import server.util.LocalMessageTags;
import server.beans.Connection;

import java.io.IOException;
import java.util.*;

/**
 * Created by aatanasov on 8/7/2017.
 */

//TODO CLOSE CONNECTIONS AFTER GIVEN TIMEOUT WITHOUT TRY_LOGIN RECEIVED

//TODO RESTRICT IPs WHICH SEND MULTIPLE UNPARSABLE MESSAGES
public class MessageHandler implements Runnable {

    private static final int ITERATION_TIME_INTERVAL = 50; //in milliseconds

    private static JobExecutor executor = JobExecutor.getInstance();

    private static JobGenerator jobGenerator = new JobGenerator(executor);

    private static Queue<Connection> connections;

    private static List<User> users;

    public MessageHandler(Queue<Connection> connections, List<User> users) {
        this.connections = connections;
        this.users = users;
    }

    @Override
    public void run() {
        Iterator connectionsIterator;
        while (true) {
            connectionsIterator = connections.iterator();
            while (connectionsIterator.hasNext()) {
                Connection connection = (Connection) connectionsIterator.next();
                try {
                    //TODO REPAIR CONNECTION BEAN
                    int n = connection.getSocket().getInputStream().available();
                    if (n > 0) {
                        byte[] data = new byte[connection.getSocket().getInputStream().available()];
                        connection.getSocket().getInputStream().read(data, 0, data.length); //TODO WRAP THIS IN READ AND WRITE METHODS IN THE CONNECTION CLASS
                        handleMessage(data);
                        System.out.println(Arrays.toString(data));
                    }
                } catch (IOException e) {
                    e.printStackTrace();
                    System.out.println(LocalMessageTags.TAG_CONNECTION_ERROR);
                    connectionsIterator.remove();
                }
            }

            try {
                Thread.sleep(ITERATION_TIME_INTERVAL);
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
        }
    }

    public void handleMessage(byte[] message) {
        if (checkProtocolMagicNumber(message)) {
            jobGenerator.generateJob(message);
        }
    }

    private boolean checkProtocolMagicNumber(byte[] message) {
        if (message.length > 3 &&
                message[0] == 2 &&
                message[1] == 2 &&
                message[2] == 3) {
            return true;
        }
        return false;
        //BREAK CURRENT CONNECTION IF FALSE
    }
}
