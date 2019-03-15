package learnjava;

import java.lang.reflect.Constructor;
import java.lang.reflect.Field;
import java.lang.reflect.Method;

public class ReflectionTest {
    class MyInnerClass {
    }
    public static void main(String[] args) {
        try {
            Class<?> stringClass = Class.forName("java.lang.String");
            stringClass = String.class;
            stringClass = Thread.currentThread().getContextClassLoader().loadClass("java.lang.String");
            stringClass = BlockingQueueTest.class;
            stringClass.newInstance();
//            Constructor con = stringClass.getConstructor(String.class);
            System.out.println(stringClass.getSimpleName());
            System.out.println("Context ClassLoader: " + Thread.currentThread().getContextClassLoader());
            System.out.println("this ClassLoader: " + ReflectionTest.class.getClassLoader());
            System.out.println("this parent ClassLoader: " + ReflectionTest.class.getClassLoader().getParent());
            System.out.println("this parent parent ClassLoader: " + ReflectionTest.class.getClassLoader().getParent().getParent());
            System.out.println("String ClassLoader: " + String.class.getClassLoader());
            for (Field field : stringClass.getDeclaredFields()) {
                System.out.println("Name: " + field.getName() + ", Type: " + field.getType());
            }
            for (Method method : stringClass.getDeclaredMethods()) {
                System.out.println("Name: " + method.getName() + ", Result Type: " + method.getReturnType());
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}
