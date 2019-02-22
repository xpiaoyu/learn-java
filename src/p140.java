import java.util.*;

public class p140 {
    public List<String> wordBreak(String s, List<String> wordDict) {
        long now = System.currentTimeMillis();

        Set<String> wordSet = new HashSet<>(wordDict);
        List<String> result = new ArrayList<>();
        System.out.println(System.currentTimeMillis() - now);

        boolean[][] dp = new boolean[s.length() + 1][s.length() + 1];
        dp[0][0] = true;
        System.out.println(System.currentTimeMillis() - now);

        wordBreak(s, dp, wordSet, 0);
        System.out.println(System.currentTimeMillis() - now);

//        helper(s, dp, s.length(), result, "");
        StringBuilder sb = new StringBuilder(s.length() << 1);
        System.out.println(System.currentTimeMillis() - now);

        return result;
    }

//    public void helper(String s, boolean[][] dp, int target, List<String> list, String now) {
//        if (target == 0) return;
//        for (int i = 0; i < target; i++) {
//            if (dp[i][target]) {
//                if (i == 0) {
//                    if (now.length() == 0)
//                        list.add(s.substring(i, target));
//                    else
//                        list.add(s.substring(i, target) + " " + now);
//                } else {
//                    if (now.length() == 0)
//                        helper(s, dp, i, list, s.substring(i, target));
//                    else
//                        helper(s, dp, i, list, s.substring(i, target) + " " + now);
//                }
//            }
//        }
//    }

//    public void helper(String s, boolean[][] dp, int target, List<String> list, StringBuilder sb) {
//        if (target == 0) return;
//        for (int i = 0; i < target; i++) {
//            if (dp[i][target]) {
//                if (i == 0) {
//                    sb.delete();
//                    list.add(sb.toString());
//                } else {
//                    now.addFirst(s.substring(i, target));
//                    helper(s, dp, i, list, now);
//                    now.removeFirst();
//                }
//            }
//        }
//    }

    public void helper2(String s, boolean[][] dp, int start) {
        for (int i = 0; i < s.length(); i++) {

        }
    }

    public void wordBreak(String s, boolean[][] dp, Set<String> wordSet, int start) {
        int max = 0;
        boolean flag = false;
        for (int i = 0; i < s.length(); i++) {
            if (i > max) {
                break;
            }
            if (!dp[i][0]) continue;
            for (int j = i; j < s.length(); j++) {
                if (dp[i][j]) {
                    int end = j + 1;
                    while (end <= s.length()) {
                        if (!dp[j][end] && wordSet.contains(s.substring(j, end))) {
                            if (max < j) max = j;
                            dp[j][end] = true;
                            if (!dp[j][0]) dp[j][0] = true;
                        }
                        end++;
                    }
                }
            }
        }
    }

    public void wordBreakFast(String s, boolean[][] dp, List<String> wordList, int prefixLen) {
        if (s.length() == 0) return;
        for (String v : wordList) {
            if (s.startsWith(v)) {
                dp[prefixLen][prefixLen + v.length()] = true;
                wordBreakFast(s.substring(v.length()), dp, wordList, prefixLen + v.length());
            }
        }
    }

    class Solution {
        public List<String> wordBreak(String s, List<String> wordDict) {
            List<String> list = new ArrayList<>();
            boolean[] isWord = new boolean[s.length() + 1];
            if (!isBreak(s, isWord, wordDict)) return list;
            HashSet<String> set = new HashSet<>(wordDict);
            helper(list, s, new StringBuilder(), set, 0, isWord);
            return list;
        }

        public void helper(List<String> list, String s, StringBuilder sb, HashSet<String> set, int start, boolean[] dp) {
            if (start == s.length()) list.add(sb.substring(0, sb.length() - 1));
            for (int i = start + 1; i <= s.length(); i++) {
                if (!dp[i]) continue;
                String sub = s.substring(start, i);
                if (set.contains(sub)) {
                    sb.append(sub).append(" ");
                    helper(list, s, sb, set, i, dp);
                    sb.delete(sb.length() - sub.length() - 1, sb.length());
                }
            }
        }

        public boolean isBreak(String s, boolean[] isWord, List<String> wordDict) {
            HashSet<String> set = new HashSet<>(wordDict);
            isWord[0] = true;
            for (int i = 1; i <= s.length(); i++) {
                for (int j = i - 1; j >= 0; j--) {
                    if (isWord[j] && set.contains(s.substring(j, i))) {
                        isWord[i] = true;
                        break;
                    }
                }
            }
            return isWord[s.length()];
        }
    }


    /*------------------------------------------------------------------------------------------------*/

    class SolutionBest {
        private boolean canBeBroken(String s, Set<String> wordSet,
                                    int maxWordLength, boolean[] breakable) {
            breakable[0] = true;
            for (int i = 1; i <= s.length(); i++) {
                for (int j = i - 1; j >= Math.max(0, i - maxWordLength); j--) {
                    if (breakable[j] && wordSet.contains(s.substring(j, i))) {
                        breakable[i] = true;
                        break;
                    }
                }
            }
            return breakable[breakable.length - 1];
        }

        private void wordBreakHelper(String s, int i, StringBuilder sb,
                                     Set<String> wordSet, List<String> result,
                                     int maxWordLength, boolean[] dp) {
            int maxJ = s.length();
            if (i == maxJ) {
                result.add(sb.toString());
                return;
            }

            if ((i + maxWordLength) < maxJ) {
                maxJ = i + maxWordLength;
            }
            for (int j = i; j < maxJ; j++) {
                if (!dp[j + 1]) continue;
                String substring = s.substring(i, j + 1);
                if (wordSet.contains(substring)) {
                    int length = sb.length();
                    if (sb.length() != 0) {
                        sb.append(" ");
                    }
                    sb.append(substring);

                    wordBreakHelper(s, j + 1, sb, wordSet, result, maxWordLength, dp);

                    sb.delete(length, sb.length());
                }
            }
        }

        public List<String> wordBreak(String s, List<String> wordDict) {
            List<String> result = new ArrayList<>();

            if ((s == null) || (wordDict == null)) {
                return result;
            }

            // Put all words in a hash set to speed up the lookup
            Set<String> wordSet = new HashSet<>();
            int maxWordLength = 0;
            for (String word : wordDict) {
                wordSet.add(word);
                maxWordLength = Math.max(maxWordLength, word.length());
            }

            boolean[] dp = new boolean[s.length() + 1];

            if (canBeBroken(s, wordSet, maxWordLength, dp)) {
                wordBreakHelper(s, 0, new StringBuilder(),
                        wordSet, result, maxWordLength, dp);
            }

            return result;
        }
    }

//    public List<String> wordBreak2(String s, boolean[][] dp, Set<String> wordSet) {
//        if (s.length() == 0) return new ArrayList<>();
//
//        List<String> result = new ArrayList<>();
//        int end = 1;
//        while (end < s.length()) {
//            if (wordSet.contains(s.substring(0, end))) {
//                List<String> tmp = wordBreak2(s.substring(end, s.length()), dp, wordSet);
//                if (tmp.size() > 0) {
//                    for (String v : tmp) {
//                        result.add(s.substring(0, end) + " " + v);
//                    }
//                }
//            }
//            end++;
//        }
//        if (wordSet.contains(s)) {
//            result.add(s);
//        }
//        return result;
//    }
}
