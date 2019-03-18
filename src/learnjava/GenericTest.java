package learnjava;

import java.util.ArrayList;
import java.util.List;

public class GenericTest {
    class A<T, ID> {
    }

    class B extends A<Integer, Integer> {
    }

    public static void main(String[] args) {
        List<String>[] lsa = new ArrayList[10]; // Not really allowed.
        Object o = lsa;
        Object[] oa = (Object[]) o;
        List<Integer> li = new ArrayList<Integer>();
        li.add(new Integer(3));
        oa[1] = li; // Unsound, but passes run time store check
        String s = (String) lsa[1].get(0); // Run-time error: ClassCastException.
    }
}
