import java.util.Scanner;

public class Ziq {
    public static void main(String[] args) {
       System.out.println("Hello I'm Ziq\n" + "What can I do for you?\n");

       Task[] list = new Task[100];
       int listCount = 0;

       Scanner sc = new Scanner(System.in);
       while (listCount<100) {
           String input = sc.nextLine();
           if (input.equalsIgnoreCase("bye")) {
               System.out.println("Bye! Hope to see you again soon!");
               break;
           } else if (input.equalsIgnoreCase("list")) {
               System.out.println("Here are the tasks in your list:");
               for (int i = 1; i <= listCount; i++) {
                   System.out.println(i + ". " +
                           list[i - 1]);
               }
           } else if (input.startsWith("mark ")) {
               int index = Integer.parseInt(input.substring(5)) - 1;
               list[index].markAsDone();
               System.out.println("OK, I've marked this task as done:\n " + list[index]);
           }  else if (input.startsWith("unmark ")) {
               int index = Integer.parseInt(input.substring(7)) - 1;
               list[index].unmark();
               System.out.println("OK, I've marked this task as not done yet:\n " + list[index]);
           } else if (input.startsWith("todo ")) {
               list[listCount] = new Todo(input.substring(5));
               listCount++;
               System.out.println("Got it. I've added this task:\n " + list[listCount-1]);
               System.out.println("Now you have " + listCount + " tasks in the list.");
           } else if (input.startsWith("deadline ")) {
               String[] parts = input.substring(9).split(" /by ");
               list[listCount] = new Deadline(parts[0], parts[1]);
               listCount++;
               System.out.println("Got it. I've added this task:\n " + list[listCount-1]);
               System.out.println("Now you have " + listCount + " tasks in the list.");
           } else if (input.startsWith("event ")) {
               String[] parts = input.substring(6).split(" /from | /to ");
               list[listCount] = new Event(parts[0], parts[1], parts[2]);
               listCount++;
               System.out.println("Got it. I've added this task:\n " + list[listCount-1]);
               System.out.println("Now you have " + listCount + " tasks in the list.");
           }
           else {
               System.out.println("added " + input);
               list[listCount] = new Task(input);
               listCount++;
           }
       }
    }
}
