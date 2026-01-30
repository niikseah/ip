package ziq;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.time.format.DateTimeParseException;

/**
 * Parses and executes user commands.
 * Handles all command types: todo, deadline, event, mark, unmark, delete, list, bye.
 */
public class Parser {

    private static final DateTimeFormatter INPUT_FORMAT = DateTimeFormatter.ofPattern("d/M/yyyy HHmm");
    public static final DateTimeFormatter OUTPUT_FORMAT = DateTimeFormatter.ofPattern("MMM dd yyyy, h:mm a");

    /**
     * Parses and executes a user command.
     *
     * @param input the user's input command
     * @param tasks the task list to modify
     * @param ui the UI handler for output
     * @param storage the storage handler for saving tasks
     * @return true if the command is "bye" (exit), false otherwise
     * @throws ZiqException if the command is invalid or cannot be executed
     */
    public static boolean executeCommand(String input, TaskList tasks, Ui ui, Storage storage) throws ZiqException {
        String trimmedInput = input.trim();

        if (trimmedInput.equalsIgnoreCase("bye")) {
            return true;
        }

        if (trimmedInput.equalsIgnoreCase("list")) {
            System.out.println("Here is your To-Do list!");
            for (int i = 0; i < tasks.size(); i++) {
                System.out.println((i + 1) + ". " + tasks.get(i));
            }
        } else if (trimmedInput.startsWith("mark ")) {
            handleMark(trimmedInput, tasks, ui, storage, true);
        } else if (trimmedInput.startsWith("unmark ")) {
            handleMark(trimmedInput, tasks, ui, storage, false);
        } else if (trimmedInput.startsWith("todo ")) {
            handleTodo(trimmedInput, tasks, ui, storage);
        } else if (trimmedInput.startsWith("deadline ")) {
            handleDeadline(trimmedInput, tasks, ui, storage);
        } else if (trimmedInput.startsWith("event ")) {
            handleEvent(trimmedInput, tasks, ui, storage);
        } else if (trimmedInput.startsWith("delete ")) {
            handleDelete(trimmedInput, tasks, ui, storage);
        } else {
            throw new ZiqException("idk lol,,, only the following commands work: todo, deadline, or event!");
        }

        return false;
    }

    /**
     * Handles mark and unmark commands.
     *
     * @param input the user's input command
     * @param tasks the task list to modify
     * @param ui the UI handler for output
     * @param storage the storage handler for saving tasks
     * @param isMark true to mark as done, false to unmark
     * @throws ZiqException if the task index is invalid
     */
    private static void handleMark(String input, TaskList tasks, Ui ui, Storage storage, boolean isMark) throws ZiqException {
        try {
            int index = Integer.parseInt(input.split(" ")[1]) - 1;
            Task task = tasks.get(index);
            if (isMark) {
                task.markAsDone();
                System.out.println("donezos");
            } else {
                task.unmark();
                System.out.println("undonezos");
            }
            System.out.println("  " + task);
            storage.save(tasks.getTaskList());
        } catch (IndexOutOfBoundsException | NumberFormatException e) {
            throw new ZiqException("got this task number?");
        }
    }

    /**
     * Handles the todo command to add a new todo task.
     *
     * @param input the user's input command
     * @param tasks the task list to modify
     * @param ui the UI handler for output
     * @param storage the storage handler for saving tasks
     * @throws ZiqException if the description is empty
     */
    private static void handleTodo(String input, TaskList tasks, Ui ui, Storage storage) throws ZiqException {
        if (input.length() <= 5) throw new ZiqException("description of ToDo cannot be empty");
        Task t = new Todo(input.substring(5));
        tasks.add(t);
        storage.save(tasks.getTaskList());
        printAdded(t, tasks.size());
    }

    /**
     * Handles the deadline command to add a new deadline task.
     *
     * @param input the user's input command
     * @param tasks the task list to modify
     * @param ui the UI handler for output
     * @param storage the storage handler for saving tasks
     * @throws ZiqException if the format is invalid or the date cannot be parsed
     */
    private static void handleDeadline(String input, TaskList tasks, Ui ui, Storage storage) throws ZiqException {
        if (!input.contains(" /by ")) throw new ZiqException("deadline must have '/by dd/mm/yyyy HHmm.'");
        try {
            String[] parts = input.substring(9).split(" /by ");
            LocalDateTime time = LocalDateTime.parse(parts[1], INPUT_FORMAT);
            Task t = new Deadline(parts[0], time);
            tasks.add(t);
            storage.save(tasks.getTaskList());
            printAdded(t, tasks.size());
        } catch (DateTimeParseException e) {
            throw new ZiqException("Please use the format: dd/mm/yyyy HHmm (e.g. 2/12/2019 1800)");
        }
    }

    /**
     * Handles the event command to add a new event task.
     *
     * @param input the user's input command
     * @param tasks the task list to modify
     * @param ui the UI handler for output
     * @param storage the storage handler for saving tasks
     * @throws ZiqException if the format is invalid or the dates cannot be parsed
     */
    private static void handleEvent(String input, TaskList tasks, Ui ui, Storage storage) throws ZiqException {
        if (!input.contains(" /from ") || !input.contains(" /to ")) {
            throw new ZiqException("event needs '/from' and '/to' time.");
        }
        try {
            String[] parts = input.substring(6).split(" /from | /to ");
            Task t = new Event(parts[0],
                    LocalDateTime.parse(parts[1], INPUT_FORMAT),
                    LocalDateTime.parse(parts[2], INPUT_FORMAT));
            tasks.add(t);
            storage.save(tasks.getTaskList());
            printAdded(t, tasks.size());
        } catch (DateTimeParseException e) {
            throw new ZiqException("Please use the format: dd/mm/yyyy HHmm (e.g. 2/12/2019 1800)");
        }
    }

    /**
     * Handles the delete command to remove a task.
     *
     * @param input the user's input command
     * @param tasks the task list to modify
     * @param ui the UI handler for output
     * @param storage the storage handler for saving tasks
     * @throws ZiqException if the task index is invalid
     */
    private static void handleDelete(String input, TaskList tasks, Ui ui, Storage storage) throws ZiqException {
        try {
            int index = Integer.parseInt(input.substring(7)) - 1;
            Task removed = tasks.delete(index);
            storage.save(tasks.getTaskList());
            System.out.println("task removed");
            System.out.println("  " + removed);
            System.out.println("Now you have " + tasks.size() + " tasks in the list.");
        } catch (NumberFormatException e) {
            throw new ZiqException("the provided number is invalid...");
        }
    }

    /**
     * Prints a message indicating a task has been added.
     *
     * @param t the task that was added
     * @param size the new total number of tasks
     */
    private static void printAdded(Task t, int size) {
        System.out.println("task added");
        System.out.println("  " + t);
        System.out.println("Now you have " + size + " tasks in the list.");
    }
}