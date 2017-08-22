package server.notusedanymore;

import server.beans.User;

import java.util.Map;

/**
 * Created by aatanasov on 8/15/2017.
 */
public class JobLoginResponse extends ProtocolInternalJob {

    private byte[] message;

    private Map<String, User> users;

    public JobLoginResponse(byte[] message, Map<String, User> users) {
        this.message = message;
        this.users = users;
    }

    @Override
    public void run() {

    }
}
