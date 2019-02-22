import java.util.ArrayList;
import java.util.LinkedList;
import java.util.List;

public class p103 {
    private class TreeNode {
        int val;
        TreeNode left;
        TreeNode right;

        TreeNode(int x) {
            val = x;
        }
    }

    public List<List<Integer>> zigzagLevelOrder(TreeNode root) {
        List<LinkedList<Integer>> result = new ArrayList<>();
        zigzagLevelOrder(root, 0, result, true);
        return new ArrayList<>(result);
    }

    public void zigzagLevelOrder(TreeNode root, int levelNum, List<LinkedList<Integer>> result, boolean direction) {
        // Direction equals TRUE left to right; FALSE right to left.
        assert result != null;
        if (root == null)
            return;
        if (result.size() < levelNum + 1) { // The target level list is empty
            LinkedList<Integer> list = new LinkedList<>();
            if (direction) {
                list.addLast(root.val);
            } else {
                list.addFirst(root.val);
            }
            result.add(list);
        } else {
            if (direction) {
                result.get(levelNum).addLast(root.val);
            } else {
                result.get(levelNum).addFirst(root.val);
            }
        }
        zigzagLevelOrder(root.left, levelNum + 1, result, !direction);
        zigzagLevelOrder(root.right, levelNum + 1, result, !direction);
    }
}
