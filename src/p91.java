/**
 * 法一：
 * 0,1-2,3-9
 * 1.数字0开头，直接返回0
 * 2.数字1开头，必有两种
 * 3.数字2开头，第二位0-6有两种，第二位7-9有一种
 * 3.数字3-9开头，只有一种
 *
 * 法二：
 * 1 1种
 * 12  2种
 * 123 3种
 * 120 1种
 */
public class p91 {
    public int numDecodings(String s) {
        char[] chars = s.toCharArray();
        return numDecodings(chars, 0);
    }

    public int numDecodings(char[] chars, int start) {
        if (chars[start] == '0')
            return 0;
        if (chars.length - start == 1) return 1;
        if ('1' == chars[start]) {
            int a = numDecodings(chars, start + 1); // 单个数字
            if (chars.length - start == 2) {
                return a + 1;
            }
            int b = numDecodings(chars, start + 2);// 两个数字
            if (a == 0 && b == 0) return 0;
            if (a == 0) return b;
            if (b == 0) return a;
            return a + b;
        }
        if ('2' == chars[start]) {
            int a = numDecodings(chars, start + 1); // 单个数字
            if (chars.length - start == 2) {
                if (chars[start + 1] >= '7' && chars[start + 1] <= '9')
                    return a;
                else return a + 1;
            }
            if (chars[start + 1] >= '7' && chars[start + 1] <= '9') {
                return a;
            } else {
                int b = numDecodings(chars, start + 2);// 两个数字
                if (a == 0 && b == 0) return 0;
                if (a == 0) return b;
                if (b == 0) return a;
                return a + b;
            }
        }
        if ('3' <= chars[start] && chars[start] <= '9') {
            return numDecodings(chars, start + 1); // 单个数字
        }

        return -1;
    }
}
