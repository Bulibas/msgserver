package server.notusedanymore;

/**
 * Created by aatanasov on 8/10/2017.
 */
public class JobMessagePersonal extends ProtocolInternalJob {

    public JobMessagePersonal(byte[] message) {
        this.message = message;
    }

    @Override
    public void run() {

    }
}
