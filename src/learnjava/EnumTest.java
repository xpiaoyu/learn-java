package learnjava;

public class EnumTest {
    static {
        System.out.println("0 " + System.currentTimeMillis());
    }

    enum MyInstance {
        Instance;

        private String s = "This is a singleton Instance created by Enum.";

        MyInstance() {
            try {
                Thread.sleep(1000);
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
            System.out.println("EnumTest Initialized.");
        }

        public void log(String s) {
            System.out.println(s + " " + this.s);
        }
    }

    public static void main(String[] args) {
        MyInstance instance;
        System.out.println("1 " + System.currentTimeMillis());

        instance = MyInstance.Instance;
        System.out.println("2 " + System.currentTimeMillis());

        MyInstance instance1 = MyInstance.Instance;
        System.out.println("3 " + System.currentTimeMillis());

        instance.log("hello");
        System.out.println("4 " + System.currentTimeMillis());

        instance1.log("23");
        System.out.println("5 " + System.currentTimeMillis());
    }
}
