import java.util.HashSet;
import java.util.List;
import java.util.Set;

public class p139 {
    public boolean wordBreak(String s, List<String> wordDict) {
        boolean[] dp = new boolean[s.length() + 1];
        dp[0] = true;
        Set<String> wordSet = new HashSet<>(wordDict);
        if (s.length() < 1) return true;
        for (int i = 1; i <= s.length(); i++) {
            int end = i - 1;
            if (dp[s.length()]) return true;
            while (end >= 0) {
                if (dp[end] && wordSet.contains(s.substring(end, i))) {
                    dp[i] = true;
                    break;
                }
                end--;
            }
        }
        return dp[s.length()];
    }


    class Solution {
        public boolean wordBreak(String s, List<String> wordDict) {
            int[] visited = new int[s.length() + 1];
            return wordBreak(s, wordDict, visited, 0);
        }

        boolean wordBreak(String s, List<String> wordDict, int[] visited, int offset) {
            visited[offset] = 1;
            if (offset == s.length()) {
                return true;
            }

            for (String word : wordDict) {
                if (s.startsWith(word, offset)) {
                    if (visited[offset + word.length()] == 0) {
                        if (wordBreak(s, wordDict, visited, offset + word.length())) {
                            return true;
                        }
                    }
                }
            }

            return false;
        }
    }
}