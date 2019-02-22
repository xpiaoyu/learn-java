package learnjava;

public class Synchronizd {
    public static synchronized void main(String[] a) {
        Thread t = new Thread() {
            public void run() {
                Sogou();
            }
        };
        t.setDaemon(true);
        t.start();
        Long l = 10L;
        Double d = 1.0d;
        Integer i = 1;
        l.equals(0L);
        System.out.print("Hello");
    }

    static synchronized void Sogou() {
        try {
            Thread.sleep(1000);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
        System.out.print("Sogou");
    }
}
