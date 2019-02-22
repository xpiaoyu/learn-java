package newcode;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.util.Arrays;

/*数字游戏*/
public class ShuZiYouXi {
    public static void main(String[] args) throws IOException {
        BufferedReader br = new BufferedReader(new InputStreamReader(System.in));
        String s = br.readLine();
        int n = Integer.parseInt(s);
        int[] a = new int[n];
        s = br.readLine();
        String[] sa = s.split(" ");

        for (int i = 0; i < n; i++) {
            a[i] = Integer.parseInt(sa[i]);
        }
        Arrays.sort(a);
        int can = 0;
        for (int i = 0; i < n; i++) {
            if (a[i] - can > 1) {
                break;
            }
            can += a[i];
        }
        System.out.println(can + 1);
    }
}
