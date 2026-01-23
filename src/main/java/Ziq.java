import java.util.Scanner;

public class Ziq {
    public static void main(String[] args) {
       System.out.println("Hello I'm Ziq\n" + "What can I do for you?\n");

       Scanner sc = new Scanner(System.in);
       while (true) {
           String input = sc.nextLine();
           if (input.equalsIgnoreCase("bye")) {
               System.out.println("Bye! Hope to see you again soon!");
               break;
           } else {
               System.out.println(input);
           }
       }
    }
}
