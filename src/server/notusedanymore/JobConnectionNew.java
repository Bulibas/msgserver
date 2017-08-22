package server.notusedanymore;

import server.beans.Connection;

import java.io.*;
import java.net.Socket;
import java.util.Iterator;
import java.util.Queue;

/**
 * Created by aatanasov on 8/10/2017.
 */
public class JobConnectionNew extends ProtocolInternalJob {

    private Socket socket;

    private PrintStream printStream;

    private BufferedReader reader;

    private Queue<Connection> connections;

    private byte[] message;

    public JobConnectionNew(byte[] message, Socket socket, Queue<Connection> connections) throws IOException {
        this.message = message;
        this.socket = socket;
        this.connections = connections;
        printStream = new PrintStream(socket.getOutputStream());
        reader = new BufferedReader(new InputStreamReader(socket.getInputStream()));
    }

    @Override
    public void run() {
        System.out.println("[NEW_CONNECTION JOB RUNNING]");

        sendToAll();
    }

    private void sendToAll() {
        Iterator iterator = connections.iterator();
        while (iterator.hasNext()) {
            Connection conn = (Connection) iterator.next();
            PrintStream ps = conn.getWriter();
            ps.println(socket.getInetAddress());
        }
    }
}
