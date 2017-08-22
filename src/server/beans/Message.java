package server.beans;

import java.io.IOException;
import java.io.OutputStream;
import java.io.PrintStream;
import java.util.Arrays;

/**
 * Created by aatanasov on 8/17/2017.
 */

//TODO ADD MESSAGE ID AND SEND MSG_SEND_COMMAND TO THE SENDER

public class Message {
    private PrintStream writer;

    private byte[] message;

    public Message(PrintStream writer, byte[] message) {
        this.writer = writer;
        this.message = message;
    }

    public OutputStream getWriter() {
        return writer;
    }

    public byte[] getMessage() {
        return message;
    }

    public void send() throws IOException {
        writer.write(message);
        System.out.println("sending: " + Arrays.toString(message));
        //TODO ADD MESSAGE ID AND SEND MSG_SEND_COMMAND TO THE SENDER
    }
}
