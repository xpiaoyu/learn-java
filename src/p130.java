public class p130 {
    public void solve(char[][] board) {
        int height = board.length;
        if (height == 0) return;
        int width = board[0].length;
        char[][] flag = new char[height][width];
        for (int i = 0; i < width; i++) {
            if (board[0][i] == 'O' && flag[0][i] == 0)
                expand(board, flag, i, 0);
            if (board[height - 1][i] == 'O' && flag[height - 1][i] == 0)
                expand(board, flag, i, height - 1);
        }
        for (int i = 0; i < height; i++) {
            if (board[i][0] == 'O' && flag[i][0] == 0)
                expand(board, flag, 0, i);
            if (board[i][width - 1] == 'O' && flag[i][width - 1] == 0)
                expand(board, flag, width - 1, i);
        }

        for (int y = 0; y < height; y++) {
            for (int x = 0; x < width; x++) {
                if (board[y][x] == 'O' && flag[y][x] == 0) board[y][x] = 'X';
            }
        }
    }

    public void expand(char[][] board, char[][] flag, int x, int y) {
        if (x < 0 || y < 0 || x >= board[0].length || y >= board.length) return;
        flag[y][x] = 1;
        if (y - 1 >= 0 && flag[y - 1][x] == 0 && board[y - 1][x] == 'O')
            expand(board, flag, x, y - 1);
        if (y + 1 < board.length && flag[y + 1][x] == 0 && board[y + 1][x] == 'O')
            expand(board, flag, x, y + 1);
        if (x - 1 >= 0 && flag[y][x - 1] == 0 && board[y][x - 1] == 'O')
            expand(board, flag, x - 1, y);
        if (x + 1 < board[0].length && flag[y][x + 1] == 0 && board[y][x + 1] == 'O')
            expand(board, flag, x + 1, y);
    }
}
