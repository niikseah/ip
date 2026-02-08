package ziq;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.time.format.DateTimeParseException;

/**
 * Parses and executes user commands.
 * Handles all command types: todo, deadline, event, mark, unmark, delete, list, find, bye.
 */
public class Parser {

    public static final DateTimeFormatter OUTPUT_FORMAT = DateTimeFormatter.ofPattern("MMM dd yyyy, h:mm a");
    private static final DateTimeFormatter INPUT_FORMAT = DateTimeFormatter.ofPattern("d/M/yyyy HHmm");

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
        assert input != null : "input must not be null";
        assert tasks != null : "tasks must not be null";
        assert ui != null : "ui must not be null";
        assert storage != null : "storage must not be null";
        String trimmedInput = input.trim();

        if (trimmedInput.equalsIgnoreCase("bye")) {
            return true;
        }

        if (trimmedInput.equalsIgnoreCase("list")) {
            ui.printLine("Here is your To-Do list!");
            for (int i = 0; i < tasks.size(); i++) {
                ui.printLine((i + 1) + ". " + tasks.get(i));
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
        } else if (trimmedInput.startsWith("find ")) {
            handleFind(trimmedInput, tasks, ui);
        } else {
            throw new ZiqException("idk lol,,, only the following commands work: "
                    + "todo, deadline, event, or find!");
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
    private static void handleMark(String input, TaskList tasks, Ui ui, Storage storage, boolean isMark)
            throws ZiqException {
        try {
            int index = Integer.parseInt(input.split(" ")[1]) - 1;
            Task task = tasks.get(index);
            if (isMark) {
                task.markAsDone();
                ui.printLine("donezos");
            } else {
                task.unmark();
                ui.printLine("undonezos");
            }
            ui.printLine("  " + task);
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
        if (input.length() <= 5) {
            throw new ZiqException("description of ToDo cannot be empty");
        }
        Task t = new Todo(input.substring(5));
        tasks.add(t);
        storage.save(tasks.getTaskList());
        printAdded(t, tasks.size(), ui);
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
        if (!input.contains(" /by ")) {
            throw new ZiqException("deadline must have '/by dd/mm/yyyy HHmm.'");
        }
        try {
            String[] parts = input.substring(9).split(" /by ");
            LocalDateTime time = LocalDateTime.parse(parts[1], INPUT_FORMAT);
            Task t = new Deadline(parts[0], time);
            tasks.add(t);
            storage.save(tasks.getTaskList());
            printAdded(t, tasks.size(), ui);
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
            printAdded(t, tasks.size(), ui);
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
            ui.printLine("task removed");
            ui.printLine("  " + removed);
            ui.printLine("Now you have " + tasks.size() + " tasks in the list.");
        } catch (NumberFormatException e) {
            throw new ZiqException("the provided number is invalid...");
        }
    }

    /**
     * Handles the find command to list tasks whose description contains the given keyword.
     *
     * @param input the user's input command (e.g. "find book")
     * @param tasks the task list to search
     */
    private static void handleFind(String input, TaskList tasks, Ui ui) {
        if (input.length() <= 5) {
            ui.printLine("Here is your To-Do list!");
            for (int i = 0; i < tasks.size(); i++) {
                ui.printLine((i + 1) + ". " + tasks.get(i));
            }
            return;
        }
        String keyword = input.substring(5).trim().toLowerCase();
        ui.printLine("Here are the matching tasks in your list:");
        int count = 0;
        for (int i = 0; i < tasks.size(); i++) {
            Task t = tasks.get(i);
            if (t.description().toLowerCase().contains(keyword)) {
                count++;
                ui.printLine(count + ". " + t);
            }
        }
    }

    /**
     * Prints a message indicating a task has been added.
     *
     * @param t the task that was added
     * @param size the new total number of tasks
     */
    private static void printAdded(Task t, int size, Ui ui) {
        ui.printLine("task added");
        ui.printLine("  " + t);
        ui.printLine("Now you have " + size + " tasks in the list.");
    }
}
