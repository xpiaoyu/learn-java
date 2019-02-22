package newcode;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;

/*统计回文*/
public class TongJiHuiWen {
    public static void main(String[] args) throws IOException {
        BufferedReader br = new BufferedReader(new InputStreamReader(System.in));
        String s1 = br.readLine();
        String s2 = br.readLine();
        System.out.println(helper(s1, s2));
    }

    public static int helper(String s1, String s2) {
        int result = 0;
        if (s1.length() == 0) {
            if (isPalindrome(s2)) return 1;
            return 0;
        }
        /*>=1*/
        if (isPalindrome(s1 + s2)) result++;
        if (isPalindrome(s2 + s1)) result++;
        if (s1.length() == 1) return result;
        /*>=2*/
        if (s1.charAt(0) == s1.charAt(s1.length() - 1)) {
            result += helper(s1.substring(1, s1.length() - 1), s2);
        }
        return result;
    }

    public static boolean isPalindrome(String s) {
        int i = 0;
        int j = s.length() - 1;
        while (i < j) {
            if (s.charAt(i) != s.charAt(j)) return false;
            i++;
            j--;
        }
        return true;
    }
}
