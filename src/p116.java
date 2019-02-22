public class p116 {
    public class TreeLinkNode {
        int val;
        TreeLinkNode left, right, next;

        TreeLinkNode(int x) {
            val = x;
        }
    }

    public void connect(TreeLinkNode root) {
        if (root == null) return;
        if (root.left != null) {
            root.left.next = root.right;
        }
        connect(root.left);
        connect(root.right);
        connectCross(root);
    }

    public void connectCross(TreeLinkNode root) {
        if (root == null) return;
        if (root.left == null) return;
        TreeLinkNode levelFirst = root;
        while (root.next != null) {
            root.right.next = root.next.left;
            root = root.next;
        }
        connectCross(levelFirst.left);
    }
}
