package ziq;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;

/**
 * Represents an event task with a start and end date/time.
 */
public class Event extends Task {
    protected LocalDateTime from;
    protected LocalDateTime to;

    /**
     * Constructs a new Event task with the given description, start and end date/time.
     */
    public Event(String description, LocalDateTime from, LocalDateTime to) {
        super(description);
        this.from = from;
        this.to = to;
    }

    /**
     * Returns the start date/time of this event.
     */
    public LocalDateTime from() {
        return this.from;
    }

    /**
     * Returns the end date/time of this event.
     */
    public LocalDateTime to() {
        return this.to;
    }

    @Override
    public String toString() {
        return "[E]" + super.toString() + " (from: " + from.format(Parser.OUTPUT_FORMAT) + " to: " + to.format(Parser.OUTPUT_FORMAT) + ")";
    }
}
