import sun.reflect.generics.tree.Tree;

public class p124 {
    public static class TreeNode {
        int val;
        TreeNode left;
        TreeNode right;

        TreeNode(int x) {
            val = x;
        }
    }

    private static int maxSum = Integer.MIN_VALUE;

    private static TreeNode treeNode = new TreeNode(0);

    public static void run() {
        maxPathSum(treeNode);
    }

    public static int maxPathSum(TreeNode root) {
        maxPath(root);
        return maxSum;
    }

    public static int maxPath(TreeNode root) {
        if (root == null) return 0;
        int maxLeftPath = maxPath(root.left);
        int maxRightPath = maxPath(root.right);
        maxLeftPath = maxLeftPath > 0 ? maxLeftPath : 0;
        maxRightPath = maxRightPath > 0 ? maxRightPath : 0;
        if (maxSum < maxLeftPath + maxRightPath + root.val) maxSum = maxLeftPath + maxRightPath + root.val;
        return maxLeftPath > maxRightPath ? maxLeftPath + root.val : maxRightPath + root.val;
    }
}
