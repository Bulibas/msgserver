package server.test;

import java.io.*;
import java.net.InetSocketAddress;
import java.net.Socket;
import java.nio.ByteBuffer;
import java.nio.ByteOrder;
import java.util.Arrays;
import java.util.Random;

/**
 * Created by aatanasov on 8/8/2017.
 */
public class TestClient {

    private static int counter = 0;

    private static int id;

    public static void main(String[] args) throws IOException, InterruptedException {


        Socket s = new Socket("127.0.0.1", 9090);
        PrintStream ps = new PrintStream(s.getOutputStream());
        BufferedReader reader = new BufferedReader(new InputStreamReader(s.getInputStream()));
        InputStream in = s.getInputStream();
        BufferedReader consoleReader = new BufferedReader(new InputStreamReader(System.in));
        //BufferedWriter writer = new BufferedWriter(new OutputStreamWriter(s.getOutputStream()));

        System.out.println("enter username:");
        while(true) {
            String username = consoleReader.readLine();
            sendTryLogin(ps, username);

            byte[] loginResponse = new byte[6 + 2];
            in.read(loginResponse);
            System.out.println(Arrays.toString(loginResponse));

            id = ((loginResponse[6] << 8) << 0xFF00) | (loginResponse[7] & 0xFF);
            if(id == 0) {
                System.out.println("username already taken, please try another:");
            } else {
                System.out.println("login successfull");
                break;
            }

        }

        //ps.println("alo");
        while (true) {
            while(consoleReader.ready()) {
                String line = consoleReader.readLine();
                String[] parts = line.split("#", 2);
                String msg = parts[1];
                if(parts[0].equals("all")) {
                    System.out.println("sending");
                    sendBroadcast(ps, msg);
                } else {
                    int dest = Integer.parseInt(parts[0]);
                    sendPersonal(ps, dest, msg);//TODO to
                }

                //ByteBuffer buffer = ByteBuffer.allocate(msg.length() + 10);
                //System.out.println(buffer.order());
                //buffer.order(ByteOrder.LITTLE_ENDIAN);
               // System.out.println("little endianess bytes: " + Arrays.toString(buffer.array()));
                //System.out.println("bytes: " + Arrays.toString(buffer.array()));
                //ps.println(msg);
                //ps.write(buffer.array());
            }
            int n;
            if((n = in.available()) > 0 ) {
                byte[] b = new byte[n];
                in.read(b);
                byte cid = b[5];

                if(cid == 3 || cid ==4){
                    System.out.println("received msg len: " + b.length);
                    int from = ((b[6] << 8) << 0xFF00) | (b[7] & 0xFF);
                    byte[] bmsg = Arrays.copyOfRange(b, 8, b.length);
                    String msg = new String(bmsg);
                    System.out.println("from " + from + ": " + msg);
                } else if(cid == 7) {
                    //int len = ((b[9] << 8) << 0xFF00) | (b[10] & 0xFF);
                    int len = b[10];
                    byte[] msg = Arrays.copyOfRange(b, 11, 11 +len);
                    String str = new String(msg);
                    System.out.println(Arrays.toString(msg));
                    System.out.println(str);
                    System.out.println("[new]["+ str + "]");
                }
            }
        }

    }

    private static void sendTryLogin(PrintStream ps, String username) throws IOException {
        ByteBuffer b = ByteBuffer.allocate(7 + username.length());
        b.put((byte) 2);
        b.put((byte) 2);
        b.put((byte) 2);
        b.put((byte) ((byte)counter >> 8));
        b.put((byte)counter);
        b.put((byte) 1);//command login
        b.put((byte) username.length());//command login
        b.put(username.getBytes());
        ps.write(b.array());
        ps.flush();
        System.out.println("sended to server:" + Arrays.toString(b.array()));
        counter++;
    }

    private static void sendPersonal(PrintStream ps, int to, String msg) throws IOException {
        ByteBuffer b = ByteBuffer.allocate(10 + msg.length());
        b.put((byte) 2);
        b.put((byte) 2);
        b.put((byte) 2);
        b.put((byte) ((byte)counter >> 8));
        b.put((byte)counter);
        b.put((byte) 3);
        b.put((byte) ((byte)id >> 8));
        b.put((byte) id);
        b.put((byte) ((byte) to >> 8));
        b.put((byte) to);
        b.put(msg.getBytes());
        ps.write(b.array());
        ps.flush();
        counter++;
    }

    private static void sendBroadcast(PrintStream ps, String msg) throws IOException {
        ByteBuffer b = ByteBuffer.allocate(10 + msg.length());
        b.put((byte) 2);
        b.put((byte) 2);
        b.put((byte) 2);
        b.put((byte) ((byte)counter >> 8));
        b.put((byte)counter);
        b.put((byte) 4);
        b.put((byte) ((byte)id >> 8));
        b.put((byte) id);
        b.put((byte) 0);
        b.put((byte) 0);
        b.put(msg.getBytes());
        ps.write(b.array());
        ps.flush();
        counter++;
    }
}



        /*SocketChannel channel = SocketChannel.open();
        channel.bind(new InetSocketAddress("172.22.205.18", 9999));
        channel.connect(new InetSocketAddress("172.22.205.18", 9090));
        */

     /*   Socket s1 = new Socket();
        s1.bind(new InetSocketAddress(9999));
        s1.connect(new InetSocketAddress("172.22.205.18", 9090));
        PrintStream printer1 = new PrintStream(s1.getOutputStream());

        Socket s2 = new Socket();
        s2.bind(new InetSocketAddress(9898));
        s2.connect(new InetSocketAddress("172.22.205.18", 9090));
        PrintStream printer2 = new PrintStream(s1.getOutputStream());

        for (int i = 0; i < 1000000; i++) {
            //Thread.sleep(100);

                printer1.println("connection1: " + i);
                counter++;
                printer2.println("connection2: " + i);
                counter++;
        }*/

        /*long counter= 0;

        PrintStream[] printStreams = new PrintStream[100];
        BufferedReader[] readers = new BufferedReader[100];

        int c = 0;
        for (int i = 0; i < 10; i++) {
            Socket s = new Socket();
            try{
                s.bind(new InetSocketAddress(i + 10000));
                c++;
            } catch (Exception e) {
                e.printStackTrace();
            }
            s.connect(new InetSocketAddress("172.22.205.18", 9090));

            PrintStream ps = new PrintStream(s.getOutputStream());
            printStreams[i] = ps;

            BufferedReader reader = new BufferedReader(new InputStreamReader(s.getInputStream()));
            readers[i] = reader;
        }

        for (int i = 0; i < 10; i++) {

            if(readers[i].ready())
                System.out.println(readers[i].readLine());
            if(i == 9) {
                i=0;
            }
        }*/

        /*for (int i = 0; i < 1000000; i++) {
                Random r = new Random();
                int random = r.nextInt(10000);
                printStreams[random].println("connection"+random);
                counter++;
        }*/

//System.out.println("senders: " + c);
//System.out.println(counter);