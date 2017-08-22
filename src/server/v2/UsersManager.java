package server.v2;

import server.beans.User;

import java.io.IOException;
import java.net.Socket;
import java.util.*;

/**
 * Created by aatanasov on 8/15/2017.
 */
//TODO
public class UsersManager {

    private volatile int idCounter = 1;

    private Hashtable<Integer, User> idToUser = new Hashtable<>();

    private volatile int byteListLength = 0;

    private static UsersManager instance = new UsersManager();

    private UsersManager() {
        //singleton
    }

    public static UsersManager getInstance() {
        return instance;
    }

    public User createUser(Socket socket, byte[] requestedUsername) {
        User user = null;
        try {
            user = new User(socket, requestedUsername, idCounter);
            idToUser.put(user.getUserId(), user);
            idCounter++;
            byteListLength += 4 + requestedUsername.length;
        } catch (IOException e) {
            e.printStackTrace();
        }

        return user;
    }

    public Hashtable<Integer, User> getUsers() {
        return idToUser;
    }

    //TODO ADD MAINTAINER THREAD FOR KEEPING A BYTE ARRAY ON TRACK AND NOT INITIALIZING EVERY TIME A GET USERS COMMAND IS SENT

    public byte[] getUsersAsBytes() {
        System.out.println("bytelenght = " + byteListLength);
        byte[] arr = new byte[2 + byteListLength];
        Set<Map.Entry<Integer, User>> entrySet = idToUser.entrySet();
        int i = 2;
        int id;
        byte[] username = null;

        arr[0] = (byte) (entrySet.size() >> 8);
        arr[1] = (byte) entrySet.size();
        for (Map.Entry<Integer, User> integerUserBeanEntry : entrySet) {
            id = integerUserBeanEntry.getKey();
            arr[i++] = (byte) (id >> 8);
            arr[i++] = (byte) id;
            username = integerUserBeanEntry.getValue().getUsername();
            arr[i++] = (byte) (username.length >> 8);
            arr[i++] = (byte) username.length;
            for (int j = 0; j < username.length; j++) {
                arr[i++] = username[j];
            }
        }
        return arr;
    }

    public User getUser(int id) {
        return idToUser.get(id);
    }

    public void deleteUser(int id) {
        idToUser.remove(id);
    }

}
