package server.core;

import server.util.LocalMessageTags;
import server.beans.Connection;
import server.beans.User;
import server.v2.MessageHandlerV2;
import server.v2.MessageSender;

import java.io.*;
import java.net.ServerSocket;
import java.net.Socket;
import java.util.*;
import java.util.concurrent.*;

/**
 * Created by aatanasov on 8/7/2017.
 */
public class ChatServer implements Runnable {

    private static final Integer PORT = 9090;

    private static List<User> users = new LinkedList<>();

    private static LinkedBlockingQueue<Connection> connections = new LinkedBlockingQueue(); //TODO rethink the best structure

    private MessageHandlerV2 messageHandler;

    private MessageSender messageSender;

    private ServerSocket server;

    @Override
    public void run() {
        System.out.println(LocalMessageTags.TAG_SERVER_STARTING);
        boolean opened = openServer(PORT);

        if(opened) {
            messageHandler = new MessageHandlerV2(connections);
            new Thread(messageHandler).start();

            messageSender = MessageSender.getInstance();
            new Thread(messageSender).start();

            try {
                startServer(server);
            } catch (IOException e) {
                e.printStackTrace();
            } catch (InterruptedException e)  {
                e.printStackTrace();
            }
        }
    }

    private void startServer(ServerSocket server) throws IOException, InterruptedException {

        while(true) {
            Socket socket = server.accept();
            Connection connection = new Connection(socket);
            connections.put(connection);
        }
    }

    private boolean openServer(final int port) {
        try {
            server = new ServerSocket(port);
            return true;
        } catch (IOException e) {
            e.printStackTrace();
            return false;
        }
    }
}
