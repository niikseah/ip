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
     *
     * @param tasks the initial list of tasks
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
     *
     * @param t the task to add
     */
    public void add(Task t) {
        tasks.add(t);
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
        if (index < 0 || index >= tasks.size())
        {
            throw new ZiqException("task does not exist");
        }
        return tasks.remove(index);
    }

    /**
     * Returns the task at the specified index.
     *
     * @param index the index of the task to retrieve (0-based)
     * @return the task at the specified index
     */
    public Task get(int index) {
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
}
