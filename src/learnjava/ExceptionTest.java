package learnjava;

public class ExceptionTest {
    public static void main(String[] args) {
        try {
            int i = 100 / 0;
        } catch (Exception e) {
            e.printStackTrace();
        }
        System.out.println("1");
    }
}
