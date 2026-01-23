import java.util.Scanner;

public class Ziq {
    public static void main(String[] args) {
       System.out.println("Hello I'm Ziq\n" + "What can I do for you?\n");

       String[] list = new String[100];
       int listCount = 0;

       Scanner sc = new Scanner(System.in);
       while (listCount<100) {
           String input = sc.nextLine();
           if (input.equalsIgnoreCase("bye")) {
               System.out.println("Bye! Hope to see you again soon!");
               break;
           } else if (input.equalsIgnoreCase("list")) {
               for (int i = 1; i <= listCount; i++) {
                   System.out.println(i + ". " +
                           list[i-1]);
               }
           } else {
               System.out.println("added " + input);
               list[listCount] = input;
               listCount++;
           }
       }
    }
}
