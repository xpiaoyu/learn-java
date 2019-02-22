import java.util.Stack;

public class p25 {
    public static class ListNode {
        int val;
        ListNode next;

        ListNode(int x) {
            val = x;
        }
    }

    public static ListNode head = new ListNode(0);

    static {
        ListNode p = head;
        for(int i =1;i<=5;i++) {
            p.next = new ListNode(i);
            p = p.next;
        }
    }

    public static ListNode reverseKGroup(ListNode head, int k) {
        if (head == null || k < 2) {
            return head;
        }
        Stack<ListNode> stack = new Stack<>();
        ListNode p = head;
        int count = 0;
        for (; count < k; count++) {
            if (p == null)
                break;
            stack.push(p);
            p = p.next;
        }
        if (count == k) {
            ListNode nextHead = reverseKGroup(p, k);
            ListNode newHead = stack.pop();
            ListNode current = newHead;
            for (int i = 0; i < k-1; i++) {
                current.next = stack.pop();
                current = current.next;
            }
            current.next = nextHead;
            return newHead;
        } else {  //链表中元素个数小于K
            return head;
        }
    }
}
