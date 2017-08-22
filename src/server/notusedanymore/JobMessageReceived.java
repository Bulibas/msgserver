package server.notusedanymore;

/**
 * Created by aatanasov on 8/11/2017.
 */
public class JobMessageReceived extends ProtocolInternalJob{

    public JobMessageReceived(byte[] message) {
        this.message = message;
    }

    @Override
    public void run() {

    }
}
