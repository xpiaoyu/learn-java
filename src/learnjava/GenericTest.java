package learnjava;

import com.sun.xml.internal.fastinfoset.util.CharArray;

import java.util.*;

public class GenericTest {
    class A<T, ID> {
    }

    class B extends A<Integer, Integer> {
    }

    class Genertic<T extends CharSequence> {
        class GenInner implements Iterable<T> {
            private T obj; // 如果不声明 GenInner 类的 T 则这里的 T 是 Generic 的泛型 T。

            public Iterator<T> iterator() {
                return null;
            }

            public <E extends CharSequence> void innerTest(List<E> list) {
                E obj = list.get(0);
            }

            <T> void useNewGen(T param) {
            }

            public void test() {
                innerTest(new LinkedList<CharArray>());
            }
        }

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
