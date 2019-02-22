import java.util.Scanner;

public class Main {

    public static void main2(String[] args) {
        System.out.println("Hello World!");
        p146 cache = new p146(2);
        cache.put(1, 1);
        cache.put(2, 2);
        cache.get(1);
        cache.put(3, 3);
        cache.get(2);
        cache.put(4, 4);
        cache.get(1);
    }

    public static void mainProblem1(String[] args) {
        Scanner sc = new Scanner(System.in);
        String str = sc.nextLine();
        String input = str.split(" ")[0];

        int end = 0;
        int begin = input.length() - 1;
        if (input.charAt(0) == '-') {
            System.out.print('-');
            end = 1;
        }
        while (begin > 0 && input.charAt(begin) == '0') begin--;

        for (int i = begin; i >= end; i--) {
            System.out.print(input.charAt(i));
        }
    }
}
