package learnjava;

public class EnumTest {
    static {
        System.out.println("0 " + System.currentTimeMillis());
    }

    enum MyInstance {
        Instance;

        MyInstance() {
            try {
                Thread.sleep(1000);
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
            System.out.println("EnumTest Initialized.");
        }

        public void log(String s) {
            System.out.println(s);
        }
    }

    public static void main(String[] args) {
        System.out.println("1 " + System.currentTimeMillis());

        MyInstance instance = MyInstance.Instance;
        System.out.println("2 " + System.currentTimeMillis());

        MyInstance instance1 = MyInstance.Instance;
        System.out.println("3 " + System.currentTimeMillis());

        instance.log("hello");
        System.out.println("4 " + System.currentTimeMillis());

        instance1.log("23");
        System.out.println("5 " + System.currentTimeMillis());
    }
}
