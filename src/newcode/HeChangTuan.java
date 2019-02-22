package newcode;

import java.util.Scanner;

/*合唱团*/
public class HeChangTuan {
    static int[] ability;
    static int d;

    public static void main(String[] args) {
        String input;
        String str;
        Scanner sc = new Scanner(System.in);
        str = sc.nextLine();
        input = str.split(" ")[0];
        int num = Integer.parseInt(input);
        ability = new int[num];

        str = sc.nextLine();
        String[] sabilities = str.split(" ");
        for (int i = 0; i < sabilities.length; i++)
            ability[i] = Integer.parseInt(sabilities[i]);

        str = sc.nextLine();
        String[] params = str.split(" ");
        d = Integer.parseInt(params[1]);
        int K = Integer.parseInt(params[0]);

        long[][][] dp = new long[K + 1][num + 1][3];  /*dp[k][y][1] 代表最大值，dp[k][y][2] 代表最小值*/
        /*dp[k][i][1] 以第i个元素为结尾，取k个元素的最大值*/
        long res = 0;

        for (int k = 1; k <= K; k++) {
            for (int i = k; i <= num; i++) {
                if (k == 1) {
                    dp[k][i][1] = ability[i - 1];
                    dp[k][i][2] = ability[i - 1];
                } else {
                    if (ability[i - 1] == 0) {
                        dp[k][i][1] = 0;
                        dp[k][i][2] = 0;
                        continue;
                    }
                    if (ability[i - 1] > 0) {
                        for (int j = i - 1; j >= k - 1 && i - j <= d; j--) {
                            dp[k][i][1] = Math.max(dp[k][i][1], dp[k - 1][j][1] * ability[i - 1]);
                            dp[k][i][2] = Math.min(dp[k][i][2], dp[k - 1][j][2] * ability[i - 1]);
                        }
                    } else {
                        /*ability < 0*/
                        for (int j = i - 1; j >= 0 && i - j <= d; j--) {
                            dp[k][i][1] = Math.max(dp[k][i][1], dp[k - 1][j][2] * ability[i - 1]);
                            dp[k][i][2] = Math.min(dp[k][i][2], dp[k - 1][j][1] * ability[i - 1]);
                        }
                    }
                }
                if (k == K) {
                    res = Math.max(res, dp[k][i][1]);
                }
            }
        }
        System.out.println(res);
    }
}
