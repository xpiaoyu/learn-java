package newcode;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;

/*两种排序方法*/
public class LiangZhongPaiXuFangFa {
    public static void main(String[] args) throws IOException {
        BufferedReader br = new BufferedReader(new InputStreamReader(System.in));
        String line = br.readLine();
        int n = Integer.parseInt(line);
        boolean isLexi = true;
        boolean isLen = true;
        String a = br.readLine();
        for (int i = 1; i < n; i++) {
            String b = br.readLine();
            if (isLexi) {
                if (!isLexicographically(a, b)) {
                    isLexi = false;
                }
            }
            if (isLen) {
                if (!isLengths(a, b)) {
                    isLen = false;
                }
            }
            a = b;
            if (!isLen && !isLexi) {
                System.out.println("none");
                return;
            }
        }
        if (isLexi && !isLen) {
            System.out.println("lexicographically");
        } else if (isLen && !isLexi) {
            System.out.println("lengths");
        } else if (isLen) {
            System.out.println("both");
        } else {
            System.out.println("none");
        }
    }

    public static boolean isLexicographically(String a, String b) {
        return a.compareTo(b) <= 0;
    }

    public static boolean isLengths(String a, String b) {
        return a.length() <= b.length();
    }
}
