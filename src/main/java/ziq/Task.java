package ziq;

/**
 * Represents a task with a description and completion status.
 * Base class for different types of tasks (Todo, Deadline, Event).
 */
public class Task {
    protected String description;
    protected boolean isDone;

    /**
     * Constructs a new Task with the given description.
     *
     * @param description the description of the task
     */
    public Task(String description) {
        this.description = description;
        this.isDone = false;
    }

    /**
     * Returns the description of this task.
     *
     * @return the task description
     */
    public String description() {
        return this.description;
    }

    /**
     * Returns the completion status as a string ("X" for done, " " for not done).
     *
     * @return "X" if task is done, " " otherwise
     */
    public String getStatus() {
        return (isDone ? "X" : " ");
    }

    /**
     * Marks this task as completed.
     */
    public void markAsDone() {
        this.isDone = true;
    }

    /**
     * Marks this task as not completed.
     */
    public void unmark() {
        this.isDone = false;
    }

    @Override
    public String toString() {
        return "[" + getStatus() + "] " + description;
    }
}
