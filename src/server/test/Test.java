package server.test;

import java.io.*;
import java.util.ArrayList;
import java.util.Hashtable;
import java.util.Iterator;
import java.util.List;

/**
 * Created by aatanasov on 8/9/2017.
 */
public class Test {
    private static Hashtable<Integer, Integer> t;

    public static void main(String[] args) throws IOException, InterruptedException {
      /*  BufferedReader[] readers = new BufferedReader[100000];
        System.out.println(readers.length);
        String output = "###sadasdasdsnaskoasdsadasdasdsnaskoasdsadasdasdsnaskoasdsadasdasdsnaskoasd asd asd!!!";

        for (int i = 0; i < 100000; i++) {
            ByteArrayInputStream in = new ByteArrayInputStream(output.getBytes());
            BufferedReader reader = new BufferedReader(new InputStreamReader(in));
            //reader.readLine();

            readers[i] = reader;
        }

        long startTime = System.currentTimeMillis();
        for (int i = 0; i < 100000; i++) {
            if(readers[i].ready()) {
                String s = readers[i].readLine();
                System.out.println(i);
            }
        }

        long endTime = System.currentTimeMillis();
        System.out.println(endTime-startTime);

        System.out.println(readers[0].readLine());
*/
        /*for (int i = 0; i < 1000000; i++) {
            System.out.println(i);
            System.out.println(i);
        }*/

        /*BlockingQueue<Runnable> queue = new LinkedBlockingDeque<>();
        ThreadPoolExecutor executor = new ThreadPoolExecutor(5,
                20,
                50000,
                TimeUnit.MILLISECONDS,
                queue);
        executor.prestartAllCoreThreads();
        Runnable r = new Runnable() {
            @Override
            public void run() {
                System.out.println("task run");
            }
        };
        queue.put(r);

        System.out.println(executor.getTaskCount());*/


     /*   int n = 0;
        for (int i = 0; i < 10000; i++) {
            n += i;
        }
        System.out.println("must be: n");

       t = new Hashtable<>();
        for (int i = 0; i < 10000; i++) {
            t.put(i, i);
        }

        for (int i = 0; i < 100000; i++) {
            new Thread(new R()).start();
        }

    }

    private static class R implements Runnable {

        @Override
        public void run() {
            int sum = 0;
            Iterator i = t.values().iterator();
            while (i.hasNext()) {
                Integer integer = (Integer) i.next();
                sum +=integer;
            }
            System.out.println(sum);
            try {
                Thread.sleep(10000);
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
        }*/

        List<byte[]> l = new ArrayList<>();
        byte[] b1 = new byte[]{1, 2, 3};
        l.add(b1);
        byte[] b2= new byte[]{1, 2, 3};

        System.out.println(l.contains(b2));
    }

}
