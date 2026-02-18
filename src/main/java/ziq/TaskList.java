package ziq;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.Comparator;
import java.util.List;
import java.util.stream.Collectors;

/**
 * Manages a collection of tasks.
 * Provides methods to add, remove, and retrieve tasks.
 */
public class TaskList {
    private final ArrayList<Task> tasks;

    /**
     * Constructs a TaskList with the given list of tasks.
     *
     * @param tasks the initial list of tasks
     */
    public TaskList(ArrayList<Task> tasks) {
        assert tasks != null : "tasks list must not be null";
        this.tasks = tasks;
    }

    /**
     * Constructs an empty TaskList.
     */
    public TaskList() {
        this.tasks = new ArrayList<>();
    }

    /**
     * Adds one or more tasks to the list.
     *
     * @param t the task(s) to add (varargs)
     */
    public void add(Task... t) {
        for (Task task : t) {
            assert task != null : "task to add must not be null";
            tasks.add(task);
        }
    }

    /**
     * Returns the list of all tasks.
     *
     * @return the list of tasks
     */
    public ArrayList<Task> getTaskList() {
        return this.tasks;
    }

    /**
     * Deletes and returns the task at the specified index.
     *
     * @param index the index of the task to delete (0-based)
     * @return the deleted task
     * @throws ZiqException if the index is invalid
     */
    public Task delete(int index) throws ZiqException {
        if (index < 0 || index >= tasks.size()) {
            throw new ZiqException("task number does not exist. Use 'list' to see valid task numbers (e.g. delete 1).");
        }
        assert index >= 0 && index < tasks.size() : "index must be valid at this point";
        return tasks.remove(index);
    }

    /**
     * Returns the task at the specified index.
     *
     * @param index the index of the task to retrieve (0-based)
     * @return the task at the specified index
     */
    public Task get(int index) {
        assert index >= 0 && index < tasks.size() : "index must be in range [0, size)";
        return tasks.get(index);
    }

    /**
     * Returns the number of tasks in the list.
     *
     * @return the number of tasks
     */
    public int size() {
        return tasks.size();
    }

    /**
     * Returns tasks that fall on the given date (deadlines due that day, events that span that day),
     * sorted by time.
     *
     * @param date the date to view the schedule for
     * @return list of tasks on that date, sorted by time
     */
    public ArrayList<Task> getTasksOnDate(LocalDate date) {
        List<Task> onDate = tasks.stream()
                .filter(task -> isTaskOnDate(task, date))
                .sorted(Comparator.comparing(t -> getScheduleTime(t)))
                .collect(Collectors.toList());
        return new ArrayList<>(onDate);
    }

    private static boolean isTaskOnDate(Task task, LocalDate date) {
        if (task instanceof Deadline) {
            return ((Deadline) task).by().toLocalDate().equals(date);
        }
        if (task instanceof Event) {
            Event e = (Event) task;
            return !date.isBefore(e.from().toLocalDate()) && !date.isAfter(e.to().toLocalDate());
        }
        return false;
    }

    private static LocalDateTime getScheduleTime(Task task) {
        if (task instanceof Deadline) {
            return ((Deadline) task).by();
        }
        if (task instanceof Event) {
            return ((Event) task).from();
        }
        return LocalDateTime.MIN;
    }
}
