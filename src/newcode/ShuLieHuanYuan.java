package newcode;

import java.util.Scanner;

/*数列还原*/
public class ShuLieHuanYuan {
    public static void main(String[] args) {
        Scanner sc = new Scanner(System.in);
        int n = sc.nextInt();
        int k = sc.nextInt();
        int[] a = new int[n];
        boolean[] b = new boolean[n + 1];
        boolean[] c = new boolean[n + 1];
        for (int i = 0; i < n; i++) {
            a[i] = sc.nextInt();
            c[a[i]] = true;
        }
        System.out.println(helper(c, b, a, 0, k));
    }

    public static int helper(boolean[] c, boolean[] b, int[] a, int start, int k) {
//        System.out.println("helper: start: " + start + " k: " + k);
        if (a.length - start == 1) {
            if (k == 0) return 1;
            return 0;
        }

        int count = 0;
        if (a[start] == 0) {
            for (int i = b.length - 1; i > 0; i--) {
                if (!b[i] && !c[i]) {
                    int calc = calc(b, i);
                    if (calc <= k) {
                        b[i] = true;
                        a[start] = i;
                        count += helper(c, b, a, start + 1, k - calc);
                        b[i] = false;
                        a[start] = 0;
                    }
                }
            }
        } else {
            int calc = calc(b, a[start]);
            if (calc <= k) {
                b[a[start]] = true;
                count += helper(c, b, a, start + 1, k - calc);
                b[a[start]] = false;
            }
        }
        return count;
    }

    public static int calc(boolean[] b, int limit) {
        int count = 0;
        for (int i = limit + 1; i < b.length; i++) {
            if (!b[i]) count++;
        }
//        System.out.println("calc: limit:" + limit + " count: " + count);
        return count;
    }
}
