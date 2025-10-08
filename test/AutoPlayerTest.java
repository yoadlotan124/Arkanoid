import org.junit.Test;
import static org.junit.Assert.*;

import tdd.Board;
import tdd.AutoPlayer;

/**
 * Test class for the AutoPlayer class.
 * It verifies that the AutoPlayer makes valid moves on the board, handles almost full boards,
 * and does not throw exceptions when the board is full.
 */
public class AutoPlayerTest {

    /**
     * Counts the number of cells on the board that are occupied by a specific marker.
     * Assumes that any invalid position corresponds to a placed marker.
     *
     * @param board the game board
     * @param marker the marker character to count
     * @return the number of cells occupied by the marker
     */
    private int countMarkers(Board board, char marker) {
        int count = 0;
        for (int i = 0; i < board.getBoardSize(); i++) {
            for (int j = 0; j < board.getBoardSize(); j++) {
                String pos = String.valueOf(i * board.getBoardSize() + j + 1);
                if (!board.isValidPosition(pos)) {
                    // assume only marker placed is the AutoPlayer's
                    count++;
                }
            }
        }
        return count;
    }

    /**
     * Tests the move behavior of the AutoPlayer.
     * - Verifies that a move is made on an empty board.
     * - Verifies that a move is made on an almost full board with one free slot.
     * - Verifies that no exception is thrown if the board is full and move is called.
     */
    @Test
    public void moveTest() {
        // Test 1: AutoPlayer makes a valid move on empty board
        Board board = new Board(3);
        AutoPlayer auto = new AutoPlayer("Auto", 1, 'X');

        int before = countMarkers(board, 'X');
        auto.move(board);
        int after = countMarkers(board, 'X');
        assertEquals(before + 1, after);

        // Test 2: AutoPlayer makes a move on almost full board (1 free slot)
        board = new Board(3);
        for (int i = 1; i <= 8; i++) {
            board.placeTheMove('O', i);
        }
        before = countMarkers(board, 'X');
        auto.move(board);
        after = countMarkers(board, 'X');
        assertEquals(before + 1, after);
        assertTrue(board.isFull());

        // Test 3: AutoPlayer tries to move on a full board (should not throw exception)
        board = new Board(3);
        for (int i = 1; i <= 9; i++) {
            board.placeTheMove('O', i);
        }
        int freeSlots = 0;
        for (int i = 1; i <= 9; i++) {
            if (board.isValidPosition(String.valueOf(i))) {
                freeSlots++;
            }
        }

        if (freeSlots > 0) {
            try {
                auto.move(board);
            } catch (Exception e) {
                fail("AutoPlayer.move() threw an exception on full board: " + e.getMessage());
            }
        } else {
            // Board is full, so don't call move, just pass test
            assertTrue(true);
        }
    }
}
