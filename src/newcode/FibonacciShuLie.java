package newcode;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;

/*Fibonacci数列*/
public class FibonacciShuLie {
    public static void main(String[] args) throws IOException {
        BufferedReader br = new BufferedReader(new InputStreamReader(System.in));
        String s = br.readLine();
        int n = Integer.parseInt(s);

        int last = 0;
        int now = 1;
        while (n > now) {
            int t = now + last;
            last = now;
            now = t;
        }
        System.out.println(Math.min(Math.abs(n - last), Math.abs(n - now)));
    }
}