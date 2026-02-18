package ziq;

import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.Scanner;
import java.util.regex.Pattern;

/**
 * Handles loading and saving tasks to/from a file.
 * Manages persistent storage of task data.
 */
public class Storage {
    private static final String FILE_DELIMITER = " | ";
    private static final String DONE_STATUS_CODE = "1";
    private static final String NOT_DONE_STATUS_CODE = "0";
    private static final int MINIMUM_PARTS_COUNT = 3;
    private static final int TODO_PARTS_COUNT = 3;
    private static final int DEADLINE_PARTS_COUNT = 4;
    private static final int EVENT_PARTS_COUNT = 5;
    private static final int TYPE_INDEX = 0;
    private static final int STATUS_INDEX = 1;
    private static final int DESCRIPTION_INDEX = 2;
    private static final int DEADLINE_TIME_INDEX = 3;
    private static final int EVENT_START_INDEX = 3;
    private static final int EVENT_END_INDEX = 4;

    private final String filePath;
    private final Ui ui;

    /**
     * Constructs a Storage instance with the specified file path and UI for messages.
     *
     * @param filePath the path to the file where tasks are stored
     * @param ui the UI for output (e.g. save error messages)
     */
    public Storage(String filePath, Ui ui) {
        this.filePath = filePath;
        this.ui = ui;
    }

    /**
     * Loads tasks from the storage file.
     *
     * @return a list of tasks loaded from the file, or an empty list if file doesn't exist
     * @throws ZiqException if there is an error reading or parsing the file
     */
    public ArrayList<Task> load() throws ZiqException {
        ArrayList<Task> loadedTasks = new ArrayList<>();
        File file = new File(filePath);
        if (!file.exists()) {
            return loadedTasks;
        }

        try (Scanner s = new Scanner(file)) {
            while (s.hasNext()) {
                Task task = parseTaskFromLine(s.nextLine());
                if (task != null) {
                    loadedTasks.add(task);
                }
            }
        } catch (IOException e) {
            throw new ZiqException("Error reading save file: " + e.getMessage());
        } catch (ZiqException e) {
            throw e;
        } catch (Exception e) {
            throw new ZiqException("Error parsing save file: " + e.getMessage());
        }
        return loadedTasks;
    }

    /**
     * Parses a single line from the save file into a Task object.
     *
     * @param line the line to parse
     * @return the parsed Task, or null if the line is invalid
     * @throws ZiqException if there is an error parsing the line
     */
    private Task parseTaskFromLine(String line) throws ZiqException {
        String[] parts = line.split(Pattern.quote(FILE_DELIMITER));
        if (parts.length < MINIMUM_PARTS_COUNT) {
            return null;
        }

        TaskType type = TaskType.fromCode(parts[TYPE_INDEX]);
        Task task = createTaskFromParts(type, parts);
        if (task != null && parts[STATUS_INDEX].equals(DONE_STATUS_CODE)) {
            task.markAsDone();
        }
        return task;
    }

    /**
     * Creates a Task object from parsed parts based on task type.
     *
     * @param type the type of task
     * @param parts the parsed parts from the save file line
     * @return the created Task, or null if type is invalid
     * @throws ZiqException if there is an error creating the task
     */
    private Task createTaskFromParts(TaskType type, String[] parts) throws ZiqException {
        switch (type) {
        case TODO:
            if (parts.length < TODO_PARTS_COUNT) {
                return null;
            }
            return new Todo(parts[DESCRIPTION_INDEX]);
        case DEADLINE:
            if (parts.length < DEADLINE_PARTS_COUNT) {
                return null;
            }
            LocalDateTime deadlineTime = LocalDateTime.parse(parts[DEADLINE_TIME_INDEX]);
            return new Deadline(parts[DESCRIPTION_INDEX], deadlineTime);
        case EVENT:
            if (parts.length < EVENT_PARTS_COUNT) {
                return null;
            }
            LocalDateTime startTime = LocalDateTime.parse(parts[EVENT_START_INDEX]);
            LocalDateTime endTime = LocalDateTime.parse(parts[EVENT_END_INDEX]);
            return new Event(parts[DESCRIPTION_INDEX], startTime, endTime);
        default:
            return null;
        }
    }

    /**
     * Saves the list of tasks to the storage file.
     *
     * @param list the list of tasks to save
     */
    public void save(ArrayList<Task> list) {
        assert list != null : "task list to save must not be null";
        try {
            File file = new File(filePath);
            createParentDirectoriesIfNeeded(file);
            writeTasksToFile(file, list);
        } catch (IOException e) {
            ui.printLine("Error: Could not save tasks to file.");
        }
    }

    /**
     * Creates parent directories for the file if they don't exist.
     *
     * @param file the file whose parent directories should be created
     */
    private void createParentDirectoriesIfNeeded(File file) {
        File parentDirectory = file.getParentFile();
        if (parentDirectory != null) {
            parentDirectory.mkdirs();
        }
    }

    /**
     * Writes all tasks to the file in the save format.
     *
     * @param file the file to write to
     * @param taskList the list of tasks to save
     * @throws IOException if there is an error writing to the file
     */
    private void writeTasksToFile(File file, ArrayList<Task> taskList) throws IOException {
        try (FileWriter fileWriter = new FileWriter(file)) {
            for (Task task : taskList) {
                String line = formatTaskForSave(task);
                fileWriter.write(line + System.lineSeparator());
            }
        }
    }

    /**
     * Formats a task into a string for saving to file.
     *
     * @param task the task to format
     * @return the formatted string representation
     */
    private String formatTaskForSave(Task task) {
        TaskType type = determineTaskType(task);
        String statusCode = task.getStatus().equals("X") ? DONE_STATUS_CODE : NOT_DONE_STATUS_CODE;
        StringBuilder line = new StringBuilder();
        line.append(type.getCode()).append(FILE_DELIMITER);
        line.append(statusCode).append(FILE_DELIMITER);
        line.append(task.description());

        if (task instanceof Deadline) {
            Deadline deadline = (Deadline) task;
            line.append(FILE_DELIMITER).append(deadline.by());
        } else if (task instanceof Event) {
            Event event = (Event) task;
            line.append(FILE_DELIMITER).append(event.from());
            line.append(FILE_DELIMITER).append(event.to());
        }
        return line.toString();
    }

    /**
     * Determines the TaskType of a given task.
     *
     * @param task the task to check
     * @return the corresponding TaskType
     */
    private TaskType determineTaskType(Task task) {
        if (task instanceof Todo) {
            return TaskType.TODO;
        } else if (task instanceof Deadline) {
            return TaskType.DEADLINE;
        } else {
            return TaskType.EVENT;
        }
    }
}
