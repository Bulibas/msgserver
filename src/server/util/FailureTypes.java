package server.util;

/**
 * Created by aatanasov on 8/17/2017.
 */
public interface FailureTypes {

    byte FAILURE_TYPE_SERVER_ERROR = 1;

    byte FAILURE_TYPE_USER_NOT_FOUND = 2;

    byte FAILURE_TYPE_GROUP_NOT_FOUND = 3;

    byte FAILURE_TYPE_MSG_NOT_DELIVERED = 4;

}
