package server.notusedanymore;

import java.util.concurrent.*;

/**
 * Created by aatanasov on 8/10/2017.
 */
public class JobExecutor {

    private static final int CORE_POOL_SIZE = 5;

    private static final int MAX_POOL_SIZE = 20;

    private static final long KEEP_ALIVE_TIME = 5000;

    private static JobExecutor instance = new JobExecutor();

    private static BlockingQueue<Runnable> jobs = new LinkedBlockingDeque<>();

    private static ThreadPoolExecutor jobExecutor = new ThreadPoolExecutor(CORE_POOL_SIZE,
                                                                        MAX_POOL_SIZE,
                                                                        KEEP_ALIVE_TIME,
                                                                        TimeUnit.MILLISECONDS,
                                                                        jobs);

    private static boolean initialized = false;

    private JobExecutor() {
        //only one instance needed, should be private
    }

    public static JobExecutor getInstance() {
        if(!initialized) {
            jobExecutor.prestartAllCoreThreads();
            initialized = false;
        }

        return instance;
    }

    public void addJob(Runnable job) {
        jobs.add(job);
    }
}
