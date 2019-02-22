import java.util.LinkedList;
import java.util.List;
import java.util.Queue;

// 使用 HashSet 遍历 'a'-'z' 更快。
public class p127 {
    public int ladderLength(String beginWord, String endWord, List<String> wordList) {
        LinkedList<String> queue = new LinkedList<>();
        int oldItemNum, newItemNum = 0, step = 0;
        queue.push(beginWord);
        oldItemNum = 1;
        while (!queue.isEmpty()) {
            String curr = queue.pop();
            oldItemNum--;
            for (int i = 0; i < wordList.size(); i++) {
                String tmp = wordList.get(i);
                if (tmp.length() > 0) {
                    if (tmp.equals(endWord)) return step + 2;
                    if (wordDistance(curr, tmp)) {
                        queue.addLast(tmp);
                        wordList.remove(i);
                        i--;
                        newItemNum++;
                    }
                }
            }
            if (oldItemNum <= 0) {
                step++;
                oldItemNum = newItemNum;
                newItemNum = 0;
            }
        }
        return 0;
    }

    public boolean wordDistance(String s1, String s2) {
        if (s1.length() != s2.length() || s1.length() == 0) return false;
        int count = 0;
        for (int i = 0; i < s1.length(); i++) {
            if (s1.charAt(i) != s2.charAt(i)) {
                count++;
                if (count > 1)
                    return false;
            }
        }
        return count == 1;
    }
}
