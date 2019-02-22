public class p98 {
    public class TreeNode {
        int val;
        TreeNode left;
        TreeNode right;

        TreeNode(int x) {
            val = x;
        }
    }

    public boolean isValidBST(TreeNode root) {
        if (root == null) return true;
        boolean realMinVal = false, realMaxVal = false;
        if (root.val == Integer.MIN_VALUE) realMinVal = true;
        if (root.val == Integer.MAX_VALUE) realMaxVal = true;
        return isValidBSTLeft(root.left, Integer.MIN_VALUE, root.val, false, realMaxVal) & isValidBSTLeft(root.right, root.val, Integer.MAX_VALUE, realMinVal, false);
    }

    public boolean isValidBSTLeft(TreeNode root, int min, int max, boolean realMinVal, boolean realMaxVal) {
        if (root == null) return true;
        if ((root.val > min || (root.val == min && !realMinVal && root.val == Integer.MIN_VALUE)) && (root.val < max || (max == root.val && !realMaxVal && root.val == Integer.MAX_VALUE))) {
            if (root.val == Integer.MIN_VALUE) realMinVal = true;
            if (root.val == Integer.MAX_VALUE) realMaxVal = true;
            return isValidBSTLeft(root.left, min, root.val, realMinVal, realMaxVal) & isValidBSTLeft(root.right, root.val, max, realMinVal, realMaxVal);
        }
        return false;
    }

//    public boolean isValidBSTRight(TreeNode root, int min, int max, boolean realMinVal, boolean realMaxVal) {
//        if (root == null) return true;
//        if ((root.val > min || (root.val == min && !realMinVal && root.val == Integer.MIN_VALUE)) && (root.val < max || (max == root.val && !realMaxVal && root.val == Integer.MAX_VALUE))) {
//            if (root.val == Integer.MIN_VALUE) realMinVal = true;
//            if (root.val == Integer.MAX_VALUE) realMaxVal = true;
//            return isValidBSTLeft(root.left, min, root.val, realMinVal, realMaxVal) & isValidBSTRight(root.right, root.val, max, realMinVal, realMaxVal);
//        }
//        return false;
//    }
}
