package ziq;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.time.format.DateTimeParseException;
import java.util.ArrayList;

/**
 * Parses and executes user commands.
 * Handles all command types: todo, deadline, event, mark, unmark, delete, list, find, schedule, bye.
 */
public class Parser {

    public static final DateTimeFormatter OUTPUT_FORMAT = DateTimeFormatter.ofPattern("MMM dd yyyy, h:mm a");
    private static final DateTimeFormatter INPUT_FORMAT = DateTimeFormatter.ofPattern("d/M/yyyy HHmm");
    private static final DateTimeFormatter DATE_ONLY_FORMAT = DateTimeFormatter.ofPattern("d/M/yyyy");
    private static final int COMMAND_DEADLINE_PREFIX_LENGTH = 9;
    private static final int COMMAND_EVENT_PREFIX_LENGTH = 6;
    private static final int COMMAND_DELETE_PREFIX_LENGTH = 7;
    private static final int COMMAND_FIND_PREFIX_LENGTH = 5;
    private static final int COMMAND_SCHEDULE_PREFIX_LENGTH = 9;
    private static final int DISPLAY_INDEX_OFFSET = 1;

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
        assert input != null : "input must not be empty";
        assert tasks != null : "tasks must not be empty";
        assert ui != null : "ui must not be empty";
        assert storage != null : "storage must not be empty";
        String trimmedInput = input.trim();

        if (trimmedInput.equalsIgnoreCase("bye")) {
            return true;
        }

