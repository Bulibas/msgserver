package server.notusedanymore;

import server.util.ProtocolCommands;

/**
 * Created by aatanasov on 8/8/2017.
 */
public class JobGenerator {

    private static final int COMMAND_BYTE_ORDER = 5; //TODO to be checked

    private static JobExecutor jobExecutor = JobExecutor.getInstance();

    public JobGenerator(JobExecutor jobExecutor) {
        this.jobExecutor = jobExecutor;
    }

    public static void generateJob(byte[] message) {

        Runnable job = null;
        byte commandId = message[COMMAND_BYTE_ORDER];

        switch (commandId) {
           // case ProtocolCommands.COMMAND_TRY_LOGIN:
           //    notusedanymore = new JobLoginResponse(message);
           //       break;
            case ProtocolCommands.COMMAND_MESSAGE_PERSONAL:
                job = new JobMessagePersonal(message);
                break;
            case ProtocolCommands.COMMAND_MESSAGE_GROUP:
                job = new JobMessageGroup(message);
                break;
            //case ProtocolCommands.COMMAND_USER_LOGGED:
            //    notusedanymore = new JobConnectionNew(message);
            //    break;
            case ProtocolCommands.COMMAND_USER_LOGGED_OUT:
                job = new JobConnectionBroken();
                break;
            case ProtocolCommands.COMMAND_MESSAGE_RECEIVED:
                job = new JobMessageReceived(message);
                break;
            case ProtocolCommands.COMMAND_GET_USERS:
                job = new JobUsersRequest(message);
                break;
        }

        jobExecutor.addJob(job);
    }

    //TODO to be called for check when logic is good
    private boolean checkMessage(byte[] message) {
        return //todo
                checkMessageSource(message);
    }

    private boolean checkMessageSource(byte[] message) {
        //TODO to check if the message source is corresponding to an IP, if command is not TRY LOGIN - security

        return true;
    }
}
