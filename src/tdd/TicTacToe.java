package tdd;

import java.util.Scanner;

/**
 * Represents a TicTacToe game, managing players, the board, and game flow.
 * Supports playing multiple rounds, tracking wins, and choosing board size.
 */
public class TicTacToe {
    private static Scanner sc;
    private Player player1 = new Player("PLAYER-X", 1, 'X');
    private Player player2 = new Player("PLAYER-O", 2, 'O');
    private Board board;

    /**
     * Default constructor that initializes the input scanner.
     */
    public TicTacToe() {
        sc = new Scanner(System.in);
    }

    /**
     * Constructor to initialize the game with specific players.
     *
     * @param player1 the first player
     * @param player2 the second player
     */
    public TicTacToe(Player player1, Player player2) {
        this.player1 = player1;
        this.player2 = player2;
        sc = new Scanner(System.in);
    }

    /**
     * Starts and controls the main game loop, handling turns, win conditions,
     * ties, and replay prompts.
     */
    public void play() {
        Player currentPlayer = this.player1;
        if (!this.playAgain()) {
            this.gameOver();
            return;
        }

        while (true) {
            currentPlayer.move(this.board);
            if (this.board.checkWin(currentPlayer.getMarker())) {
                this.handleWinner(currentPlayer);
                if (!this.playAgain()) {
                    this.gameOver();
                    return;
                }
            } else if (this.board.isFull()) {
                System.out.println("The board is full. It's a tie!");
                if (!this.playAgain()) {
                    this.gameOver();
                    return;
                } else {
                    continue;
                }
            }

            currentPlayer = currentPlayer == this.player1 ? this.player2 : this.player1;
        }
    }

    /**
     * Ends the game session by printing results and closing all input scanners.
     */
    private void gameOver() {
        this.printResults();
        sc.close();
        this.player1.closeSC();
        this.player2.closeSC();
    }

    /**
     * Handles the logic when a player wins a round, increments win count and
     * prints a winning message.
     *
     * @param winner the player who won the round
     */
    public void handleWinner(Player winner) {
        winner.incrementNumberOfWins();
        System.out.println("PLAYER-" + winner.getMarker() + " has won this round!");
    }

    /**
     * Prints a welcome message prompting the user to start a new game or exit.
     */
    private void welcome() {
        System.out.println("Hit \"y/Y\" to start a new game. Or hit any other key to exit.");
    }

    /**
     * Prompts the user to enter a preferred board size, validates input,
     * and returns the valid size.
     *
     * @return the valid board size as an integer
     */
    private int getBoardSize() {
        while (true) {
            System.out.print("Please enter your preferred SIZE of the board");
            System.out.println(" (from 3 to 10. 3 -> 3x3; 4 -> 4x4; 10 -> 10x10, etc): ");

            if (sc.hasNextLine()) {
                String userInput = sc.nextLine();
                if (this.verifyBoardSize(userInput)) {
                    return Integer.parseInt(userInput);
                }
            }
        }
    }

    /**
     * Verifies if the given board size string is valid.
     * - Not null or empty
     * - An integer between 3 and 10 inclusive
     *
     * @param boardSize the board size string to verify
     * @return true if valid, false otherwise
     */
    public boolean verifyBoardSize(String boardSize) {
        if (boardSize == null || boardSize.isEmpty()) {
            return false;  // null or empty is invalid
        }

        int size;
        try {
            size = Integer.parseInt(boardSize);
        } catch (NumberFormatException e) {
            return false;  // not a valid integer
        }

        final int min = 3;
        final int max = 10;

        if (size < min || size > max) {
            return false;  // outside valid range
        }

        return true;
    }

    /**
     * Prompts the user whether they want to play a new game.
     * If yes, prompts for board size and creates a new Board.
     *
     * @return true if user wants to play again, false otherwise
     */
    private boolean playAgain() {
        this.welcome();
//        sc = new Scanner((System.in));
        if (sc.hasNextLine()) {
            String userDecision = sc.nextLine();
            if (userDecision.equalsIgnoreCase("Y")) {
                int boardSize = this.getBoardSize();
                this.board = new Board(boardSize);
                return true;
            }
        }
        return false;
    }

    /**
     * Prints the final results of the game, including each player's total wins
     * and the overall winner or a tie message.
     */
    public void printResults() {
        System.out.println("Player " + this.player1.getName()
                + " has won: " + this.player1.getNumberOfWins() + " time(s).");
        System.out.println("Player " + this.player2.getName()
                + " has won: " + this.player2.getNumberOfWins() + " time(s).");

        if (this.player1.getNumberOfWins() == this.player2.getNumberOfWins()) {
            System.out.println("Its a tie!");
        } else {
            String winner = this.player1.getNumberOfWins() > this.player2.getNumberOfWins()
                    ? this.player1.getName() : this.player2.getName();
            System.out.println("The final winner is: " + winner + "!!!");
        }

        System.out.println();
    }

    /**
     * Returns the current game board.
     *
     * @return the Board object
     */
    public Board getBoard() {
        return this.board;
    }
}
