package learnjava;

public class ThreadLocalTest {
    static ThreadLocal<Integer> threadLocal = new ThreadLocal<Integer>() {
        @Override
        protected Integer initialValue() {
            return 1;
        }
    };

    static class Test implements Runnable {
        @Override
        public void run() {
            Integer i = threadLocal.get();
            for (int j = 0; j < 100; j++) {
                i++;
            }
        }
    }

    public static void main(String[] a) {
        new Thread(new Test()).start();
        new Thread(new Test()).start();

        System.out.println(threadLocal.get());
    }
}
