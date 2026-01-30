package ziq;

import java.util.ArrayList;

/**
 * Manages a collection of tasks.
 * Provides methods to add, remove, and retrieve tasks.
 */
public class TaskList {
    private final ArrayList<Task> tasks;

    /**
     * Constructs a TaskList with the given list of tasks.
     */
    public TaskList(ArrayList<Task> tasks) {
        this.tasks = tasks;
    }

    /**
     * Constructs an empty TaskList.
     */
    public TaskList() {
        this.tasks = new ArrayList<>();
    }

    /**
     * Adds a task to the list.
     */
    public void add(Task t) {
        tasks.add(t);
    }

    /**
     * Returns the list of all tasks.
     */
    public ArrayList<Task> getTaskList() {
        return this.tasks;
    }

    /**
     * Returns results matched with search keywords.
     */
    public ArrayList<Task> findTasks(String keyword) {
        ArrayList<Task> matches = new ArrayList<>();
        for (Task task : tasks) {
            if (task.description().contains(keyword)) {
                matches.add(task);
            }
        }
        return matches;
    }

    /**
     * Deletes and returns the task at the specified index.
     */
    public Task delete(int index) throws ZiqException {
        if (index < 0 || index >= tasks.size()) {
            throw new ZiqException("task does not exist");
        }
        return tasks.remove(index);
    }

    /**
     * Returns the task at the specified index.
     */
    public Task get(int index) {
        return tasks.get(index);
    }

    /**
     * Returns the number of tasks in the list.
     */
    public int size() {
        return tasks.size();
    }
}
