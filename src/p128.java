import java.util.HashSet;

public class p128 {
    public int longestConsecutive(int[] nums) {
        if (nums.length <= 1) return nums.length;
        HashSet<Integer> set = new HashSet<>();
        for (int i : nums)
            set.add(i);

        int bestBegin = Integer.MIN_VALUE, bestEnd = Integer.MIN_VALUE;
        int bestLen = 0;

        int remain = nums.length;
        for (int i : nums) {
            if (!set.contains(i)) continue;
            if (i < bestBegin || i > bestEnd) {
                int begin = i;
                int end = i+1;
                while (set.remove(begin)) begin--;
                while (set.remove(end)) end++;
                begin++;
                end--;
                if (bestLen < (end - begin + 1)) {
                    bestBegin = begin;
                    bestEnd = end;
                    bestLen = (bestEnd - bestBegin + 1);
                }
                if (bestLen >= remain)
                    break;
            }
            remain--;
        }
        return bestLen;
    }
}
