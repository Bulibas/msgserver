package server.notusedanymore;

/**
 * Created by aatanasov on 8/10/2017.
 */
public class JobMessageGroup extends ProtocolInternalJob {

    public JobMessageGroup(byte[] message) {
        this.message = message;
    }

    @Override
    public void run() {

    }
}
