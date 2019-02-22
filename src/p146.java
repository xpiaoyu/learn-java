import java.util.HashMap;

public class p146 {
    class Node {
        Node pre;
        Node next;
        int key;
        int val;

        Node(int key, int val) {
            this.key = key;
            this.val = val;
        }
    }

    int capacity;
    int remain;
    Node head;
    Node tail;
    HashMap<Integer, Node> nodeMap;

    public p146(int capacity) {
        this.nodeMap = new HashMap<>();
        this.capacity = capacity;
        this.remain = capacity;
        this.head = new Node(0, 0);
        this.tail = null;
    }

    public int get(int key) {
        Node node = nodeMap.get(key);
        if (node != null) {
            if (node.pre == null) {
                /*this is the first node*/
                return node.val;
            }
            if (node.next == null) {
                /*it's the last node but it's not first node*/
                tail = node.pre;
                tail.next = null;

                node.pre = null;
                node.next = head.next;
                node.next.pre = node;

                head.next = node;
                return node.val;
            }
            /*it's a common node*/
            node.pre.next = node.next;
            node.next.pre = node.pre;

            node.pre = null;
            node.next = head.next;

            node.next.pre = node;
            head.next = node;
            return node.val;
        } else {
            return -1;
        }
    }

    public void put(int key, int value) {
        Node node = nodeMap.get(key);
        if (node != null) {
            node.val = value;
            if (node.pre == null) {
                /*this is the first node*/
                return;
            }
            if (node.next == null) {
                /*it's the last node and it's not first node*/
                tail = node.pre;
                tail.next = null;

                node.pre = null;
                node.next = head.next;
                node.next.pre = node;

                head.next = node;
                return;
            }
            /*it's a common node*/
            node.pre.next = node.next;
            node.next.pre = node.pre;

            node.pre = null;
            node.next = head.next;

            node.next.pre = node;
            head.next = node;
            return;
        }
        /*key's NOT existed*/
        if (remain > 0) {
            /*cache's NOT full*/
            Node newNode = new Node(key, value);
            newNode.next = head.next;
            if (head.next != null) head.next.pre = newNode;
            head.next = newNode;
            if (tail == null) tail = newNode;
            nodeMap.put(key, newNode);
            remain--;
        } else {
            /*cache is full*/
            /*remove the last node*/
            nodeMap.remove(tail.key);
            if (tail.pre != null) {
                /*cache has more than 1 elem*/
                tail.pre.next = null;
                tail = tail.pre;
            }
            Node newNode = new Node(key, value);
            newNode.next = head.next;
            head.next.pre = newNode;
            head.next = newNode;
            nodeMap.put(key, newNode);
        }
    }
}
