package server.core;

/**
 * Created by aatanasov on 8/7/2017.
 */
public class ServerActivator {

    public static void main(String[] args) {
        Runnable server = new ChatServer();
        server.run();

        /*String ip = "192.168.18.107";                      //maybe to use for id generation
        String[] ipArray = ip.split("\\.");
        long ipDecimal = 0;
        for (int i = 0; i < ipArray.length; i++) {
            int power = 3 -i;
            ipDecimal += ((Integer.parseInt(ipArray[i]) % 256 * Math.pow(256, power)));
        }

        System.out.println(ipDecimal);*/
    }

}
