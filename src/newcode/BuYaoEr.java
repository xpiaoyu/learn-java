package newcode;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;

/*不要二*/
public class BuYaoEr {
    public static void main(String[] args) throws IOException {
        BufferedReader br = new BufferedReader(new InputStreamReader(System.in));
        String line = br.readLine();
        String[] lines = line.split(" ");
        int h = Integer.parseInt(lines[0]);
        int w = Integer.parseInt(lines[1]);
        if (w == 0 || h == 0) System.out.println(0);
        int wc = w % 4 < 2 ? (w / 4 * 2 + w % 4) : (w / 4 * 2 + 2);
        int hc = h % 4 < 2 ? (h / 4 * 2 + h % 4) : (h / 4 * 2 + 2);
        System.out.println((wc * hc) + (w - wc) * (h - hc));
    }
}
