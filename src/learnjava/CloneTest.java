package learnjava;

public class CloneTest implements Cloneable {
    private int innerInt = 0;

    @Override
    public Object clone() {
        try {
            innerInt++;
            CloneTest ct = (CloneTest) super.clone();
            ct.innerInt += 10;
            return ct;
        } catch (CloneNotSupportedException e) {
            e.printStackTrace();
            return null;
        } finally {
            innerInt++;
        }
    }

    public static void main(String[] args) {
        CloneTest ct = new CloneTest();
        CloneTest ct2 = (CloneTest) ct.clone();
        System.out.println(ct.innerInt + " " + ct2.innerInt);
    }
}
