package ziq;

import java.util.Scanner;

/**
 * Handles user interface interactions.
 * Manages input/output operations for the application.
 */
public class Ui {
    private final Scanner sc;

    /**
     * Constructs a new Ui instance with a Scanner for reading user input.
     */
    public Ui() {
        this.sc = new Scanner(System.in);
    }

    /**
     * Displays the welcome message to the user.
     */
    public void welcomeUser() {
        System.out.println("Hello, I'm Ziq!\nWhat can I do for you?\n");
    }

    /**
     * Reads a command from the user.
     */
    public String readCommand() {
        return sc.nextLine();
    }

    /**
     * Displays an error message to the user.
     */
    public void diagnoseError(String message) {
        System.out.println("oop. " + message);
    }

    /**
     * Displays an error message when loading tasks fails.
     */
    public void showLoadingError() {
        System.out.println("oop. error loading,,, starting afresh!");
    }
}
