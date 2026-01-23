import java.util.Scanner;
import java.util.ArrayList;

public class Ziq {
    public static void main(String[] args) {
       System.out.println("Hello I'm Ziq\n" + "What can I do for you?\n");

       ArrayList<Task> list = new ArrayList<>();

       Scanner sc = new Scanner(System.in);
       while (list.size()<100) {
           String input = sc.nextLine();
           try {
               if (input.equalsIgnoreCase("bye")) {
                   System.out.println("Bye! Hope to see you again soon!");
                   break;
               } else if (input.equalsIgnoreCase("list")) {
                   System.out.println("Here are the tasks in your list:");
                   for (int i = 1; i <= list.size(); i++) {
                       System.out.println(i + ". " +
                               list.get(i - 1));
                   }
               } else if (input.startsWith("mark ")) {
                   int index = Integer.parseInt(input.substring(5)) - 1;
                   list.get(index).markAsDone();
                   System.out.println("OK, I've marked this task as done:\n " + list.get(index));
               } else if (input.startsWith("unmark ")) {
                   int index = Integer.parseInt(input.substring(7)) - 1;
                   list.get(index).unmark();
                   System.out.println("OK, I've marked this task as not done yet:\n " + list.get(index));
               } else if (input.startsWith("todo ")) {
                   list.add(handleTodo(input));
                   System.out.println("Got it. I've added this task:\n " + list.get(list.size() - 1));
                   System.out.println("Now you have " + list.size() + " tasks in the list.");
               } else if (input.startsWith("deadline ")) {
                   list.add(handleDeadline(input));
                   System.out.println("Got it. I've added this task:\n " + list.get(list.size() - 1));
                   System.out.println("Now you have " + list.size() + " tasks in the list.");
               } else if (input.startsWith("event ")) {
                   String[] parts = input.substring(6).split(" /from | /to ");
                   list.add(new Event(parts[0], parts[1], parts[2]));
                   System.out.println("Got it. I've added this task:\n " + list.get(list.size() - 1));
                   System.out.println("Now you have " + list.size() + " tasks in the list.");
               } else if (input.startsWith("delete ")) {
                   try {
                       int index = Integer.parseInt(input.substring(7)) - 1;

                       if (index < 0 || index >= list.size()) {
                           throw new ZiqException("does not exist");
                       }

                       Task removedTask = list.remove(index);

                       System.out.println("task removed:\n  " + removedTask);
                       System.out.println("Now you have " + list.size() + " tasks in the list.");

                   } catch (NumberFormatException e) {
                       throw new ZiqException("Please provide a valid number to delete.");
                   }
               }else {
                   throw new ZiqException("idk lol");
           }
       }
       catch (ZiqException e) {
            System.out.println("oop. " + e.getMessage());
        }
       }
    }

private static Todo handleTodo(String input) throws ZiqException {
    if (input.trim().equals("todo")) {
        throw new ZiqException("The description of a todo cannot be empty.");
    }
    return new Todo(input.substring(5));
}

private static Deadline handleDeadline(String input) throws ZiqException {
    if (!input.contains(" /by ")) {
        throw new ZiqException("deadline must have a time (use /by).");
    }
    String[] parts = input.substring(9).split(" /by ");
    return new Deadline(parts[0], parts[1]);
}

private static void handleList(Task[] list, int count) throws ZiqException {
    if (count == 0) {
        throw new ZiqException("list empty...");
    }
    for (int i = 0; i < count; i++) {
        System.out.println((i + 1) + "." + list[i]);
    }
}

}