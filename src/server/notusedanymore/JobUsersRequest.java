package server.notusedanymore;

/**
 * Created by aatanasov on 8/11/2017.
 */
public class JobUsersRequest extends ProtocolInternalJob {

    public JobUsersRequest(byte[] message) {
        this.message = message;
    }

    @Override
    public void run() {

    }
}