        if (trimmedInput.equalsIgnoreCase("list")) {
            printTaskList(tasks, ui);
        } else if (trimmedInput.equals("mark") || trimmedInput.startsWith("mark ")) {
            handleMark(trimmedInput, tasks, ui, storage, true);
        } else if (trimmedInput.equals("unmark") || trimmedInput.startsWith("unmark ")) {
            handleMark(trimmedInput, tasks, ui, storage, false);
        } else if (trimmedInput.equals("todo") || trimmedInput.startsWith("todo ")) {
            handleTodo(trimmedInput, tasks, ui, storage);
        } else if (trimmedInput.equals("deadline") || trimmedInput.startsWith("deadline ")) {
            handleDeadline(trimmedInput, tasks, ui, storage);
        } else if (trimmedInput.equals("event") || trimmedInput.startsWith("event ")) {
            handleEvent(trimmedInput, tasks, ui, storage);
        } else if (trimmedInput.equals("delete") || trimmedInput.startsWith("delete ")) {
            handleDelete(trimmedInput, tasks, ui, storage);
        } else if (trimmedInput.startsWith("find ")) {
            handleFind(trimmedInput, tasks, ui);
        } else if (trimmedInput.equals("schedule") || trimmedInput.startsWith("schedule ")) {
            handleSchedule(trimmedInput, tasks, ui);
        } else if (trimmedInput.startsWith("help")) {
            getHelp(ui);
        } else {
            throw new ZiqException("Unknown command. Enter 'help' for a list of commands.");
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

            if (isMark) {
                task.markAsDone();
                ui.printLine("task marked as done:");
            } else {
                task.unmark();
                ui.printLine("task marked as not done:");
            }
            ui.printLine("  " + task);
            storage.save(tasks.getTaskList());
        } catch (IndexOutOfBoundsException | NumberFormatException e) {
            String cmd = isMark ? "mark" : "unmark";
            throw new ZiqException("invalid task number. Correct format: " + cmd + " <number> (e.g. " + cmd + " 1). "
                    + "Use 'list' to see task numbers.");
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
            throw new ZiqException("description of task cannot be empty. Correct format: todo <description> "
                    + "(e.g. todo read book)");
        }
        Task t = new Todo(input.substring(5));
        addTaskAndSave(t, tasks, storage, ui);
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
            throw new ZiqException("deadline must have '/by dd/mm/yyyy HHmm' (e.g. /by 2/22/2022 1200)");
        }
        try {
            String commandBody = input.substring(COMMAND_DEADLINE_PREFIX_LENGTH);
            String[] parts = commandBody.split(" /by ");
            String description = parts[0];
            LocalDateTime deadlineTime = LocalDateTime.parse(parts[1], INPUT_FORMAT);
            Task task = new Deadline(description, deadlineTime);
            addTaskAndSave(task, tasks, storage, ui);
        } catch (DateTimeParseException e) {
            throw new ZiqException("invalid date format for deadline. Correct format: "
                    + "deadline <description> /by d/M/yyyy HHmm (e.g. deadline submit report /by 2/22/2022 1200)");
        } catch (ArrayIndexOutOfBoundsException e) {
            throw new ZiqException("deadline needs a date. Correct format: "
                    + "deadline <description> /by d/M/yyyy HHmm (e.g. deadline submit report /by 2/22/2022 1200)");
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
            throw new ZiqException("event must include '/from' and '/to' with date and time "
                    + "(e.g. /from 2/22/2022 1200 /to 2/22/2022 1400)");
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
            throw new ZiqException("invalid date format for event. Correct format: "
                    + "event <description> /from d/M/yyyy HHmm /to d/M/yyyy HHmm "
                    + "(e.g. event meeting /from 2/22/2022 1200 /to 2/22/2022 1400)");
        } catch (ArrayIndexOutOfBoundsException e) {
            throw new ZiqException("event needs both /from and /to with dates. Correct format: "
                    + "event <description> /from d/M/yyyy HHmm /to d/M/yyyy HHmm "
                    + "(e.g. event meeting /from 2/22/2022 1200 /to 2/22/2022 1400)");
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
            ui.printLine("task removed:");
            ui.printLine("  " + removedTask);
            ui.printLine("now you have " + tasks.size() + " task(s) in the list.");
        } catch (NumberFormatException e) {
            throw new ZiqException("invalid task number. Correct format: delete <number> (e.g. delete 1). "
                    + "Use 'list' to see task numbers.");
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
     * Handles the schedule command to view tasks on a specific date.
     *
     * @param input the user's input (e.g. "schedule 8/2/2026")
     * @param tasks the task list to search
     * @param ui the UI handler for output
     * @throws ZiqException if the date format is invalid
     */
    private static void handleSchedule(String input, TaskList tasks, Ui ui) throws ZiqException {
        if (input.length() <= COMMAND_SCHEDULE_PREFIX_LENGTH) {
            throw new ZiqException("schedule needs a date. use schedule d/M/yyyy (e.g. schedule 2/22/2022)");
        }
        try {
            String dateStr = input.substring(COMMAND_SCHEDULE_PREFIX_LENGTH).trim();
            LocalDate date = LocalDate.parse(dateStr, DATE_ONLY_FORMAT);
            ArrayList<Task> onDate = tasks.getTasksOnDate(date);
            ui.printLine("schedule for " + date.format(DateTimeFormatter.ofPattern("MMM dd yyyy")) + ":");
            if (onDate.isEmpty()) {
                ui.printLine("  (no tasks on this date)");
            } else {
                for (int i = 0; i < onDate.size(); i++) {
                    ui.printLine((i + DISPLAY_INDEX_OFFSET) + ". " + onDate.get(i));
                }
            }
        } catch (DateTimeParseException e) {
            throw new ZiqException("invalid date for schedule. Correct format: schedule d/M/yyyy "
                    + "(e.g. schedule 2/22/2022)");
        }
    }

    /**
     * Prints all tasks in the task list.
     *
     * @param tasks the task list to print
     * @param ui the UI handler for output
     */
    private static void printTaskList(TaskList tasks, Ui ui) {
        ui.printLine("here is your to-do list!");
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
        ui.printLine("here are the matching tasks in your list:");
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
        ui.printLine("task added:");
        ui.printLine("  " + task);
        ui.printLine("now you have " + tasks.size() + " task(s) in the list.");
    }

    /**
     * Gives user a list of commands.
     *
     * @return list of commands available
     */
    private static void getHelp(Ui ui) {
        ui.printLine("here are the commands available:");
        ui.printLine("todo <description> - add a todo task");
        ui.printLine("deadline <description> /by <date> - add a deadline task");
        ui.printLine("event <description> /from <date> /to <date> - add an event task");
        ui.printLine("mark <index> - mark a task as done");
        ui.printLine("unmark <index> - mark a task as not done");
        ui.printLine("delete <index> - delete a task");
        ui.printLine("find <keyword> - find tasks by keyword");
        ui.printLine("schedule <date> - view tasks on a specific date");
        ui.printLine("help - display this list of commands");
        ui.printLine("bye - terminate Ziq");
    }
}

