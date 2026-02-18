package ziq;

import java.time.LocalDateTime;

/**
 * Represents a Deadline task with a due date/time.
 */
public class Deadline extends Task {

    protected LocalDateTime by;

    /**
     * Constructs a new Deadline task with the given description and due date/time.
     *
     * @param description the description of the deadline task
     * @param by the due date/time for the deadline
     */
    public Deadline(String description, LocalDateTime by) {
        super(description);
        this.by = by;
    }

    /**
     * Returns the due date/time of this deadline.
     *
     * @return the due date/time
     */
    public LocalDateTime by() {
        return this.by;
    }

    @Override
    public String toString() {
        return "[D]" + super.toString() + " (by: " + by.format(Parser.OUTPUT_FORMAT) + ")";
    }
}
