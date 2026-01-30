import java.util.ArrayList;
import java.util.Scanner;
import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.nio.file.Paths;

public class Ziq {
    private static final String FILE_PATH = Paths.get(".", "data", "ziq.txt").toString();

    public static void main(String[] args) {
        System.out.println("Hello, I'm Ziq!\n" + "What can I do for you?\n");

        ArrayList<Task> list = loadTasks();
        Scanner sc = new Scanner(System.in);

        while (list.size() < 100) {
            String input = sc.nextLine();
            try {
                if (input.equalsIgnoreCase("bye")) {
                    System.out.println("buh-bye!");
                    break;
                } else if (input.equalsIgnoreCase("list")) {
                    System.out.println("Here is your To-Do list!");
                    for (int i = 1; i <= list.size(); i++) {
                        System.out.println(i + ". " + list.get(i - 1));
                    }
                } else if (input.startsWith("mark ")) {
                    int index = Integer.parseInt(input.substring(5)) - 1;
                    list.get(index).markAsDone();
                    saveTasks(list);
                    System.out.println("donezos:\n " + list.get(index));
                } else if (input.startsWith("unmark ")) {
                    int index = Integer.parseInt(input.substring(7)) - 1;
                    list.get(index).unmark();
                    saveTasks(list);
                    System.out.println("un-donezos:\n " + list.get(index));
                } else if (input.startsWith("todo ")) {
                    list.add(handleToDo(input));
                    saveTasks(list);
                    System.out.println("One more To-Do\n " + list.get(list.size() - 1));
                    System.out.println("You got " + list.size() + " tasks in the list.");
                } else if (input.startsWith("deadline ")) {
                    list.add(handleDeadline(input));
                    saveTasks(list);
                    System.out.println("Dun dun dun!\n " + list.get(list.size() - 1));
                    System.out.println("You got " + list.size() + " tasks in the list.");
                } else if (input.startsWith("event ")) {
                    String[] parts = input.substring(6).split(" /from | /to ");
                    list.add(new Event(parts[0], parts[1], parts[2]));
                    saveTasks(list);
                    System.out.println("Oki, added,,,\n " + list.get(list.size() - 1));
                    System.out.println("You got " + list.size() + " tasks in the list.");
                } else if (input.startsWith("delete ")) {
                    try {
                        int index = Integer.parseInt(input.substring(7)) - 1;
                        if (index < 0 || index >= list.size()) {
                            throw new ZiqException("task does not exist");
                        }
                        Task removedTask = list.remove(index);
                        saveTasks(list);
                        System.out.println("task removed:\n  " + removedTask);
                        System.out.println("You got " + list.size() + " tasks in the list.");
                    } catch (NumberFormatException e) {
                        throw new ZiqException("the provided number is invalid...");
                    }
                } else {
                    throw new ZiqException("idk lol");
                }
            } catch (ZiqException e) {
                System.out.println("oop. " + e.getMessage());
            }
        }
    }

    private static void saveTasks(ArrayList<Task> list) {
        try {
            File f = new File(FILE_PATH);
            if (f.getParentFile() != null) {
                f.getParentFile().mkdirs();
            }
            FileWriter fw = new FileWriter(f);
            for (Task t : list) {
                TaskType type = (t instanceof Todo) ? TaskType.TODO :
                        (t instanceof Deadline) ? TaskType.DEADLINE : TaskType.EVENT;
                String isDone = t.getStatus().equals("X") ? "1" : "0";
                String line = type.getCode() + " | " + isDone + " | " + t.description;

                if (t instanceof Deadline) {
                    line += " | " + ((Deadline) t).by;
                } else if (t instanceof Event) {
                    line += " | " + ((Event) t).from + " | " + ((Event) t).to;
                }
                fw.write(line + System.lineSeparator());
            }
            fw.close();
        } catch (IOException e) {
            System.out.println("oop. could not save tasks.");
        }
    }

    private static ArrayList<Task> loadTasks() {
        ArrayList<Task> loadedList = new ArrayList<>();
        File f = new File(FILE_PATH);
        if (!f.exists()) return loadedList;

        try (Scanner s = new Scanner(f)) {
            while (s.hasNext()) {
                String[] parts = s.nextLine().split(" \\| ");
                TaskType type = TaskType.fromCode(parts[0]);
                Task task = null;

                switch (type) {
                    case TODO:
                        task = new Todo(parts[2]);
                        break;
                    case DEADLINE:
                        task = new Deadline(parts[2], parts[3]);
                        break;
                    case EVENT:
                        task = new Event(parts[2], parts[3], parts[4]);
                        break;
                }

                if (task != null) {
                    if (parts[1].equals("1")) task.markAsDone();
                    loadedList.add(task);
                }
            }
        } catch (Exception e) {
            System.out.println("oop. error loading save file");
        }
        return loadedList;
    }

    private static Todo handleToDo(String input) throws ZiqException {
        if (input.trim().equals("todo")) {
            throw new ZiqException("description of ToDo cannot be empty");
        }
        return new Todo(input.substring(5));
    }

    private static Deadline handleDeadline(String input) throws ZiqException {
        if (!input.contains(" /by ")) {
            throw new ZiqException("deadline must have '/by <time>'.");
        }
        String[] parts = input.substring(9).split(" /by ");
        return new Deadline(parts[0], parts[1]);
    }
}