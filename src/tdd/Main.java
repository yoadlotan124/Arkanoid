package tdd;

import java.util.Scanner;

/**
 * Entry point for the TicTacToe game application.
 * This class contains the main method which initializes
 * a TicTacToe game instance and starts the game loop.
 */
public class Main {

    /**
     * The main method that launches the TicTacToe game.
     *
     * @param args Command line arguments (not used)
     */
    public static void main(String[] args) {
        Scanner scanner = new Scanner(System.in);
        int choice;
        System.out.println("How many players [0-2]?");
        while (true) {
            String input = scanner.nextLine();
            try {
                choice = Integer.parseInt(input);
                if (choice >= 0 && choice <= 2) {
                    break;  // valid input, exit the loop
                } else {
                    System.out.println("How many players [0-2]?");
                }
            } catch (NumberFormatException e) {
                System.out.println("How many players [0-2]?");
            }
        }

        Player p1, p2;

        switch (choice) {
            case 0:
                p1 = new AutoPlayer("PLAYER-X", 1, 'X');
                p2 = new AutoPlayer("PLAYER-O", 2, 'O');
                break;
            case 1:
                p1 = new Player("PLAYER-X", 1, 'X');
                p2 = new AutoPlayer("PLAYER-O", 2, 'O');
                break;
            default:
                p1 = new Player("PLAYER-X", 1, 'X');
                p2 = new Player("PLAYER-O", 2, 'O');
                break;
        }

        TicTacToe game = new TicTacToe(p1, p2);
        game.play();
    }
}