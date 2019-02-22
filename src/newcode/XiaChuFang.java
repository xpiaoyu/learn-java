package newcode;

import java.util.Arrays;
import java.util.HashSet;
import java.util.Scanner;

/*下厨房*/
public class XiaChuFang {
    public static void main(String[] args) {
        Scanner sc = new Scanner(System.in);
        HashSet<String> hashSet = new HashSet<>();

        while (sc.hasNext()) {
            String str = sc.nextLine();
            String[] strs = str.split(" ");
            hashSet.addAll(Arrays.asList(strs));
        }

        System.out.println(hashSet.size());
    }
}
