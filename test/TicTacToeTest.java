import org.junit.Before;
import org.junit.BeforeClass;
import org.junit.Test;
import static org.junit.Assert.*;

import tdd.Player;
import tdd.Board;
import tdd.TicTacToe;

/**
 * Test class for the TicTacToe game logic and Board behavior.
 * Includes tests for checking winners, validating positions, board fullness,
 * handling winners, and verifying board size input.
 */
public class TicTacToeTest {

    private static TicTacToe game;
    private Board board3;
    private Board board6;

    /**
     * Initializes the TicTacToe game instance once before all tests.
     */
    @BeforeClass
    public static void setUpClass() {
        // Must be static for @BeforeClass
        game = new TicTacToe();
    }

    /**
     * Sets up fresh boards before each test to ensure tests do not interfere.
     */
    @Before
    public void setUp() {
        // Runs before each test, init fresh boards
        board3 = new Board(3);
        board6 = new Board(6);
    }

    /**
     * Tests the checkWin method for various winning and non-winning board states.
     * Verifies horizontal, vertical, diagonal wins, and no-win situations
     * on different board sizes.
     */
    @Test
    public void checkWinnerTest() {
        char[][] winningBoardX = {
                {'X', 'X', 'X'},
                {'O', 'O', ' '},
                {' ', ' ', ' '}
        };
        board3.setBoard(winningBoardX);

        assertTrue("Player X should be detected as winner.", board3.checkWin('X'));

        char[][] winningBoardO = {
                {'O', 'X', 'X'},
                {'O', ' ', ' '},
                {'O', ' ', ' '}
        };
        board3.setBoard(winningBoardO);

        assertTrue("Player O should be detected as winner.", board3.checkWin('O'));

        char[][] noWinnerBoard = {
                {'X', 'O', 'X'},
                {'O', 'X', 'O'},
                {'O', 'X', 'O'}
        };
        board3.setBoard(noWinnerBoard);

        assertFalse("Player X should NOT be detected as winner.", board3.checkWin('X'));
        assertFalse("Player O should NOT be detected as winner.", board3.checkWin('O'));

        char[][] winningBoardX3 = {
                {'X', ' ', ' '},
                {'O', 'X', ' '},
                {'O', ' ', 'X'}
        };
        board3.setBoard(winningBoardX3);

        assertTrue("Player X should be detected as winner.", board3.checkWin('X'));

        char[][] winningBoardX2 = {
                {'X', 'X', 'X', 'X', 'X', 'X'},
                {'O', 'O', ' ', 'O', 'O', 'O'},
                {'O', ' ', ' ', ' ', ' ', ' '},
                {'O', ' ', ' ', ' ', 'X', ' '},
                {'O', ' ', 'X', ' ', ' ', ' '},
                {' ', ' ', ' ', ' ', ' ', 'O'}
        };
        board6.setBoard(winningBoardX2);

        assertTrue("Player X should be detected as winner.", board6.checkWin('X'));
    }

    /**
     * Tests that the handleWinner method correctly increments the winner's score
     * without affecting the other player's score.
     */
    @Test
    public void handleWinnerTest() {
        Player player1 = new Player("PLAYER-X", 1, 'X');
        Player player2 = new Player("PLAYER-O", 2, 'O');

        int oldScore = player1.getNumberOfWins();

        game.handleWinner(player1);

        assertEquals("Player1's score should increment by 1", oldScore + 1, player1.getNumberOfWins());
        assertEquals("Player2's score should remain unchanged", 0, player2.getNumberOfWins());
    }

    /**
     * Tests the isValidPosition method to ensure it correctly identifies
     * valid and invalid positions on the board, including positions already taken
     * or out of range.
     */
    @Test
    public void isValidPositionTest() {
        assertTrue("Position 2 should be valid on empty board", board3.isValidPosition("2"));

        board3.placeTheMove('X', 1);
        assertFalse("Position 1 should be invalid after a move", board3.isValidPosition("1"));

        assertFalse("Outside of the board should be invalid", board3.isValidPosition("-1"));
        assertFalse("Outside of the board should be invalid", board3.isValidPosition("17"));

        assertTrue("Position 2 should be valid if not played yet", board3.isValidPosition("2"));
        board6.placeTheMove('X', 1);

        assertFalse("Position 1 should be invalid after a move", board6.isValidPosition("1"));
        assertFalse("Outside of the board should be invalid", board6.isValidPosition("-1"));
        assertFalse("Outside of the board should be invalid", board6.isValidPosition("38"));
    }

    /**
     * Tests the isFull method to verify it returns true for full boards
     * and false for boards with empty spaces, on different board sizes.
     */
    @Test
    public void isFullTest() {
        char[][] fullBoard = {
                {'X', 'O', 'X'},
                {'O', 'X', 'O'},
                {'O', 'X', 'O'}
        };
        board3.setBoard(fullBoard);
        assertTrue("Board should be full", board3.isFull());

        char[][] notFullBoard = {
                {'X', ' ', 'X'},
                {' ', ' ', ' '},
                {'O', ' ', ' '}
        };
        board3.setBoard(notFullBoard);
        assertFalse("Board isn't full", board3.isFull());

        char[][] fullBoard6 = {
                {'X', 'X', 'X', 'X', 'X', 'X'},
                {'X', 'X', 'X', 'X', 'X', 'X'},
                {'X', 'X', 'X', 'X', 'X', 'X'},
                {'X', 'X', 'X', 'X', 'X', 'X'},
                {'X', 'X', 'X', 'X', 'X', 'X'},
                {'X', 'X', 'X', 'X', 'X', 'X'}
        };
        board6.setBoard(fullBoard6);
        assertTrue("Board should be full", board6.isFull());

        char[][] notFullBoard6 = {
                {' ', ' ', ' ', ' ', ' ', ' '},
                {' ', ' ', ' ', ' ', ' ', ' '},
                {' ', ' ', ' ', ' ', ' ', ' '},
                {' ', ' ', ' ', ' ', ' ', ' '},
                {' ', ' ', ' ', ' ', ' ', ' '},
                {' ', ' ', ' ', ' ', ' ', ' '}
        };
        board6.setBoard(notFullBoard6);
        assertFalse("Board isn't full", board6.isFull());
    }

    /**
     * Tests the verifyBoardSize method to confirm that it correctly
     * rejects invalid inputs (non-numeric, negative, zero, empty, too large)
     * and accepts valid board sizes.
     */
    @Test
    public void verifyBoardSizeTest() {
        assertFalse("The board size cannot be letters.", game.verifyBoardSize("jdfgfd"));
        assertFalse("The board size cannot be a sequence of symbols.", game.verifyBoardSize("%gre4#$$#"));

        assertFalse("The board size cannot be zero.", game.verifyBoardSize("0"));
        assertFalse("The board size cannot be negative.", game.verifyBoardSize("-3"));
        assertFalse("Empty input should be invalid.", game.verifyBoardSize(""));
        assertFalse("Null input should be invalid.", game.verifyBoardSize(null));

        // For valid inputs, just assert true
        assertTrue("3 should be a valid board size.", game.verifyBoardSize("3"));
        assertTrue("5 should be a valid board size.", game.verifyBoardSize("5"));

        assertFalse("Board size too large should be invalid.", game.verifyBoardSize("1000"));
    }
}
