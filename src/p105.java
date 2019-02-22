public class p105 {
    private class TreeNode {
        int val;
        TreeNode left;
        TreeNode right;

        TreeNode(int x) {
            val = x;
        }
    }

    int inIndex=0,preIndex=0;

    public TreeNode buildTree(int[] preorder, int[] inorder) {
        if(preorder==null || inorder==null || preorder.length==0 || inorder.length==0)
            return null;
        return buildTreeUtil(preorder,inorder,Integer.MAX_VALUE);
    }

    public TreeNode buildTreeUtil(int[] preorder,int[] inorder,int target){
        if(inIndex>=inorder.length || inorder[inIndex]==target) return null;
        TreeNode root = new TreeNode(preorder[preIndex++]);
        root.left = buildTreeUtil(preorder,inorder,root.val);
        inIndex++;
        root.right = buildTreeUtil(preorder,inorder,target);
        return root;
    }
}
