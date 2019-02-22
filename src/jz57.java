import java.util.ArrayList;

public class jz57 {
    public ArrayList<ArrayList<Integer>> FindContinuousSequence(int sum) {
        ArrayList<ArrayList<Integer>> ret = new ArrayList<>();
        int start = 1, end = 2;
        int curSum = 3;
        while (end < sum) {
            if (curSum > sum) {
                curSum -= start;
                start++;
            } else if (curSum < sum) {
                end++;
                curSum += end;
            } else {
                ArrayList<Integer> list = new ArrayList<>();
                for (int i = start; i <= end; i++)
                    list.add(i);
                ret.add(list);
                curSum -= start;
                start++;
                end++;
                curSum += end;
            }
        }
        return ret;
    }

    public ArrayList<ArrayList<Integer>> FindContinuousSequence2(int s) {
        ArrayList<ArrayList<Integer>> ret = new ArrayList<>();
        if (s < 2) return ret;
        int b = 2;
        while (b * b < (s << 1)) {
            if (b % 2 == 0) {
                if ((s << 1) % b == 0 && ((s << 1) / b) % 2 == 1) {
                    int start = s / b - b / 2 + 1;
                    ArrayList<Integer> list = new ArrayList<>();
                    for (int i = 0; i < b; i++) {
                        list.add(start + i);
                    }
                    ret.add(0, list);
                }
            } else {
                if (s % b == 0) {
                    int start = s / b - b / 2;
                    ArrayList<Integer> list = new ArrayList<>();
                    for (int i = 0; i < b; i++) {
                        list.add(start + i);
                    }
                    ret.add(0, list);
                }
            }
            b++;
        }
        return ret;
    }
}
