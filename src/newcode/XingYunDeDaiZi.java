package newcode;

import java.util.Scanner;

/*幸运的袋子*/
public class XingYunDeDaiZi {
    public static int result = 0;

    public static void main(String[] args) {
        Scanner sc = new Scanner(System.in);
        int n = sc.nextInt();
        int[] a = new int[1001];
        for (int i = 0; i < n; i++) {
            a[sc.nextInt()]++;
        }
        int oneNum = a[1];
        result += oneNum - 1;
        calc(a, 2, oneNum, 0, 1);
        System.out.println(result);
    }

    public static void calc(int[] a, int start, int oneNum, int sum, int product) {
        if (product - sum > oneNum) return;
        for (int i = start; i < a.length; i++) {
            if (a[i] > 0) {
                sum += i;
                product *= i;
                int delta = product - sum;
                if (delta < oneNum) {
                    result += oneNum - delta;
                    a[i]--;
                    calc(a, i, oneNum, sum, product);
                    sum -= i;
                    product /= i;
                    a[i]++;
                } else {
                    return;
                }
            }
        }
    }
}