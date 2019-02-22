package newcode;

import java.util.Scanner;

/*藏宝图*/
public class CangBaoTu {
    public static void main(String[] args) {
        Scanner sc = new Scanner(System.in);
        String s1 = sc.nextLine();
        String s2 = sc.nextLine();

        System.out.println(isSubSeq(s1, s2, 0, 0) ? "Yes" : "No");
    }

    public static boolean isSubSeq(String s1, String s2, int start1, int start2) {
        if (s2.length() - start2 <= 0) return true;
        if (s2.length() - start2 > s1.length() - start1) return false;
        if (s1.charAt(start1) == s2.charAt(start2)) {
            return isSubSeq(s1, s2, start1 + 1, start2 + 1) || isSubSeq(s1, s2, start1 + 1, start2);
        } else {
            return isSubSeq(s1, s2, start1 + 1, start2);
        }
    }
}
