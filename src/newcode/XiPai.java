package newcode;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;

/*洗牌*/
public class XiPai {
    public static void main(String[] args) throws IOException {
        BufferedReader br = new BufferedReader(new InputStreamReader(System.in));
        String[] sa = br.readLine().split(" ");
        int eachGroup = sa.length / 2;
        for (int i = 0; i < eachGroup; i++) {
            System.out.print(sa[i] + " " + sa[eachGroup + i]);
            if (i < eachGroup - 1)
                System.out.print(" ");
        }
    }
}
