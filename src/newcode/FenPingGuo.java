package newcode;

import java.util.Scanner;

public class FenPingGuo {
    public static void main(String[] args) {
        Scanner sc = new Scanner(System.in);
        int n = sc.nextInt();
        int[] a = new int[n];
        int sum = 0;
        int count = 0;

        for (int i = 0; i < n; i++) {
            a[i] = sc.nextInt();
            sum += a[i];
        }
        if (sum % n != 0) {
            System.out.println(-1);
            return;
        }
        int quotient = sum / n;

        for (int ai : a) {
            if (ai < quotient) {
                if ((quotient - ai) % 2 != 0) {
                    System.out.println(-1);
                    return;
                } else {
                    count += (quotient - ai) / 2;
                }
            } else {
                if ((quotient - ai) % 2 != 0) {
                    System.out.println(-1);
                    return;
                }
            }
        }
        System.out.println(count);
    }
}
