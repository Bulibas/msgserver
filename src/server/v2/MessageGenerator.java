package server.v2;

import server.beans.Connection;
import server.beans.Message;
import server.beans.User;
import server.util.ProtocolCommands;

import java.io.PrintStream;
import java.util.Arrays;
import java.util.Iterator;
import java.util.Map;

/**
 * Created by aatanasov on 8/16/2017.
 */

    //todo add method for HEADER generation

    //todo create interface with core message lengths which should be used in byte[] msg initialization
public class MessageGenerator {

    //TODO ADD GENERATION METHODS FOR ALL MESSAGES

    private static MessageGenerator instance = new MessageGenerator();

    private static GeneratorExecutor generatorExecutor = GeneratorExecutor.getInstance();

    private static Map<Integer, User> idToUser;

    private static MessageSender messageSender = MessageSender.getInstance();

    private static UsersManager usersManager = UsersManager.getInstance();

    private MessageGenerator() {
        //singleton
    }

    public static MessageGenerator getInstance() {
        idToUser = usersManager.getUsers();
        return instance;
    }

    public void generateLoginResponseSuccess(User user) {
        generatorExecutor.addJob(new Runnable() {
            @Override
            public void run() {
                byte[] response = new byte[8];
                //setting magic number
                response[0] = 2;
                response[1] = 2;
                response[2] = 2;
                //setting message id
                response[3] = 0;
                response[4] = 0;
                //setting command id
                response[5] = 2;
                //setting user id
                response[6] = (byte) (user.getUserId() >> 8);
                response[7] = (byte) user.getUserId();
                Message msg = new Message(user.getWriter(), response);
                messageSender.sendMessage(msg);
            }
        });

    }

    public void generateLoginResponseFailure(PrintStream ps) {
        generatorExecutor.addJob(new Runnable() {
            @Override
            public void run() {
                byte[] response = new byte[8];
                //setting magic number
                response[0] = 2;
                response[1] = 2;
                response[2] = 2;
                //setting message id
                response[3] = 0;
                response[4] = 0;
                //setting command id
                response[5] = 2;
                //setting user id
                response[6] = (byte) 0;
                response[7] = (byte) 0;

                Message msg = new Message(ps, response);
                messageSender.sendMessage(msg);
            }
        });

    }

    public void generateUserLoggedMessage(int id, byte[] username) {
        generatorExecutor.addJob(new Runnable() {
            @Override
            public void run() {
                byte[] msg = new byte[9 + 2 + username.length];
                msg[0] = 2;
                msg[1] = 2;
                msg[2] = 2;
                msg[3] = 0;
                msg[4] = 0;
                msg[5] = 7; //command user logged
                msg[6] = 0; //not correct destination
                msg[7] = 0; //
                msg[8] = ((byte) (id >> 8));
                msg[9] = (byte) id;
                //msg[10] = ((byte) ((byte)username.length >> 8));
                msg[10] = (byte) username.length;
                System.arraycopy(username, 0, msg, 11, username.length);

               generateBroadcast(msg);
            }
        });
    }

    public void generatePersonalMessage(User user, byte[] message) {
        Message msg = new Message(user.getWriter(), message);
        MessageSender.getInstance().sendMessage(msg);
    }

    public void generateGroupMessage(byte[] message) {
        generatorExecutor.addJob(new Runnable() {
            @Override
            public void run() {
                generateBroadcast(message);
            }
        });
    }


    public void generateUsersReport(Connection connection) {
        byte[] data = usersManager.getUsersAsBytes();
        byte[] response = new byte[data.length + 8];
        response[0] = 2;
        response[1] = 2;
        response[2] = 2;
        response[3] = 0;
        response[4] = 0;
        response[5] = 6; //command id
        response[6] = 0;
        response[7] = 0;
        for (int i = 0; i < data.length; i++) {
            response[i + 8] = data[i];
        }

        Message message = new Message(connection.getWriter(), response);
        messageSender.sendMessage(message);
    }

    public void generateFailure(byte type, PrintStream ps, int msgId) {
        byte[] msg = new byte[11];
        msg[0] = 2;
        msg[1] = 2;
        msg[2] = 2;
        msg[3] = 0;
        msg[4] = 0;
        msg[5] = ProtocolCommands.COMMAND_FAILURE; //command user logged
        msg[6] = 0; //not correct destination
        msg[7] = 0; //
        msg[8] = ((byte) ((byte)msgId >> 8));
        msg[9] = (byte) msgId;
        //msg[10] = ((byte) ((byte)username.length >> 8));
        msg[10] = type;

        Message message = new Message(ps, msg);
        messageSender.sendMessage(message);
    }

    /**
     * Used if a message should be sent to every connection -
     * in case of NEW_USER_LOGGED, USER_LOGGED_OUT or GROUP_MESSAGE.
     */
    private void generateBroadcast(byte[] msg) {
        Iterator i = idToUser.values().iterator();
        while (i.hasNext()) {
            User user = (User) i.next();
            instance.generatePersonalMessage(user, msg);
        }
    }


}
