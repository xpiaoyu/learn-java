package newcode;

import java.util.LinkedList;
import java.util.Scanner;

/*地牢逃脱*/
public class DiLaoTaoTuo {
    static int max = 0;

    public static void main(String[] args) {
        Scanner sc = new Scanner(System.in);
        int n = sc.nextInt();
        int m = sc.nextInt();

        boolean map[][] = new boolean[n][m];
        int step[][] = new int[n][m];

        for (int i = 0; i < n; i++) {
            String str = sc.nextLine();
            if (str.length() != m) {
                i--;
                continue;
            }
//            System.out.println("new str: " + str);
            for (int j = 0; j < m; j++) {
                map[i][j] = str.charAt(j) == '.';
            }
        }

        int x0 = sc.nextInt();
        int y0 = sc.nextInt();
        int k = sc.nextInt();

        int[][] method = new int[k][2];

        for (int i = 0; i < k; i++) {
            method[i][0] = sc.nextInt();
            method[i][1] = sc.nextInt();
        }

        step[x0][y0] = -1;
        f(map, step, method, x0, y0);
        for (int i = 0; i < step.length; i++) {
            for (int j = 0; j < step[0].length; j++) {
                if (map[i][j]) {
                    if (step[i][j] == 0) {
                        System.out.println(-1);
                        return;
                    } else {
                        max = Math.max(max, step[i][j]);
                    }
                }
            }
        }
        System.out.println(max);
    }

    public static void f(boolean[][] map, int[][] step, int[][] method, int x, int y) {
        /*(x,y) 当前递归坐标*/
        if (x < 0 || x >= map.length || y < 0 || y >= map[0].length) return;
        if (!map[x][y]) return;
        LinkedList<Integer> xList = new LinkedList<>();
        LinkedList<Integer> yList = new LinkedList<>();
        xList.add(x);
        yList.add(y);

        while (!xList.isEmpty()) {
            x = xList.pop();
            y = yList.pop();
            int curStep = Math.max(0, step[x][y]); /*当前的步数*/
            for (int k = 0; k < method.length; k++) {
                /*遍历每一种方法*/
                int xEnd = x + method[k][0];
                int yEnd = y + method[k][1];
                if (xEnd >= map.length || xEnd < 0 || yEnd >= map[0].length || yEnd < 0) continue;
                if (map[xEnd][yEnd] && step[xEnd][yEnd] == 0) {
                    step[xEnd][yEnd] = curStep + 1;
                    xList.add(xEnd);
                    yList.add(yEnd);
                }
            }
        }
    }
}
