package tdd;

/**
 * Represents the TicTacToe game board and provides methods to manipulate
 * and query the board state.
 */
public class Board {
    private int boardSize;
    private char[][] board;

    /**
     * Constructs a new Board with the given size and initializes it.
     *
     * @param boardSize the size of the board (e.g., 3 for a 3x3 board)
     */
    public Board(int boardSize) {
        this.boardSize = boardSize;
        this.setBoard();
    }

    /**
     * Sets the size of the board.
     *
     * @param boardSize the new size of the board
     */
    public void setBoardSize(int boardSize) {
        this.boardSize = boardSize;
    }

    /**
     * Returns the current board size.
     *
     * @return the size of the board
     */
    public int getBoardSize() {
        return this.boardSize;
    }

    /**
     * Initializes or resets the board with empty spaces.
     */
    private void setBoard() {
        this.board = new char[this.boardSize][this.boardSize];

        for (int i = 0; i < this.boardSize; ++i) {
            for (int j = 0; j < board[0].length; ++j) {
                board[i][j] = ' ';
            }
        }
    }

    /**
     * Copies a given 2D character array into this board.
     *
     * @param board the 2D array representing a board state to copy from
     */
    public void setBoard(char[][] board) {
        this.board = new char[this.boardSize][this.boardSize];

        for (int i = 0; i < this.boardSize; ++i) {
            for (int j = 0; j < board[0].length; ++j) {
                this.board[i][j] = board[i][j];
            }
        }
    }

    /**
     * Prints the current board state to the console in a formatted manner.
     */
    public void print() {
        StringBuilder topBottomBoundary = new StringBuilder();

        topBottomBoundary.append("+---".repeat(Math.max(0, this.boardSize)));
        topBottomBoundary.append("+");

        for (char[] row : this.board) {
            System.out.println(topBottomBoundary);

            for (char cell : row) {
                System.out.print("| " + cell + " ");
            }
            System.out.println("|");
        }
        System.out.println(topBottomBoundary);
        System.out.println();
    }

    /**
     * Places a player's marker on the board at the specified position.
     *
     * @param checkMark the character representing the player's marker ('X' or 'O')
     * @param movePosition the board position (1-based index) where the marker will be placed
     */
    public void placeTheMove(char checkMark, int movePosition) {
        int i = (movePosition - 1) / this.board.length;
        int j = (movePosition - 1) % this.board.length;
        this.board[i][j] = checkMark;
    }

    /**
     * Checks if the given player has won the game by forming a full row,
     * column, or diagonal.
     *
     * @param player the character representing the player ('X' or 'O')
     * @return true if the player has a winning condition, false otherwise
     */
    public boolean checkWin(char player) {
        // Check rows
        for (int row = 0; row < boardSize; row++) {
            boolean rowWin = true;
            for (int col = 0; col < boardSize; col++) {
                if (board[row][col] != player) {
                    rowWin = false;
                    break;
                }
            }
            if (rowWin) {
                return true;
            }
        }

        // Check columns
        for (int col = 0; col < boardSize; col++) {
            boolean colWin = true;
            for (int row = 0; row < boardSize; row++) {
                if (board[row][col] != player) {
                    colWin = false;
                    break;
                }
            }
            if (colWin) {
                return true;
            }
        }

        // Check main diagonal
        boolean diagWin = true;
        for (int i = 0; i < boardSize; i++) {
            if (board[i][i] != player) {
                diagWin = false;
                break;
            }
        }
        if (diagWin) {
            return true;
        }

        // Check anti-diagonal
        diagWin = true;
        for (int i = 0; i < boardSize; i++) {
            if (board[i][boardSize - 1 - i] != player) {
                diagWin = false;
                break;
            }
        }
        if (diagWin) {
            return true;
        }

        // No winning condition met
        return false;
    }

    /**
     * Validates whether a given string position is valid and available on the board.
     *
     * @param position the position as a string to validate (1-based index)
     * @return true if the position is valid and empty, false otherwise
     */
    public boolean isValidPosition(String position) {
        if (position == null || position.isEmpty()) {
            return false; // null or empty is invalid
        }

        int pos;
        try {
            pos = Integer.parseInt(position);
        } catch (NumberFormatException e) {
            return false;  // not a valid integer
        }

        // Outside of bounds
        if (pos < 1 || pos > boardSize * boardSize) {
            return false;
        }

        int posRow = (pos - 1) / boardSize;
        int posCol = (pos - 1) % boardSize;

        // Position already occupied
        if (board[posRow][posCol] != ' ') {
            return false;
        }
        return true;
    }

    /**
     * Checks if the board is completely full (no empty spaces left).
     *
     * @return true if the board is full, false otherwise
     */
    public boolean isFull() {
        for (int i = 0; i < boardSize; i++) {
            for (int j = 0; j < board[0].length; j++) {
                if (board[i][j] == ' ') {
                    return false;
                }
            }
        }
        return true;
    }
}
