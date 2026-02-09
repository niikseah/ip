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
            printTaskList(tasks, ui);
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
            String[] parts = input.split(" ");
            int taskNumber = Integer.parseInt(parts[1]);
            int index = taskNumber - DISPLAY_INDEX_OFFSET;
            Task task = tasks.get(index);
            
            if (shouldMark) {
                task.markAsDone();
                ui.printLine("Task marked as done:");
            } else {
                task.unmark();
                ui.printLine("Task unmarked:");
            }
            ui.printLine("  " + task);
            storage.save(tasks.getTaskList());
        } catch (IndexOutOfBoundsException | NumberFormatException e) {
            throw new ZiqException("Invalid task number. Please provide a valid task number.");
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
            String commandBody = input.substring(COMMAND_DEADLINE_PREFIX_LENGTH);
            String[] parts = commandBody.split(" /by ");
            String description = parts[0];
            LocalDateTime deadlineTime = LocalDateTime.parse(parts[1], INPUT_FORMAT);
            Task task = new Deadline(description, deadlineTime);
            addTaskAndSave(task, tasks, storage, ui);
        } catch (DateTimeParseException e) {
            throw new ZiqException("Invalid date format. Please use: dd/mm/yyyy HHmm (e.g. 2/12/2019 1800)");
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
            throw new ZiqException("Event must include '/from' and '/to' with date and time");
        }
        try {
            String commandBody = input.substring(COMMAND_EVENT_PREFIX_LENGTH);
            String[] parts = commandBody.split(" /from | /to ");
            String description = parts[0];
            LocalDateTime startTime = LocalDateTime.parse(parts[1], INPUT_FORMAT);
            LocalDateTime endTime = LocalDateTime.parse(parts[2], INPUT_FORMAT);
            Task task = new Event(description, startTime, endTime);
            addTaskAndSave(task, tasks, storage, ui);
        } catch (DateTimeParseException e) {
            throw new ZiqException("Invalid date format. Please use: dd/mm/yyyy HHmm (e.g. 2/12/2019 1800)");
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
            String taskNumberString = input.substring(COMMAND_DELETE_PREFIX_LENGTH);
            int taskNumber = Integer.parseInt(taskNumberString);
            int index = taskNumber - DISPLAY_INDEX_OFFSET;
            Task removedTask = tasks.delete(index);
            storage.save(tasks.getTaskList());
            ui.printLine("Task removed:");
            ui.printLine("  " + removedTask);
            ui.printLine("Now you have " + tasks.size() + " task(s) in the list.");
        } catch (NumberFormatException e) {
            throw new ZiqException("Invalid task number. Please provide a valid number.");
        }
    }

    /**
     * Handles the find command to list tasks whose description contains the given keyword.
     *
     * @param input the user's input command (e.g. "find book")
     * @param tasks the task list to search
     */
    private static void handleFind(String input, TaskList tasks, Ui ui) {
        if (input.length() <= COMMAND_FIND_PREFIX_LENGTH) {
            printTaskList(tasks, ui);
            return;
        }
        String keyword = input.substring(COMMAND_FIND_PREFIX_LENGTH).trim().toLowerCase();
        printMatchingTasks(tasks, keyword, ui);
    }

    /**
     * Prints all tasks in the task list.
     *
     * @param tasks the task list to print
     * @param ui the UI handler for output
     */
    private static void printTaskList(TaskList tasks, Ui ui) {
        ui.printLine("Here is your To-Do list!");
        for (int i = 0; i < tasks.size(); i++) {
            int displayNumber = i + DISPLAY_INDEX_OFFSET;
            ui.printLine(displayNumber + ". " + tasks.get(i));
        }
    }

    /**
     * Prints tasks whose description contains the given keyword.
     *
     * @param tasks the task list to search
     * @param keyword the keyword to search for (case-insensitive)
     * @param ui the UI handler for output
     */
    private static void printMatchingTasks(TaskList tasks, String keyword, Ui ui) {
        ui.printLine("Here are the matching tasks in your list:");
        int matchCount = 0;
        for (int i = 0; i < tasks.size(); i++) {
            Task task = tasks.get(i);
            if (task.description().toLowerCase().contains(keyword)) {
                matchCount++;
                ui.printLine(matchCount + ". " + task);
            }
        }
    }

    /**
     * Adds a task to the list, saves to storage, and prints confirmation.
     *
     * @param task the task to add
     * @param tasks the task list to modify
     * @param storage the storage handler for saving
     * @param ui the UI handler for output
     */
    private static void addTaskAndSave(Task task, TaskList tasks, Storage storage, Ui ui) {
        tasks.add(task);
        storage.save(tasks.getTaskList());
        ui.printLine("Task added:");
        ui.printLine("  " + task);
        ui.printLine("Now you have " + tasks.size() + " task(s) in the list.");
    }
}
