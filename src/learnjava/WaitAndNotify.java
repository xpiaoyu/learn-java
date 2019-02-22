package learnjava;

import java.util.LinkedList;
import java.util.List;

public class WaitAndNotify {
    public static final List<Integer> list = new LinkedList<>();
    public static final long start = System.nanoTime();

    public static void print(String s) {
        System.out.println((System.nanoTime() - start) + "\t" + s);
    }

    public static class Test implements Runnable {
        @Override
        public void run() {
            synchronized (list) {
                print("get list lock");
                try {
                    print("waiting");
                    list.wait();
                    print("awake from wait()");
                    Thread.sleep(1000);
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }
            }
        }
    }

    public static void main(String[] args) {
        Test test1 = new Test();
        Test test2 = new Test();
        Thread t1 = new Thread(test1);
        Thread t2 = new Thread(test2);
        t1.start();
        t2.start();

        try {
            Thread.sleep(1000);
            synchronized (list) {
                list.notify();
                print("notifyAll done");
            }
            print("10s sleeping");
            Thread.sleep(10000);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
    }
}
