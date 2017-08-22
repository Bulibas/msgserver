package server.util;

import java.io.OutputStream;

/**
 * Created by aatanasov on 8/7/2017.
 */

//TODO MOVE IN CLIENT
public interface LocalMessageTags {

    String TAG_INFO = "[INFO]";

    String TAG_ERROR = "[ERROR]";

    String TAG_SERVER_STARTED = "[SERVER STARTED]";

    String TAG_SERVER_STOPPED = "[SERVER STOPPED]";

    String TAG_SERVER_STARTING = "[SERVER STARTING]";

    String TAG_USER_CONNECTED = "[USER CONNECTED]";

    String TAG_USER_DISCONNECTED = "[USER DISCONNECTED]";

    String TAG_MESSAGE_SUCCESSFUL = "[MESSAGE SENT]";

    String TAG_MESSAGE_FAILED = "[MESSAGE FAILED";

    String TAG_CONNECTION_ERROR = "[SOCKET CONNECTION FAILURE]";

    void printMessage(OutputStream os);

}
