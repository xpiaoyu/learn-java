package newcode;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;

/*解救小易*/
public class JieJiuXiaoYi {
    public static void main(String[] args) throws IOException {
        BufferedReader br = new BufferedReader(new InputStreamReader(System.in));
        String line = br.readLine();
        int n = Integer.parseInt(line);
        int min = Integer.MAX_VALUE;
        String[] xs = br.readLine().split(" ");
        String[] ys = br.readLine().split(" ");
        for (int i = 0; i < n; i++) {
            int x = Integer.parseInt(xs[i]);
            int y = Integer.parseInt(ys[i]);
            if ((x - 1 + y - 1) < min) min = x - 1 + y - 1;
        }
        System.out.println(min);
    }
}
