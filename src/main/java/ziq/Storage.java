package ziq;

import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.Scanner;

/**
 * Handles loading and saving tasks to/from a file.
 * Manages persistent storage of task data.
 */
public class Storage {
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
        ArrayList<Task> loadedList = new ArrayList<>();
        File f = new File(filePath);
        if (!f.exists()) {
            return loadedList;
        }

        try (Scanner s = new Scanner(f)) {
            while (s.hasNext()) {
                String[] parts = s.nextLine().split(" \\| ");
                assert parts.length >= 3 : "save file line must have at least 3 parts (type | done | description)";
                TaskType type = TaskType.fromCode(parts[0]);
                Task task = null;
                switch (type) {
                case TODO:
                    task = new Todo(parts[2]);
                    break;
                case DEADLINE:
                    assert parts.length >= 4 : "deadline line must have 4 parts (type | done | description | by)";
                    task = new Deadline(parts[2], LocalDateTime.parse(parts[3]));
                    break;
                case EVENT:
                    assert parts.length >= 5 : "event line must have 5 parts (type | done | description | from | to)";
                    task = new Event(parts[2], LocalDateTime.parse(parts[3]), LocalDateTime.parse(parts[4]));
                    break;
                default:
                    break;
                }
                if (task != null) {
                    if (parts[1].equals("1")) {
                        task.markAsDone();
                    }
                    loadedList.add(task);
                }
            }
        } catch (Exception e) {
            throw new ZiqException("error loading save file");
        }
        return loadedList;
    }

    /**
     * Saves the list of tasks to the storage file.
     *
     * @param list the list of tasks to save
     */
    public void save(ArrayList<Task> list) {
        assert list != null : "task list to save must not be null";
        try {
            File f = new File(filePath);
            if (f.getParentFile() != null) {
                f.getParentFile().mkdirs();
            }
            FileWriter fw = new FileWriter(f);
            for (Task t : list) {
                TaskType type = (t instanceof Todo)
                        ? TaskType.TODO
                        : (t instanceof Deadline)
                        ? TaskType.DEADLINE
                        : TaskType.EVENT;
                String isDone = t.getStatus().equals("X") ? "1" : "0";
                String line = type.getCode() + " | " + isDone + " | " + t.description();
                if (t instanceof Deadline) {
                    line += " | " + ((Deadline) t).by();
                } else if (t instanceof Event) {
                    line += " | " + ((Event) t).from() + " | " + ((Event) t).to();
                }
                fw.write(line + System.lineSeparator());
            }
            fw.close();
        } catch (IOException e) {
            ui.printLine("oop. could not save tasks.");
        }
    }
}
