package crawler.tools;

import java.util.concurrent.ArrayBlockingQueue;
import java.util.concurrent.ThreadPoolExecutor;
import java.util.concurrent.TimeUnit;

public class ThreadPool {
	
	private ThreadPoolExecutor ProcessTaskPool;
	
	public synchronized void execute(Runnable runnable){
		this.ProcessTaskPool.execute(runnable);
	}
	
    public ThreadPool(int poolSize,int maxPoolSize,long keepAliveTime){
         final ArrayBlockingQueue<Runnable> queue = new ArrayBlockingQueue<Runnable>(100);
         ProcessTaskPool = new ThreadPoolExecutor(poolSize, maxPoolSize, keepAliveTime, TimeUnit.SECONDS, queue);
    }
}