package newcode;

import java.util.Scanner;

/*星际穿越*/
public class XingJiChuanYue {
    public static void main(String[] args) {
        Scanner sc = new Scanner(System.in);

        long h = sc.nextLong();

        long n = (long) Math.floor((Math.sqrt(1 + 4 * h) - 1) / 2);

        System.out.println(n);
    }
}
