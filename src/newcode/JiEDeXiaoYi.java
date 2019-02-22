package newcode;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.util.LinkedList;
import java.util.Set;

/*饥饿的小易*/
public class JiEDeXiaoYi {
    public static void main(String[] args) throws IOException {
        BufferedReader br = new BufferedReader(new InputStreamReader(System.in));
        String line = br.readLine();
        long x = Long.parseLong(line);
//        LinkedList<Long> list = new LinkedList<>();
//        Set<Long> set = new HashSet<>();
//        list.add(x);
//        int result = -2;
//        for (int depth = 0; depth < 100000 && result == -2; depth++) {
//            result = bfs(depth, list, set);
//        }
//        System.out.println(result == -2 ? -1 : result);
        fast(x);
    }

    public static void fast(long x) {
        int result = -1;
        for (int i = 1; i <= 300000; i++) {
            if ((x = (x * 2 + 1) % 1000000007) == 0) {
                result = i / 3 + (i % 3 > 0 ? 1 : 0);
                break;
            }
        }
        System.out.println(result);
    }

    public static int bfs(int depth, LinkedList<Long> list, Set<Long> set) {
        if (depth > 100000) return -1;
        int count = list.size();
        boolean haveNew = false;
        for (int i = 0; i < count; i++) {
            long x = list.pop();
            long x1 = (x * 4 + 3) % 1000000007;
            long x2 = (x * 8 + 7) % 1000000007;
            if (x1 == 0 || x2 == 0) return depth + 1;
            if (set.add(x1)) {
                haveNew = true;
                list.add(x1);
            }
            if (set.add(x2)) {
                haveNew = true;
                list.add(x2);
            }
        }
        if (!haveNew) {
            return -1;
        }
        return -2;
    }
}
