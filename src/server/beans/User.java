package server.beans;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.PrintStream;
import java.net.Socket;
import java.util.Arrays;

/**
 * Created by aatanasov on 8/9/2017.
 */
public class User {

    private Socket socket;

    private byte[] username;

    private int userId;

    private PrintStream writer;

    private BufferedReader reader;

    public User(Socket socket, byte[] username, int userId) throws IOException {
        this.socket = socket;
        this.username = username;
        this.userId = userId;
        writer = new PrintStream(socket.getOutputStream());
    }

    public Socket getSocket() {
        return socket;
    }

    public byte[] getUsername() {
        return username;
    }

    public int getUserId() {
        return userId;
    }

    public PrintStream getWriter() {
        return writer;
    }

    public BufferedReader getReader() {
        return reader;
    }

    @Override
    public boolean equals(Object obj) {
        return Arrays.equals(((User)obj).getUsername(), username);
    }
}
