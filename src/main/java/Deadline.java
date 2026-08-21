/**
 * Represents a task that must be completed by a specified time.
 */
public class Deadline extends Task {
    private final String by;

    /**
     * Creates an incomplete deadline task.
     *
     * @param description the task description
     * @param by the deadline, kept as text
     */
    public Deadline(String description, String by) {
        super(description);
        this.by = by;
    }

    /**
     * Formats this deadline with its type, completion status, and due time.
     *
     * @return the formatted deadline
     */
    @Override
    public String toString() {
        return "[D][" + getStatusIcon() + "] " + getDescription() + " (by: " + by + ")";
    }
}
