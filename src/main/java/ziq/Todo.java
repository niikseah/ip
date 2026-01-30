package ziq;

/**
 * Represents a todo task without any date/time.
 */
public class Todo extends Task {
    /**
     * Constructs a new Todo task with the given description.
     */
    public Todo(String description) {
        super(description);
    }

    @Override
    public String toString() {
        return "[T]" + super.toString();
    }
}
