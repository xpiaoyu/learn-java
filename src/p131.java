import java.util.LinkedList;
import java.util.List;

/*
    Given a string s, partition s such that every substring of the partition is a palindrome.
    Return all possible palindrome partitioning of s.
*/

public class p131 {
    public List<List<String>> partition(String s) {
        List<List<String>> ans = new LinkedList<>();
        if (s.length() == 0) return ans;
        if (s.length() == 1) {
            List<String> list = new LinkedList<>();
            list.add(s);
            ans.add(list);
            return ans;
        }
        if (isPalindrome(s)) {
            List<String> list = new LinkedList<>();
            list.add(s);
            ans.add(list);
        }
        for (int i = 1; i < s.length(); i++) {
            String firstHalf = s.substring(0, i);
            if (isPalindrome(firstHalf)) {
                List<List<String>> tmp = partition(s.substring(i, s.length()));
                for (List<String> list : tmp) {
                    list.add(0, firstHalf);
                    ans.add(list);
                }
            }
        }
        return ans;
    }

    public boolean isPalindrome(String s) {
        if (s.length() == 0 || s.length() == 1) return true;
        int halfLen = s.length() / 2;
        int head = 0, tail = s.length() - 1;
        for (int i = 0; i < halfLen; i++) {
            if (s.charAt(head + i) != s.charAt(tail - i)) return false;
        }
        return true;
    }
}
