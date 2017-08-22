package server.v2;

import java.util.concurrent.BlockingQueue;
import java.util.concurrent.LinkedBlockingDeque;
import java.util.concurrent.ThreadPoolExecutor;
import java.util.concurrent.TimeUnit;

/**
 * Created by aatanasov on 8/17/2017.
 */
public class GeneratorExecutor {

    private static final int CORE_POOL_SIZE = 5;

    private static final int MAX_POOL_SIZE = 20;

    private static final long KEEP_ALIVE_TIME = 5000;

    private static GeneratorExecutor instance = new GeneratorExecutor();

    private static BlockingQueue<Runnable> handlers = new LinkedBlockingDeque<>();

    private static ThreadPoolExecutor jobExecutor = new ThreadPoolExecutor(CORE_POOL_SIZE,
            MAX_POOL_SIZE,
            KEEP_ALIVE_TIME,
            TimeUnit.MILLISECONDS,
            handlers);

    private static boolean initialized = false;

    private GeneratorExecutor() {
        //only one instance needed, should be private
    }

    public static GeneratorExecutor getInstance() {
        if(!initialized) {
            jobExecutor.prestartAllCoreThreads();
            initialized = false;
        }

        return instance;
    }

    public void addJob(Runnable job) {
        handlers.add(job);
    }
}
