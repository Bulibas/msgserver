package server.v2;

import server.beans.Connection;
import server.beans.User;
import server.util.LocalMessageTags;
import server.util.ProtocolCommands;
import server.util.FailureTypes;

import java.io.IOException;
import java.util.*;

/**
 * Created by aatanasov on 8/7/2017.
 */

//TODO RESTRICT IPs WHICH SEND MULTIPLE UNPARSABLE MESSAGES

public class MessageHandlerV2 implements Runnable {

    private static final int ITERATION_TIME_INTERVAL = 50; //in milliseconds

    private HandlerExecutor executor = HandlerExecutor.getInstance();

    private MessageGenerator messageGenerator = MessageGenerator.getInstance();

    private UsersManager usersManager = UsersManager.getInstance();

    private Map<Integer, User> idToUser;

    private Queue<Connection> connections;

    public MessageHandlerV2(Queue<Connection> connections) {
        this.connections = connections;
    }

    @Override
    public void run() {
        idToUser = usersManager.getUsers();

        Iterator connectionsIterator;
        while (true) {
            connectionsIterator = connections.iterator();
            while (connectionsIterator.hasNext()) {
                Connection connection = (Connection) connectionsIterator.next();
                try {
                    int n = connection.getSocket().getInputStream().available();
                    if (n > 0) {
                        byte[] data = new byte[n];
                        connection.getSocket().getInputStream().read(data, 0, data.length);
                        handleMessage(data, connection);
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

    private void handleMessage(byte[] message, Connection connection) {
        if (checkProtocolMagicNumber(message)) {
            System.out.println("GOOD");
            Runnable job = new RunnableHandler(message, connection);
            executor.addJob(job);
        } else {
            System.out.println("NOT GOOD");
        }
    }

    private boolean checkProtocolMagicNumber(byte[] message) {
        if (message.length > 3 &&
                message[0] == 2 &&
                message[1] == 2 &&
                message[2] == 2) {
            return true;
        }
        return false;
        //BREAK CURRENT CONNECTION IF FALSE
    }


    //TODO DELETE THIS
    private boolean userExists(byte[] requestedUsername) {
        Iterator i = idToUser.values().iterator();
        while (i.hasNext()) {
            User u = (User) i.next();
            if(Arrays.equals(u.getUsername(), requestedUsername)) {
                return true;
            }
        }

        return false;
    }

    public class RunnableHandler implements Runnable {

        private static final int COMMAND_BYTE_ORDER = 5;

        private byte[] message;

        private Connection connection;

        public RunnableHandler(byte[] message, Connection connection) {
            this.message = message;
            this.connection = connection;
        }

        @Override
        public void run() {
            byte commandId = message[COMMAND_BYTE_ORDER];
            System.out.println("COMMAND RECEIVED: " + commandId);

            //TODO CHECK IF ONLY ONE MESSAGE IS IN THE BYTE ARRAY - CHECK FOR MAGIC NUMBER

            switch (commandId) {
                case ProtocolCommands.COMMAND_TRY_LOGIN:
                    handleTryLogin(message, connection);
                    break;
                case ProtocolCommands.COMMAND_MESSAGE_PERSONAL:
                    handlePersonal(message, connection);
                    break;
                case ProtocolCommands.COMMAND_MESSAGE_GROUP:
                    handleGroup(message);
                    break;
                case ProtocolCommands.COMMAND_GET_USERS:
                    handleGetUsers(message, connection);
                    break;
            }
        }

        private void handleTryLogin(byte[] message, Connection connection) {
            byte length = message[6];
            byte[] requestedUsername = Arrays.copyOfRange(message, 7, 7 + length);

            if (userExists(requestedUsername)) {
                System.out.println("[USER ALREADY LOGGED]");
                messageGenerator.generateLoginResponseFailure(connection.getWriter());
            } else {
                System.out.println("[USER LOGIN SUCCESS]");
                User user = usersManager.createUser(connection.getSocket(), requestedUsername);
                connections.add(connection);

                messageGenerator.generateLoginResponseSuccess(user);
                messageGenerator.generateUserLoggedMessage(user.getUserId(), user.getUsername());
            }
        }

        private void handlePersonal(byte[] message, Connection connection) {
            //int src = ((message[6] << 8) << 0xFF00) | (message[7] & 0xFF); //TODO SOURCE NOT NEEDED EVENTUALLY
            int msgId = ((message[4] << 8) << 0xFF00) | (message[5] & 0xFF);
            int des = ((message[8] << 8) << 0xFF00) | (message[9] & 0xFF);
            //TODO security and validation check eventually
            User destUser = usersManager.getUser(des);
            if(destUser != null) {
                //int len = ((message[10] << 8) << 0xFF00) | (message[11] & 0xFF);
                //byte[] msg = Arrays.copyOfRange(message, 12, 12 + message.length);
                //byte[] toSend = new byte[];
                messageGenerator.generatePersonalMessage(destUser, message);
            } else {
                messageGenerator.generateFailure(FailureTypes.FAILURE_TYPE_USER_NOT_FOUND, connection.getWriter(), msgId);
                System.out.println("[NO SUCH USER FOUND]" + des);
            }
        }

        private void handleGroup(byte[] message) {
            messageGenerator.generateGroupMessage(message);
        }

        public void handleGetUsers(byte[] message, Connection connection) {
            messageGenerator.generateUsersReport(connection);
        }
    }
}
