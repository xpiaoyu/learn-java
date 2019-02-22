package newcode;

import java.util.*;

/*混合颜料*/
public class HunHeYanLiao {
    public static void main(String[] args) {
        Scanner sc = new Scanner(System.in);
        int n = sc.nextInt();
        int[] a = new int[n];
        int count = 0;
        for (int i = 0; i < n; i++) {
            a[i] = sc.nextInt();
        }

        for (int i = n - 1; i >= 0; i--) {
            if (a[i] == 0) continue;
            count++;
            for (int j = i - 1; j >= 0; j--) {
                if (a[j] > (a[i] ^ a[j])) {
                    a[j] = a[i] ^ a[j];
                }
            }
        }

        System.out.println(count);
    }
}