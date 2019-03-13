package learnjava;

public class StringDemo{
    private static final String MESSAGE="taobao";
    public static void main(String [] args) {
        String a ="tao"+"bao";
        final String b="tao";
        final String c="bao";
        System.out.println(a==MESSAGE);
        System.out.println((b+c)==MESSAGE);
    }
}