import java.util.ArrayList;
import java.util.List;

public class p102 {
    private class TreeNode {
        int val;
        TreeNode left;
        TreeNode right;

        TreeNode(int x) {
            val = x;
        }
    }

    public List<List<Integer>> levelOrder(TreeNode root) {
        List<List<Integer>> result = new ArrayList<>();
        levelOrder(root, 0, result);
        return result;
    }

    private void levelOrder(TreeNode root, int levelNum, List<List<Integer>> result) {
        assert result != null;
        if (root == null)
            return;
        if (result.size() < levelNum + 1) { // The target level list is empty
            List<Integer> list = new ArrayList<>();
            list.add(root.val);
            result.add(list);
        } else {
            result.get(levelNum).add(root.val);
        }
        levelOrder(root.left, levelNum + 1, result);
        levelOrder(root.right, levelNum + 1, result);
    }
}
