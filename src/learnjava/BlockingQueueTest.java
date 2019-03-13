package learnjava;

import java.util.List;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.ScheduledExecutorService;
import java.util.concurrent.TimeUnit;

public class BlockingQueueTest {
    static class Task implements Runnable {
        //        static final Integer lock = 0;
        static Integer current = 0;
        Integer number;

        Task(int number) {
            this.number = number;
        }

        @Override
        public void run() {
            System.out.println("Thread: " + this.number + " running");
            try {
                synchronized (Task.class) {
                    while (!current.equals(number)) {
//                        lock.notifyAll();
//                        lock.wait();
                        Task.class.notifyAll();
                        Task.class.wait();
                    }
                    System.out.println("Thread executed: " + number);
                    current++;
//                    lock.notifyAll();
                    Task.class.notifyAll();
                }
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
        }
    }

    public static void main(String[] args) {
//        LinkedBlockingQueue<String> linkedBlockingQueue = new LinkedBlockingQueue<>(100);
//        ReentrantLock reentrantLock = new ReentrantLock();
//        reentrantLock.newCondition();
//        reentrantLock.tryLock(1,TimeUnit.SECONDS);
//        linkedBlockingQueue.offer("123");
//        linkedBlockingQueue.add("123");
        ExecutorService executor = Executors.newScheduledThreadPool(1);
        for (int i = 0; i < 10; i++) {
            Task task = new Task(i);
//            new Thread(task).start();
            ((ScheduledExecutorService) executor).scheduleAtFixedRate(task, 0, 3, TimeUnit.SECONDS);
        }
//        try {
//            executor.awaitTermination(10, TimeUnit.MINUTES);
//        } catch (InterruptedException e) {
//            e.printStackTrace();
//        }
//        executor.shutdown();
    }
}
