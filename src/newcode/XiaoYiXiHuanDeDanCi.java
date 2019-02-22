package newcode;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;

/*小易喜欢的单词*/
public class XiaoYiXiHuanDeDanCi {
    public static void main(String[] args) throws IOException {
        BufferedReader br = new BufferedReader(new InputStreamReader(System.in));
        String s = br.readLine();
        char lastChar = 'A' - 1;
        int[][] continuous = new int[27][27];
        for (int i = 0; i < s.length(); i++) {
            boolean notNow = false;
            char thisChar = s.charAt(i);
            if (thisChar > 'Z' || thisChar < 'A') {
                System.out.println("Dislikes");
                return;
            }
            if (lastChar == thisChar) {
                System.out.println("Dislikes");
                return;
            }
            for (int k = 1; k <= 26; k++) {
                if (continuous[k][0] > 0) {
                    int thisIndex = thisChar - 'A' + 1;
                    if (continuous[k][thisIndex] == 0) {
                        continuous[k][thisIndex] = 1;
                        if (k == thisIndex) notNow = true;
                    } else if (continuous[k][thisIndex] == 2) {
                        System.out.println("Dislikes");
                        return;
                    }
                }
            }
            for (int k = 1; k <= 26; k++) {
                int thisIndex = thisChar - 'A' + 1;
                if (continuous[thisIndex][k] == 1) {
                    if (thisIndex == k && notNow)
                        continue;
                    continuous[thisIndex][k] = 2;
                }
            }
            continuous[thisChar - 'A' + 1][0] = 1;
            lastChar = thisChar;
        }
        System.out.println("Likes");
    }
}
