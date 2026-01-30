import java.util.Scanner;

public class Ui {
    private final Scanner sc;

    public Ui() {
        this.sc = new Scanner(System.in);
    }

    public void welcomeUser() {
        System.out.println("Hello, I'm Ziq!\nWhat can I do for you?\n");
    }

    public String readCommand() {
        return sc.nextLine();
    }

    public void diagnoseError(String message) {
        System.out.println("oop. " + message);
    }

    public void showLoadingError() {
        System.out.println("oop. error loading,,, starting afresh!");
    }
}
